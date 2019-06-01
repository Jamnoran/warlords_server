package game.util;

import game.GameServer;
import game.logging.Log;
import game.vo.Hero;
import game.vo.Minion;

import java.util.ArrayList;

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

	public static ArrayList<Minion> getMinionListById(ArrayList<Integer> targetEnemies, GameServer gameServer) {
		ArrayList<Minion> minions = new ArrayList<>();
		if (targetEnemies != null) {
			for(Integer minionId : targetEnemies){
				minions.add(GameUtil.getMinionById(minionId, gameServer.getMinions()));
			}
		}
		return minions;
	}

	public static ArrayList<Hero> getHeroListById(ArrayList<Integer> targetFriendlies, GameServer gameServer) {
		ArrayList<Hero> heroes = new ArrayList<>();
		if (targetFriendlies != null) {
			for(Integer heroId : targetFriendlies){
				heroes.add(GameUtil.getHeroById(heroId, gameServer.getHeroes()));
			}
		}

		if (heroes.size() == 0) {
			heroes.add(gameServer.getGameUtil().getHeroWithLowestHp());
		}
		return heroes;
	}
}
