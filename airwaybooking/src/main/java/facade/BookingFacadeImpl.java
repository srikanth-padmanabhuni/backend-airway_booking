package facade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import dto.BookingDto;
import dto.BookingsDto;
import entites.Bookings;
import entites.Flights;
import entites.Users;
import enums.BookingStatus;
import exceptions.InvalidBookingException;
import exceptions.InvalidDataException;
import exceptions.InvalidFlightException;
import exceptions.InvalidUserException;
import exceptions.UnauthorizedUserException;
import exceptions.UserAlreadyExistsException;
import utils.MappingUtility;
import utils.ValidationUtility;

public class BookingFacadeImpl implements BookingFacade {

private static Logger log = LogManager.getLogger(BookingFacadeImpl.class);
	
	@Inject 
	Provider<EntityManager> entityManagerProvider;
	
	@Inject
	private FlightFacade flightFacade;
	
	@Inject
	private UserFacade userFacade;
	
	@Override
	public BookingDto addBooking(BookingDto bookingDto) throws InvalidDataException, InvalidFlightException, InvalidUserException {
		log.debug("addBooking is called");
		
		ValidationUtility.validateBookingDetails(bookingDto);
		this.isValidBooking(bookingDto);
		
		Long bookingId = this.saveBooking(bookingDto);
		bookingDto.setId(bookingId);
		
		return bookingDto;
	}

	@Override
	public void deleteBooking(Long bookingId) throws InvalidBookingException {
		log.debug("deleteBooking is called with bookingId : " + bookingId);
		EntityManager entityManager = entityManagerProvider.get();
		Bookings booking = this.getBookingById(bookingId);
		booking.setBookingStatus(BookingStatus.CANCELLED.toString());
		entityManager.persist(booking);
	}

	@Override
	public BookingDto updateBooking(BookingDto bookingDto) throws InvalidDataException, InvalidFlightException, InvalidUserException, InvalidBookingException {
		log.debug("updateBooking is called");
		
		ValidationUtility.validateBookingDetails(bookingDto);
		this.isValidBooking(bookingDto);
		
		EntityManager entityManager = entityManagerProvider.get();
		
		Flights flight = this.flightFacade.getFlightByFlightNumber(bookingDto.getFlightNumber());
		
		Bookings booking = this.getBookingById(bookingDto.getId());
		booking.setDiscountApplied(bookingDto.getDiscountApplied());
		booking.setBookingStatus(bookingDto.getBookingStatus());
		booking.setFlightNumber(flight);
		entityManager.persist(booking);
		
		return bookingDto;
	}

	@Override
	public BookingDto getBooking(Long bookingId) throws InvalidBookingException {
		log.debug("getBooking is called with bookingId : " + bookingId);
		Bookings booking = this.getBookingById(bookingId);
		BookingDto bookingDto = MappingUtility.mapBookingEntityToBookingDto(booking);
		return bookingDto;
	}

	@Override
	public BookingsDto getBookings() {
		log.debug("getBookings is called");
		List<Bookings> bookingsList  = this.getBookingsList();
		
		List<BookingDto> bookingDtosList = null;
		
		for(Bookings booking: bookingsList) {
			if(bookingDtosList == null) {
				bookingDtosList = new ArrayList<>();
			}
			BookingDto bookingDto = MappingUtility.mapBookingEntityToBookingDto(booking);
			bookingDtosList.add(bookingDto);
		}
		
		BookingsDto bookingsDto = new BookingsDto();
		bookingsDto.setBookings(bookingDtosList);
		
		return bookingsDto;
	}
	
	@Override
	public BookingsDto getBookingsByUserName(String userName) {
		log.debug("getBookings is called");
		List<Bookings> bookingsList  = this.getBookingsListByUserName(userName);
		
		List<BookingDto> bookingDtosList = null;
		
		for(Bookings booking: bookingsList) {
			if(bookingDtosList == null) {
				bookingDtosList = new ArrayList<>();
			}
			BookingDto bookingDto = MappingUtility.mapBookingEntityToBookingDto(booking);
			bookingDtosList.add(bookingDto);
		}
		
		BookingsDto bookingsDto = new BookingsDto();
		bookingsDto.setBookings(bookingDtosList);
		
		return bookingsDto;
	}
	
