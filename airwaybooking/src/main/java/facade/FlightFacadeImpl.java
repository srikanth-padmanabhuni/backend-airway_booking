package facade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import dto.FlightDto;
import dto.FlightsDto;
import entites.Flights;
import exceptions.InvalidDataException;
import exceptions.InvalidFlightException;
import utils.MappingUtility;
import utils.ValidationUtility;

public class FlightFacadeImpl implements FlightFacade {
	
	private static Logger log = LogManager.getLogger(FlightFacadeImpl.class);
	
	@Inject 
	Provider<EntityManager> entityManagerProvider;
	
	@Inject
	private BookingFacade bookingFacade;

	@Override
	public void createFlight(FlightDto flight) throws InvalidDataException, InvalidFlightException {
		log.debug("createFlight is called");
		ValidationUtility.validateFlightDetails(flight);
		this.isFlightAvailable(flight.getFlightNumber());
		this.saveFlight(flight);
	}

	@Override
	public FlightDto updateFlight(FlightDto flightDto) throws InvalidDataException, InvalidFlightException {
		log.debug("updateFlight is called");
		ValidationUtility.validateFlightDetails(flightDto);
		
		EntityManager entityManager = entityManagerProvider.get();
		Flights flight = this.getFlightByFlightId(flightDto.getId());
		flight.setFlightNumber(flightDto.getFlightNumber());
		flight.setFlightName(flightDto.getFlightName());
		flight.setOrigin(flightDto.getOrigin());
		flight.setDestination(flightDto.getDestination());
		flight.setArrival(flightDto.getArraival());
		flight.setDeparture(flightDto.getDeparture());
		flight.setFare(flightDto.getFare());
		entityManager.persist(flight);
		
		return flightDto;
	}

	@Override
	public void deleteFlight(Long flightId) throws InvalidFlightException {
		log.debug("deleteFlight is called with flightId : " + flightId);
		EntityManager entityManager = entityManagerProvider.get();
		Flights flight = this.getFlightByFlightId(flightId);
		
		String flightNumber = flight.getFlightNumber();
		bookingFacade.deleteBookingsByFlightNumber(flightNumber);
		
		entityManager.remove(flight);
	}

	@Override
	public FlightDto getFlight(Long flightId) throws InvalidFlightException {
		log.debug("getFlight is called with flightId : " + flightId);
		Flights flight = this.getFlightByFlightId(flightId);
		FlightDto flightDto = MappingUtility.mapFlightEntityToFlightDto(flight);
		return flightDto;
	}

	@Override
	public FlightsDto getFlights() {
		log.debug("getFlights is called");
		List<Flights> flights = this.getFlightsList();
		List<FlightDto> flightDtoList = null;
		for(Flights flight : flights) {
			if(flightDtoList == null) {
				flightDtoList = new ArrayList<>();
			}
			FlightDto flightDto = MappingUtility.mapFlightEntityToFlightDto(flight);
			flightDtoList.add(flightDto);
		}
		FlightsDto flightsDto = new FlightsDto();
		flightsDto.setFlights(flightDtoList);
		
		return flightsDto;
	}
	
	@Transactional
	public void saveFlight(FlightDto flightDto) {
		log.debug("saveFlight is called");
		EntityManager entityManager = entityManagerProvider.get();
		Flights flight = new Flights();
		flight.setFlightNumber(flightDto.getFlightNumber());
		flight.setFlightName(flightDto.getFlightName());
		flight.setOrigin(flightDto.getOrigin());
		flight.setDestination(flightDto.getDestination());
		flight.setArrival(flightDto.getArraival());
		flight.setDeparture(flightDto.getDeparture());
		flight.setFare(flightDto.getFare());
		entityManager.persist(flight);
	}
	
	@Override
	@Transactional
	public Flights getFlightByFlightNumber(String flightNumber) throws InvalidFlightException {
		log.debug("getFlightByFlightNumber is called with flightnumber : " + flightNumber);
		EntityManager entityManager = entityManagerProvider.get();
		List<Flights> flights = entityManager.createNamedQuery("Flights.findFlightsByFlightNumber", Flights.class)
												.setParameter("flightNumber", flightNumber)
												.getResultList();
		if(flights == null || flights.size() == 0) {
			throw new InvalidFlightException("No flights available with the given flightNumber : " + flightNumber);
		} else {
			return flights.get(0);
		}
	}
	
	@Transactional
	public boolean isFlightAvailable(String flightNumber) throws InvalidFlightException {
		log.debug("isFlightAvailable is called with flightnumber : " + flightNumber);
		EntityManager entityManager = entityManagerProvider.get();
		Long flightsCount = entityManager.createNamedQuery("Flights.findFlightsCountByFlightNumber", Long.class)
												.setParameter("flightNumber", flightNumber)
												.getSingleResult();
		if(flightsCount != null && !flightsCount.equals(0l)) {
			throw new InvalidFlightException("Flights with the given flightNumber : " + flightNumber + " already available");
		}
		return false;
	}
	
	@Transactional
	public Flights getFlightByFlightId(Long flightId) throws InvalidFlightException {
		log.debug("getFlightByFlightId is called with flightId : " + flightId);
		EntityManager entityManager = entityManagerProvider.get();
		List<Flights> flights = entityManager.createNamedQuery("Flights.findFlightsByFlightId", Flights.class)
												.setParameter("flightNumber", flightId)
												.getResultList();
		if(flights == null || flights.size() == 0) {
			throw new InvalidFlightException("No flights available with the given flightId : " + flightId);
		} else {
			return flights.get(0);
		}
	}

	@Transactional
	public List<Flights> getFlightsList() {
		log.debug("getFlightsList is called");
		EntityManager entityManager = entityManagerProvider.get();
		List<Flights> flights = entityManager.createNamedQuery("Flights.getFlights", Flights.class)
												.getResultList();
		return flights;
	}
}
