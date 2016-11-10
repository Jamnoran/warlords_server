package game;

import com.google.gson.Gson;
import game.logging.Log;
import io.*;
import util.DatabaseUtil;
import vo.*;
import vo.classes.Priest;
import vo.classes.Warrior;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameServer {
	private static final String TAG = GameServer.class.getSimpleName();
	private boolean gameRunning = true;
	private boolean gameStarted = false;
	private ServerDispatcher server;

	private int minionCount = 0;
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();
	private World world;
	private int gameLevel = 1;


	public GameServer(ServerDispatcher server) {
		Log.i(TAG, "Game server is initilized, creating world");
		this.server = server;
		createWorld();
		startAI();
	}


	/**
	 * Creates the world and all in it, minions, walls, lighting, stairs up and down.
	 */
	private void createWorld() {
		// Call World.generate()
		world = new World().generate(this, 100, 100, gameLevel);
		Log.i(TAG, "World generated : " + world.toString());
	}


	/**
	 * When a player join he needs to have a hero, this makes sure his hero join as the correct class etc.
	 * @param hero
	 */
	public void addHero(Hero hero) {
		sendWorldOperation(hero.getId());
		if (hero.isClass(Hero.WARRIOR)) {
			Log.i(TAG, "Added a warrior");
			Warrior warrior = (Warrior) hero;
			warrior.generateHeroInformation();
			warrior.setStartPosition(getFreeStartPosition());
			heroes.add(warrior);
		} else if (hero.isClass(Hero.PRIEST)){
			Log.i(TAG, "Added a priest");
			Priest priest = (Priest) hero;
			priest.generateHeroInformation();
			priest.setStartPosition(getFreeStartPosition());
			heroes.add(priest);
		}
		Log.i(TAG, "Hero joined with this user id: " + hero.getUser_id() + " characters in game: " + heroes.size());
		sendGameStatus();
		sendAbilities("" + hero.getUser_id());
		if(!gameStarted){
			gameStarted = true;
		}
	}

	/**
	 * Loops through the different spawn points thats available to start on
 	 */
	private Vector3 getFreeStartPosition() {
		if(world.getSpawnPoints().size() > 0){
			Vector3 spawnPoint = world.getSpawnPoints().get(0);
			world.getSpawnPoints().remove(0);
			return spawnPoint;
		}
		return null;
	}

	/**
	 * Send the world to a specific hero, this is done when the client joins the game
	 * @param heroIdToSend
	 */
	private void sendWorldOperation(final int heroIdToSend) {
		Thread thread = new Thread(){
			public void run(){
				sendWorld(heroIdToSend);
			}
		};
		thread.start();
	}


	/**
	 * Sends the created world down to a specific client
	 * @param heroId
	 */
	private void sendWorld(Integer heroId) {
		Gson gson = new Gson();
		String jsonInString = gson.toJson(new WorldResponse(world));
		server.dispatchMessage(new Message(getClientIdByHeroId(heroId), jsonInString));
	}

	/**
	 * Spawns a minion at a specific point in the world, this is called from the World creation
	 * @param posX
	 * @param posZ
	 */
	public void spawnMinion(float posX, float posZ) {
		minionCount++;
		Minion minion = new Minion(this);
		minion.setId(minionCount);
		minion.setLevel(gameLevel);
		minion.generateMinionInformation(posX, posZ);
//		minion.startAI();
		minions.add(minion);
		sendGameStatus();
	}

	/**
	 * A hero has clicked a portal, check that all have then start new level
	 * @param heroId
	 */
	public void clickPortal(int heroId){
		Log.i(TAG, "This hero has clicked on the stairs " + heroId);
		Hero hero = getHeroById(heroId);
		hero.setStairsPressed();
		boolean startNewGame = true;
		for (Hero heroInList : heroes ) {
			if(!heroInList.isStairsPressed()){
				startNewGame = false;
			}
		}
		// Start new level
		if (startNewGame) {
			gameLevel = gameLevel + 1;
			clearWorld();
			sendClearWorld();
			Log.i(TAG, "World is cleared. spawning a new world with a higher level");
			createWorld();
			for(Hero heroLoop : heroes){
				sendWorldOperation(heroLoop.getId());
			}
			// Send teleport operation to all heroes
			int i = 0;
			for(Hero teleportHero : getHeroes()){
				Vector3 spawnPoint = world.getSpawnPoints().get(i);
				teleportHero.setDesiredPositionX(spawnPoint.getX());
				teleportHero.setDesiredPositionZ(spawnPoint.getZ());
				teleportHero.setPositionX(spawnPoint.getX());
				teleportHero.setPositionZ(spawnPoint.getZ());
				Log.i(TAG, "Sending player : " + teleportHero.getId() + " to position : " + spawnPoint.toString());
				i++;
			}
			sendTeleportPlayers();
		} else {
			Log.i(TAG, "Not all heroes has clicked the portal");
		}
	}

	private void sendTeleportPlayers() {
		String jsonInString = new Gson().toJson(new TeleportHeroesResponse(heroes));
		server.dispatchMessage(new Message(jsonInString));
	}

	public void sendAbilities(String userId) {
		Gson gson = new Gson();
		String jsonInString = gson.toJson(new AbilitiesResponse(DatabaseUtil.getAllAbilities(getHeroByUserId(userId).getClass_type())));
		server.dispatchMessage(new Message(getClientIdByHeroId(getHeroByUserId(userId).getId()), jsonInString));
	}

	/**
	 * Clear the world on server
	 */
	private void clearWorld() {
		minions.clear();
		world.getObstacles().clear();
		world = null;
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
	public void sendGameStatus(){
		String jsonInString = new Gson().toJson(new GameStatusResponse(minions, heroes, animations));
		server.dispatchMessage(new Message(jsonInString));
		clearSentAnimations();
	}

	/**
	 * Removes all animations in list, this is done after animations have been sent to clients so we don't send them multiple times
	 */
	private void clearSentAnimations() {
		animations.clear();
	}


	/**
	 * Important method that handles the spells sent up from clients that a hero wants to initiate a spell
	 * This method takes as parameters
	 * heroId
	 * spellId
	 * targetFriendly
	 * targetEnemy
	 * Location
	 * @param parsedRequest
	 */
	public void spell(SpellRequest parsedRequest) {
		Log.i(TAG , "Handle spell " + parsedRequest.toString());
		Hero hero = getHeroByUserId(parsedRequest.getUser_id());
		if (hero.getClass_type().equals(Hero.PRIEST)){
			switch (parsedRequest.getSpell_id()){
				case 1:
					Log.i(TAG, "Priest used Heal!");
					priestHeal((Priest) hero, parsedRequest);
					break;
				default:
					break;
			}
		}else if (hero.getClass_type().equals(Hero.WARRIOR)){
			switch (parsedRequest.getSpell_id()){
				case 1:
					Log.i(TAG, "Warrior used cleave!");
					break;
				default:
					break;
			}
		}
	}

	/**
	 * This method is called when a minion attacks a hero.
	 * @param heroId
	 * @param damage
	 * @param minionId
	 */
	public void attackHero(Integer heroId, float damage, Integer minionId) {
		Log.i(TAG, "Minion : " + minionId + " attacked hero: " + heroId);
		Hero hero = getHeroById(heroId);
		if (hero != null) {
			damage = hero.calculateDamageReceived(damage);
			hero.takeDamage(damage);
			if(hero.getHp() <= 0){
				Log.i(TAG, "Hero died, send death animation to client");
				int numbersAlive = 0;
				for (Hero listHero : heroes) {
					if(listHero.getHp() > 0){
						numbersAlive++;
					}
				}
				if(numbersAlive == 0){
					Log.i(TAG, "Nobody is alive, send endgame screen");
				}
			}
			animations.add(new GameAnimation("MINION_ATTACK", heroId, minionId, null));
			sendGameStatus();
		}

	}

	/**
	 * This method is called when a hero wants to attack a minion,
	 * @param userId
	 * @param minionId
	 */
	public void attack(String userId, Integer minionId) {
		Log.i(TAG, "User " + userId + " Hero attacked minion: " + minionId + " minions count : " + minions.size());

		// TODO: Check that time since last time is correct
		Hero hero = getHeroByUserId(userId);
		Minion minion = getMinionById(minionId);
		if (minion != null) {
			Log.i(TAG, "Found hero that's attacking : " + hero.getClass_type() + " hp of minion is : " + minion.getHp());
			float totalDamage = Math.round(minion.calculateDamageReceived(hero.getAttackDamage()));
			if (minion.takeDamage(totalDamage)) {
				Log.i(TAG, "Found minion to attack : " + minion.getId() + " new hp is: " + minion.getHp());
				minionDied(userId, minion.getId());
				removeMinion(minion.getId());
			}else {
				minion.addThreat(new Threat(hero.getId(), 0.0f, totalDamage, 0.0f));
			}
		} else {
			Log.i(TAG, "Hero is trying to attack a minion that is already dead or non existing");
		}
		Log.i(TAG, "Minion size now: " + minions.size());
		animations.add(new GameAnimation("ATTACK", minionId, hero.id, null));
		sendGameStatus();
	}

	public void endGame(){
		gameRunning = false;
	}


	private Minion getMinionById(Integer minionId) {
		for(Minion minion : minions) {
			if (minion.getId() == minionId) {
				return minion;
			}
		}
		return null;
	}

	private void removeMinion(Integer minionId) {
		Iterator<Minion> ut = minions.iterator();
		while (ut.hasNext()) {
			Minion minion = ut.next();
			if (minion.getId() == minionId) {
				ut.remove();
				removeMinion(minion.getId());
			}
		}
	}

	private void minionDied(String userId, Integer minionId) {
		animations.add(new GameAnimation("MINION_DIED", minionId, Integer.parseInt(userId), null));
	}

	public void heroMove(MoveRequest parsedRequest) {
		Log.i(TAG, "User wants to move : " + parsedRequest.getUser_id());
		Hero usersHero = getHeroByUserId(parsedRequest.getUser_id());
		if (usersHero != null) {
			usersHero.setPositionX(parsedRequest.getPositionX());
			usersHero.setPositionZ(parsedRequest.getPositionZ());
			usersHero.setDesiredPositionX(parsedRequest.getDesiredPositionX());
			usersHero.setDesiredPositionZ(parsedRequest.getDesiredPositionZ());
			Log.i(TAG, "Hero : " + usersHero.toString());
		}
		sendGameStatus();
	}
















	// Utility methods

	private Hero getHeroByUserId(String user_id) {
		for(Hero hero : heroes){
			if(hero.getUser_id() == Integer.parseInt(user_id)){
				return hero;
			}
		}
		return null;
	}


	public Hero getHeroById(Integer heroId) {
		for(Hero hero : heroes){
			if(hero.getId() == heroId){
				return hero;
			}
		}
		return null;
	}

	/**
	 * Util method to get a client by his hero id, good to have if needing to send specified message to that client instead of to all clients
	 * @param heroId
	 * @return
	 */
	private Integer getClientIdByHeroId(Integer heroId) {
		for (int i = 0; i < server.getClients().size(); i++) {
			ClientInfo clientInfo = (ClientInfo) server.getClients().get(i);
			if(clientInfo.getHeroId() == heroId){
				return clientInfo.getId();
			}
		}
		return null;
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}

	public void minionAggro(MinionAggroRequest parsedRequest) {
		Minion minion = getMinionById(parsedRequest.getMinion_id());
		if(minion != null && minion.getHeroIdWithMostThreat() == null){
			Log.i(TAG, "This minion had no aggro, add towards this hero [" + parsedRequest.getHero_id() + "] Since first to see it");
			minion.addThreat(new Threat(parsedRequest.getHero_id(), Threat.inRangeThreath, 0 ,0));
		}
	}
	public void minionTargetInRange(MinionAggroRequest parsedRequest) {
		Minion minion = getMinionById(parsedRequest.getMinion_id());
		if(minion != null && parsedRequest.getHero_id() > 0){
			Log.i(TAG, "Target is in range for an attack");
			minion.targetInRangeForAttack = true;
		}else{
			Log.i(TAG, "Target is out of range for an attack");
			if (minion != null) {
				minion.targetInRangeForAttack = false;
			}
		}
	}

	public int getMinionCount() {
		return minionCount;
	}





	// Living game world

	public void startAI(){
		Thread thread = new Thread(){
			public void run(){
				while(gameRunning){
					for(Minion minion : minions){
						minion.takeAction();
					}
					sendGameStatus();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}














	// All spells



	//          Priest


	private void priestHeal(Priest hero, SpellRequest parsedRequest) {
		Hero targetHero = null;
		// Check if has a friendly target
		if(parsedRequest.getTarget_friendly() != null){
			targetHero = getHeroById(parsedRequest.getTarget_friendly());
			if (targetHero != null) {
				Log.i(TAG, "Target hero : " + targetHero.getId());
			}
		}
		// If not then heal lowest % hp ally
		if(targetHero == null){
			for (Hero lowestHHpHero : heroes){
				if(lowestHHpHero.getHp() < lowestHHpHero.getMaxHp()){
					if(targetHero == null){
						targetHero = lowestHHpHero;
					}else if (lowestHHpHero.getHp() < targetHero.getHp()){
						targetHero = lowestHHpHero;
						Log.i(TAG, "Found a target with lower hp : " + lowestHHpHero.getId());
					}
				}
			}
		}
		if (targetHero != null) {
			Log.i(TAG, "Target Hero to heal : " + targetHero.getId());
		}
		// Remove mana return false if cant use spell (should be handeled on client side as well)
		if (targetHero != null && hero.hasManaForSpellHeal()) {
			// Get heal amount
			float healAmount = hero.getSpellHealAmount();
			Log.i(TAG, "Healing for this amount : " + healAmount);

			// Heal target (dont overheal)
			if(targetHero != null){
				targetHero.heal(healAmount);
				animations.add(new GameAnimation("HEAL",targetHero.getId(), hero.getId(), null));
			}

			// Add threat to all targets close by (of target location or healer location?)
		}
		sendGameStatus();
	}


	//          Warrior
















}
