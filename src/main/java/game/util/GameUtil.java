package game.util;

import game.GameServer;
import game.io.Requests.MinionAggroRequest;
import game.logging.Log;
import game.vo.Hero;
import game.vo.Item;
import game.vo.Minion;
import game.vo.World;

import java.util.ArrayList;

/**
 * Created by Eric on 2017-02-02.
 */
public class GameUtil {
	private static final String TAG = GameUtil.class.getSimpleName();
	private GameServer gameServer;

	public GameServer getGameServer() {
		return gameServer;
	}

	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	public static boolean isWorldType(int type, int worldLevel) {
		if(type == getWorldTypeFromLevel(worldLevel)){
			return true;
		}
		return false;
	}

	public static int getWorldTypeFromLevel(int worldLevel) {
		int worldType = worldLevel % 5;
		if(worldType == 4 || worldType == 0 ){
			worldType = World.CRAWLER;
		}
		return worldType;
	}



	public void minionTargetInRange(MinionAggroRequest parsedRequest) {
		Minion minion = gameServer.getMinionById(parsedRequest.getMinion_id());
		if (minion != null && parsedRequest.getHero_id() > 0) {
			Log.i(TAG, "Target is in range for an attack");
			minion.targetInRangeForAttack = true;
		} else {
			Log.i(TAG, "Target is out of range for an attack");
			if (minion != null) {
				minion.targetInRangeForAttack = false;
			}
		}
	}


	/**
	 * Goes through the list of heroes and returning the hero that has the lowest hp
	 *
	 * @return hero
	 */
	public Hero getHeroWithLowestHp() {
		Hero targetHero = null;
		for (Hero lowestHpHero : gameServer.getHeroes()) {
			if (targetHero == null) {
				targetHero = lowestHpHero;
			} else if (lowestHpHero.getHp() < targetHero.getHp()) {
				targetHero = lowestHpHero;
				Log.i(TAG, "Found a target with lower hp : " + lowestHpHero.getId());
			}
		}
		return targetHero;
	}

	public static ArrayList<Item> generateLoot(Hero hero) {
		ArrayList<Item> loot = new ArrayList<>();

		ArrayList<Item> items = DatabaseUtil.getItems(hero.getLevel());

		// Check if there is enough items to roll otherwise create new items
		if(items.size() < 5){
			Log.i(TAG, "Not enough items to loot through, generate a couple more. " + items.size() + " of 5");
			for(int i = 0; i < (5 - items.size()) ; i++ ){
				Item item = ItemUtil.generateItem(hero.getLevel(), hero);
				Log.i(TAG, "Generated item cause there was not enough in database.");
				items.add(item);
				DatabaseUtil.addItem(item);
			}
		}

		// Roll if hero got these items
		for (Item item : items) {
			int rate = CalculationUtil.getRandomInt(0,1000);
			Log.i(TAG, "Drop rate : " + rate + " /" + Math.round(item.getDropRate() * 1000));
			if(rate <= (item.getDropRate() * 1000)){
				item.generateInfo(true);
				item.setHeroId(hero.getId());
				DatabaseUtil.addLoot(item);
				loot.add(item);
			}
		}
		return loot;
	}
}
