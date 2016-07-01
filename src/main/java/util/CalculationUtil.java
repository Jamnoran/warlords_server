package util;

/**
 * Created by Jamnoran on 01-Jul-16.
 */
public class CalculationUtil {

	public static int getRandomInt(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
}
