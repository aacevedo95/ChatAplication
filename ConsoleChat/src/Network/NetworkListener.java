package Network;

import java.io.IOException;
import java.net.Socket;

public class NetworkListener implements Runnable{
	
	private Socket socket;
	private Receivable obj;
	private NetworkReader nr;
	private String threadName;
	private Thread thread;
	private boolean running;

	public NetworkListener(Socket newSocket, Receivable newReceivable, String newThreadName) {
		//LOG
		socket = newSocket;
		obj = newReceivable;
		threadName = newThreadName;
		running = true;
		nr = new NetworkReader(socket);
		thread = new Thread(this, threadName);
		thread.start();
	}
	
	public void stop(){
		running = false;
		nr.close();
		thread.interrupt();
		thread = null;
		try {
			socket.close();
		} catch (IOException e) {
			//DO CODE
		}
	}

	@Override
	public void run() {
		while(running){
			byte[] data = nr.readBytes();
			if(data!=null)obj.receiveData(data);
		}
		//LOG
	}
}