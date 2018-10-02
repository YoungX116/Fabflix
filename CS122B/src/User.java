/*
 * This User class only has the username field in this example.
 * 
 * However, in the real project, this User class can contain many more things,
 * for example, the user's shopping cart items.
 * 
 */
import java.util.*;
public class User {
	
	final String username;
	HashMap<String, Integer> cart;
	
	public User(String email) {
		this.username = email;
	}
	
	public String getUsername() {
		return this.username;
	}

}