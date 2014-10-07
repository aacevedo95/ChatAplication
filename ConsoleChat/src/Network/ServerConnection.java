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

public class ServerConnection extends NetworkingClass implements Receivable{
	
	private Socket socket;
	private NetworkListener serverListener;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
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
					disconnect();
					Logger.logInfo("Tried to register an already existing username");
					Logger.showWarning("Username already exists");
					break;
				case LOGIN_APPROVED:
					Logger.logInfo("Successfully connected to " + socket.getRemoteSocketAddress());
					setupChat();
					break;
				}
				break;
			case INCORRECT_PASSWORD:
				disconnect();
				Logger.logInfo("Tried to log in with incorrect password");
				Logger.showWarning("Incorrect password");
				break;
			case SERVER_FULL:
				disconnect();
				Logger.logInfo("Server is full");
				Logger.showWarning("Server is full");
				break;
			case BAD_LOGIN_PACKET:
				disconnect();
				Logger.logSevere("Sent a bad packet");
				Logger.showWarning("Sent a bad packet, seek administrator inmidietly");
				break;
			case IP_LOCK:
				disconnect();
				Logger.logInfo("Connection to server failed, IP locking is enabled");
				Logger.showWarning("IP locking is enabled on this server");
				break;
			case LOGIN_APPROVED:
				Logger.logInfo("Successfully connected to " + socket.getRemoteSocketAddress());
				setupChat();
				break;
			}
			ois.close();
		} catch (UnknownHostException e) {
			Logger.logError("Could not identify host " + address);
			e.printStackTrace();
		} catch (IOException e) {
			Logger.logSevere("Connection reset for " + address);
			e.printStackTrace();
			disconnect();
		}
	}

	private void setupChat() {
		try {
			String[] users = (String[])ois.readObject();
			serverListener = new NetworkListener(socket, this, "ServerListener", ois);
			window = new Window_Chat();
			window.refreshUsers(users);
		} catch (ClassNotFoundException e) {
			Logger.logSevere("Could not cast object to String Array");
			e.printStackTrace();
			disconnect();
		} catch (IOException e) {
			Logger.logError("Could not read object from server");
			e.printStackTrace();
			disconnect();
		}
	}

	public Socket getSocket(){
		return socket;
	}
	
	public void disconnect(){
		try {
			if(serverListener!=null)serverListener.stop();
			if(oos!=null)oos.close();
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close the socket for " + socket.getRemoteSocketAddress());
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg){
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			Logger.logError("Could not send data to server");
			e.printStackTrace();
		}
	}

	@Override
	public void receiveData(DataPacket data) {
		String d = data.getString();
		if(d.startsWith("0000")){
			String msg = "";
			for(int x = 3; x < d.length(); x++){
				msg += d.charAt(x);
			}
			window.write(msg);
		}else if(d.startsWith("0001")){
			String msg = "";
			for(int x = 3; x < d.length(); x++){
				msg += d.charAt(x);
			}
			String[] users = msg.split(",");
			window.refreshUsers(users);
		}
	}
}