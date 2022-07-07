package entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "users")
@NamedQueries({ 
	@NamedQuery(name = "Users.findUserNameCount", query = "SELECT COUNT(u) FROM Users u WHERE u.userName = :uName"),
	@NamedQuery(name = "Users.findUserCountByUserNameNdPassword", query = "SELECT COUNT(u) FROM Users u WHERE u.userName = :uName AND u.password = :password"),
	@NamedQuery(name = "Users.findUserIdByUserName", query = "SELECT u.id FROM Users u WHERE u.userName = :uName"),
	@NamedQuery(name = "Users.findUserByUserName", query = "SELECT u FROM Users u WHERE u.userName = :uName"),
	@NamedQuery(name = "Users.getAllUsers", query = "SELECT u FROM Users u")
})
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String role;
	private String userName;
	private String name;
	private String password;
	private String phoneNumber;
	private Double discountPoints;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Double getDiscountPoints() {
		return discountPoints;
	}
	public void setDiscountPoints(Double discountPoints) {
		this.discountPoints = discountPoints;
	}
	
}
