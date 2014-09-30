package Command;

import Main.Logger;
import Main.Server;

public abstract class Command {

	protected String[] usage;
	protected String command;
	protected String description;
	protected int arguments;
	protected boolean scmd;
	protected boolean adminCommand;

	private static Command[] cmdlist;
	private static int cmds;

	public static void registerCommands(){
		cmdlist = new Command[4];
		new Command_HostServer();
		new Command_Connect();
		new Command_Quit();
		new Command_Online();
		new Command_StopServer();
		new Command_Message();
		new Command_MakeAdmin();
		compressList();
	}

	private static void add(Command c){
		if(cmdlist.length == cmds){
			Logger.logInfo("Command list is full, rescaling from " + cmds + " to " + (cmds*2));
			Command[] temp = new Command[cmds*2];
			for(int x = 0; x < cmds; x++){
				temp[x] = cmdlist[x];
			}
			cmdlist = temp;
		}
		cmdlist[cmds] = c;
		cmds++;
	}

	private static void compressList(){
		Logger.logInfo("Compressing command list");
		Command[] list = new Command[cmds];
		for(int x = 0; x < cmds; x++){
			list[x] = cmdlist[x];
		}
		cmdlist = list;
	}

	public static void executeCommand(String[] cmd, int i){
		Logger.logInfo("User has executed command " + cmd[0]);
		if(!cmd[0].equals("commands")){
			boolean ran = false;
			for(Command c : cmdlist){
				if(c!=null){
					ran = c.execute(cmd, i);
					if(ran)break;
				}
			}
			if(!ran){
				if(i!=-1)Server.getList()[i].sendMessage(cmd[0] + " is an invalid command, do \'commands\' for a list of valid commands");
				else Logger.sendMessage(cmd[0] + " is an invalid command, do \'commands\' for a list of valid commands");
			}
		}else{
			String msg = (cmds+1) + " valid commands: \n----------------\n";
			for(int x = 0; x < cmds; x++){
				if(cmdlist[x]!=null)msg += x + ". " + cmdlist[x].getUsage() + "\n\tDesc: " + cmdlist[x].getDescription() + "\n";
			}
			if(i!=-1)Server.getList()[i].sendMessage(msg);
			else Logger.sendMessage(msg);
		}
	}

	protected Command(){
		add(this);
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

	protected void setUsage(String[] s){
		usage = s;
	}

	protected String getCommand(){
		return command;
	}

	protected void setCommand(String cmd){
		command = cmd;
	}

	protected String getDescription(){
		return description;
	}

	protected void setDescription(String desc){
		description = desc;
	}

	protected int getArguments(){
		return arguments;
	}

	protected void setArguments(int a){
		arguments = a;
	}

	protected boolean isAdminCommand(){
		return adminCommand;
	}

	protected void setAdminCommand(boolean b){
		adminCommand = b;
	}

	public boolean execute (String[] cmd, int i){
		if(cmd[0].equalsIgnoreCase(command)){
			if((adminCommand && Server.getList()[i].isAdmin())||!adminCommand){
				if(cmd.length >= arguments){
					return run(cmd, i);
				}else{
					Logger.sendMessage("Incorrect command usage, correct usage: " + getUsage());
					return true;
				}
			}else{
				Server.getList()[i].sendMessage("You do not have permission to use command \'" + command + '\'');
				return true;
			}
		}
		return false;
	}

	public abstract boolean run(String[] cmd, int i);
}