package Main;

import Network.Server;
import Network.ServerConnection;
import Utility.Logger;

public class Main{
	
	public static boolean nogui = false;
	public static boolean serverHost = false;
	
	private static Server server;
	
	public static void main(String[] a) throws InterruptedException{
		if(a.length>0){
			System.out.println(a[0]);
			if(a[0].equals("server")){
				serverHost = true;
				if(a.length > 1 && a[1].equals("nogui")){
					nogui = true;
					//NOGUI code
				}else{
					//Server GUI code
				}
				server = new Server();
				server.start();
			}else if(a[0].equals("client")){
				new ServerConnection("localhost");
			}else{
				System.out.println('\'' + a[0] + "' is an invalid argument");
				Thread.sleep(5000);
				shutdown();
			}
		}else{
			System.out.println("No arguments found");
			Thread.sleep(5000);
			shutdown();
		}
	}
	
	public static void shutdown(){
		Logger.disable();
		if(server!=null){
			server.stop();
		}
		System.exit(0);
	}
}