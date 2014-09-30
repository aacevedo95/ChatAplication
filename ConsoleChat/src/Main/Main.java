package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Command.Command;

public class Main {

	public static void main(String[] a) throws IOException{
		setup();
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		String in;
		System.out.println("ConsoleChat V.0.0.5_14\t\tMade by Juan Alvarez");
		while(true){
			System.out.print('>');
			in = kb.readLine();
			if(in != null){
				Command.executeCommand(in.split(" "), -1);
			}
		}
	}
	
	private static void setup(){
		Command.registerCommands();
	}
}