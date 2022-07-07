package utils;

import dto.BookingDto;
import dto.FlightDto;
import dto.UserDto;
import exceptions.InvalidDataException;

public class ValidationUtility {

	public static void validateSignUpDetails(UserDto user) throws InvalidDataException {
		if(user == null) {
			throw new InvalidDataException("Please provide valid user details to signup the user");
		}
		
		if(user.getUserName() == null || user.getUserName().trim().length() == 0) {
			throw new InvalidDataException("Please provide username to register the user");
		}
		
		if(user.getName() == null || user.getName().trim().length() == 0) {
			throw new InvalidDataException("Please provide name to register the user");
		}
		
		if(user.getPassword() == null || user.getPassword().trim().length() < 8) {
			throw new InvalidDataException("Please provide password to register the user. Security is priority for profile");
		}
		
		if(user.getPhoneNumber() == null || user.getPhoneNumber().trim().length() < 8) {
			throw new InvalidDataException("Please provide phone number to register the user. Mobile number helps you to provide notifications and other information of your bookings");
		}
	}
	
	public static void validateLoginDetails(UserDto user) throws InvalidDataException {
		if(user == null) {
			throw new InvalidDataException("Please provide valid login details to login");
		}
		
		if(user.getUserName() == null || user.getUserName().trim().length() == 0) {
			throw new InvalidDataException("Please provide username to login the user");
		}
		
		if(user.getPassword() == null || user.getPassword().trim().length() < 8) {
			throw new InvalidDataException("Please provide password to login the user");
		}
	}
	
	public static void validateFlightDetails(FlightDto flight) throws InvalidDataException {
		if(flight == null) {
			throw new InvalidDataException("Please provide valid flight details to register flight details");
		}
		
		if(flight.getFlightName() == null || flight.getFlightName().trim().length() == 0) {
			throw new InvalidDataException("Please provide valid flight Name to register flight details. Flight name helps in identifying flight to user easily");
		}
		
		if(flight.getFlightNumber() == null || flight.getFlightNumber().trim().length() == 0) {
			throw new InvalidDataException("Please provide valid flight number to register flight details. Flight number helps in identifying the flight uniquely for application");
		}
		
		if(flight.getOrigin() == null || flight.getOrigin().trim().length() == 0 
				|| flight.getDestination() == null || flight.getDestination().trim().length() == 0) {
			throw new InvalidDataException("Please provide valid flight origin and destination details to register flight details. Origin and destination details helps in identifying the flight journey details");
		}

		
		if(flight.getFare() == null) {
			throw new InvalidDataException("Please provide valid flight fare details to register flight details.");
		}
		
		if(flight.getArraival() == null || flight.getDeparture() == null) {
			throw new InvalidDataException("Please provide valid flight arraival and departure time details to register flight details. Timings helps in finding the journey times easily for user");
		}
	}
	
	public static void validateBookingDetails(BookingDto booking) throws InvalidDataException {
		if(booking == null) {
			throw new InvalidDataException("Please provide valid booking details to book a flight");
		}
		
		if(booking.getFlightNumber() == null || booking.getFlightNumber().trim().length() == 0) {
			throw new InvalidDataException("Please select valid flight to make a booking of seat");
		}
		
	}
}
