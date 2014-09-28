import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{

	private Socket socket;
	private String username;

	private static int users = 0;

	public Client(){
		username = "USER"+users;
		users++;
	}

	public Client(Socket s, String u){
		this();
		socket = s;
		username = u;
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
			Logger.logError("Client class, could not create listener for " + username + "- " + e.getMessage());
		}
	}
}