package sd2223.trab1.server.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import sd2223.trab1.api.User;
import sd2223.trab1.api.rest.UsersService;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@Singleton
public class UsersResource implements UsersService {

	private final Map<String,User> users = new HashMap<>();

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());
	
	public UsersResource() {
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);
		
		// Check if user data is valid
		if(user.getName() == null || user.getPwd() == null || user.getDisplayName() == null || user.getDomain() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}
		
		// Insert user, checking if name already exists
		if( users.putIfAbsent(user.getName(), user) != null ) {
			Log.info("User already exists.");
			throw new WebApplicationException( Status.CONFLICT );
		}

		return user.getName();
	}
	
	@Override
	public User getUser(String name, String pwd) {
			Log.info("getUser : user = " + name + "; pwd = " + pwd);
			
			// Check if user is valid
			if(name == null || pwd == null) {
				Log.info("Name or Password null.");
				throw new WebApplicationException( Status.BAD_REQUEST );
			}
			
			User user = users.get(name);			
			// Check if user exists 
			if( user == null ) {
				Log.info("User does not exist.");
				throw new WebApplicationException( Status.NOT_FOUND );
			}
			
			//Check if the password is correct
			if( !user.getPwd().equals( pwd)) {
				Log.info("Password is incorrect.");
				throw new WebApplicationException( Status.FORBIDDEN );
			}
			
			return user;
		}

	@Override
	public User updateUser(String name, String pwd, User user) {
		Log.info("updateUser : user = " + name + "; pwd = " + pwd + " ; user = " + user);
		
		// TODO Complete method
		var userAux = users.get(name);
			
		// Check if user exists 
		if( userAux == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
				
		//Check if the password is correct
		if( !userAux.getPwd().equals( pwd)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
				
		//Check if the password is correct
		if( !userAux.getPwd().equals( pwd)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
		else {
			if( !((user.getName() == null) || (user.getPwd() == null)) ) {
				userAux.setName(user.getPwd());
				userAux.setPwd(user.getPwd());
				throw new WebApplicationException( Status.OK );
			}
		}
				
		throw new WebApplicationException( Status.BAD_REQUEST );
	}

	@Override
	public User deleteUser(String name, String pwd) {
		Log.info("deleteUser : user = " + name + "; pwd = " + pwd);
		
		// TODO Complete method
		var user = users.get(name);
				
		// Check if user exists 
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}
				
		//Check if the password is correct
		if( !user.getPwd().equals(pwd)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
				
		users.remove(name);
				
		throw new WebApplicationException( Status.OK );
	}

	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);
		
		// TODO Complete method
		List<User> endList = new ArrayList<User>();
		String auxPattern = pattern.toUpperCase();
		String[] fullName;
		User userAux;
		
		for (Entry<String, User> user : users.entrySet()) {
			userAux = user.getValue();
			fullName = userAux.getName().toUpperCase().split(" ");
			for(String n: fullName) {
				if( n.equals(auxPattern)) {
					userAux.setPwd("");
					endList.add(userAux);
				}
			}
		}
		
		return endList;
	}
	
}
