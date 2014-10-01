package Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkReader {
	
	private DataInputStream dis;
	private Socket socket;

	public NetworkReader(Socket s) {
		socket = s;
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			//DO CODE
		}
	}
	
	public void close(){
		try {
			dis.close();
		} catch (IOException e) {
			//DO CODE
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
			//DO CODE
		}
		return null;
	}
}