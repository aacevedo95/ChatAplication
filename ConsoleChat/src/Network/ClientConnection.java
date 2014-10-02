package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import Utility.Logger;
import Command.CommandHandler;

public class ClientConnection implements Receivable{
	
	private CommandHandler cmd;
	private Server server;
	private Socket socket;
	private String username;
	private long connectTime;
	private NetworkListener listener;
	private DataOutputStream dos;
	private boolean admin;

	public ClientConnection(Server srv, CommandHandler c, Socket s, String u) {
		server = srv;
		cmd = c;
		setSocket(s);
		setUsername(u);
		connectTime = System.currentTimeMillis();
		listener = new NetworkListener(socket, this, "ClientListener-" + username);
		setAdmin(false);
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Logger.logError("Could not create a data stream for " + getFormalUsername());
		}
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
		return String.format("'%s'(%s", username, socket.getRemoteSocketAddress().toString());
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		cmd.process(server, this, d);
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}