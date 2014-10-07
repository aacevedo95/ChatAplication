package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Command.*;
import Network.LocalConnection;
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
				LocalConnection host = new LocalConnection();
				if(a.length > 1 && a[1].equals("nogui")){
					nogui = true;
					Logger.logInfo("Starting server on 'nogui' mode");
					server = new Server();
					CommandHandler cmd = new CommandHandler();
					cmd.add(new Command_Stop());
					cmd.add(new Command_Start());
					cmd.add(new Command_MakeAdmin());
					BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
					String input;
					try {
						while(!(input = kb.readLine()).equals("stop")){
							cmd.process(server, host, input);
						}
						kb.close();
					} catch (IOException e) {
						System.out.println("SERVERSIDE reading failed");
					}
					shutdown();
				}else{
					//Server GUI code
				}
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