package facade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import dto.UserDto;
import entites.Users;
import exceptions.InvalidDataException;
import exceptions.InvalidUserException;
import exceptions.UnauthorizedUserException;
import exceptions.UserAlreadyExistsException;
import ninja.Context;
import utils.MappingUtility;
import utils.ValidationUtility;

public class UserFacadeImpl implements UserFacade {

	private static Logger log = LogManager.getLogger(UserFacadeImpl.class);
	
	@Inject 
	Provider<EntityManager> entityManagerProvider;
	
	@Inject
	private BookingFacade bookingFacade;
	
	
	@Override
	public void signUp(UserDto user) throws InvalidDataException, UserAlreadyExistsException, InvalidUserException {
		log.debug("signUp called with user: " + user);
		ValidationUtility.validateSignUpDetails(user);
		this.isUserExists(user.getUserName());
		this.saveUser(user);
	}

	@Override
	public void signIn(UserDto user, Context context) throws InvalidDataException, UserAlreadyExistsException, InvalidUserException, UnauthorizedUserException {
		log.debug("signIn called with user: " + user);
		ValidationUtility.validateLoginDetails(user);
		this.isUserExists(user.getUserName());
		this.isUserNameNdPasswordValid(user.getName(), user.getPassword());
		Long userId = this.getUserIdByUserName(user.getUserName());
		user.setId(userId);
		this.setSessionDetails(user, context);
	}

	@Override
	public UserDto getUser(String userName) throws UserAlreadyExistsException, InvalidUserException {
		log.debug("getUser called with userName: " + userName);
		this.isUserExists(userName);
		Users users = this.getUserByUserName(userName);
		UserDto userDto = MappingUtility.mapUserEntityToUserDto(users);
		
		return userDto;
	}

	@Override
	public List<UserDto> getUsers() {
		log.debug("getUsers is called");
		List<Users> usersList = this.getAllUsers();
		
		List<UserDto> userDtoList = null;
		for(Users users : usersList) {
			if(userDtoList == null) {
				userDtoList = new ArrayList<>();
			}
			UserDto userDto = MappingUtility.mapUserEntityToUserDto(users);
			userDtoList.add(userDto);
		}
		return userDtoList;
	}

	@Override
	@Transactional
	public void deleteUser(String userName) throws UserAlreadyExistsException, InvalidUserException {
		log.debug("deleteUser called with userName: " + userName);
		this.isUserExists(userName);
		EntityManager entityManager = entityManagerProvider.get();
		Users users = this.getUserByUserName(userName);
		bookingFacade.deleteBookingsByUserName(userName);
		entityManager.remove(users);
	}

	@Override
	@Transactional
	public UserDto updateUser(UserDto user) throws InvalidDataException {
		log.debug("updateUser called with user: " + user);
		ValidationUtility.validateSignUpDetails(user);
		
		EntityManager entityManager = entityManagerProvider.get();
		Users users = this.findUserByUserId(user.getId());
		users.setName(user.getName());
		users.setPhoneNumber(user.getPhoneNumber());
		entityManager.persist(users);
		
		return user;
	}
	
	@Override
	@Transactional
	public void updatePassword(UserDto userDto) throws InvalidUserException, InvalidDataException, UnauthorizedUserException {
		log.debug("updatePassword is called");
		
		if(userDto.getNewPassword() == null || userDto.getNewPassword().trim().length() == 0) {
			throw new InvalidDataException("Please provide new password to update old password");
		}
		this.isUserNameNdPasswordValid(userDto.getUserName(), userDto.getPassword());
		
		EntityManager entityManager = entityManagerProvider.get();
		Users user = this.getUserByUserName(userDto.getUserName());
		user.setPassword(userDto.getNewPassword());
		entityManager.persist(user);
	}
	
