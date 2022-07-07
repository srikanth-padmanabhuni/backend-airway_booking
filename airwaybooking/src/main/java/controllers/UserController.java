package controllers;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

import dto.UserDto;
import exceptions.InvalidDataException;
import exceptions.InvalidUserException;
import exceptions.UnauthorizedUserException;
import exceptions.UserAlreadyExistsException;
import facade.UserFacade;
import ninja.Context;
import ninja.Cookie;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import ninja.utils.NinjaProperties;

public class UserController {

	private static Logger log = LogManager.getLogger(UserController.class);
	
	@Inject
	private UserFacade userFacade;
	
	@Inject
	private NinjaProperties ninjaProperties;
	
	private CorsHeadersController corsHeaders;
	
	public Result signUp(UserDto user) {
		log.debug("signUp is called");
		try {
			this.userFacade.signUp(user);
			return corsHeaders.addHeaders(Results.status(200).json().render("User has registered successfully"));
		} catch (InvalidDataException | UserAlreadyExistsException | InvalidUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while regetesring the User"));
		}
		
	}
	
	public Result signIn(UserDto user, Context context) {
		log.debug("signIn is called");
		try {
			this.userFacade.signIn(user, context);
			return corsHeaders.addHeaders(Results.status(200).json().render("User has Logged in successfully"));
		} catch (InvalidDataException | UserAlreadyExistsException | InvalidUserException
				| UnauthorizedUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while logging in the User"));
		}
	}
	
	public Result getUserByUserName(@PathParam("userName") String userName) {
		log.debug("getUserByUserName is called with userName : " + userName);
		try {
			UserDto user = this.userFacade.getUser(userName);
			return corsHeaders.addHeaders(Results.status(200).json().render(user));
		} catch (UserAlreadyExistsException | InvalidUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while fetching the User"));
		}
	}
	
	public Result getUsers() {
		log.debug("getUsers is called");
		List<UserDto> users = this.userFacade.getUsers();
		return corsHeaders.addHeaders(Results.status(200).json().render(users));
	}
	
	public Result deleteUserByUserName(@PathParam("userName") String userName) {
		log.debug("deleteUserByUserName is called with userName : " + userName);
		try {
			this.userFacade.deleteUser(userName);
			return corsHeaders.addHeaders(Results.status(200).json().render("User has deleted successfully"));
		} catch (UserAlreadyExistsException | InvalidUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while deleting the User"));
		}
	}
	
	public Result updateUser(UserDto user) {
		log.debug("updateUser has been called");
		try {
			UserDto updatedUser = this.userFacade.updateUser(user);
			return corsHeaders.addHeaders(Results.status(200).json().render(updatedUser));
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while deleting the User"));
		}
	}
	
	public Result updatePassword(UserDto user) {
		log.debug("update password is called");
		try {
			this.userFacade.updatePassword(user);
			return corsHeaders.addHeaders(Results.status(200).json().render("User password has been updated successfully"));
		} catch (InvalidUserException | InvalidDataException | UnauthorizedUserException e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(400).json().render(e.getMessage()));
		} catch(Exception e) {
			e.printStackTrace();
			return corsHeaders.addHeaders(Results.status(500).json().render("An error occurred while deleting the User"));
		}
	}
	
	public Result logOut(Context context) {
		log.debug("logOut is called");
		this.userFacade.logOut(context);
		String ninjaSessionName = ninjaProperties.get("application.cookie.prefix");
		Cookie cookie = new Cookie(ninjaSessionName, "INVALID", "", "localhost", 0, "/", true, true);
		return corsHeaders.addHeaders(Results.ok().addCookie(cookie));
	}
}
