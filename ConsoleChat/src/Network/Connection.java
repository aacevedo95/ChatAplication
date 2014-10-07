package Network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Connection extends NetworkingClass{
	
	protected Socket socket;
	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;
	protected NetworkListener listener;
	
	public abstract void disconnect();
	public abstract void write(String data);
	public abstract void receiveData(String data);
	public abstract String getUsername();
	public abstract boolean isAdmin();
}