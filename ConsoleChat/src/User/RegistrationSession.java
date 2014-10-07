package User;

import java.io.Serializable;
import Utility.Logger;
import Window.Window_Register;

@SuppressWarnings("serial")
public class RegistrationSession implements Serializable{
	
	private String username;
	private String password;
	private String email;
	private String group;
	
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
	
	public static RegistrationSession showRegistration(LoginSession ls){
		Window_Register wr = new Window_Register(ls);
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