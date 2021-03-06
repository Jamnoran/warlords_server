package game.util;

import game.GameServer;
import game.io.Requests.MinionAggroRequest;
import game.io.Requests.UpdateHeroItemPositionRequest;
import game.io.Responses.CombatTextResponse;
import game.logging.Log;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Warlock;
import game.vo.classes.Warrior;

import java.text.MessageFormat;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Created by Eric on 2017-02-02.
 */
public class GameUtil {
	private static final String TAG = GameUtil.class.getSimpleName();
	public static final String COLOR_DAMAGE = "#FFFFFFFF";
	public static final String COLOR_HEAL = "#FF00FF00";
	public static final String COLOR_CRIT = "#FFFF0000";

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


	public void startHordeMinionSpawner(GameServer server) {
		Thread thread = new Thread(() -> {
			while (server.getHordeMinionsLeft() > 0) {
				Point point = getRandomEnemySpawnPoint(server);
				if (point != null) {
					Minion minion = server.spawnMinion(point.getPosX(), point.getPosZ(), point.getPosY());
					minion.setDesiredPositionX(0);
					minion.setDesiredPositionZ(0);
				} else {
					Log.i(TAG, "We did not get a spawn point, something is wrong");
				}
				server.setHordeMinionsLeft(server.getHordeMinionsLeft() -1);
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private Point getRandomEnemySpawnPoint(GameServer server) {
		Point point = null;
		while (point == null) {
			int randomPos = CalculationUtil.getRandomInt(0, (server.getWorld().getSpawnPoints().size() - 1));
			point = server.getWorld().getSpawnPoints().get(randomPos);
			Log.i(TAG, "Trying to find a random spawn point at pos:  " + randomPos + " its of type : " + point.getPointType());
			if (point.getPointType() == Point.SPAWN_POINT) {
				point = null;
			}
		}
		return point;
	}


	public void minionTargetInRange(MinionAggroRequest parsedRequest) {
		if (gameServer != null && parsedRequest.getMinion_id() != null) {
			Minion minion = getMinionById(parsedRequest.getMinion_id(), gameServer.getMinions());
			if (minion != null && parsedRequest.getHero_id() > 0) {
//				Log.i(TAG, "Target is in range for an attack");
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
				}
			}
		}
		return targetHero;
	}

	public static ArrayList<Item> generateLoot(Hero hero) {
		ArrayList<Item> loot = new ArrayList<>();
		ArrayList<Item> items = null;
		if (hero != null) {
			items = DatabaseUtil.getItems(hero.getLevel());
		}

		if(items == null){
			items = new ArrayList<>();
		}
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

	public static Hero getHeroByUserId(String user_id, ArrayList<Hero> heroes) {
		for (Hero hero : heroes) {
			if (hero.getUser_id() != null && hero.getUser_id() == Integer.parseInt(user_id)) {
				return hero;
			}
		}
		return null;
	}

	public static Hero getHeroById(Integer heroId, ArrayList<Hero> heroes) {
		for (Hero hero : heroes) {
			if (hero.getId() == heroId) {
				return hero;
			}
		}
		return null;
	}

	public static Minion getMinionById(Integer minionId, ArrayList<Minion> minions) {
		for (Minion minion : minions) {
			if (minion.getId() == minionId) {
				return minion;
			}
		}
		return null;
	}

	public static void removeMinion(Integer minionId, ArrayList<Minion> minions) {
		Iterator<Minion> ut = minions.iterator();
		while (ut.hasNext()) {
			Minion minion = ut.next();
			if (minion.getId() == minionId) {
				ut.remove();
				removeMinion(minion.getId(), minions);
			}
		}
	}

	private void setTalents(Hero hero, ArrayList<Talent> talents) {
		hero.setTalents(talents);
	}


	public void updateItemPosition(String userId, UpdateHeroItemPositionRequest request) {
		Log.i(TAG, "Updated item " + request.getItemId() + " To pos: " + request.getNewPosition());
		DatabaseUtil.updateHeroItem(request.getItemId(), request.getNewPosition(), request.getEquipped());
		getHeroById(request.getHeroId(), getGameServer().getHeroes()).updateStats();
		getGameServer().sendGameStatus();
	}


	public static void minionAggro(MinionAggroRequest parsedRequest, ArrayList<Minion> minions) {
		Minion minion = GameUtil.getMinionById(parsedRequest.getMinion_id(), minions);
		if (minion != null && minion.getHeroIdWithMostThreat() == null) {
			Log.i(TAG, "This minion had no aggro, add towards this hero [" + parsedRequest.getHero_id() + "] Since first to see it");
			minion.addThreat(new Threat(parsedRequest.getHero_id(), Threat.inRangeThreath, 0, 0));
		}else{
			if (minion != null) {
				Log.i(TAG, "Minion had aggro : " + minion.getHeroIdWithMostThreat());
			} else {
				Log.i(TAG, "Did not find minion with id : " + parsedRequest.getMinion_id());
			}
		}
	}

	public static void updateMinionPositions(ArrayList<Minion> updatedMinions, ArrayList<Minion> minions) {
		// TODO: Do this more efficient
		for (Minion minion : updatedMinions) {
			for (Minion gameMinion : minions) {
				if (minion.getId() == gameMinion.getId()) {
					gameMinion.setPositionX(minion.getPositionX());
					gameMinion.setPositionY(minion.getPositionY());
					gameMinion.setPositionZ(minion.getPositionZ());
				}
			}
		}
	}

	public void dealDamageToMinion(Hero hero, Minion minion, float damage) {
		if (minion.takeDamage(damage)) {
			Log.i(TAG, "Found minion to attack : " + minion.getId() + " Minion died!!!");
			getGameServer().minionDied(hero.getId(), minion.getId());
			// Send stop movement to all attacking this minion
			HeroUtil.stopHero(hero.id, getGameServer());
		} else {
			Log.i(TAG, "Found minion to attack : " + minion.getId() + " new hp is: " + minion.getHp());
			minion.addThreat(new Threat(hero.getId(), 0.0f, damage, 0.0f));
		}
	}


	/**
	 * A hero has clicked a portal, check that all have then start new level
	 *
	 * @param heroId
	 */
	public void clickPortal(int heroId, ArrayList<Hero> heroes) {
		Log.i(TAG, "This hero has clicked on the stairs " + heroId);
		Hero hero = GameUtil.getHeroById(heroId, heroes);
		if (hero != null) {
			hero.setStairsPressed();
		}
		boolean startNewGame = true;
		for (Hero heroInList : heroes) {
			if (!heroInList.isStairsPressed()) {
				startNewGame = false;
			}
		}
		// Start new level
		if (startNewGame) {
			getGameServer().gameLevel = getGameServer().gameLevel + 1;
			getGameServer().startLevel();
		} else {
			Log.i(TAG, "Not all heroes has clicked the portal");
		}
	}


	/**
	 * Loops through the different spawn points that's available to start on
	 * @param world
	 */
	public static Vector3 getFreeStartPosition(World world) {
		Collections.shuffle(world.getSpawnPoints(), new Random(System.nanoTime()));
		for (Point point : world.getSpawnPoints()) {
			if (!point.isUsed() && point.getPointType() == Point.SPAWN_POINT) {
				Vector3 spawnPoint = point.getLocation();
				point.setUsed(true);
				Log.i(TAG, "Found spawnpoint to use: " + point.toString());
				return spawnPoint;
			}
		}
		return null;
	}


	/**
	 * When a player join he needs to have a hero, this makes sure his hero join as the correct class etc.
	 *
	 * @param hero
	 */
	public void addHero(Hero hero, ArrayList<Hero> heroes) {
		getGameServer().sendWorldOperation(hero.getId());
		if (hero.isClass(Hero.WARRIOR)) {
			Log.i(TAG, "Added a warrior");
			Warrior warrior = (Warrior) hero;
			warrior.generateHeroInformation();
			heroes.add(warrior);
		} else if (hero.isClass(Hero.PRIEST)) {
			Log.i(TAG, "Added a priest");
			Priest priest = (Priest) hero;
			priest.generateHeroInformation();
			heroes.add(priest);
		} else if (hero.isClass(Hero.WARLOCK)) {
			Log.i(TAG, "Added a warlock");
			Warlock warlock = (Warlock) hero;
			warlock.generateHeroInformation();
			heroes.add(warlock);
		}
		Log.i(TAG, "Hero joined with this user id: " + hero.getUser_id() + " characters in game: " + heroes.size());
		getGameServer().sendGameStatus();
		CommunicationUtil.sendAbilities("" + hero.getUser_id(), getGameServer());
		CommunicationUtil.sendTalents(hero.getUser_id(), getGameServer());
		HeroUtil.getHeroItems(hero, true, getGameServer());

		getGameServer().setGameStarted();
	}

	public void addSpawnPoints(ArrayList<Point> points) {
		boolean pointAdded = false;
		Log.i(TAG, "Got this many spawnpoints : " + points.size());
		for (Point point : points) {
			if (getGameServer().getWorld().getSpawnPoint(point.getLocation()) == null) {
				Log.i(TAG, "Adding point of type : " + point.getPointType() + " position: " + point.getLocation().toString());
				if (point.getPointType() == Point.SPAWN_POINT) {
					pointAdded = true;
					getGameServer().getWorld().addSpawPoint(point);
				} else if (point.getPointType() == Point.ENEMY_POINT) {
					getGameServer().getWorld().addSpawPoint(point);
					if (getGameServer().getWorld().getWorldType() != World.HORDE) {
						getGameServer().spawnMinion(point.getPosX(), point.getPosZ(), point.getPosY());
					}
				}
			}
		}

		if (getGameServer().getWorld().getWorldType() == World.HORDE) {
			startHordeMinionSpawner(getGameServer());
		}

		if (pointAdded) {
			Log.i(TAG, "Sending new spawn location for heroes");
			for (Hero hero : getGameServer().getHeroes()) {
				Vector3 location = GameUtil.getFreeStartPosition(getGameServer().getWorld());
				hero.setPositionX(location.getX());
				hero.setPositionZ(location.getZ());
				hero.setPositionY(location.getY());
				hero.setDesiredPositionY(location.getY());
				hero.setDesiredPositionX(location.getX());
				hero.setDesiredPositionZ(location.getZ());
				Log.i(TAG, "Setting new location for hero " + hero.getId() + " " + hero.getPositionX() + "x" + hero.getPositionZ() + "y" + hero.getPositionY());
			}
			CommunicationUtil.sendTeleportPlayers(getGameServer());
		}
	}

	public void minionDebuffs(ArrayList<Minion> minions, ArrayList<Hero> heroes) {
		for (Minion minion : minions) {
			if (minion.isAlive()) {
				// Take action for debuffs
				if (minion.getDeBuffs().size() > 0) {
					Log.i(TAG, "Debuffs left : " + minion.getDeBuffs().size());
					Iterator<Buff> iterator = minion.getDeBuffs().iterator();
					while (iterator.hasNext()) {
						Buff debuff = iterator.next();
						long tickTimeConv = Long.parseLong(debuff.tickTime);
						if (tickTimeConv > 0 && (System.currentTimeMillis() >= tickTimeConv)) {
							Log.i(TAG, "It was time for minion debuff changed debuff tick time to " + (debuff.tickTime + debuff.duration) + " from " + debuff.tickTime);
							debuff.tickTime = "" + (tickTimeConv + debuff.duration);
							if (debuff.type == Buff.DOT) {
								dealDamageToMinion(GameUtil.getHeroById(debuff.heroId, heroes), minion, debuff.value);

								CommunicationUtil.sendCombatText(new CombatTextResponse(false, minion.getId(), "" + debuff.value, false, COLOR_DAMAGE), getGameServer());

								debuff.ticks--;
								if (debuff.ticks == 0) {
									iterator.remove();
								}
							}
						}
					}
				}
			}
		}
	}

	public void heroBuffs(ArrayList<Minion> minions, ArrayList<Hero> heroes) {
		Log.i(TAG, "Got tick to go through hero buffs");
		for(Hero hero : heroes){
			if(hero.getBuffs().size() > 0){
				Iterator<Buff> iterator = hero.getBuffs().iterator();
				while (iterator.hasNext()) {
					Buff buff = iterator.next();
					Log.i(TAG, "Hero [" + hero.getId() + "] got buffs : " + hero.getBuffs());
					if (buff.type == Buff.HOT) {
						Log.i(TAG, "Healed for " + buff.value);
						hero.heal(new Amount(buff.value));
						CommunicationUtil.sendCombatText(new CombatTextResponse(true, hero.getId(), "" + buff.value, false, COLOR_HEAL), getGameServer());

						buff.ticks--;
						if (buff.ticks == 0) {
							iterator.remove();
						}
					}
				}
			}
		}
	}
}
