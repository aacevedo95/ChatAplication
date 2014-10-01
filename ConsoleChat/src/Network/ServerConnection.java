package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
			//HANDLE CODE
		} catch (IOException e) {
			//HANDLE CODE
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
			//DO CODE
		}
	}
	
	public void sendMessage(String msg){
		try {
			dos.write(msg.getBytes());
		} catch (IOException e) {
			//DO CODE
		}
	}

	@Override
	public void receiveData(byte[] data) {
	}
}
