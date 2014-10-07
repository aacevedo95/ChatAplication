package User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		load();
	}

	public void load(){
		try{
			File file = new File("users.list");
			if(!file.exists())return;
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			UserHandler tmp = (UserHandler)ois.readObject();
			list = tmp.getList();
			users = tmp.getNumberOfUsers();
			ois.close();
		}catch(IOException e){
			Logger.logError("Unidentified IO error ocurred while loading users.list");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.logSevere("UserHandler class not found");
			e.printStackTrace();
		}
	}

	public void save(){
		try {
			File file = new File("users.list");
			if(!file.exists())file.createNewFile();
			FileOutputStream fos;
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			Logger.logError("Could not find user list file");
			e.printStackTrace();
		} catch (IOException e) {
			Logger.logError("Unidentified IO error ocurred while saving user list");
			e.printStackTrace();
		}
	}

	public void add(UserData u){
		if(u==null)return;
		Logger.logInfo("Registering user " + u.getUsername() + " with id " + u.getUserid());
		if(users == list.length)rescale();
	}

	public UserData searchById(String id){
		Logger.logInfo("Searching for id " + id);
		for(int x = 0; x < users; x++)if(list[x].getUserid().equals(id))return list[x];
		Logger.logInfo("Could not find user id " + id);
		return null;
	}

	public UserData searchByUsername(String un){
		Logger.logInfo("Searching for user " + un);
		for(int x = 0; x < users; x++)if(list[x].getUsername().equals(un))return list[x];
		Logger.logInfo("Could not find user " + un);
		return null;
	}

	public int searchIndexById(String id){
		Logger.logInfo("Searching index for id " + id);
		for(int x = 0; x < users; x++)if(list[x].getUserid().equals(id))return x;
		Logger.logInfo("Could not find user id " + id);
		return -1;
	}

	public void delete(int index){
		Logger.logInfo("Deleting the user at position " + index);
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