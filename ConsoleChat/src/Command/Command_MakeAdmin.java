package Command;

import Network.Connection;
import Network.Server;
import User.UserData;

public class Command_MakeAdmin extends Command{

	public Command_MakeAdmin(){
		command = "makeadmin";
		usage = new String(String.format("%s username", command)).split(" ");
		description = "Makes a user an admin";
		arguments = 1;
		adminCommand = true;	
	}

	@Override
	public int run(Server server, Connection c, String[] cmd) {
		String user = cmd[1];
		UserData tmp = server.getUserHandler().searchByUsername(user);
		if(tmp==null){
			c.write(cmd[1] + " is a nonexistant user");
			return Command.ARGUMENT_ERROR;
		}else{
			if(tmp.isAdmin()){
				tmp.setAdmin(false);
				c.write(cmd[1] + " is no longer an admin");
			}else{
				tmp.setAdmin(true);
				c.write(cmd[1] + " is now an admin");
			}
		}
		return RAN_SUCCESSFULY;
	}
}