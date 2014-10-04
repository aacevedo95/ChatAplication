package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import User.LoginSession;
import User.UserData;
import Utility.Logger;

public class ClientConnection implements Receivable{
	
	private Server server;
	private Socket socket;
	private NetworkListener listener;
	private DataOutputStream dos;
	private UserData user;
	private long connectTime;
	private boolean isValid;
	
	public static final int LOGIN_APPROVED = 0;
	public static final int INCORRECT_PASSWORD = 1;
	public static final int IP_LOCK = 2;
	public static final int BAD_LOGIN_PACKET = 3;
	public static final int REGISTRATION_REQUIRED = 4;
	public static final int SERVER_FULL = 5;

	public ClientConnection(Server srv, Socket s) {
		isValid = false;
		server = srv;
		setSocket(s);
		connectTime = System.currentTimeMillis();
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Logger.logError("Could not create a data stream for " + getFormalUsername());
		}
		user = getLoginDetails(s);
		listener = new NetworkListener(socket, this, "ClientListener-" + user.getUsername());
	}
	
	private UserData getLoginDetails(Socket s){
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			LoginSession ls = (LoginSession)ois.readObject();
			UserData u = server.getUserHandler().searchByUsername(ls.getUsername());
			if(u==null)u = registerClient(s);
			else if(u.getPassword().equals(ls.getPassword())){
				dos.write(LOGIN_APPROVED);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(server.getUserList());
				isValid = true;
			}else{
				dos.write(INCORRECT_PASSWORD);
				s.close();
			}
		} catch (IOException e) {
			Logger.logError("Could not create a login session input stream");
			try {
				s.close();
			} catch (IOException e1) {
				Logger.logError("Could not close socket for " + s.getRemoteSocketAddress());
			}
		} catch (ClassNotFoundException e) {
			Logger.logSevere("LogginSession class not found");
		}
		return null;
	}
	
	private UserData registerClient(Socket s){
		/*
		 * DO CODE
		 */
		return null;
	}
	
	public void disconnect(){
		Logger.logInfo("Disconnecting " + getFormalUsername());
		listener.stop();
		try {
			dos.close();
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close socket for " + getFormalUsername());
		}
	}
	
	private String getFormalUsername(){
		return String.format("'%s'(%s", user.getUsername(), socket.getRemoteSocketAddress().toString());
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
	
	public void write(String msg){
		try {
			dos.write(msg.getBytes());
		} catch (IOException e) {
			Logger.logError("Could not send data to " + getFormalUsername());
		}
	}

	@Override
	public void receiveData(byte[] data) {
		String d = new String(data);
		server.getCommandHandler().process(server, this, d);
	}

	public boolean isValid() {
		return isValid;
	}
}