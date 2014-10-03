package Network;

import java.io.IOException;
import java.net.ServerSocket;

import Command.CommandHandler;
import Utility.Logger;

public class Server extends NetworkingClass{
	
	private String serverName;
	private ClientConnection[] clientList;
	private CommandHandler handler;
	private ServerSocket ss;
	private NewClientListener ncl;
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

	public void receiveClient(ClientConnection c) {
		Logger.logInfo("Received new client " + c.getUsername() + " from " + c.getSocket().getRemoteSocketAddress());
		addClient(c);
	}

	public ServerSocket getServerSocket() {
		return ss;
	}
}