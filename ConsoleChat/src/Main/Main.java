package Main;

import Network.Server;
import Network.ServerConnection;
import Utility.Logger;

public class Main{
	
	public static boolean nogui = false;
	public static boolean serverHost = false;
	
	public static void main(String[] a) throws InterruptedException{
		if(a.length>0){
			System.out.println(a[0]);
			if(a[0].equals("server")){
				serverHost = true;
				Server server;
				if(a.length > 1 && a[1].equals("nogui")){
					nogui = true;
					//NOGUI code
				}else{
					//Server GUI code
				}
				server = new Server();
				server.setIpLock(false);
				server.setMaxClients(16);
				server.setServerName("DefaultServerName");
				server.start();
			}else if(a[0].equals("client")){
				new ServerConnection("localhost");
			}else{
				System.out.println('\'' + a[0] + "' is an invalid argument");
			}
		}else{
			System.out.println("No arguments found");
			System.console().wait();
		}
	}
	
	public static void shutdown(){
		Logger.close();
		System.exit(0);
	}
}