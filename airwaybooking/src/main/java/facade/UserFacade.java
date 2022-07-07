package facade;

import java.util.List;

import dto.UserDto;
import entites.Users;
import exceptions.InvalidDataException;
import exceptions.InvalidUserException;
import exceptions.UnauthorizedUserException;
import exceptions.UserAlreadyExistsException;
import ninja.Context;

public interface UserFacade {

	void signUp(UserDto user) throws InvalidDataException, UserAlreadyExistsException, InvalidUserException;
	
	void signIn(UserDto user, Context context) throws InvalidDataException, UserAlreadyExistsException, InvalidUserException, UnauthorizedUserException;
	
	UserDto getUser(String userName) throws UserAlreadyExistsException, InvalidUserException;
	
	List<UserDto> getUsers();
	
	void deleteUser(String userName) throws UserAlreadyExistsException, InvalidUserException;
	
	UserDto updateUser(UserDto user) throws InvalidDataException;
	
	void updatePassword(UserDto userDto) throws InvalidUserException, InvalidDataException, UnauthorizedUserException;
	
	void logOut(Context context);

	Users getUserByUserName(String userName) throws InvalidUserException;
}
