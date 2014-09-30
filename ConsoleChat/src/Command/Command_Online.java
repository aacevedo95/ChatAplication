package Command;

import Main.ClientConnection;
import Main.Server;

public class Command_Online extends Command{

	public Command_Online() {
		super();
		command = "online";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Gets a list of online users";
		arguments = 0;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		String msg = "";
		if(Server.getList().length >= 2)for(ClientConnection c : Server.getList()){
			if(c!=null)msg += c.getUsername() + "\t" + c.getSocket().getRemoteSocketAddress() + "\n";
		}else{
			msg = "No one else is online!";
		}
		Server.getList()[i].sendMessage(msg);
		return true;
	}
}