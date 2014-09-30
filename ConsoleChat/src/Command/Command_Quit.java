package Command;

public class Command_Quit extends Command {

	public Command_Quit() {
		command = "quit";
		usage = new String(String.format("%s", command)).split(" ");
		description = "Closes the chat application";
		arguments = 0;
	}

	@Override
	public boolean run(String[] cmd, int i) {
		System.exit(0);
		return true;
	}

}
