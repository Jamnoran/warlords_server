package game.logging;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class Log {

	// If false then only log Log.d logs
	public static boolean verbose = true;

	public static void i(String className, String message){
		if (verbose) {
			System.out.println(className + ": " + message);
		}
	}

	public static void d(String className, String message){
		System.out.println(className + ": " + message);
	}
}
