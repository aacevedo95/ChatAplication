package Network;

public class LocalConnection extends Connection{

	@Override
	public void disconnect() {}

	@Override
	public void write(String data) {
		System.out.println(data);
	}

	@Override public void receiveData(String data) {}

	@Override
	public String getUsername() {
		return "SERVERSIDE";
	}

	@Override
	public boolean isAdmin() {
		return true;
	}

}