	@Override
	@Transactional
	public void deleteBookingsByFlightNumber(String flightNumber) {
		log.debug("deleteBookingsByFlightNumber is called");
		EntityManager entityManager = entityManagerProvider.get();
		Long deletedCount = entityManager.createNamedQuery("Bookings.deleteBookingsByFlightNumber", Long.class)
					.setParameter("flightNumber", flightNumber)
					.getSingleResult();
		log.debug("Deleted records count : " + deletedCount);
	}
	
	@Override
	@Transactional
	public void deleteBookingsByUserName(String userName) {
		log.debug("deleteBookingsByFlightNumber is called");
		EntityManager entityManager = entityManagerProvider.get();
		Long deletedCount = entityManager.createNamedQuery("Bookings.deleteBookingsByUserName", Long.class)
					.setParameter("userName", userName)
					.getSingleResult();
		log.debug("Deleted records count : " + deletedCount);
	}
	
	@Override
	@Transactional
	public void isUserAuthorizedToGetBookings(String userName, Long bookingId) throws UnauthorizedUserException {
		log.debug("isUserAuthorizedToGetBookings is called with userName : " + userName + " | bookingId : " + bookingId);
		EntityManager entityManager = entityManagerProvider.get();
		Long count = entityManager.createNamedQuery("Bookings.getBookingsCountByUserNameNdBookingId", Long.class)
								.setParameter("userName", userName)
								.setParameter("bookingId", bookingId)
								.getSingleResult();
		if(count.equals(0l)) {
			throw new UnauthorizedUserException("user is not valid to access this booking details");
		}
		
	}
	
	@Transactional
	public Long saveBooking(BookingDto bookingDto) throws InvalidUserException, InvalidFlightException {
		log.debug("saveBooking is called");
		EntityManager entityManager = entityManagerProvider.get();
		Users user = this.userFacade.getUserByUserName(bookingDto.getUserName());
		Flights flight = this.flightFacade.getFlightByFlightNumber(bookingDto.getFlightNumber());
		
		Bookings booking = new Bookings();
		booking.setBookedOn(System.currentTimeMillis());
		booking.setBookingStatus(BookingStatus.BOOKED.toString());
		booking.setDiscountApplied(bookingDto.getDiscountApplied());
		booking.setUserName(user);
		booking.setFlightNumber(flight);
		
		entityManager.persist(booking);
		
		return booking.getId();
	}
	
	@Transactional
	public Bookings getBookingById(Long bookingId) throws InvalidBookingException {
		log.debug("getBookingById is called with bookingId : " + bookingId);
		EntityManager entityManager = entityManagerProvider.get();
		List<Bookings> bookingsList = entityManager.createNamedQuery("Bookings.findBookingByBookingId", Bookings.class)
										.setParameter("bookingId", bookingId)
										.getResultList();
		if(bookingsList == null || bookingsList.size() == 0) {
			throw new InvalidBookingException("No Bookings found with the given bookingId : " + bookingId);
		} else {
			return bookingsList.get(0);
		}
	}
	
	@Transactional
	public List<Bookings> getBookingsList() {
		log.debug("getBookingsList is called");
		EntityManager entityManager = entityManagerProvider.get();
		List<Bookings> bookingsList = entityManager.createNamedQuery("Bookings.findBookings", Bookings.class)
										.getResultList();
		return bookingsList;
	}
	
	@Transactional
	public List<Bookings> getBookingsListByUserName(String userName) {
		log.debug("getBookingsList is called");
		EntityManager entityManager = entityManagerProvider.get();
		List<Bookings> bookingsList = entityManager.createNamedQuery("Bookings.findBookingsByUserName", Bookings.class)
										.setParameter("userName", userName)
										.getResultList();
		return bookingsList;
	}
	
	public void isValidBooking(BookingDto bookingDto) throws InvalidFlightException, InvalidUserException {
		log.debug("isValidBooking is called");
		this.flightFacade.getFlightByFlightNumber(bookingDto.getFlightNumber());
		this.userFacade.getUserByUserName(bookingDto.getUserName());
	}
	
}
