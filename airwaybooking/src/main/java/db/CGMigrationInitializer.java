package db;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ninja.lifecycle.Start;

@Singleton
public class CGMigrationInitializer {

	@Inject
    private CGMigrationEngine migrationEngine;    
	
    /**
     * We start it at order 9 which is below order 10 (where JPA is started)
     */
    @Start(order=1)
    public void start() {  
    	boolean isMigrate = true;
    	if(isMigrate) {
    		migrationEngine.migrate(); 
    	}
    }
}
