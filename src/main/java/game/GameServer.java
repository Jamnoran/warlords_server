package game;

import com.google.gson.Gson;
import game.logging.Log;
import game.spells.*;
import game.io.*;
import game.util.CalculationUtil;
import game.util.DatabaseUtil;
import game.util.SpellUtil;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Warlock;
import game.vo.classes.Warrior;
import game.io.MessageResponse;

import java.util.*;

import static java.lang.Thread.sleep;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameServer {
	private static final String TAG = GameServer.class.getSimpleName();

	private int HORDE_MINIONS = 10;

	private boolean gameRunning = true;
	private boolean gameStarted = false;
	private ServerDispatcher server;

	private int minionCount = 0;
	private int hordeMinionsLeft = 0;
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();
	private ArrayList<Message> messages = new ArrayList<>();
	private World world;
	private int gameLevel = 1;
	private SpellUtil spellUtil;


	public GameServer(ServerDispatcher server) {
		Log.i(TAG, "Game server is initilized, creating world");
		this.server = server;
		createWorld();
		startGameTicks();
		spellUtil = new SpellUtil();
	}


	/**
	 * Creates the world and all in it, minions, walls, lighting, stairs up and down.
	 */
	private void createWorld() {
		// Call World.generate()
		world = new World().generate(this, 100, 100, gameLevel);
		hordeMinionsLeft = HORDE_MINIONS;
		Log.i(TAG, "World generated : " + world.toString());
	}


	/**
	 * When a player join he needs to have a hero, this makes sure his hero join as the correct class etc.
	 *
	 * @param hero
	 */
	public void addHero(Hero hero) {
		sendWorldOperation(hero.getId());
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
		sendGameStatus();
		sendAbilities("" + hero.getUser_id());
		sendTalents(hero.getUser_id());

		if (!gameStarted) {
			gameStarted = true;
		}
	}

	/**
	 * Loops through the different spawn points thats available to start on
	 */
	private Vector3 getFreeStartPosition() {
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

	public void addSpawnPoints(ArrayList<Point> points) {
		boolean pointAdded = false;
		Log.i(TAG, "Got this many spawnpoints : " + points.size());
		for (Point point : points) {
			if (world.getSpawnPoint(point.getLocation()) == null) {
				Log.i(TAG, "Adding point of type : " + point.getPointType() + " position: " + point.getLocation().toString());
				if (point.getPointType() == Point.SPAWN_POINT) {
					pointAdded = true;
					world.addSpawPoint(point);
				} else if (point.getPointType() == Point.ENEMY_POINT) {
					world.addSpawPoint(point);
					if (world.getWorldType() != World.HORDE) {
						spawnMinion(point.getPosX(), point.getPosZ(), point.getPosY());
					}
				}
			}
		}

		if (world.getWorldType() == World.HORDE) {
			startHordeMinionSpawner();
		}

		if (pointAdded) {
			Log.i(TAG, "Sending new spawn location for heroes");
			for (Hero hero : heroes) {
				//if (hero.getPositionX() == 0.0f && hero.getPositionZ() == 0.0f) {
				Vector3 location = getFreeStartPosition();
				hero.setPositionX(location.getX());
				hero.setPositionZ(location.getZ());
				hero.setPositionY(location.getY());
				hero.setDesiredPositionY(location.getY());
				hero.setDesiredPositionX(location.getX());
				hero.setDesiredPositionZ(location.getZ());
				Log.i(TAG, "Setting new location for hero " + hero.getId() + " " + hero.getPositionX() + "x" + hero.getPositionZ() + "y" + hero.getPositionY());
				//}
			}
			sendTeleportPlayers();
		}
	}

	private void startHordeMinionSpawner() {
		Thread thread = new Thread(() -> {
			while (hordeMinionsLeft > 0) {
				Point point = getRandomEnemySpawnPoint();
				if (point != null) {
					Minion minion = spawnMinion(point.getPosX(), point.getPosZ(), point.getPosY());
					minion.setDesiredPositionX(0);
					minion.setDesiredPositionZ(0);
				} else {
					Log.i(TAG, "We did not get a spawn point, something is wrong");
				}
				hordeMinionsLeft--;
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private Point getRandomEnemySpawnPoint() {
		Point point = null;
		while (point == null) {
			int randomPos = CalculationUtil.getRandomInt(0, (world.getSpawnPoints().size() - 1));
			point = world.getSpawnPoints().get(randomPos);
			Log.i(TAG, "Trying to find a random spawn point at pos:  " + randomPos + " its of type : " + point.getPointType());
			if (point.getPointType() == Point.SPAWN_POINT) {
				point = null;
			}
		}
		return point;
	}

	/**
	 * Send the world to a specific hero, this is done when the client joins the game
	 *
	 * @param heroIdToSend
	 */
	private void sendWorldOperation(final int heroIdToSend) {
		Thread thread = new Thread() {
			public void run() {
				sendWorld(heroIdToSend);
			}
		};
		thread.start();
	}


	/**
	 * Sends the created world down to a specific client
	 *
	 * @param heroId
	 */
	private void sendWorld(Integer heroId) {
		String jsonInString = new Gson().toJson(new WorldResponse(world));
		if (server != null) {
			Log.i(TAG, "Sending world to hero: " + heroId);
			server.dispatchMessage(new Message(getClientIdByHeroId(heroId), jsonInString));
		}
	}

	/**
	 * Sending cool down use down to a specific user
	 *
	 * @param abi
	 * @param heroId
	 */
	public void sendCooldownInformation(Ability abi, Integer heroId) {
		String jsonInString = new Gson().toJson(new CooldownResponse(abi));
		if (server != null) {
			server.dispatchMessage(new Message(getClientIdByHeroId(heroId), jsonInString));
		}
	}

	/**
	 * Spawns a minion at a specific point in the world, this is called from the World creation
	 *
	 * @param posX
	 * @param posZ
	 */
	public Minion spawnMinion(float posX, float posZ, float posY) {
		minionCount++;
		Minion minion = new Minion(this);
		minion.setId(minionCount);
		minion.setLevel(gameLevel);
		minion.generateMinionInformation(posX, posZ, posY);
		minions.add(minion);
		Log.i(TAG, "Minions spawned : " + minion.toString());
		sendGameStatus();
		return minion;
	}

	public void addMinion(Minion minion) {
		if (minions == null) {
			minions = new ArrayList<>();
		}
		minions.add(minion);
	}

	/**
	 * A hero has clicked a portal, check that all have then start new level
	 *
	 * @param heroId
	 */
	public void clickPortal(int heroId) {
		Log.i(TAG, "This hero has clicked on the stairs " + heroId);
		Hero hero = getHeroById(heroId);
		hero.setStairsPressed();
		boolean startNewGame = true;
		for (Hero heroInList : heroes) {
			if (!heroInList.isStairsPressed()) {
				startNewGame = false;
			}
		}
		// Start new level
		if (startNewGame) {
			gameLevel = gameLevel + 1;
			startLevel();
		} else {
			Log.i(TAG, "Not all heroes has clicked the portal");
		}
	}

	private void startLevel() {
		clearWorld();
		sendClearWorld();
		Log.i(TAG, "World is cleared. spawning a new world with a higher level");
		createWorld();
		for (Hero heroLoop : heroes) {
			sendWorldOperation(heroLoop.getId());
		}
	}

	private void sendTeleportPlayers() {
		String jsonInString = new Gson().toJson(new TeleportHeroesResponse(heroes));
		server.dispatchMessage(new Message(jsonInString));
	}

	public void sendRotateTargetResponse(RotateTargetResponse response){
		String jsonInString = new Gson().toJson(response);
		server.dispatchMessage(new Message(jsonInString));
	}

	public void sendCombatText(CombatTextResponse combatTextResponse) {
		String jsonInString = new Gson().toJson(combatTextResponse);
		server.dispatchMessage(new Message(jsonInString));
	}


	/**
	 * This will be called when a hero joins a game or when requested
	 *
	 * @param userId
	 */
	public void sendAbilities(String userId) {
		Gson gson = new Gson();
		Hero hero = getHeroByUserId(userId);
		if (hero != null) {
			ArrayList<Ability> heroAbilities = DatabaseUtil.getAllAbilities(hero.getClass_type());
			ArrayList<AbilityPosition> abilityPositions = DatabaseUtil.getHeroAbilityPositions(hero.getId());
			for (AbilityPosition abilityPosition : abilityPositions) {
				for (Ability ability : heroAbilities) {
					if (abilityPosition.getAbilityId() == ability.getId()) {
						ability.setPosition(abilityPosition.getPosition());
					}
				}
			}

			Collections.sort(heroAbilities, new Ability());

			getHeroByUserId(userId).setAbilities(heroAbilities);
			String jsonInString = gson.toJson(new AbilitiesResponse(heroAbilities));
			if (server != null) {
				server.dispatchMessage(new Message(getClientIdByHeroId(getHeroByUserId(userId).getId()), jsonInString));
			}
		} else {
			Log.i(TAG, "Did not find hero with user id : " + userId);
		}
	}

	private void sendTalents(Integer userId) {
		Gson gson = new Gson();
		Hero hero = getHeroByUserId("" + userId);
		Log.i(TAG, "Sending talents to user");
		if (hero != null) {
			ArrayList<Talent> talents = DatabaseUtil.getHeroTalents(hero.getId());
			hero.setTalents(talents);
			hero.recalculateStats();
			int totalPoints = hero.getLevel();
			String jsonInString = gson.toJson(new TalentResponse(talents, totalPoints));
			if (server != null) {
				server.dispatchMessage(new Message(getClientIdByHeroId(hero.getId()), jsonInString));
			}
		}
	}

	private void setTalents(Integer heroId, ArrayList<Talent> talents) {
		for (Hero hero : heroes) {
			if (hero.getId() == heroId) {
				hero.setTalents(talents);
			}
		}
	}


	public void updateAbilityPosition(AbilityPositionRequest request) {
		DatabaseUtil.updateAbilityPosition(request.getHeroId(), request.getAbilityId(), request.getPosition());
	}

	/**
	 * Clear the world on server
	 */
	private void clearWorld() {
		minions.clear();
		world.setSpawnPoints(null);
		world = null;

		for (Hero hero : heroes) {
			hero.setDesiredPositionY(0.0f);
			hero.setDesiredPositionX(0.0f);
			hero.setDesiredPositionZ(0.0f);
			hero.setPositionY(0.0f);
			hero.setPositionX(0.0f);
			hero.setPositionZ(0.0f);
		}
	}

	public void addMessage(Message message) {
		messages.add(message);
		String jsonInString = new Gson().toJson(new MessageResponse(message));
		server.dispatchMessage(new Message(jsonInString));
	}

	public ArrayList<Message> getAllMessages() {
		return messages;
	}

	/**
	 * Send message to clients that they should clear their local world
	 */
	private void sendClearWorld() {
		server.dispatchMessage(new Message(new Gson().toJson(new ClearWorldResponse())));
	}

	/**
	 * Sends the Game status down to the clients (this needs to improve that only new information is being sent, now everything is being sent)
	 */
	public void sendGameStatus() {
		GameStatusResponse response = new GameStatusResponse(minions, heroes, animations);
		if (world.isWorldType(World.HORDE)) {
			response.setTotalMinionsLeft(hordeMinionsLeft);
		}
		if (server != null) {
			server.dispatchMessage(new Message(new Gson().toJson(response)));
		}
		clearSentAnimations();
	}


	public void sendCastBarInformation(Integer heroId, Ability ability) {
		String jsonInString = new Gson().toJson(new AbilityStatusResponse(ability));
		if (server != null) {
			server.dispatchMessage(new Message(getClientIdByHeroId(heroId), jsonInString));
		}
	}

	/**
	 * Removes all animations in list, this is done after animations have been sent to clients so we don't send them multiple times
	 */
	private void clearSentAnimations() {
		animations.clear();
	}


	/**
	 * This method is called when a minion attacks a hero.
	 *
	 * @param heroId
	 * @param damage
	 * @param minionId
	 */
	public void attackHero(Integer heroId, Amount damage, Integer minionId) {
		Log.i(TAG, "Minion : " + minionId + " attacked hero: " + heroId);
		Hero hero = getHeroById(heroId);
		if (hero != null) {
			animations.add(new GameAnimation("MINION_ATTACK", heroId, minionId, null, 0));
			sendGameStatus();

			final float fDamage = hero.calculateDamageReceived(damage);
			Thread thread = new Thread() {
				public void run() {
					int timeAfterAnimationHasStartedToDamageIsDealt = 1100;
					try {
						sleep(timeAfterAnimationHasStartedToDamageIsDealt);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Minion min = getMinionById(minionId);
					if (min != null && min.getHp() > 0) {
						hero.takeDamage(fDamage, min.getArmorPenetration(), "PHYSICAL");
						sendCombatText(new CombatTextResponse(true, hero.getId(), "" + damage.getAmount(), damage.isCrit(), "#FFFF0000"));
						if (hero.getHp() <= 0) {
							Log.i(TAG, "Hero died, send death animation to client");
							int numbersAlive = 0;
							for (Hero listHero : heroes) {
								if (listHero.getHp() > 0) {
									numbersAlive++;
								}
							}
							if (numbersAlive == 0) {
								Log.i(TAG, "Nobody is alive, send endgame screen");
							}
						}
					}
					sendGameStatus();
				}
			};
			thread.start();
		}

	}

	public void sendMinionMoveAnimation(Integer minionId) {
		animations.add(new GameAnimation("MINION_RUN", null, minionId, null, 0));
	}


	/**
	 * This method is called when a hero wants to attack a minion,
	 *
	 * @param heroId
	 * @param minionId
	 */
	public void attack(Integer heroId, Integer minionId, long timeForAttackRequest) {
		Hero hero = getHeroById(heroId);
		if (hero != null) {
			Log.i(TAG, "Hero " + hero.getId() + " Hero attacked minion: " + minionId + " minions count : " + minions.size());
		}
		if (hero != null && hero.readyForAutoAttack(timeForAttackRequest)) {
			Minion minion = getMinionById(minionId);
			if (minion != null) {
				animations.add(new GameAnimation("ATTACK", minionId, hero.id, null, 0));
				sendGameStatus();

				Thread thread = new Thread() {
					public void run() {
						int timeAfterAnimationHasStartedToDamageIsDealt = 500;
						try {
							sleep(timeAfterAnimationHasStartedToDamageIsDealt);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						Log.i(TAG, "Found hero that's attacking : " + hero.getClass_type() + " hp of minion is : " + minion.getHp());
						Amount amount = hero.getAttackDamage();
						float totalDamage = Math.round(minion.calculateDamageReceived(amount, hero.getArmorPenetration(), "PHYSICAL"));
						dealDamageToMinion(hero, minion, totalDamage);

						sendCombatText(new CombatTextResponse(false, minion.getId(), "" + amount.getAmount(), amount.isCrit(), "#FFFF0000"));

						//sendCooldownInformation(hero.getAbility(0), hero.getId());

						Log.i(TAG, "Minion size now: " + minions.size());
						// Send updated status a while after animation is sent for mapping to animation hitting minion.
						sendGameStatus();
						sendCastBarInformation(hero.getId(), hero.getAbility(0));
					}
				};
				thread.start();
			} else {
				Log.i(TAG, "Hero is trying to attack a minion that is already dead or non existing");
			}
		} else {
			Log.i(TAG, "Hero is not ready for auto attack");
		}
	}

	public void dealDamageToMinion(Hero hero, Minion minion, float damage) {
		if (minion.takeDamage(damage)) {
			Log.i(TAG, "Found minion to attack : " + minion.getId() + " new hp is: " + minion.getHp());
			minionDied(hero.getId(), minion.getId());
			// Send stop movement to all attacking this minion
			stopHero(hero.id);
		} else {
			minion.addThreat(new Threat(hero.getId(), 0.0f, damage, 0.0f));
		}
	}

	public void endGame() {
		gameRunning = false;
	}


	public void restartLevel() {
		resetHeroes();
		startLevel();
	}

	private void resetHeroes() {
		heroes.forEach(Hero::generateHeroInformation);
	}

	public void stopHero(Integer heroId) {
		Hero hero = getHeroById(heroId);
		hero.setDesiredPositionX(hero.getPositionX());
		hero.setDesiredPositionZ(hero.getPositionZ());
		Log.i(TAG, "Send stop hero to heroId : " + heroId);
		String jsonInString = new Gson().toJson(new StopHeroResponse(heroId));
		server.dispatchMessage(new Message(jsonInString));

		animations.add(new GameAnimation("HERO_IDLE", null, heroId, null, 0));
		sendGameStatus();
	}

	public void heroMove(MoveRequest parsedRequest) {
		//Log.i(TAG, "User wants to move : " + parsedRequest.getHeroId());
		Hero usersHero = getHeroById(parsedRequest.getHeroId());
		if (usersHero != null) {
			usersHero.setPositionX(parsedRequest.getPositionX());
			usersHero.setPositionY(parsedRequest.getPositionY());
			usersHero.setPositionZ(parsedRequest.getPositionZ());
			usersHero.setDesiredPositionX(parsedRequest.getDesiredPositionX());
			usersHero.setDesiredPositionY(parsedRequest.getDesiredPositionY());
			usersHero.setDesiredPositionZ(parsedRequest.getDesiredPositionZ());
			Log.i(TAG, "Hero : " + usersHero.toString());

			animations.add(new GameAnimation("HERO_RUN", null, usersHero.getId(), null, 0));
		}
		sendGameStatus();
	}

	public void heroIdle(MoveRequest parsedRequest) {
		Hero usersHero = getHeroByUserId(parsedRequest.getUser_id());
		animations.add(new GameAnimation("HERO_IDLE", null, usersHero.getId(), null, 0));
	}


	public void updateMinionPositions(ArrayList<Minion> updatedMinions) {
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

	public void addTick(Tick tick) {
		ticks.add(tick);
	}

	// Utility methods

	private Hero getHeroByUserId(String user_id) {
		for (Hero hero : heroes) {
			if (hero.getUser_id() != null && hero.getUser_id() == Integer.parseInt(user_id)) {
				return hero;
			}
		}
		return null;
	}


	public Hero getHeroById(Integer heroId) {
		for (Hero hero : heroes) {
			if (hero.getId() == heroId) {
				return hero;
			}
		}
		return null;
	}

	public Minion getMinionById(Integer minionId) {
		for (Minion minion : minions) {
			if (minion.getId() == minionId) {
				return minion;
			}
		}
		return null;
	}

	public void removeMinion(Integer minionId) {
		Iterator<Minion> ut = minions.iterator();
		while (ut.hasNext()) {
			Minion minion = ut.next();
			if (minion.getId() == minionId) {
				ut.remove();
				removeMinion(minion.getId());
			}
		}
	}

	public int getWorldLevel() {
		return world.getWorldLevel();
	}

	public void minionDied(int heroId, Integer minionId) {
		animations.add(new GameAnimation("MINION_DIED", minionId, heroId, null, 0));
	}

	public ArrayList<GameAnimation> getAnimations() {
		return animations;
	}

	public void setAnimations(ArrayList<GameAnimation> animations) {
		this.animations = animations;
	}

	/**
	 * Goes through the list of heroes and returning the hero that has the lowest hp
	 *
	 * @return hero
	 */
	public Hero getHeroWithLowestHp() {
		Hero targetHero = null;
		for (Hero lowestHpHero : heroes) {
			if (targetHero == null) {
				targetHero = lowestHpHero;
			} else if (lowestHpHero.getHp() < targetHero.getHp()) {
				targetHero = lowestHpHero;
				Log.i(TAG, "Found a target with lower hp : " + lowestHpHero.getId());
			}
		}
		return targetHero;
	}

	/**
	 * Util method to get a client by his hero id, good to have if needing to send specified message to that client instead of to all clients
	 *
	 * @param heroId
	 * @return
	 */
	private Integer getClientIdByHeroId(Integer heroId) {
		if (server != null && server.getClients() != null) {
			for (int i = 0; i < server.getClients().size(); i++) {
				ClientInfo clientInfo = (ClientInfo) server.getClients().get(i);
				if (clientInfo.getHeroId() == heroId) {
					return clientInfo.getId();
				}
			}
		}
		return null;
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}

	public void minionAggro(MinionAggroRequest parsedRequest) {
		Minion minion = getMinionById(parsedRequest.getMinion_id());
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

	public void minionTargetInRange(MinionAggroRequest parsedRequest) {
		Minion minion = getMinionById(parsedRequest.getMinion_id());
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


	// Living game world

	private Thread tickThread;
	private int threadTick = 100;
	private int gameStatusTickTime = 1000;
	private int minionActionTickTime = 500;
	private int heroRegenTickTime = 2000;
	private int requestMinionPositionTickTime = 1000;

	public ArrayList<Tick> ticks = new ArrayList<>();

	private void startGameTicks() {
		ticks.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), Tick.GAME_STATUS));
		ticks.add(new Tick((System.currentTimeMillis() + minionActionTickTime), Tick.MINION_ACTION));
		ticks.add(new Tick((System.currentTimeMillis() + heroRegenTickTime), Tick.HERO_REGEN));
		ticks.add(new Tick((System.currentTimeMillis() + requestMinionPositionTickTime), Tick.REQUEST_MINION_POSITION));
		Log.i(TAG, "Starting new thread");
		tickThread = new Thread(() -> {
			while (gameRunning) {
				Collections.sort(ticks);

				boolean addNewGameStatus = false;
				boolean addMinionAction = false;
				boolean addHeroRegen = false;
				boolean addRequestMinionPosition = false;

				boolean minionDebuffAction = false;

				// Check ticks if they should be actioned
				Iterator<Tick> iterator = ticks.iterator();
				while (iterator.hasNext()) {
					Tick tick = iterator.next();
					if (System.currentTimeMillis() >= tick.timeToActivate) {
						if (tick.typeOfTick != Tick.GAME_STATUS && tick.typeOfTick != Tick.REQUEST_MINION_POSITION && tick.typeOfTick != Tick.HERO_REGEN && tick.typeOfTick != Tick.MINION_ACTION) {
							Log.i(TAG, "Activated this tick : " + tick.timeToActivate + " of type " + tick.typeOfTick);
						}
						if (tick.typeOfTick == Tick.GAME_STATUS) {
							addNewGameStatus = true;
						}
						if (tick.typeOfTick == Tick.MINION_ACTION) {
							addMinionAction = true;
						}
						if (tick.typeOfTick == Tick.HERO_REGEN) {
							addHeroRegen = true;
						}
						if (tick.typeOfTick == Tick.REQUEST_MINION_POSITION) {
							addRequestMinionPosition = true;
						}
						if (tick.typeOfTick == Tick.MINION_DEBUFF) {
							minionDebuffAction = true;
						}
						iterator.remove();
					}
				}

				try {
					sleep(threadTick);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (addNewGameStatus) {
					sendGameStatus();
					ticks.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), Tick.GAME_STATUS));
				}
				if (addMinionAction) {
					for (Minion minion : minions) {
						if (minion.isAlive()) {
							minion.takeAction();
						}
					}
					ticks.add(new Tick((System.currentTimeMillis() + minionActionTickTime), Tick.MINION_ACTION));
				}

				if (minionDebuffAction) {
					minionDebuffs();
				}

				if (addHeroRegen) {
					for (Hero hero : heroes) {
						hero.regenTick();
					}
					ticks.add(new Tick((System.currentTimeMillis() + heroRegenTickTime), Tick.HERO_REGEN));
				}

				if (addRequestMinionPosition) {
					// Send request to a random client to update server with actual position of minions
					sendRequestMinionPosition();
					ticks.add(new Tick((System.currentTimeMillis() + requestMinionPositionTickTime), Tick.HERO_REGEN));
				}
			}
		});
		tickThread.start();
	}

	private void minionDebuffs() {
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
							Log.i(TAG, "It was time for minion debuff changed debuff ticktime to " + (debuff.tickTime + debuff.duration) + " from " + debuff.tickTime);
							debuff.tickTime = "" + (tickTimeConv + debuff.duration);
							if (debuff.type == Buff.DOT) {
								dealDamageToMinion(getHeroById(debuff.heroId), minion, debuff.value);

								sendCombatText(new CombatTextResponse(false, minion.getId(), "" + debuff.value, false, "#FFFF0000"));

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

	private void sendRequestMinionPosition() {
		if (server != null && server.getClientCount() > 0) {
			int positionOfRandomClient = CalculationUtil.getRandomInt(0, (server.getClientCount() - 1));
			ClientInfo clientInfo = (ClientInfo) server.getClients().get(positionOfRandomClient);
			server.dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse("UPDATE_MINION_POSITION", 200))));
		}
	}


	public void sendSpell(SpellRequest parsedRequest) {
		spellUtil.spell(parsedRequest, this);
	}
}
