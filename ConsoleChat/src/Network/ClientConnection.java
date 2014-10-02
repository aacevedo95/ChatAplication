package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Command.CommandHandler;

public class ClientConnection implements Receivable{
	
	private CommandHandler cmd;
	private Socket socket;
	private String username;
	private long connectTime;
	private NetworkListener listener;
	private DataOutputStream dos;

	public ClientConnection(CommandHandler c, Socket s, String u) {
		cmd = c;
		setSocket(s);
		setUsername(u);
		connectTime = System.currentTimeMillis();
		listener = new NetworkListener(socket, this, "ClientListener-" + username);
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			//DO CODE
		}
	}
	
	public void disconnect(){
		//LOG
		listener.stop();
		try {
			dos.close();
			socket.close();
		} catch (IOException e) {
			//DO CODE
		}
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
			//DO CODE
		}
	}

	@Override
	public void receiveData(byte[] data) {
		String d = new String(data);
		cmd.process(this, d);
	}
}