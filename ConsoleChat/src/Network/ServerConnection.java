package Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	private DataOutputStream dos;
	private ObjectOutputStream oos;
	private Window_Chat window;

	public ServerConnection(String address) {
		try {
			socket = new Socket(address, PORT);
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			Logger.logInfo("Sending login session to server");
			oos.writeObject(LoginSession.showLoginScreen(socket.getRemoteSocketAddress().toString()));
			int reply = dis.read();
			Logger.logInfo("Server replied with id " + reply);
			switch(reply){
			case REGISTRATION_REQUIRED:
				RegistrationSession rs = RegistrationSession.showRegistration();
				oos.writeObject(rs);
				int reply2 = dis.read();
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
		} catch (UnknownHostException e) {
			Logger.logError("Could not identify host " + address);
		} catch (IOException e) {
			Logger.logError("Encountered an I/O error while attempting to connect to " + address);
		}
	}

	private void setupChat() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String[] users = (String[])ois.readObject();
			ois.close();
			serverListener = new NetworkListener(socket, this, "ServerListener");
			window = new Window_Chat();
			window.refreshUsers(users);
		} catch (ClassNotFoundException e) {
			Logger.logSevere("Could not cast object to String Array");
			disconnect();
		} catch (IOException e) {
			Logger.logError("Could not read object from server");
			disconnect();
		}
	}

	public Socket getSocket(){
		return socket;
	}
	
	public void disconnect(){
		sendMessage("/disconnect");
		serverListener.stop();
		try {
			dos.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close the socket for " + socket.getRemoteSocketAddress());
		}
	}
	
	public void sendMessage(String msg){
		try {
			dos.write(msg.getBytes());
		} catch (IOException e) {
			Logger.logError("Could not send data to server");
		}
	}

	@Override
	public void receiveData(byte[] data) {
		String d = new String(data);
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