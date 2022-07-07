/**
 * Copyright (C) the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package conf;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.persist.jpa.JpaPersistModule;

import db.CGMigrationEngine;
import db.CGMigrationInitializer;
import db.WebMigrationEngineFlyway;
import facade.BookingFacade;
import facade.BookingFacadeImpl;
import facade.FlightFacade;
import facade.FlightFacadeImpl;
import facade.UserFacade;
import facade.UserFacadeImpl;
import ninja.jpa.JpaInitializer;
import ninja.utils.NinjaMode;
import ninja.utils.NinjaPropertiesImpl;

@Singleton
public class Module extends AbstractModule {
    

	protected void configure() {
		
    	final NinjaPropertiesImpl ninjaProperties = new NinjaPropertiesImpl(NinjaMode.dev);
    	
		install(new ThreadLocalCleaner());
		
    	install(new PrivateModule() {
			@Override
			protected void configure() {
				
				String connectionUrl = ninjaProperties.get("db.connection.url");
	            String connectionUsername = ninjaProperties.get("db.connection.username");
	            String connectionPassword = ninjaProperties.get("db.connection.password");
	            
	            Properties jpaProperties = new Properties();
	            jpaProperties.put("hibernate.connection.url", connectionUrl);
	            jpaProperties.put("hibernate.connection.username", connectionUsername);
	            jpaProperties.put("hibernate.connection.password", connectionPassword);
				
	            install(new JpaPersistModule("mysql").properties(jpaProperties));
				bind(JpaInitializer.class).asEagerSingleton();
				bind(CGMigrationEngine.class).to(WebMigrationEngineFlyway.class);
				bind(CGMigrationInitializer.class).asEagerSingleton();
				
				bind(UserFacade.class).to(UserFacadeImpl.class).in(Singleton.class);
				expose(UserFacade.class);
				bind(BookingFacade.class).to(BookingFacadeImpl.class).in(Singleton.class);
				expose(BookingFacade.class);
				bind(FlightFacade.class).to(FlightFacadeImpl.class).in(Singleton.class);
				expose(FlightFacade.class);
			}
		});
	}

}
