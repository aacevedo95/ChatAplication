package Command;

import Network.Connection;
import Network.Server;

public class Command_Stop extends Command{
	
	public Command_Stop(){
		command = "stop";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Stops the server";
		arguments = 0;
		adminCommand = true;
	}

	@Override
	public int run(Server server, Connection c, String[] cmd) {
		server.stop();
		return RAN_SUCCESSFULY;
	}

}
