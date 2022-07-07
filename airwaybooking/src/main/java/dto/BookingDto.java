package dto;

public class BookingDto {

	private Long id;
	private String userName;
	private String flightNumber;
	private Long bookedOn;
	private Double discountApplied;
	private String bookingStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public Long getBookedOn() {
		return bookedOn;
	}
	public void setBookedOn(Long bookedOn) {
		this.bookedOn = bookedOn;
	}
	public Double getDiscountApplied() {
		return discountApplied;
	}
	public void setDiscountApplied(Double discountApplied) {
		this.discountApplied = discountApplied;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	
}
