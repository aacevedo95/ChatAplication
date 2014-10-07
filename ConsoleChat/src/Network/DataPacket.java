package Network;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataPacket implements Serializable{
	
	private int Int;
	private String string;
	
	public int getInt() {
		return Int;
	}
	
	public void setInt(int i) {
		Int = i;
	}
	
	public String getString() {
		return string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
}