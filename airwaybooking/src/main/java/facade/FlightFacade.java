package facade;


import dto.FlightDto;
import dto.FlightsDto;
import entites.Flights;
import exceptions.InvalidDataException;
import exceptions.InvalidFlightException;

public interface FlightFacade {

	public void createFlight(FlightDto flight) throws InvalidDataException, InvalidFlightException;
	
	public FlightDto updateFlight(FlightDto flight) throws InvalidDataException, InvalidFlightException;
	
	public void deleteFlight(Long flightId) throws InvalidFlightException;
	
	public FlightDto getFlight(Long flightId) throws InvalidFlightException;
	
	public FlightsDto getFlights();

	Flights getFlightByFlightNumber(String flightNumber) throws InvalidFlightException;
	
}
