package User;

import java.util.Properties;

public class RegistrationSession {
	
	private String username;
	private String password;
	private String email;
	private String group;
	private Properties system;
	
	public RegistrationSession(){
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Properties getSystem() {
		return system;
	}
}