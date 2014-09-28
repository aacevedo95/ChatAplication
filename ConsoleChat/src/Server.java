import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static Thread clientListener;
	static Thread chatListener;
	static ServerSocket ssocket;
	static Client[] list;

	public static void start(){
		Logger.log("Starting server...");
		list = new Client[8];
		try {
			ssocket = new ServerSocket(1337);
		} catch (IOException e) {
			Logger.logError("Could not create server - " + e.getMessage());
		}
		clientListener = new Thread(new Runnable(){
			@Override
			public void run() {
				Socket client = null;
				try {
					client = ssocket.accept();
					DataInputStream dis = new DataInputStream(client.getInputStream());
					String username = dis.readUTF();
					DataOutputStream dos = new DataOutputStream(client.getOutputStream());
					dos.writeUTF("valid");
					Logger.log(client.getRemoteSocketAddress() + " connected with username " + username);
					for(int x = 0; x < list.length; x++){
						if(list[x] == null){
							list[x] = new Client(client, username);
							Thread t = new Thread(list[x], username + "LThread");
							t.start();
							Logger.log("Adding " + username + " at postition " + x);
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
		Logger.log("Server started successfuly");
	}

	public static void chat(String msg, String user){
		for(Client r : list){
			if(r != null){
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