package entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "flights")
@NamedQueries({ 
	@NamedQuery(name = "Flights.findFlightsByFlightNumber", query = "SELECT f FROM Flights f WHERE f.flightNumber = :flightNumber"),
	@NamedQuery(name = "Flights.findFlightsCountByFlightNumber", query = "SELECT count(f) FROM Flights f WHERE f.flightNumber = :flightNumber"),
	@NamedQuery(name = "Flights.findFlightsByFlightId", query = "SELECT f FROM Flights f WHERE f.id = :flightId"),
	@NamedQuery(name = "Flights.getFlights", query = "SELECT f FROM Flights f")
})
public class Flights {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String origin;
	private String destination;
	private String flightNumber;
	private Double fare;
	private Long arrival;
	private Long departure;
	private String flightName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}
	public Long getArrival() {
		return arrival;
	}
	public void setArrival(Long arrival) {
		this.arrival = arrival;
	}
	public Long getDeparture() {
		return departure;
	}
	public void setDeparture(Long departure) {
		this.departure = departure;
	}
	public String getFlightName() {
		return flightName;
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	
}
