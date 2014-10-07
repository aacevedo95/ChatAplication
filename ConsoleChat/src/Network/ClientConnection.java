package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import User.LoginSession;
import User.RegistrationSession;
import User.UserData;
import Utility.Logger;

public class ClientConnection extends NetworkingClass implements Receivable{
	
	private Server server;
	private Socket socket;
	private NetworkListener listener;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private UserData user;
	private Properties properties;
	private long connectTime;
	private boolean isValid;

	public ClientConnection(Server srv, Socket s) {
		isValid = false;
		server = srv;
		setSocket(s);
		connectTime = System.currentTimeMillis();
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Logger.logError("Could not create a data stream for " + getFormalUsername());
			e.printStackTrace();
		}
		getLoginDetails(s);
		if(isValid)try {
				listener = new NetworkListener(socket, this, "ClientListener-" + getFormalUsername(), ois);
			} catch (IOException e) {
				Logger.logInfo("Could not create listener for connection " + getFormalUsername());
				e.printStackTrace();
			}
	}
	
	private void getLoginDetails(Socket s){
		try {
			 ois = new ObjectInputStream(s.getInputStream());
			LoginSession ls = (LoginSession)ois.readObject();
			user = server.getUserHandler().searchByUsername(ls.getUsername());
			if(user==null){
				Logger.logInfo("User was not found, starting registration process");
				registerClient(s);
				if(user==null){
					Logger.logInfo("Client reponded with an invalid registration, ending connection");
					disconnect();
					return;
				}
			}
			if(user.getPassword().equals(ls.getPassword())){
				Logger.logInfo("Client '" + ls.getUsername() + "' was logged in successfuly");
				oos.writeInt(LOGIN_APPROVED);
				oos.flush();
				oos.writeObject(server.getUserList());
				oos.flush();
				isValid = true;
			}else{
				Logger.logInfo("Client attempted to access account '" + ls.getUsername() + "' with the wrong password");
				oos.writeInt(INCORRECT_PASSWORD);
				oos.flush();
				s.close();
			}
		} catch (IOException e) {
			Logger.logError("Could not create a login session input stream");
			e.printStackTrace();
			try {
				s.close();
			} catch (IOException e1) {
				Logger.logError("Could not close socket for " + s.getRemoteSocketAddress());
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			Logger.logSevere("LogginSession class not found");
			e.printStackTrace();
		}
	}
	
	private void registerClient(Socket s){
		try {
			oos.writeInt(REGISTRATION_REQUIRED);
			oos.flush();
			Logger.logInfo("Sent registration request");
			RegistrationSession nu = (RegistrationSession)ois.readObject();
			Logger.logInfo("Received registration response");
			if(nu!=null){
				UserData tmp = server.getUserHandler().searchByUsername(nu.getUsername());
				if(tmp==null){
					Logger.logInfo("Creating new user " + nu.getUsername());
					user = new UserData();
					user.setUsername(nu.getUsername());
					user.setPassword(nu.getPassword());
					user.setEmail(nu.getEmail());
					user.setGroup(nu.getGroup());
					user.setJoinIp(s.getRemoteSocketAddress().toString());
				}else{
					Logger.logInfo(nu.getUsername() + " already exists, cancelling registration");
					oos.writeInt(REGISTRATION_EXISTS);
					oos.flush();
					disconnect();
				}
			}
		} catch (IOException e) {
			Logger.logSevere("Connection reset for '" + s.getRemoteSocketAddress() + '\'');
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.logSevere("Could not find RegistrationSession class");
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		Logger.logInfo("Disconnecting " + getFormalUsername());
		try {
			if(listener!=null)listener.stop();
			if(user!=null)user.setOnline(false);
			if(oos!=null)oos.close();
			if(ois!=null)ois.close();
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close connection for " + getFormalUsername());
		}
	}
	
	private String getFormalUsername(){
		return '('+user.getUsername() +")["+user.getUserid()+"]{"+socket.getRemoteSocketAddress()+'}';
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public UserData getUser() {
		return user;
	}

	public long getConnectTime() {
		return connectTime;
	}
	
	public void write(DataPacket data){
		try {
			oos.writeObject(data.getString());
			oos.flush();
		} catch (IOException e) {
			Logger.logError("Could not send data to " + getFormalUsername());
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg){
		DataPacket packet = new DataPacket();
		packet.setString("0000" + msg);
		write(packet);
	}

	@Override
	public void receiveData(DataPacket data) {
		String d = data.getString();
		server.getCommandHandler().process(server, this, d);
	}

	public boolean isValid() {
		return isValid;
	}

	public Properties getProperties() {
		return properties;
	}
}