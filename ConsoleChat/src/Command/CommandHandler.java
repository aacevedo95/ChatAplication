package Command;

import Network.Connection;
import Network.Server;
import Utility.Logger;

public class CommandHandler {
	
	public int commands = 0;
	public Command[] list = new Command[16];
	
	public void add(Command c){
		Logger.logInfo("Registering command " + c.getCommand());
		if(list.length == commands){
			Logger.logInfo("Command register is full, rescaling...");
			Command[] tmp = new Command[commands*2];
			for(int x = 0; x < commands; x++){
				tmp[x] = list[x];
			}
			list = tmp;
		}
		for(int x = 0; x < list.length; x++){
			if(list[x]==null){
				Logger.logInfo("Empty register slot found at position " + x + ", assigning new command");
				list[x] = c;
				break;
			}
		}
		commands++;
	}

	public void process(Server server, Connection con, String data) {
		Logger.logInfo("Processing command...");
		if(data.charAt(0)=='/'){
			String[] cmdstr = data.split(" ");
			cmdstr[0] = cmdstr[0].split("/")[1];
			boolean found = false;
			for(Command c : list){
				if(c!=null && c.getCommand().equals(cmdstr[0])){
					int runtime = c.execute(server, con, cmdstr);
					switch(runtime){
					case Command.RAN_SUCCESSFULY:
						Logger.logInfo("Executed command " + c.getCommand() + " successfuly");
						found = true;
						break;
					case Command.PERMISSION_ERROR:
						Logger.logInfo(con.getUsername() + " tried to use command " + c.getCommand());
						con.write("You do not have permission to use " + c.getCommand());
						found = true;
						break;
					case Command.ARGUMENT_ERROR:
						Logger.logInfo(con.getUsername() + " passed an invalid amount of arguments to command " + c.getCommand());
						con.write("You passed an invalid amount of arguments to command " + c.getCommand() + ", correct usage: " + c.getUsage());
						found = true;
						break;
					case Command.STATE_ERROR:
						Logger.logSevere("A command state error ocurred");
						found = true;
						break;
					default:
						Logger.logSevere("Could not process command");
					}
				}
			}
			if(!found)con.write('\'' + cmdstr[0] + "' is an invalid command");
		}else{
			server.sendMessage((con.isAdmin() ? '*' : "") + con.getUsername() + " : " + data);
		}
	}
}