package controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

import argumentextractors.UserDtoExtractor;
import dto.FlightDto;
import dto.FlightsDto;
import dto.UserDto;
import enums.UserRole;
import exceptions.InvalidDataException;
import exceptions.InvalidFlightException;
import facade.FlightFacade;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

public class FlightController {

	private static Logger log = LogManager.getLogger(FlightController.class);
	
	private CorsHeadersController corsHeaders;
	
	@Inject
	private FlightFacade flightFacade;
	
	public Result addFlight(@UserDtoExtractor UserDto userDto,  FlightDto flightDto) {
		log.debug("addFlight is called");
		
		if(!isUserAdmin(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			this.flightFacade.createFlight(flightDto);
			return corsHeaders.addHeaders(Results.status(200).json().render("Flight has registered successfully"));
		} catch (InvalidDataException | InvalidFlightException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while regetesring the flight"));
		}
		
	}
	
	public Result updateFlight(@UserDtoExtractor UserDto userDto,  FlightDto flightDto) {
		log.debug("updateFlight is called");
		
		if(!isUserAdmin(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			FlightDto flight = this.flightFacade.updateFlight(flightDto);
			return corsHeaders.addHeaders(Results.status(200).json().render(flight));
		} catch (InvalidDataException | InvalidFlightException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while updating the flight"));
		}
	}
	
	public Result deleteFlight(@UserDtoExtractor UserDto userDto, @PathParam("flightId") Long flightId) {
		log.debug("deleteFlight is called with flightId : " + flightId);
		if(!isUserAdmin(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			this.flightFacade.deleteFlight(flightId);
			return corsHeaders.addHeaders(Results.status(200).json().render("Flight has deleted successfully"));
		} catch (InvalidFlightException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while deleting the flight"));
		}
	}
	
	public Result getFlightByFlightId(@UserDtoExtractor UserDto userDto, @PathParam("flightId") Long flightId) {
		log.debug(" getFlightByFlightId is called with flightId :" + flightId);
		if(!isUserAvailable(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		
		try {
			FlightDto flightDto = this.flightFacade.getFlight(flightId);
			return corsHeaders.addHeaders(Results.status(200).json().render(flightDto));
		} catch (InvalidFlightException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while fetching the flight"));
		}
	}
	
	public Result getFlights(@UserDtoExtractor UserDto userDto) {
		log.debug("getFlights is called");
		if(!isUserAvailable(userDto)) {
			return corsHeaders.addHeaders(Results.status(401).json().render("Unauthorized"));
		}
		try {
			FlightsDto flights = this.flightFacade.getFlights();
			return corsHeaders.addHeaders(Results.status(200).json().render(flights));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while fetching the flights"));
		}
	}

	private boolean isUserAdmin(UserDto userDto) {
		if(userDto != null && userDto.getRole().equals(UserRole.ADMIN.toString())) {
			return true;
		}
		return false;
	}
	
	private boolean isUserAvailable(UserDto userDto) {
		if(userDto != null && userDto.getRole() != null) {
			return true;
		}
		return false;
	}
}
