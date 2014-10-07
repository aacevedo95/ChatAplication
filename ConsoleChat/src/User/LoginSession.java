package User;

import java.io.Serializable;
import Utility.Logger;
import Window.Window_Login;

@SuppressWarnings("serial")
public class LoginSession implements Serializable{
	
	private String username;
	private String password;
	
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
	
	public static LoginSession showLoginScreen(String address){
		Window_Login wl = new Window_Login(address);
		while(!wl.hasResult()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Logger.logSevere("Login session interrupted");
			}
		}
		return wl.getSession();
	}
}