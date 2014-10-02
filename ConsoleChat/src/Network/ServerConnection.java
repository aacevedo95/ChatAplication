package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import Utility.Logger;

public class ServerConnection extends NetworkingClass implements Receivable{
	
	private Socket socket;
	private NetworkListener serverListener;
	private DataOutputStream dos;

	public ServerConnection(String address) {
		try {
			socket = new Socket(address, PORT);
			dos = new DataOutputStream(socket.getOutputStream());
			serverListener = new NetworkListener(socket, this, "ServerListener");
		} catch (UnknownHostException e) {
			Logger.logError("Could not identify host " + address);
		} catch (IOException e) {
			Logger.logError("Encountered an I/O error while attempting to connect to " + address);
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
		/*
		 * DO THE FUCKING CODE
		 */
	}
}