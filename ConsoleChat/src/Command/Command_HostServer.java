package Command;

import Main.Server;

public class Command_HostServer extends Command{

	public Command_HostServer() {
		command = "hostserver";
		usage = new String(command + " ").split(" ");
		description = "Starts a chat server on port " + Server.PORT;
		arguments = 0;
	}

	@Override
	public boolean run(String[] cmd) {
		Server.start();
		return true;
	}

}
