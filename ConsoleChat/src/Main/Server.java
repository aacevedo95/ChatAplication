package Main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static final int PORT = 1337;
	
	private static Thread clientListener;
	private static ServerSocket ssocket;
	private static Client[] list;
	
	public static boolean serverIsAlive = false;
	
	public static void start(){
		serverIsAlive = true;
		Logger.sendMessage("Starting server...");
		list = new Client[8];
		try {
			ssocket = new ServerSocket(PORT);
			clientListener = new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						Socket client = ssocket.accept();
						DataInputStream dis = new DataInputStream(client.getInputStream());
						String username = dis.readUTF();
						DataOutputStream dos = new DataOutputStream(client.getOutputStream());
						dos.writeUTF("valid");
						Logger.sendMessage(client.getRemoteSocketAddress() + " connected with username " + username);
						for(int x = 0; x < list.length; x++){
							if(list[x] == null){
								list[x] = new Client(client, username, x);
								Logger.sendMessage("Adding " + username + " at postition " + x);
								chat(username + " has joined the chatroom", "Console");
								break;
							}
						}
					} catch (IOException e) {
						Logger.logError("Could not accept socket - " + e.getMessage());
					}
					run();
				}
			},"NewClientListener");
			clientListener.start();
		} catch (IOException e) {
			Logger.logError("Could not create server - " + e.getMessage());
		}
		Logger.sendMessage("Server started successfuly");
	}
	
	public static void stop(){
		Logger.sendMessage("Stopping server...");
		clientListener.interrupt();
		clientListener = null;
		try {
			for(Client c : list){
				if(c!=null)c.close();
			}
			ssocket.close();
		} catch (IOException e) {
			Logger.logError("Coudl not close server socket - " + e.getMessage());
		}
		Logger.sendMessage("Server stopped");
	}
	
	public static void deleteClient(int index){
		list[index].close();
		list[index]=null;
	}

	public static void chat(String msg, String user){
		for(Client r : list){
			if(r != null){
				if(!r.getUsername().equals(user)){
					try {
						DataOutputStream dos = new DataOutputStream(r.getSocket().getOutputStream());
						dos.writeUTF(user + ": " + msg);
					} catch (IOException e) {
						Logger.logError("Could not connect to output stream - " + e.getMessage());
					}
				}
			}
		}
	}
}