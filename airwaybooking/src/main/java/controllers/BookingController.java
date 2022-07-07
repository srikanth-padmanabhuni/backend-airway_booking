package controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

import argumentextractors.UserDtoExtractor;
import dto.BookingDto;
import dto.BookingsDto;
import dto.UserDto;
import enums.UserRole;
import exceptions.InvalidBookingException;
import exceptions.InvalidDataException;
import exceptions.InvalidFlightException;
import exceptions.InvalidUserException;
import exceptions.UnauthorizedUserException;
import exceptions.UserAlreadyExistsException;
import facade.BookingFacade;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

public class BookingController {

private static Logger log = LogManager.getLogger(BookingController.class);
	
	private CorsHeadersController corsHeaders;
	
	@Inject
	private BookingFacade bookingFacade;
	
	public Result addBooking(@UserDtoExtractor UserDto userDto, BookingDto bookingDto) {
		log.debug("addBooking is called");
		if(!isUserAvailable(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			BookingDto booking = this.bookingFacade.addBooking(bookingDto);
			return corsHeaders.addHeaders(Results.status(200).json().render(booking));
		} catch (InvalidDataException | InvalidFlightException | UserAlreadyExistsException | InvalidUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while adding the booking"));
		}
	}
	
	public Result updateBooking(@UserDtoExtractor UserDto userDto, BookingDto bookingDto) {
		log.debug("updateBooking is called");
		if(!isUserAvailable(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			BookingDto booking = this.bookingFacade.updateBooking(bookingDto);
			return corsHeaders.addHeaders(Results.status(200).json().render(booking));
		} catch (InvalidDataException | InvalidFlightException | InvalidUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while updating the booking"));
		}
	}
	
	public Result deleteBooking(@UserDtoExtractor UserDto userDto, @PathParam("bookingId") Long bookingId) {
		log.debug("deleteBooking is called with b ookingId : " + bookingId);
		if(!isUserAvailable(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			this.bookingFacade.deleteBooking(bookingId);
			return corsHeaders.addHeaders(Results.status(200).json().render("Booking cancelled succesfully"));
		} catch (InvalidBookingException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while cancelling the bookings"));
		}
	}
	
	public Result getBookingByBookingId(@UserDtoExtractor UserDto userDto, @PathParam("bookingId") Long bookingId) {
		log.debug("getBookingByBookingId is called with b ookingId : " + bookingId);
		if(!isUserAvailable(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			this.bookingFacade.isUserAuthorizedToGetBookings(userDto.getUserName(), bookingId);
			BookingDto bookingDto = this.bookingFacade.getBooking(bookingId);
			return corsHeaders.addHeaders(Results.status(200).json().render(bookingDto));
		} catch (InvalidBookingException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch (UnauthorizedUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while fetching the booking"));
		}
	}
	
	public Result getBookings(@UserDtoExtractor UserDto userDto) {
		log.debug("getBookings is called");
		if(userDto == null) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			BookingsDto bookings = new BookingsDto();
			
			if(userDto.getRole().equals(UserRole.ADMIN.toString())) 
				bookings = this.bookingFacade.getBookings();
			if(userDto.getRole().equals(UserRole.PASSANGER.toString())) 
				bookings = this.bookingFacade.getBookingsByUserName(userDto.getUserName());
			
			return corsHeaders.addHeaders(Results.status(200).json().render(bookings));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while fetching the bookings"));
		}
		
	}
	
	private boolean isUserAvailable(UserDto userDto) {
		if(userDto == null || !userDto.getRole().equals(UserRole.PASSANGER.toString())) {
			return false;
		}
		return true;
	}
}
