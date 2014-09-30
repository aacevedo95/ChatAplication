package Command;

import Main.Server;

public class Command_HostServer extends Command{

	public Command_HostServer() {
		super();
		command = "hostserver";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Starts a chat server on port " + Server.PORT;
		arguments = 0;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		Server.start();
		return true;
	}

}
