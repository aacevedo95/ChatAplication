package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import Utility.Logger;

public class NetworkListener implements Runnable{
	
	private Socket socket;
	private Receivable obj;
	private ObjectInputStream ois;
	private String threadName;
	private Thread thread;
	private boolean running;

	public NetworkListener(Socket newSocket, Receivable newReceivable, String newThreadName, ObjectInputStream o) throws IOException {
		Logger.logInfo("Creating new network listener for " + newSocket.getRemoteSocketAddress());
		socket = newSocket;
		ois = o;
		obj = newReceivable;
		threadName = newThreadName;
		running = true;
		thread = new Thread(this, threadName);
		thread.start();
	}
	
	public void stop() throws IOException{
		Logger.logInfo("Stopping network listener for " + socket.getRemoteSocketAddress());
		running = false;
		thread.interrupt();
		thread = null;
		try {
			socket.close();
		} catch (IOException e) {
			Logger.logError("Could not close socket for " + socket.getRemoteSocketAddress());
		}
	}

	@Override
	public void run() {
		while(running){
			DataPacket data;
			try {
				data = (DataPacket)ois.readObject();
				if(data!=null)obj.receiveData(data);
			} catch (ClassNotFoundException e) {
				Logger.logSevere("Class not found in network listener " + threadName);
				e.printStackTrace();
			} catch (IOException e) {
				Logger.logError("IO Exception in network listener " + threadName);
				e.printStackTrace();
			}
		}
		Logger.logInfo("Stopped thread: " + threadName);
	}
}