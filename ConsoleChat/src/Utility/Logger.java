package Utility;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Window.Window_Console;

public class Logger{

	private static File log;
	private static PrintWriter printWriter;
	private static Window_Console console;
	private static boolean loggerAlive = false;
	private static boolean saveLogs = false;

	private static void setup(){
		loggerAlive = true;
		if(!new File("logs").exists())new File("logs").mkdir();
		log = new File("logs/log " + getDateAndTimeLogName() + ".log");
		try {
			log.createNewFile();
		} catch (IOException e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Contact administrator!\n" + e.getMessage() + "\n" + e.getLocalizedMessage());
		}
		try {
			printWriter = new PrintWriter(log);
		} catch (IOException e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Contact administrator!\n" + e.getMessage() + "\n" + e.getLocalizedMessage() + e.getStackTrace());
		}
		setupConsole();
	}

	public static void setupConsole(){
		if(!loggerAlive)setup();
		console = new Window_Console();
	}

	public static void close(){
		printWriter.close();
	}

	public static void logError(String msg){
		writeToLog("[ERROR] " + msg);
	}

	public static void logInfo(String msg){
		writeToLog("[INFO] " + msg);
	}

	public static void logSevere(String msg){
		writeToLog("[SEVERE] " + msg);
	}

	private static void writeToLog(String msg){
		if(saveLogs){
			if(!loggerAlive)setup();
			printWriter.println(getDateAndTime() + " " + msg);
			System.out.println(getDateAndTime() + " " + msg);
		}
	}

	public static String getDateAndTime(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getDateAndTimeLogName(){
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getFileName(){
		if(!loggerAlive)setup();
		return log.getName();
	}

	public static void setConsoleVisible(boolean b) {
		if(!loggerAlive)setup();
		console.setVisible(b);
	}
}