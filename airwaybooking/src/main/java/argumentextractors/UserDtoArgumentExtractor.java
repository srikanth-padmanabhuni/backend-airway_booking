package argumentextractors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dto.UserDto;
import ninja.Context;

import ninja.params.ArgumentExtractor;
import ninja.session.Session;

public class UserDtoArgumentExtractor implements ArgumentExtractor<UserDto> {
	
	private static Logger log = LogManager.getLogger(UserDtoArgumentExtractor.class);
	
	@Override
	public UserDto extract(Context context) {
		// if we got no session we break:
		UserDto userDto = null;
		Session session = context.getSession();
        if (context.getSession() != null) {
        	log.debug("session available");
        	
        	String idAsString = session.get("id");
        	String userName = session.get("username");
        	String role = session.get("role");
        	String name = session.get("name");
        	String phoneNumber = session.get("phoneNumber");
        	
        	Long id = null;
			try {
				if (context.getSession().get("id") != null && !context.getSession().get("id").isEmpty() && context.getSession().get("id") != null
						&& context.getSession().get("id") != "") {
					id = Long.valueOf(context.getSession().get("id"));
				}
			} catch (NumberFormatException e) {
				log.error("Error : ", e);
			}
        	
        	if (userName == null || role ==null) {
        		log.debug("no session avilable");
        		return null;
        	}
        	
            userDto = new UserDto();
            userDto.setId(id);
            userDto.setName(name);
            userDto.setPhoneNumber(phoneNumber);
            userDto.setRole(role);
            userDto.setUserName(userName);
            
            return userDto;
        }
        log.debug("user Dto is null");
        return userDto;
	}

	@Override
	public Class<UserDto> getExtractedType() {
		return UserDto.class; 
	}

	@Override
	public String getFieldName() {		
		return null;
	}

}
