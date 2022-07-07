package utils;

import dto.BookingDto;
import dto.FlightDto;
import dto.UserDto;
import entites.Bookings;
import entites.Flights;
import entites.Users;

public class MappingUtility {

	public static UserDto mapUserEntityToUserDto(Users user) {
		
		if(user == null) {
			return null;
		}
		
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setPhoneNumber(user.getPhoneNumber());
		userDto.setUserName(user.getUserName());
		userDto.setRole(user.getRole());
		return userDto;
	}
	
	public static FlightDto mapFlightEntityToFlightDto(Flights flight) {
		if(flight == null) {
			return null;
		}
		
		FlightDto flightDto = new FlightDto();
		flightDto.setId(flight.getId());
		flightDto.setFlightName(flight.getFlightName());
		flightDto.setFlightNumber(flight.getFlightNumber());
		flightDto.setArraival(flight.getArrival());
		flightDto.setDeparture(flight.getDeparture());
		flightDto.setOrigin(flight.getOrigin());
		flightDto.setDestination(flight.getDestination());
		flightDto.setFare(flight.getFare());
		return flightDto;
	}
	
	public static BookingDto mapBookingEntityToBookingDto(Bookings booking) {
		if(booking == null) {
			return null;
		}
		
		BookingDto bookingDto = new BookingDto();
		bookingDto.setBookedOn(booking.getBookedOn());
		bookingDto.setBookingStatus(booking.getBookingStatus());
		bookingDto.setDiscountApplied(booking.getDiscountApplied());
		bookingDto.setId(booking.getId());
		bookingDto.setFlightNumber(booking.getFlightNumber().getFlightNumber());
		bookingDto.setUserName(booking.getUserName().getUserName());
		
		return bookingDto;
	}
}
