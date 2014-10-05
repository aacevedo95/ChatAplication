package User;

import java.io.Serializable;
import java.util.Properties;

@SuppressWarnings("serial")
public class LoginSession implements Serializable{
	
	private String username;
	private String password;
	private Properties system;
	
	public LoginSession(){
		system = System.getProperties();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getSystem() {
		return system;
	}
}