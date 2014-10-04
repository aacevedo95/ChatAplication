package User;

import java.io.Serializable;
import java.util.Random;

@SuppressWarnings("serial")
public class UserData implements Serializable{
	
	private String userid;
	private String username;
	private String email;
	private String group;
	private long joinDate;
	private boolean isOnline;
	
	public UserData(){
		userid = generateId();
		joinDate = System.currentTimeMillis();
		setOnline(true);
	}
	
	public String generateId(){
		String str = "";
		Random g = new Random();
		for(int x = 0; x < 12; x++)str+=g.nextInt(9)+'0';
		return str;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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
	
	public long getJoinDate() {
		return joinDate;
	}
	
	public void setJoinDate(long joinDate) {
		this.joinDate = joinDate;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
}
