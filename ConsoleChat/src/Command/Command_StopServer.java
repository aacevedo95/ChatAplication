package Command;

import Main.Server;

public class Command_StopServer extends Command {

	public Command_StopServer() {
		super();
		command = "stopserver";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Stops the local chat server";
		arguments = 0;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		Server.stop();
		return true;
	}

}
