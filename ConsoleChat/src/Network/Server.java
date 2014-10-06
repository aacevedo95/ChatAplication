package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Command.CommandHandler;
import User.UserHandler;
import Utility.Logger;

public class Server extends NetworkingClass{
	
	private ClientConnection[] clientList;
	private CommandHandler clientHandler;
	private UserHandler userHandler;
	private ServerSocket ss;
	private NewClientListener ncl;
	private String serverName;
	private String serverPassword;
	private int clients;
	private int maxClients;
	private boolean iplock;
	
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
		iplock = false;
		clientList = new ClientConnection[maxClients];
		clientHandler = new CommandHandler();
		userHandler = new UserHandler();
		/*
		 * 
		 * REGISTER THE COMMAND LIST
		 * 
		 */
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
		for(int x = 0; x < clients; x++)tmp[x]=clientList[x].getUser().getUsername();
		return tmp;
	}
	
	public void deleteClient(String username){
		for(int x = 0; x < clients; x++){
			if(clientList[x]!=null&&username.equals(clientList[x].getUser().getUsername())){
				Logger.logInfo(username + " found at position " + x);
				clientList[x].disconnect();
				clientList[x] = null;
				Logger.logInfo(username + " deleted, shifting list downwards");
				for(int y = x; y < clients; y++){
					if(y!=clients-1)clientList[y]=clientList[y+1];
					else clientList[y] = null;
				}
				clients--;
				break;
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
		clientList[clients]=c;
		clients++;
	}
	
	public void sendMessage(String msg){
		for(int x = 0; x < clients; x++)clientList[x].sendMessage(msg);
	}

	public CommandHandler getHandler() {
		return clientHandler;
	}

	public void receiveClient(Socket c) {
		Logger.logInfo("Received new client : " + c.getRemoteSocketAddress());
		ClientConnection cc = new ClientConnection(this, c);
		if(cc.isValid())addClient(cc);
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

	public boolean isIpLocked() {
		return iplock;
	}

	public void setIpLock(boolean iplock) {
		this.iplock = iplock;
	}
	
	public CommandHandler getCommandHandler(){
		return clientHandler;
	}

	public UserHandler getUserHandler() {
		return userHandler;
	}
}