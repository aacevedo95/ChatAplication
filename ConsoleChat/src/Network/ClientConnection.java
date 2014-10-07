package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import User.LoginSession;
import User.RegistrationSession;
import User.UserData;
import Utility.Logger;

public class ClientConnection extends Connection{
	
	private Server server;
	private Socket socket;
	private UserData user;
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
					oos.writeInt(BAD_LOGIN_PACKET);
					oos.flush();
					return;
				}else{
					server.getUserHandler().add(user);
				}
			}
			if(user.getPassword().equals(ls.getPassword())){
				Logger.logInfo("Client '" + ls.getUsername() + "' was logged in successfuly");
				oos.writeInt(LOGIN_APPROVED);
				oos.flush();
				oos.writeUTF(server.getUserList());
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
				}
			}
		} catch (IOException e) {
			Logger.logSevere("Connection reset for '" + s.getRemoteSocketAddress() + '\'');
			e.printStackTrace();
			close();
		} catch (ClassNotFoundException e) {
			Logger.logSevere("Could not find RegistrationSession class");
			e.printStackTrace();
		}
	}
	
	public void close(){
		server.deleteClient(getUsername());
	}
	
	@Override
	public void disconnect(){
		Logger.logInfo("Disconnecting " + getFormalUsername());
		try {
			if(listener!=null)listener.stop();
			if(user!=null)user.setOnline(false);
			if(oos!=null)oos.close();
			if(ois!=null)ois.close();
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
	
	@Override
	public void write(String data){
		try {
			oos.writeUTF("0000" + data);
			oos.flush();
		} catch (IOException e) {
			Logger.logError("Could not send data to " + getFormalUsername());
			e.printStackTrace();
		}
	}
	
	public void rawWrite(String msg){
		try {
			oos.writeUTF(msg);
			oos.flush();
		} catch (IOException e) {
			Logger.logError("Could not send raw data through network");
			e.printStackTrace();
		}
	}

	@Override
	public void receiveData(String data) {
		server.getCommandHandler().process(server, this, data);
	}

	public boolean isValid() {
		return isValid;
	}

	@Override
	public boolean isAdmin() {
		return user.isAdmin();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
}