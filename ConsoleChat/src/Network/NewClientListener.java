package Network;

import java.io.IOException;
import java.net.Socket;
import Utility.Logger;

public class NewClientListener implements Runnable{
	
	private boolean running;
	private Thread thread;
	private Server server;

	@Override
	public void run() {
		Socket socket = null;
		while(running){
			try {
				socket = server.getServerSocket().accept();
				Logger.logInfo("Processing connection " + socket.getRemoteSocketAddress());
				server.receiveClient(socket);
			} catch (IOException e) {
				Logger.logError("Could not accept new client ");
			}
		}
		Logger.logInfo("Incomin client listener has been stopped");
	}
	
	public NewClientListener(Server server){
		running = false;
		this.server = server;
		thread = new Thread(this, "NewClientListener");
	}
	
	public void start(){
		running = true;
		thread.start();
	}
	
	public void stop(){
		running = false;
		thread.interrupt();
		thread = null;
	}
}