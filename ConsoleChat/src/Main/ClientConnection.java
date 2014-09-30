package Main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Runnable{

	private Socket socket;
	private String username;
	private Thread listener;
	private int index;
	private DataOutputStream dos;

	private static int users = 0;

	public ClientConnection(){
		username = "USER"+users;
		users++;
		listener = new Thread(this, "UserThread-"+username);
		listener.start();
	}

	public ClientConnection(Socket s, String u, int i){
		this();
		socket = s;
		username = u;
		index = i;
		try {
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			Logger.logError("Coud not create output stream for " + u);
		}
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
			dos.close();
			listener.interrupt();
			listener = null;
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close client socket");
		}
	}
	
	public void sendMessage(String msg){
		try {
			Logger.logInfo("msg -> " + username + ": " + msg);
			dos.writeUTF(msg);
		} catch (IOException e) {
			Logger.logError("Could not send message to " + username);
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
					Server.analyzeMessage(msg, index);
				}
			}
		} catch (IOException e) {
			Logger.logInfo(username + " has disconnected");
			Server.deleteClient(index);
			Server.chatToAll(username + " has disconnected", -1);
			users--;
		}
	}
}