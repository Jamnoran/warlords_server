package game;

import com.google.gson.Gson;
import game.logging.Log;
import io.GameStatusResponse;
import io.MoveRequest;
import io.SpellRequest;
import io.WorldResponse;
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
	private ServerDispatcher server;

	private int minionCount = 0;
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();
	private World world;


	public GameServer(ServerDispatcher server) {
		Log.i(TAG, "Game server is initilized, creating world");
		this.server = server;
		createWorld();
//		spawnMinions();
	}


	private void createWorld() {
		// Call World.generate()
		world = new World().generate(this, 100, 100, 1);
		Log.i(TAG, "World generated : " + world.toString());
	}


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
	}

	private Vector3 getFreeStartPosition() {
		if(world.getSpawnPoints().size() > 0){
			Vector3 spawnPoint = world.getSpawnPoints().get(0);
			world.getSpawnPoints().remove(0);
			return spawnPoint;
		}
		return null;
	}

	private void sendWorld(Integer heroId) {
		Gson gson = new Gson();
		String jsonInString = gson.toJson(new WorldResponse(world));
		server.dispatchMessage(new Message(getClientIdByHeroId(heroId), jsonInString));
	}

	private Integer getClientIdByHeroId(Integer heroId) {
		for (int i = 0; i < server.getClients().size(); i++) {
			ClientInfo clientInfo = (ClientInfo) server.getClients().get(i);
			if(clientInfo.getHeroId() == heroId){
				return clientInfo.getId();
			}
		}
		return null;
	}

	private void sendWorldOperation(int heroId) {
		Thread thread = new Thread(){
			public void run(){
				sendWorld(heroId);
			}
		};
		thread.start();
	}


	public void spawnMinion(float posX, float posZ) {
		minionCount++;
		Minion minion = new Minion(this);
		minion.setId(minionCount);
		minion.generateMinionInformation(posX, posZ);
		minion.startAI();
		minions.add(minion);
		sendGameStatus();
	}

	public void sendGameStatus(){
		Gson gson = new Gson();
		String jsonInString = gson.toJson(new GameStatusResponse(minions, heroes, animations));
		server.dispatchMessage(new Message(jsonInString));
		clearSentAnimations();
	}

	private void clearSentAnimations() {
		animations.clear();
	}



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


	public void attackHero(Integer userId, float damage, Integer minionId) {
		Log.i(TAG, "Minion : " + minionId + " attacked hero: " + userId);
		Hero hero = getHeroByUserId("" + userId);
		damage = hero.calculateDamageReceived(damage);
		hero.takeDamage(damage);
		animations.add(new GameAnimation("MINION_ATTACK", userId, minionId, null));
		sendGameStatus();
	}

	public void attack(String userId, Integer minionId) {
		Log.i(TAG, "User " + userId + " Hero attacked minion: " + minionId + " minons count : " + minions.size());
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
				minion.addThreat(new Threat(Integer.parseInt(userId), 0.0f, totalDamage, 0.0f));
			}
		} else {
			Log.i(TAG, "Hero is trying to attack a minion that is already dead or non existing");
		}
		Log.i(TAG, "Minion size now: " + minions.size());
		sendGameStatus();
	}

	public int getMinionCount() {
		return minionCount;
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


	private Hero getHeroByUserId(String user_id) {
		for(Hero hero : heroes){
			if(hero.getUser_id() == Integer.parseInt(user_id)){
				return hero;
			}
		}
		return null;
	}


	public void endGame(){
		gameRunning = false;
		this.server.endGame();
	}


	public Hero getHeroById(Integer heroId) {
		for(Hero hero : heroes){
			if(hero.getId() == heroId){
				return hero;
			}
		}

		return null;
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
