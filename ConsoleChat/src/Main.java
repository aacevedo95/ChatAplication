import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] a) throws IOException{
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		String in;
		System.out.println("ConsoleChat V.0.0.5_14\t\tMade by Juan Alvarez");
		while(true){
			System.out.print('>');
			if((in = kb.readLine()) != null){
				String[] cmd = in.split(" ");
				if(!command(cmd))System.out.println(cmd[0] + " is an invalid command, use \'commands\' for a list of commands.");
			}
		}
	}

	private static boolean command(String[] cmd){
		if(cmd[0].equals("connect")){
			if(cmd.length > 2){
				try {
					Socket socket = new Socket(cmd[1], 1337);
					if(socket.isConnected()){
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF(cmd[2]);
						DataInputStream dis = new DataInputStream(socket.getInputStream());
						String rsp = dis.readUTF();
						if(rsp.equals("valid")){
							Logger.log("Successfully connected to " + socket.getRemoteSocketAddress());
							new ChatSession(socket);
						}
						else Logger.log("Could not connect to " + socket.getRemoteSocketAddress());
					}
					return true;
				} catch (UnknownHostException e) {
					Logger.logError(("error - " + e.getMessage()));
				} catch (IOException e) {
					Logger.logError("error - " + e.getMessage());
				}
			}else{
				Logger.log("Correct command usage: \"connect {127.0.0.1} {username}\"");
				Logger.log("You need more than one argument for this command");
			}
		}else if(cmd[0].equals("hostserver")){
			Server.start();
			return true;
		}else if(cmd[0].equals("quit")){
			System.exit(0);
			return true;
		}else if(cmd[0].equals("commands")){
			System.out.println("connect {ip} {username}\nhostserver\nstopserver\nquit");
			return true;
		}else if(cmd[0].equals("stopserver")){
			if(Server.serverIsAlive){
				Server.stop();
				Logger.log("Server stopped");
			}
			else Logger.log("Server isn't alive");
			return true;
		}
		return false;
	}
}