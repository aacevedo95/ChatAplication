import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatSession{

	Socket socket;
	Thread chatListener;

	public ChatSession(Socket s){
		socket = s;
		Logger.log("Successfully connected to " + s.getRemoteSocketAddress());
		chatListener = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					while(true){
						String msg;
						if((msg = dis.readUTF()) != null)System.out.println(msg);
					}
				} catch (IOException e) {
					Logger.logError("Could not create input stream - " + e.getMessage());
				}
			}
		}, "ClientChatListener");
		chatListener.start();
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			try {
				while(true){
					String msg = kb.readLine();
					dos.writeUTF(msg);
				}
			} catch (IOException e) {
				Logger.logError("Could not create buffered reader - " + e.getMessage());
			}
		} catch (IOException e1) {
			Logger.logError("Coudl not create output stream - " + e1.getMessage());
		}
	}
}
