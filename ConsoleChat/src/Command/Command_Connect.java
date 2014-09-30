package Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import Main.ChatSession;
import Main.Logger;

public class Command_Connect extends Command{

	public Command_Connect() {
		super();
		command = "connect";
		usage = new String(String.format("%s ip_address username", command)).split(" ");
		description = "Connects to a remote chat server";
		arguments = 2;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		try {
			Socket socket = new Socket(cmd[1], 1337);
			if(socket.isConnected()){
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(cmd[2]);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String rsp = dis.readUTF();
				if(rsp.equals("valid")){
					Logger.sendMessage("Successfully connected to " + socket.getRemoteSocketAddress());
					new ChatSession(socket);
				}
				else Logger.sendMessage("Could not connect to " + socket.getRemoteSocketAddress());
			}
			return true;
		} catch (UnknownHostException e) {
			Logger.logError(("error - " + e.getMessage()));
		} catch (IOException e) {
			Logger.logError("error - " + e.getMessage());
		}
		return true;
	}

}
