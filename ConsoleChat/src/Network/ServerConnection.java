package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import User.LoginSession;
import User.RegistrationSession;
import Utility.Logger;
import Window.Window_Chat;

public class ServerConnection extends Connection{
	
	private Socket socket;
	private Window_Chat window;

	public ServerConnection(String address) {
		try {
			socket = new Socket(address, PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			LoginSession ls = LoginSession.showLoginScreen(address);
			oos.writeObject(ls);
			oos.flush();
			Logger.logInfo("Sent login session to server");
			int reply = ois.readInt();
			Logger.logInfo("Server replied with id " + reply);
			switch(reply){
			case REGISTRATION_REQUIRED:
				RegistrationSession rs = RegistrationSession.showRegistration(ls);
				oos.writeObject(rs);
				oos.flush();
				int reply2 = ois.readInt();
				switch(reply2){
				case REGISTRATION_EXISTS:
					Logger.logInfo("Tried to register an already existing username");
					Logger.showWarning("Username already exists");
					disconnect();
					break;
				case LOGIN_APPROVED:
					Logger.logInfo("Successfully connected to " + socket.getRemoteSocketAddress());
					String[] users = (ois.readUTF()).split(",");
					window = new Window_Chat(this);
					window.write("Connected to " + address);
					window.refreshUsers(users);
					listener = new NetworkListener(socket, this, "ServerListener", ois);
					break;
				}
				break;
			case INCORRECT_PASSWORD:
				Logger.logInfo("Tried to log in with incorrect password");
				Logger.showWarning("Incorrect password");
				disconnect();
				break;
			case SERVER_FULL:
				Logger.logInfo("Server is full");
				Logger.showWarning("Server is full");
				disconnect();
				break;
			case BAD_LOGIN_PACKET:
				Logger.logSevere("Sent a bad packet");
				Logger.showWarning("Sent a bad packet, seek administrator inmidietly");
				disconnect();
				break;
			case IP_LOCK:
				Logger.logInfo("Connection to server failed, IP locking is enabled");
				Logger.showWarning("IP locking is enabled on this server");
				disconnect();
				break;
			case LOGIN_APPROVED:
				Logger.logInfo("Successfully connected to " + socket.getRemoteSocketAddress());
				String[] users = (ois.readUTF()).split(",");
				listener = new NetworkListener(socket, this, "ServerListener", ois);
				window = new Window_Chat(this);
				window.refreshUsers(users);
				break;
			}
		} catch (UnknownHostException e) {
			Logger.logError("Could not identify host " + address);
			e.printStackTrace();
		} catch (IOException e) {
			Logger.logSevere("Connection reset for " + address);
			e.printStackTrace();
			disconnect();
		}
	}

	public Socket getSocket(){
		return socket;
	}
	
	@Override
	public void disconnect(){
		try {
			if(oos!=null)oos.close();
			if(ois!=null)ois.close();
			if(listener!=null)listener.stop();
		} catch (IOException e) {
			Logger.logError("Could not close the socket for " + socket.getRemoteSocketAddress());
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(String msg){
		try {
			oos.writeUTF(msg);
			oos.flush();
		} catch (IOException e) {
			Logger.logError("Could not send data to server");
			e.printStackTrace();
		}
	}

	@Override
	public void receiveData(String data) {
		Logger.logInfo("Performing data analysis");
		String msg = "";
		if(data.startsWith("0000")){
			for(int x = 4; x < data.length(); x++){
				msg += data.charAt(x);
			}
			window.write(msg);
		}else if(data.startsWith("0001")){
			for(int x = 4; x < data.length(); x++){
				msg += data.charAt(x);
			}
			String[] users = msg.split(",");
			window.refreshUsers(users);
		}
	}

	@Override public boolean isAdmin() {return false;}

	@Override
	public String getUsername() {
		return "CLIENTSIDE";
	}
}