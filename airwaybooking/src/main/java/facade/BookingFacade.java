package facade;

import dto.BookingDto;
import dto.BookingsDto;
import exceptions.InvalidBookingException;
import exceptions.InvalidDataException;
import exceptions.InvalidFlightException;
import exceptions.InvalidUserException;
import exceptions.UnauthorizedUserException;
import exceptions.UserAlreadyExistsException;

public interface BookingFacade {

	public BookingDto addBooking(BookingDto bookingDto) throws InvalidDataException, InvalidFlightException, UserAlreadyExistsException, InvalidUserException;
	
	public void deleteBooking(Long bookingId) throws InvalidBookingException;
	
	public BookingDto updateBooking(BookingDto bookingDto) throws InvalidDataException, InvalidFlightException, InvalidUserException, InvalidBookingException;
	
	public BookingDto getBooking(Long bookingId) throws InvalidBookingException;
	
	public BookingsDto getBookings();

	void deleteBookingsByFlightNumber(String flightNumber);

	void deleteBookingsByUserName(String userName);

	void isUserAuthorizedToGetBookings(String userName, Long bookingId) throws UnauthorizedUserException;

	BookingsDto getBookingsByUserName(String userName);
}
