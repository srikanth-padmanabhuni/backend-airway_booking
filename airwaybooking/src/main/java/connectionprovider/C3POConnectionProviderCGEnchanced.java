package connectionprovider;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;

public class C3POConnectionProviderCGEnchanced extends C3P0ConnectionProvider {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(C3POConnectionProviderCGEnchanced.class);
	private static Map<String, ConnCheckoutDetails> connectionTimeTracker = new ConcurrentHashMap<>();
	static {
		// start thread which observe the connection and issue a warning if a connection is not
		//close in within 10 sec
		Thread t = connectionObserver();
		t.start();
	}

	private static Thread connectionObserver() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(20_000l);
					} catch (InterruptedException e1) {			
						e1.printStackTrace(); 
						return;
					}
					if (Thread.interrupted()) {
						return;
					}
					try {
						checkConnections();
					} catch (Exception e) {
						log.error("error", e);
					}
				}
			}

			private void checkConnections() {
				for (Iterator<Map.Entry<String, ConnCheckoutDetails>> it = connectionTimeTracker.entrySet()
						.iterator(); it.hasNext();) {
					Map.Entry<String, ConnCheckoutDetails> ent = it.next();
					ConnCheckoutDetails conDetails = ent.getValue();
					long checkoutTimeStamp = conDetails.getTimeStamp();
					if (System.currentTimeMillis() - checkoutTimeStamp > 10_000) {
						// connection is checked out for more then 10 sec, issue
						// warning as a log.error so the it is surfaced in logs
						log.debug("*********************************************************************************");
						log.error("WARNING TRANSACTION TOOK MORE THEN 10 Sec", conDetails.getStackTrace()); 
						//remove from hashmap to protect ooo
						it.remove();
					}
				}
			}
		});
		return t;
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection connection = super.getConnection();
		Exception stacktrace = getStackTrace();		
		ConnCheckoutDetails connDetails = new ConnCheckoutDetails(System.currentTimeMillis(), stacktrace);
		connectionTimeTracker.put(connection.toString(), connDetails);
		return connection;
	}
	
	@Override
	public void closeConnection(Connection conn) throws SQLException {
		connectionTimeTracker.remove(conn.toString());
		super.closeConnection(conn);
	}
	private Exception getStackTrace() {
		Exception stacktrace;
		try {
			throw new Exception();
		} catch (Exception e) {
			stacktrace = e;
		}
		return stacktrace;
	}
}

class ConnCheckoutDetails {
	private Long timeStamp;
	private Exception stackTrace;

	public ConnCheckoutDetails(Long timeStamp, Exception stackTrace) {
		this.timeStamp = timeStamp;
		this.stackTrace = stackTrace;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Exception getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(Exception stackTrace) {
		this.stackTrace = stackTrace;
	}

}