package User;

import java.io.Serializable;

import Utility.Logger;

@SuppressWarnings("serial")
public class UserHandler implements Serializable{
	
	private UserData[] list;
	private int users;
	
	public static final int DEFAULT_LIST_SIZE = 32;
	
	public UserHandler(){
		list = new UserData[DEFAULT_LIST_SIZE];
		users = 0;
	}
	
	public void add(UserData u){
		if(u==null)return;
		Logger.logInfo("Registering user " + u.getUsername() + " with id " + u.getUserid());
		if(users == list.length)rescale();
	}
	
	public UserData searchById(String id){
		Logger.logInfo("Searching for id " + id);
		for(int x = 0; x < users; x++)if(list[x].getUserid().equals(id))return list[x];
		return null;
	}
	
	public UserData searchByUsername(String un){
		Logger.logInfo("Searching for user " + un);
		for(int x = 0; x < users; x++)if(list[x].getUsername().equals(un))return list[x];
		return null;
	}
	
	public int searchIndexById(String id){
		Logger.logInfo("Searching index for id " + id);
		for(int x = 0; x < users; x++)if(list[x].getUserid().equals(id))return x;
		return -1;
	}
	
	public void delete(int index){
		for(int x = index; x < users-1; x++)list[x]=list[x+1];
		list[users-1]=null;
		users--;
	}
	
	public void rescale(){
		int newSize = list.length*2;
		Logger.logInfo("Rescaling user list to hold " + newSize + " users");
		UserData[] tmp = new UserData[newSize];
		for(int x = 0; x < users; x++)tmp[x]=list[x];
		list = tmp;
	}

	public UserData[] getList() {
		return list;
	}

	public int getNumberOfUsers() {
		return users;
	}
}