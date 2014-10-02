package Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import Utility.Logger;

public class NetworkReader {
	
	private DataInputStream dis;
	private Socket socket;

	public NetworkReader(Socket s) {
		socket = s;
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			Logger.logError("Could not create a data input stream for " + socket.getRemoteSocketAddress());
		}
	}
	
	public void close(){
		try {
			dis.close();
		} catch (IOException e) {
			Logger.logError("Could not close input stream for " + socket.getRemoteSocketAddress());
		}
	}
	
	public byte[] readBytes(){
		int size;
		try {
			size = dis.readInt();
			byte[] list = new byte[size];
			for(int x = 0; x < size; x++){
				list[x] = dis.readByte();
			}
			return list;
		} catch (IOException e) {
			Logger.logError("Could not read from data stream for " + socket.getRemoteSocketAddress());
		}
		return null;
	}
}