package Main;
public class Logger {
	
	private static final String CONSOLE_NAME = "Console";
	
	public static void sendMessage(String msg){
		System.out.println(CONSOLE_NAME + ": " + msg);
	}
	
	public static void logInfo(String msg){
		System.out.println("INFO: " + msg);
	}
	
	public static void logError(String msg){
		System.out.println("ERROR: " + msg);
	}
}