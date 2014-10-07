package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import Utility.Logger;

public class NetworkListener implements Runnable{
	
	private Socket socket;
	private Connection connection;
	private ObjectInputStream ois;
	private String threadName;
	private Thread thread;
	private boolean running;

	public NetworkListener(Socket newSocket, Connection con, String newThreadName, ObjectInputStream o) throws IOException {
		Logger.logInfo("Creating new network listener for " + newSocket.getRemoteSocketAddress());
		socket = newSocket;
		ois = o;
		connection = con;
		threadName = newThreadName;
		running = true;
		thread = new Thread(this, threadName);
		thread.start();
	}
	
	public void stop() throws IOException{
		Logger.logInfo("Stopping network listener for " + socket.getRemoteSocketAddress());
		running = false;
		if(thread!=null)thread.interrupt();
		thread = null;
		try {
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close socket for " + socket.getRemoteSocketAddress());
		}
	}

	@Override
	public void run() {
		String msg;
		while(running){
			try {
				if((msg = ois.readUTF())!=null)connection.receiveData(msg);
			} catch (IOException e) {
				Logger.logError("IO Exception in network listener " + threadName);
				e.printStackTrace();
				if(!(connection instanceof ClientConnection))connection.disconnect();
				else ((ClientConnection)connection).close();
			}
		}
		Logger.logInfo("Stopped thread: " + threadName);
	}
}