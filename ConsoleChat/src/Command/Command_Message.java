package Command;

import Main.ClientConnection;
import Main.Server;

public class Command_Message extends Command{

	public Command_Message() {
		super();
		command = "msg";
		usage = new String(String.format("%s targetuser message", command)).split(" ");
		description = "Sends a private message to a target user";
		arguments = 2;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		boolean sent = false;
		for(ClientConnection c : Server.getList()){
			if(c!=null){
				if(c.getUsername().equals(cmd[1])){
					String msg = "";
					for(int x = 2; x < cmd.length; x++)msg+=cmd[x];
					c.sendMessage(Server.getList()[i].getUsername() + " SAYS: " + msg);
					sent = true;
				}
			}
		}
		if(!sent){
			Server.getList()[i].sendMessage("Could not find the user \'" + cmd[1] + "\'");
		}
		return true;
	}

}
