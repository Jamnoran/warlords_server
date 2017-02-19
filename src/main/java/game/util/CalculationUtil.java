package game.util;

/**
 * Created by Jamnoran on 01-Jul-16.
 */
public class CalculationUtil {

	private static final String TAG = CalculationUtil.class.getSimpleName();

	public static int getRandomInt(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	public static float getRandomFloat(float min, float max){
		return min + (float)(Math.random() * ((max - min) + 1));
	}


	public static boolean rollDice(int percentage) {
		if(getRandomInt(0,100) <= percentage){
//			Log.i(TAG, "Successful dice");
			return true;
		}
		return false;
	}
}
