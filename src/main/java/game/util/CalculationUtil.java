package game.util;

import game.logging.Log;

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
			return true;
		}
		return false;
	}

	public static float calculateDamageAfterReduction(float resistance, float penetration, float damage) {
		float armorAfterPenetration = resistance * (1 - (penetration / 100));
		float damageMultiplier = damage / ( damage + armorAfterPenetration);
		float totalDamage = damage * damageMultiplier;
//		Log.i(TAG, "Caculating damage with this resistance: " + resistance + " This penetration : " + penetration + " and this base damage: " + damage + " total damage : " + totalDamage);
		return totalDamage;
	}
}
