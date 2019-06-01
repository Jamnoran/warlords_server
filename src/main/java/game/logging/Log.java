package game.logging;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class Log {

	// If false then only log Log.d logs
	public static boolean verbose = true;

	public static void i(String className, String message){
		if (verbose) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");
			String tab = "	";
			if(className.length() < 6){
				tab = "						";
			}else if (className.length() < 12){
				tab = "					";
			}
			String logMessage = sdf.format(Calendar.getInstance().getTime()) + " [" + className + "]" + tab + message;
			System.out.println(logMessage);
			writeToFile(logMessage);
		}
	}
	public static void d(String className, String message){
		System.out.println(className + ": " + message);
	}


	private static String OS = System.getProperty("os.name").toLowerCase();

	private static void writeToFile(String logMessage) {
		try {
			String fileName = "server.txt";
			File dir = new File("../logs");
			File file = new File (dir, fileName);
			file.getParentFile().mkdirs();

//			File file = new File (fileName);
			PrintWriter writer;
			if ( file.exists() && !file.isDirectory() ) {
				writer = new PrintWriter(new FileOutputStream(file, true));
			}
			else {
				writer = new PrintWriter(file);
			}
			writer.println(logMessage);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
}
