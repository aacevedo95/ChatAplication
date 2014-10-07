package Command;

import Network.Connection;
import Network.Server;

public abstract class Command {

	protected String[] usage;
	protected String command;
	protected String description;
	protected int arguments;
	protected boolean scmd;
	protected boolean adminCommand;
	
	public static final int RAN_SUCCESSFULY = 0;
	public static final int PERMISSION_ERROR = 1;
	public static final int ARGUMENT_ERROR = 2;
	public static final int STATE_ERROR = 3;

	protected Command(){
		command = "example";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Example command";
		arguments = 0;
		adminCommand = false;
	}

	protected String getUsage(){
		String r = command + " ";
		for(int x = 1; x <= arguments; x++){
			r += "{" + usage[x] + "} ";
		}
		return r;
	}

	protected String getCommand(){
		return command;
	}

	protected String getDescription(){
		return description;
	}

	protected int getArguments(){
		return arguments;
	}

	protected boolean isAdminCommand(){
		return adminCommand;
	}
	
	public int execute(Server server, Connection c, String[] cmd){
		if(cmd.length < arguments)return ARGUMENT_ERROR;
		if(server == null || c == null)return STATE_ERROR;
		if(adminCommand && !c.isAdmin())return PERMISSION_ERROR;
		return run(server, c, cmd);
	}

	public abstract int run(Server server, Connection c, String[] cmd);
}