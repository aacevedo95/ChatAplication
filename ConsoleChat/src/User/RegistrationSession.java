package User;

import java.util.Properties;

import Utility.Logger;
import Window.Window_Register;

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
	
	public static RegistrationSession showRegistrationWindow(){
		Window_Register wr = new Window_Register();
		while(!wr.hasResult()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Logger.logSevere("Regristration session interrupted");
			}
		}
		return wr.getSession();
	}
}