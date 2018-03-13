package game.util;

import game.GameServer;
import game.io.Requests.MinionAggroRequest;
import game.logging.Log;
import game.vo.Hero;
import game.vo.Item;
import game.vo.Minion;
import game.vo.World;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Eric on 2017-02-02.
 */
public class GameUtil {
	private static final String TAG = GameUtil.class.getSimpleName();
	private GameServer gameServer;
	private static int itemsForEachLevel = 5;

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
		if (gameServer != null && parsedRequest.getMinion_id() != null) {
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
		}else{
			Log.i(TAG, "Something wrong, either gameserver is null or minion_id is");
			if (gameServer == null) {
				Log.i(TAG, "GameServer is null");
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

		if (gameServer != null && gameServer.getHeroes() != null) {
			for (Hero lowestHpHero : gameServer.getHeroes()) {
				if (targetHero == null) {
					targetHero = lowestHpHero;
				} else if (lowestHpHero.getHp() < targetHero.getHp()) {
					targetHero = lowestHpHero;
					Log.i(TAG, "Found a target with lower hp : " + lowestHpHero.getId());
				}
			}
		}else{
			Log.i(TAG, "We did not find heroes or gameserver");
		}
		return targetHero;
	}

	public static ArrayList<Item> generateLoot(Hero hero) {
		ArrayList<Item> loot = new ArrayList<>();
		ArrayList<Item> items = DatabaseUtil.getItems(hero.getLevel());

		// Check if there is enough items to roll otherwise create new items
		if(items.size() < itemsForEachLevel){
			Log.i(TAG, "Not enough items to loot through, generate a couple more. " + items.size() + " of " + itemsForEachLevel);
			for(int i = 0; i < (itemsForEachLevel - items.size()) ; i++ ){
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

	public static String generateItemName(String position) {
		String name = null;
		List<String> adjectives = Arrays.asList("Metal {0}", "Starkiller {0}", "{0} of doom", "{0} of death", "{0} of fluff", "{0} of iron", "{0} of leather" , "{0} of nerds", "{0} of diamond", "{0} of hamsters" , "{0} of destruction" , "{0} of wood");
		List<String> fullNames = Arrays.asList(new String[]{});
		if(position.equals(Item.MAIN_HAND)){
			name = "Sword";
			fullNames = Arrays.asList("Ameranthe", "Death-poker", "Mondser");
		}else if(position.equals(Item.OFF_HAND)){
			name = "Shield";
			fullNames = Arrays.asList("Warmogz", "Hammerlander", "Dumdum");
		}else if (position.equals(Item.HEAD)){
			name = "Helmet";
			fullNames = Arrays.asList("Warmogz", "Hammerlander", "Dumdum");
		}else if (position.equals(Item.SHOULDERS)){
			name = "Shoulders";
			fullNames = Arrays.asList("Warmogz", "Hammerlander", "Dumdum");
		}else if (position.equals(Item.CHEST)){
			name = "Armor";
			fullNames = Arrays.asList("Warmogz", "Hammerlander", "Dumdum");
		}else if (position.equals(Item.LEGS)){
			name = "Leggings";
			fullNames = Arrays.asList("Warmogz", "Hammerlander", "Dumdum");
		}else if (position.equals(Item.BOOTS)){
			name = "Boots";
			fullNames = Arrays.asList("Warmogz", "Hammerlander", "Dumdum");
		}

		int random = CalculationUtil.getRandomInt(0, (adjectives.size() + fullNames.size() -1));
		if (random < adjectives.size()) {
//			name = name + adjectives.get(random);
//			name = String.format(name, adjectives.get(random));
			name = MessageFormat.format(adjectives.get(random), name);
		} else {
			name = fullNames.get((random - adjectives.size()));
		}
		return name;
	}
}
