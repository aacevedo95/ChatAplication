package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Command.CommandHandler;
import Utility.Logger;

public class Server extends NetworkingClass{
	
	private ClientConnection[] clientList;
	private CommandHandler handler;
	private ServerSocket ss;
	private NewClientListener ncl;
	private String serverName;
	private String serverPassword;
	private int clients;
	private int maxClients;
	
	public static final int CONNECTION_ACCEPTED = 0;
	public static final int SERVER_FULL = 1;
	public static final int BANNED_USER = 2;
	public static final int CONNECTION_REJECTED = 3;
	public static final int WRONG_PASSWORD = 4;
	
	private static final int DEFAULT_SIZE = 16;

	public Server() {
		serverName = "DefaultChatServer";
		serverPassword = "";
		maxClients = DEFAULT_SIZE;
		clientList = new ClientConnection[maxClients];
		handler = new CommandHandler();
		handler.registerCommands();
		clients = 0;
	}
	
	public void start(){
		try {
			ss = new ServerSocket(PORT);
			ncl = new NewClientListener(this);
			ncl.start();
		} catch (IOException e) {
			Logger.logError("Could not create server and bind to port " + PORT);
		}
	}
	
	public void stop(){
		ncl.stop();
		for(ClientConnection c : clientList){
			c.disconnect();
		}
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getMaxClients() {
		return maxClients;
	}

	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}

	public int getNumberOfClients() {
		return clients;
	}
	
	public void modifyNumberOfClients(int mod){
		clients+=mod;
	}

	public ClientConnection[] getClientList() {
		return clientList;
	}
	
	public String[] getUserList(){
		String[] tmp = new String[clients];
		for(int x = 0; x < clients; x++)tmp[x]=clientList[x].getUsername();
		return tmp;
	}
	
	public void deleteClient(String username){
		for(int x = 0; x < clients; x++){
			if(clientList[x]!=null&&username.equals(clientList[x].getUsername())){
				Logger.logInfo(username + " found at position " + x);
				clientList[x].disconnect();
				clientList[x] = null;
				for(int y = x; y < clients; y++){
					Logger.logInfo(username + " deleted, shifting list downwards");
					if(y!=clients-1)clientList[y]=clientList[y+1];
					else clientList[y] = null;
				}
			}
		}
	}
	
	public void addClient(ClientConnection c){
		if(c==null)return;
		if(clients == clientList.length){
			ClientConnection[] tmp = new ClientConnection[clientList.length*2];
			for(int x = 0; x < clients; x++)tmp[x]=clientList[x];
			clientList = tmp;
		}
	}

	public CommandHandler getHandler() {
		return handler;
	}

	public void receiveClient(Socket c) {
		Logger.logInfo("Received new client : " + c.getRemoteSocketAddress());
		/*
		 * DO CODE
		 */
	}

	public ServerSocket getServerSocket() {
		return ss;
	}

	public String getServerPassword() {
		return serverPassword;
	}

	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}
}