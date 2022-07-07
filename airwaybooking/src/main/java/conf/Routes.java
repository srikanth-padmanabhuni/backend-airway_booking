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


import controllers.BookingController;
import controllers.CorsHeadersController;
import controllers.FlightController;
import controllers.UserController;
import ninja.Router;
import ninja.application.ApplicationRoutes;


public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
    	
    	/**
    	 * User Routes
    	 */
    	router.OPTIONS().route("/api/signup").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/signin").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/update/user").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/update/password").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/get/user/{userName}").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/get/users").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/delete/user/{userName}").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/logout").with(CorsHeadersController.class, "routeForOptions");
    	
    	router.POST().route("/api/signup").with(UserController.class, "signUp");
    	router.POST().route("/api/signin").with(UserController.class, "signIn");
    	router.POST().route("/api/update/user").with(UserController.class, "updateUser");
    	router.POST().route("/api/update/password").with(UserController.class, "updatePassword");
    	router.DELETE().route("/api/delete/user/{userName}").with(UserController.class, "deleteUserByUserName");
    	router.GET().route("/api/get/user/{userName}").with(UserController.class, "getUserByUserName");
    	router.GET().route("/api/get/users").with(UserController.class, "getUsers");
    	router.GET().route("/api/logout").with(UserController.class, "logOut");
    	
    	/**
    	 * Flight Routes
    	 */
    	router.OPTIONS().route("/api/add/flight").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/update/flight").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/delete/flight/{flightId}").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/get/flight/{flightId}").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/get/flights").with(CorsHeadersController.class, "routeForOptions");
    	
    	router.POST().route("/api/add/flight").with(FlightController.class, "addFlight");
    	router.POST().route("/api/update/flight").with(FlightController.class, "updateFlight");
    	router.DELETE().route("/api/delete/flight/{flightId}").with(FlightController.class, "deleteFlight");
    	router.GET().route("/api/get/flight/{flightId}").with(FlightController.class, "getFlightByFlightId");
    	router.GET().route("/api/get/flights").with(FlightController.class, "getFlights");
    	
    	/**
    	 * Booking Routes
    	 */
    	router.OPTIONS().route("/api/add/booking").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/update/booking").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/get/booking/{bookingId}").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/delete/booking/{bookingId}").with(CorsHeadersController.class, "routeForOptions");
    	router.OPTIONS().route("/api/get/bookings").with(CorsHeadersController.class, "routeForOptions");
    	
    	router.POST().route("/api/add/booking").with(BookingController.class, "addBooking");
    	router.POST().route("/api/update/booking").with(BookingController.class, "updateBooking");
    	router.DELETE().route("/api/delete/booking/{bookingId}").with(BookingController.class, "deleteBooking");
    	router.GET().route("/api/get/booking/{bookingId}").with(BookingController.class, "getBookingByBookingId");
    	router.GET().route("/api/get/bookings").with(BookingController.class, "getBookings");
    	
    }

}
