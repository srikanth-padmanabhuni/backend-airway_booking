package conf;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.persist.Transactional;


public class ThreadLocalCleaner extends AbstractModule {
	private static Logger log = LogManager.getLogger(ThreadLocalCleaner.class);
	@Override
	protected void configure() {
		TransactionVariableCleaner interceptor = new TransactionVariableCleaner();			
		bindInterceptor(
                any(),
                annotatedWith(Transactional.class),
                interceptor);		
	}	
}

class TransactionVariableCleaner implements MethodInterceptor{
	private static Logger log = LogManager.getLogger(TransactionVariableCleaner.class);
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
	
		try{
			return invocation.proceed();
		}catch(Exception e) {			
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if(rootCause instanceof SQLException){
				log.debug("FOUND A SQL EXCEPTION , Hence try to clean the threadlocal leak of entity manager in jpa persist");				
				try{ cleanUpPersistanceLeakThreadLocals(); }catch(Exception e1 ){e1.printStackTrace();}
			}else{
				log.debug("Root cause is not a sql exception, hence jpa will not leak a threadlocal variable"); 
			}
			// in any case Rethrow the exception
			throw e;
		}
		
	}
	
	private void cleanUpPersistanceLeakThreadLocals() {
        try {
            // Get a reference to the thread locals table of the current thread
            Thread thread = Thread.currentThread();
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocalTable = threadLocalsField.get(thread);

            // Get a reference to the array holding the thread local variables inside the
            // ThreadLocalMap of the current thread
            Class threadLocalMapClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object table = tableField.get(threadLocalTable);

            // The key to the ThreadLocalMap is a WeakReference object. The referent field of this object
            // is a reference to the actual ThreadLocal variable
            Field referentField = Reference.class.getDeclaredField("referent");
            referentField.setAccessible(true);

            for (int i=0; i < Array.getLength(table); i++) {
                // Each entry in the table array of ThreadLocalMap is an Entry object
                // representing the thread local reference and its value
                Object entry = Array.get(table, i);
                if (entry != null) {
                    // Get a reference to the thread local object and remove it from the table
                    ThreadLocal threadLocal = (ThreadLocal)referentField.get(entry);
                    Object object = threadLocal.get();
                    if(object instanceof EntityManager){
                    	log.debug("Found the leaked threadLocal variable");
                    	threadLocal.remove();
                    	break;
                    }
                }
            }
            log.debug("No leak found");
        } catch(Exception e) {
            // We will tolerate an exception here and just log it
            throw new IllegalStateException(e);
        }
    }
}