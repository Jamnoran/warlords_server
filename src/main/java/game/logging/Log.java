package game.logging;

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
			System.out.println(sdf.format(Calendar.getInstance().getTime()) + " [" + className + "]" + tab + message);
		}
	}

	public static void d(String className, String message){
		System.out.println(className + ": " + message);
	}
}
