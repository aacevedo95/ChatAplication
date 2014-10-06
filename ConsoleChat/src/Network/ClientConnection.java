package Network;

import java.io.DataOutputStream;
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
	private DataOutputStream dos;
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
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Logger.logError("Could not create a data stream for " + getFormalUsername());
		}
		user = getLoginDetails(s);
		if(isValid)listener = new NetworkListener(socket, this, "ClientListener-" + user.getUsername());
	}
	
	private UserData getLoginDetails(Socket s){
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			LoginSession ls = (LoginSession)ois.readObject();
			UserData u = server.getUserHandler().searchByUsername(ls.getUsername());
			if(u==null){
				u = registerClient(s);
				if(u==null){
					ois.close();
					s.close();
					return null;
				}
			}
			if(u.getPassword().equals(ls.getPassword())){
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
		try {
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.write(REGISTRATION_REQUIRED);
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			RegistrationSession nu = (RegistrationSession)ois.readObject();
			properties = nu.getSystem();
			if(nu!=null){
				UserData tmp = server.getUserHandler().searchByUsername(nu.getUsername());
				if(tmp==null){
					Logger.logInfo("Creating new user " + nu.getUsername());
					UserData ud = new UserData();
					ud.setUsername(nu.getUsername());
					ud.setPassword(nu.getPassword());
					ud.setEmail(nu.getEmail());
					ud.setGroup(nu.getGroup());
					ud.setJoinIp(s.getRemoteSocketAddress().toString());
					return ud;
				}else{
					Logger.logInfo(nu.getUsername() + " already exists, cancelling registration");
					dos.write(REGISTRATION_EXISTS);
				}
			}
			Logger.logInfo("Could not register new user @ " + s.getRemoteSocketAddress());
			dos.close();
			ois.close();
			s.close();
		} catch (IOException e) {
			Logger.logInfo("Could not create socket stream durring registration for " + s.getRemoteSocketAddress());
		} catch (ClassNotFoundException e) {
			Logger.logSevere("Could not find RegistrationSession class");
		}
		return null;
	}
	
	public void disconnect(){
		Logger.logInfo("Disconnecting " + getFormalUsername());
		listener.stop();
		user.setOnline(false);
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
	
	public void sendMessage(String msg){
		write("0000" + msg.getBytes());
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