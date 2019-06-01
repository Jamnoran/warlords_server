package game;

import com.google.gson.Gson;
import game.communication.ClientInfo;
import game.communication.ServerDispatcher;
import game.io.Requests.SpellRequest;
import game.io.Responses.AbilityStatusResponse;
import game.io.Responses.ClearWorldResponse;
import game.io.Responses.GameStatusResponse;
import game.io.Responses.MessageResponse;
import game.logging.Log;
import game.util.CommunicationUtil;
import game.util.GameUtil;
import game.util.SpellUtil;
import game.util.TickEngine;
import game.vo.*;

import java.util.ArrayList;

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
//		Log.i(TAG, "World generated : " + world.toString());
	}

	/**
	 * Send the world to a specific hero, this is done when the client joins the game
	 *
	 * @param heroIdToSend
	 */
	public void sendWorldOperation(final int heroIdToSend) {
		Thread thread = new Thread() {
			public void run() {
				CommunicationUtil.sendWorld(heroIdToSend, GameServer.this);
			}
		};
		thread.start();
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

	public void startLevel() {
		clearWorld();
		sendClearWorld();
		Log.i(TAG, "World is cleared. spawning a new world with a higher level");
		createWorld();
		for (Hero heroLoop : heroes) {
			sendWorldOperation(heroLoop.getId());
		}
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

	public void sendMinionMoveAnimation(Integer minionId) {
		animations.add(new GameAnimation("MINION_RUN", null, minionId, null, 0));
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
	public Integer getClientIdByHeroId(Integer heroId) {
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


	public void sendSpell(SpellRequest parsedRequest) {
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

	public ServerDispatcher getServer() {
		return server;
	}

	public void setServer(ServerDispatcher server) {
		this.server = server;
	}

	public int getHordeMinionsLeft() {
		return hordeMinionsLeft;
	}

	public void setHordeMinionsLeft(int hordeMinionsLeft) {
		this.hordeMinionsLeft = hordeMinionsLeft;
	}

	public void addMessage(Message message) {
		messages.add(message);
		String jsonInString = new Gson().toJson(new MessageResponse(message));
		server.dispatchMessage(new Message(jsonInString));
	}

}
