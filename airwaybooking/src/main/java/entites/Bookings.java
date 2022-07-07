package entites;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="bookings")
@NamedQueries({ 
	@NamedQuery(name = "Bookings.findBookingByBookingId", query = "SELECT b FROM Bookings b WHERE b.id = :bookingId"),
	@NamedQuery(name = "Bookings.findBookings", query = "SELECT b FROM Bookings b"),
	@NamedQuery(name = "Bookings.findBookingsByUserName", query = "SELECT b FROM Bookings b WHERE b.userName = :userName"),
	@NamedQuery(name = "Bookings.deleteBookingsByFlightNumber", query = "DELETE b FROM Bookings b WHERE b.flightNumber = :flightNumber"),
	@NamedQuery(name = "Bookings.deleteBookingsByUserName", query = "DELETE b FROM Bookings b WHERE b.userName = :userName"),
	@NamedQuery(name = "Bookings.getBookingsCountByUserNameNdBookingId", query = "SELECT COUNT(b) FROM Bookings b WHERE b.userName = :userName AND b.id = :bookingId"),
})
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Users.class, cascade = CascadeType.REMOVE)
	private Users userName;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Flights.class, cascade = CascadeType.REMOVE)
	private Flights flightNumber;
	
	private Long bookedOn;
	private Double discountApplied;
	private String bookingStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Users getUserName() {
		return userName;
	}
	public void setUserName(Users userName) {
		this.userName = userName;
	}
	public Flights getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(Flights flightNumber) {
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
