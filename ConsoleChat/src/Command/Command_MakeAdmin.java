package Command;

import Main.ClientConnection;
import Main.Logger;
import Main.Server;

public class Command_MakeAdmin extends Command {

	public Command_MakeAdmin() {
		command = "makeadmin";
		usage = new String(String.format("%s user", command)).split(" ");
		description = "Turns a user into an admin with access to more commands";
		arguments = 1;
		adminCommand = true;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		ClientConnection tmp = Server.search(cmd[1]);
		if(tmp!=null){
			Server.setAdmin(tmp.getIndex(), true);
			return true;
		}else{
			String msg = cmd[1] + " is not a valid user";
			if(i!=-1)Server.getList()[i].sendMessage(msg);
			else Logger.sendMessage(msg);
			return false;
		}
	}
}