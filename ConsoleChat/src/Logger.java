public class Logger {
	
	static void log(String msg){
		System.out.println("Console: " + msg);
	}
	
	static void logInfo(String msg){
		System.out.println("INFO: " + msg);
	}
	
	static void logError(String msg){
		System.out.println("ERROR: " + msg);
	}
}