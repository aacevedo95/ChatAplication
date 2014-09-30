package Main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatSession{

	Socket socket;
	Thread chatListener;
	boolean closed;

	public ChatSession(Socket s){
		closed = false;
		socket = s;
		chatListener = new Thread(new ChatListener(), "ClientChatListener");
		chatListener.start();
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			try {
				while(!closed){
					if(!closed){
						String msg = kb.readLine();
						dos.writeUTF(msg);
					}else{
						dos.close();
						kb.close();
						break;
					}
				}
			} catch (IOException e) {
				Logger.logError("Could not create buffered reader - " + e.getMessage());
			}
		} catch (IOException e1) {
			Logger.logError("Coudl not create output stream - " + e1.getMessage());
		}
	}

	public void close(){
		chatListener.interrupt();
		chatListener = null;
		try {
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close chat listener socket - " + e.getMessage());
		}
	}
	
	private class ChatListener implements Runnable{
		@Override
		public void run() {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				while(!closed){
					String msg;
					if((msg = dis.readUTF()) != null)System.out.println(msg);
				}
			} catch (IOException e) {
				Logger.sendMessage("Disconnected from " + socket.getRemoteSocketAddress().toString());
				close();
				closed = true;
			}
		}
	}
}
