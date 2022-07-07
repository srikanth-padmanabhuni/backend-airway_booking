package dto;

public class FlightDto {

	private Long id;
	private String flightNumber;
	private String flightName;
	private Double fare;
	private Long arraival;
	private Long departure;
	private String origin;
	private String destination;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getFlightName() {
		return flightName;
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}
	public Long getArraival() {
		return arraival;
	}
	public void setArraival(Long arraival) {
		this.arraival = arraival;
	}
	public Long getDeparture() {
		return departure;
	}
	public void setDeparture(Long departure) {
		this.departure = departure;
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
	
}
