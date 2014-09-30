package Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Command.Command;

public class Server {

	public static final int PORT = 1337;

	private static Thread clientListener;
	private static ServerSocket ssocket;
	private static ClientConnection[] list;

	public static boolean serverIsAlive = false;

	public static void start(){
		serverIsAlive = true;
		Logger.sendMessage("Starting server...");
		list = new ClientConnection[8];
		try {
			ssocket = new ServerSocket(PORT);
			clientListener = new Thread(new ClientListener(),"NewClientListener");
			clientListener.start();
		} catch (IOException e) {
			Logger.logError("Could not start server - " + e.getMessage());
			serverIsAlive = false;
		}
		Logger.sendMessage("Server started successfuly");
	}

	public static void stop(){
		Logger.sendMessage("Stopping server...");
		clientListener.interrupt();
		clientListener = null;
		try {
			for(ClientConnection c : list){
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

	public static void analyzeMessage(String msg, int i){
		if(msg.charAt(0)=='/'){
			String[] cmd = msg.split(" ");
			cmd[0] = cmd[0].split("/")[1];
			Command.executeCommand(cmd, i);
		}else{
			chatToAll(msg, i);
		}
	}
	
	public static ClientConnection[] getList(){
		return list;
	}

	public static void chatToAll(String msg, int user){
		for(ClientConnection r : list){
			if(r != null){
				if(user == -1 || !r.getUsername().equals(list[user].getUsername())){
					r.sendMessage(((user == -1) ? "Server" : list[user].getUsername()) + ": " + msg);
				}
			}
		}
	}

	private static class ClientListener implements Runnable{
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
						list[x] = new ClientConnection(client, username, x);
						Logger.sendMessage("Adding " + username + " at postition " + x);
						chatToAll(username + " has joined the chatroom", -1);
						break;
					}
				}
			} catch (IOException e) {
				Logger.logError("Could not connect to client - " + e.getMessage());
			}
			run();
		}
	}
	
	public static void setAdmin(int i, boolean b){
		Logger.logInfo("Making " + list[i].getUsername() + " an admin...");
		list[i].setAdmin(b);
	}
	
	public static ClientConnection search(String name){
		for(int x = 0; x < list.length; x++)if(list[x]!=null && list[x].getUsername().equalsIgnoreCase(name))return list[x];
		return null;
	}
}