	@Override
	public void logOut(Context context) {
		log.debug("logOut is called");
		context.getSession().clear();
		context.getSession().put("id", "");
		context.getSession().put("role", "");
		context.getSession().put("userName", "");
		context.getSession().put("name", "");
		context.getSession().put("phoneNumber", "");
		context.getSession().put("SameSite", "None; Secure");
	}
	
	private void setSessionDetails(UserDto user, Context context) {
		log.debug("setSessionDetails is called");
		context.getSession().put("id", user.getId().toString());
		context.getSession().put("role", user.getRole());
		context.getSession().put("userName", user.getUserName());
		context.getSession().put("name", user.getName());
		context.getSession().put("phoneNumber", user.getPhoneNumber());
	}
	
	
	@Transactional
	public void saveUser(UserDto user) {
		EntityManager entityManager = entityManagerProvider.get();
		Users users = new Users();
		users.setName(user.getName());
		users.setUserName(user.getUserName());
		users.setPhoneNumber(user.getPhoneNumber());
		users.setRole(user.getRole());
		users.setPassword(user.getPassword());
		
		entityManager.persist(users);
	}
	
	@Transactional
	public void isUserExists(String userName) throws UserAlreadyExistsException, InvalidUserException {
		log.debug("isUserNameUnique called with userName : " + userName);
		EntityManager entityManager = entityManagerProvider.get();
		Long count = entityManager.createNamedQuery("Users.findUserNameCount", Long.class)
									.setParameter("uName", userName)
									.getSingleResult();
		Integer usersCount = Integer.parseInt(count.toString());
		
		if(!usersCount.equals(0)) {
			throw new UserAlreadyExistsException("User with username: " + userName + " already exists. Please try with different userName");
		}
		
		if(usersCount.equals(0)) {
			throw new InvalidUserException("No user found with userName: " + userName + ".");
		}
	}
	
	@Transactional
	public void isUserNameNdPasswordValid(String userName, String password) throws UnauthorizedUserException {
		log.debug("isUserNameNdPasswordValid called with userName : " + userName + " | password : " + password);
		EntityManager entityManager = entityManagerProvider.get();
		Long count = entityManager.createNamedQuery("Users.findUserCountByUserNameNdPassword", Long.class)
									.setParameter("uName", userName)
									.setParameter("password", password)
									.getSingleResult();
		
		Integer usersCount = Integer.parseInt(count.toString());
		if(usersCount.equals(0)) {
			throw new UnauthorizedUserException("Invalid username and password. Please try again");
		}
	}

	@Transactional
	public Long getUserIdByUserName(String userName) throws InvalidUserException {
		log.debug("getUserIdByUserName called with userName : " + userName);
		EntityManager entityManager = entityManagerProvider.get();
		Long userId = entityManager.createNamedQuery("Users.findUserIdByUserName", Long.class)
									.setParameter("uName", userName)
									.getSingleResult();
		if(userId == null) {
			throw new InvalidUserException("No user found with userName: " + userName + ".");
		}
		
		return userId;
	}
	
	@Override
	@Transactional
	public Users getUserByUserName(String userName) throws InvalidUserException {
		log.debug("getUserIdByUserName called with userName : " + userName);
		EntityManager entityManager = entityManagerProvider.get();
		List<Users> user = entityManager.createNamedQuery("Users.findUserByUserName", Users.class)
									.setParameter("uName", userName)
									.getResultList();
		if(user == null || user.size() == 0) {
			throw new InvalidUserException("No user found with userName: " + userName + ".");
		}
		
		return user.get(0);
	}
	
	@Transactional
	public List<Users> getAllUsers() {
		log.debug("getAllUsers is called");
		EntityManager entityManager = entityManagerProvider.get();
		List<Users> usersList = entityManager.createNamedQuery("Users.getAllUsers", Users.class)
											.getResultList();
		return usersList;
	}
	
	@Transactional
	public Users findUserByUserId(Long userId) {
		log.debug("getAllUsers is called");
		EntityManager entityManager = entityManagerProvider.get();
		Users user = entityManager.find(Users.class, userId);
		return user;
	}
	
} 
