package Command;

import Main.Logger;

public abstract class Command {

	protected String[] usage;
	protected String command;
	protected String description;
	protected int arguments;

	private static Command[] cmdlist;
	private static int cmds;

	public static void registerCommands(){
		cmdlist = new Command[16];
		new Command_HostServer();
		new Command_Connect();
		new Command_Quit();
		new Command_StopServer();
		compressList();
	}

	private static void add(Command c){
		cmdlist[cmds] = c;
	}

	private static void compressList(){
		Command[] list = new Command[cmds];
		for(int x = 0; x < cmds; x++){
			list[x] = cmdlist[x];
		}
		cmdlist = list;
	}

	public static void executeCommand(String cmd){
		String[] scmd = cmd.split(" ");
		if(!scmd[0].equals("commands")){
			boolean ran = false;
			for(Command c : cmdlist){
				if(c!=null){
					c.execute(scmd);
					ran = true;
				}
			}
			if(!ran){
				Logger.sendMessage(scmd[0] + " is an invalid command, do \'commands\' for a list of valid commands");
			}
		}else{
			Logger.sendMessage("Valid commands: \n----------------");
			for(int x = 0; x < cmdlist.length; x++){
				if(cmdlist[x]!=null)Logger.sendMessage(x + ". " + cmdlist[x].getUsage() + "\n\tDesc: " + cmdlist[x].getDescription());
			}
		}
	}

	protected Command(){
		cmds++;
		add(this);
		usage = new String("example").split(" ");
		command = "example";
		description = "Example command";
		arguments = 0;
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

	public boolean execute (String[] cmd){
		if(cmd[0].equalsIgnoreCase(command)){
			if(cmd.length-1 == arguments){
				return run(cmd);
			}else{
				Logger.sendMessage("Incorrect command usage, correct usage: " + getUsage());
				return true;
			}
		}
		return false;
	}

	public abstract boolean run(String[] cmd);
}