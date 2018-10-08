package game;

import com.google.gson.Gson;
import game.io.Requests.*;
import game.io.Responses.*;
import game.logging.Log;
import game.io.*;
import game.util.*;
import game.vo.*;
import game.vo.Server;
import game.vo.classes.Priest;
import game.vo.classes.Warlock;
import game.vo.classes.Warrior;

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
	public int gameLevel = 5;
	private SpellUtil spellUtil;
	private GameUtil gameUtil;
	private TickEngine tickEngine;


	public GameServer(ServerDispatcher server) {
		Log.i(TAG, "Game server is initilized, creating world");
		this.server = server;
		createWorld();
		tickEngine = new TickEngine(GameServer.this);
		tickEngine.startGameTicks();
		spellUtil = new SpellUtil();
		gameUtil = new GameUtil();
		gameUtil.setGameServer(this);
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

	public void startHordeMinionSpawner() {
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
	public void sendWorldOperation(final int heroIdToSend) {
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


	public void startLevel() {
		clearWorld();
		sendClearWorld();
		Log.i(TAG, "World is cleared. spawning a new world with a higher level");
		createWorld();
		for (Hero heroLoop : heroes) {
			sendWorldOperation(heroLoop.getId());
		}
	}

	public void sendTeleportPlayers() {
		String jsonInString = new Gson().toJson(new TeleportHeroesResponse(heroes));
		server.dispatchMessage(new Message(jsonInString));
	}

	public void sendRotateTargetResponse(RotateTargetResponse response){
		String jsonInString = new Gson().toJson(response);
		if (server != null) {
			server.dispatchMessage(new Message(jsonInString));
		}
	}

	public void sendCombatText(CombatTextResponse combatTextResponse) {
		String jsonInString = new Gson().toJson(combatTextResponse);
		if (server != null) {
			server.dispatchMessage(new Message(jsonInString));
		}
	}


	/**
	 * This will be called when a hero joins a game or when requested
	 *
	 * @param userId
	 */
	public void sendAbilities(String userId) {
		Gson gson = new Gson();
		Hero hero = GameUtil.getHeroByUserId(userId, heroes);
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

			hero.setAbilities(heroAbilities);
			String jsonInString = gson.toJson(new AbilitiesResponse(heroAbilities));
			if (server != null) {
				server.dispatchMessage(new Message(getClientIdByHeroId(hero.getId()), jsonInString));
			}
		} else {
			Log.i(TAG, "Did not find hero with user id : " + userId);
		}
	}

	public void sendTalents(Integer userId) {
		Gson gson = new Gson();
		Hero hero = GameUtil.getHeroByUserId("" + userId, heroes);
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
		//Log.i(TAG, "Minion : " + minionId + " attacked hero: " + heroId);
		Hero hero = GameUtil.getHeroById(heroId, heroes);
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

					Minion min = GameUtil.getMinionById(minionId, minions);
					if (min != null && min.getHp() > 0) {
						hero.takeDamage(fDamage, min.getArmorPenetration(), Amount.PHYSICAL);

						// Check if hero has retaliation buff
						hero.checkForRetaliation(min);

						sendCombatText(new CombatTextResponse(true, hero.getId(), "" + damage.getAmount(), damage.isCrit(), GameUtil.COLOR_CRIT));
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
		}else {
			Log.i(TAG, "Could not find hero");
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
		Hero hero = GameUtil.getHeroById(heroId, heroes);
		if (hero != null && hero.readyForAutoAttack(timeForAttackRequest)) {
			Minion minion = GameUtil.getMinionById(minionId, minions);
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
						float totalDamage = Math.round(minion.calculateDamageReceived(amount, hero.getPenetration(Amount.PHYSICAL), Amount.PHYSICAL));
						getGameUtil().dealDamageToMinion(hero, minion, totalDamage);

						sendCombatText(new CombatTextResponse(false, minion.getId(), "" + amount.getAmount(), amount.isCrit(), GameUtil.COLOR_DAMAGE));

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
		Hero hero = GameUtil.getHeroById(heroId, heroes);
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
		Hero usersHero = GameUtil.getHeroById(parsedRequest.getHeroId(), heroes);
		if (usersHero != null) {
			usersHero.setPositionX(parsedRequest.getPositionX());
			usersHero.setPositionY(parsedRequest.getPositionY());
			usersHero.setPositionZ(parsedRequest.getPositionZ());
			usersHero.setDesiredPositionX(parsedRequest.getDesiredPositionX());
			usersHero.setDesiredPositionY(parsedRequest.getDesiredPositionY());
			usersHero.setDesiredPositionZ(parsedRequest.getDesiredPositionZ());
			//Log.i(TAG, "Hero : " + usersHero.toString());

			animations.add(new GameAnimation("HERO_RUN", null, usersHero.getId(), null, 0));
		}
		sendGameStatus();
	}

	public void heroIdle(MoveRequest parsedRequest) {
		Hero usersHero = GameUtil.getHeroById(parsedRequest.getHeroId(), heroes);
		animations.add(new GameAnimation("HERO_IDLE", null, usersHero.getId(), null, 0));
	}

	// Utility methods
	public ArrayList<Item> getHeroItems(Hero hero, boolean sendToClient) {
		ArrayList<Item> items = DatabaseUtil.getLoot(hero.getId());
		hero.setItems(items);
		if(sendToClient){
			HeroItemsResponse response = new HeroItemsResponse(items);
			if (server != null) {
				String json = new Gson().toJson(response);
				Log.i(TAG, "Sending these items to client : " + json);
				server.dispatchMessage(new Message(getClientIdByHeroId(hero.getId()), json));
			}
		}
		return items;
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


	public void sendRequestMinionPosition() {
		if (server != null && server.getClientCount() > 0) {
			int positionOfRandomClient = CalculationUtil.getRandomInt(0, (server.getClientCount() - 1));
			ClientInfo clientInfo = (ClientInfo) server.getClients().get(positionOfRandomClient);
			server.dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse("UPDATE_MINION_POSITION", 200))));
		}
	}

	void sendSpell(SpellRequest parsedRequest) {
		spellUtil.spell(parsedRequest, this);
	}

	public GameUtil getGameUtil(){
		return gameUtil;
	}

	public ArrayList<Minion> getMinions() {
		return minions;
	}

	public void setGameStarted() {
		if (!gameStarted) {
			gameStarted = true;
		}
	}

	public World getWorld() {
		return world;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public TickEngine getTickEngine() {
		return tickEngine;
	}
}
