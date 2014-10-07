package Command;

import Network.Connection;
import Network.Server;

public class Command_Start extends Command{
	
	public Command_Start(){
		command = "start";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Starts the server";
		arguments = 0;
		adminCommand = true;
	}

	@Override
	public int run(Server server, Connection c, String[] cmd) {
		server.start();
		return RAN_SUCCESSFULY;
	}

}
