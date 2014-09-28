package Main;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{

	private Socket socket;
	private String username;
	private Thread listener;
	private int index;

	private static int users = 0;

	public Client(){
		username = "USER"+users;
		users++;
		listener = new Thread(this, "UserThread-"+username);
		listener.start();
	}

	public Client(Socket s, String u, int i){
		this();
		socket = s;
		username = u;
		index = i;
	}

	public Socket getSocket(){
		return socket;
	}

	public void setSocket(Socket s){
		socket = s;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername(String u){
		username = u;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIntex(int i){
		index = i;
	}
	
	public void close(){
		try {
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close client socket");
		}
	}
	
	@Override
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while(true){
				String msg;
				if((msg = dis.readUTF()) != null){
					Logger.logInfo(username + " said \'" + msg + "\'");
					Server.chat(msg, username);
				}
			}
		} catch (IOException e) {
			Logger.logInfo(username + " has disconnected");
			Server.deleteClient(index);
			Server.chat(username + " has disconnected", "Server");
			users--;
		}
	}
}