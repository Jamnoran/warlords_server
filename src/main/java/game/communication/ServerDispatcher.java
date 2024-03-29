package game.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.GameServer;
import game.Server;
import game.io.JsonRequest;
import game.io.Requests.*;
import game.logging.Log;
import game.io.CommunicationHandler;
import game.util.DatabaseUtil;
import game.util.GameUtil;
import game.util.HeroUtil;
import game.models.heroes.Hero;
import game.models.server.Message;

import java.util.Vector;

public class ServerDispatcher extends Thread {
	private static final String TAG = ServerDispatcher.class.getSimpleName();

	private Vector mMessageQueue = new Vector();
	private Vector mClients = new Vector();
	private String serverId;
	private GameServer gameServer;
	private String gameType;
	private long createdAt = 0;
	private boolean online = true;
	private long startTime = 0;

	/**
	 * Adds given client to the server's client list.
	 */
	synchronized void addClient(ClientInfo aClientInfo) {
		mClients.add(aClientInfo);
		getClientsHero(aClientInfo.getHeroId());
	}

	private void getClientsHero(Integer heroId) {
		Log.i(TAG, "Add hero with id: " + heroId);
		Hero hero = DatabaseUtil.getHero(heroId);
		if (hero != null && hero.id > 0) {
			Log.i(TAG, "Got this hero: " + hero.toString());
			gameServer.getGameUtil().addHero(hero, gameServer.getHeroes());
		} else {
			Log.i(TAG, "Could not find hero");
		}
	}

	/**
	 * Deletes given client from the server's client list if the client is in
	 * the list.
	 */
	synchronized void deleteClient(ClientInfo aClientInfo) {
		int clientIndex = mClients.indexOf(aClientInfo);
		if (clientIndex != -1) {
			mClients.removeElementAt(clientIndex);
		}
		if (mClients.size() == 0) {
			Log.i(TAG, "No players left in game, lets end it.");
			getGameServer().endGame();
			LobbyServerDispatcher.deleteServer(this);
			Log.i(TAG, "Database used this many request now: " + DatabaseUtil.getCounter());
		}
	}

	/**
	 * Adds given message to the dispatcher's message queue and notifies this
	 * thread to wake up the message queue reader (getNextMessageFromQueue
	 * method). dispatchMessage method is called by other threads
	 * (warlords.game.communication.ClientListener) when a message is arrived.
	 */
	public synchronized void dispatchMessage(Message aMessage) {
		mMessageQueue.add(aMessage);
		notify();
	}

	synchronized void handleClientRequest(Message aMessage) {
		Gson gson = new GsonBuilder().create();
		JsonRequest request = JsonRequest.parse(aMessage);
		if (request != null) {
			if (!request.isType("UPDATE_MINION_POSITION") && !request.isType("MOVE") && !request.isType("ATTACK")) {
				Log.i(TAG, "JsonRequest: " + request.toString());
			}
			if (request.isType("GET_STATUS")) {
				JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.sendGameStatus();
			} else if (request.isType("SPAWN_POINTS")) {
				SpawnPointsRequest parsedRequest = gson.fromJson(aMessage.getMessage(), SpawnPointsRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.getGameUtil().addSpawnPoints(parsedRequest.getPoints());
			} else if (request.isType("ATTACK")) {
				AttackRequest parsedRequest = gson.fromJson(aMessage.getMessage(), AttackRequest.class);
				//Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				HeroUtil.attack(parsedRequest.getHeroId(), parsedRequest.getMinion_id(), parsedRequest.getTime(), gameServer);
			} else if (request.isType("MOVE")) {
				MoveRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MoveRequest.class);
				//Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				HeroUtil.heroMove(parsedRequest, gameServer);
			} else if (request.isType("SPELL")) {
				SpellRequest parsedRequest = gson.fromJson(aMessage.getMessage(), SpellRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.sendSpell(parsedRequest);
			} else if (request.isType("MINION_AGGRO")) {
				MinionAggroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MinionAggroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				GameUtil.minionAggro(parsedRequest, gameServer.getMinions());
			} else if (request.isType("MINION_TARGET_IN_RANGE")) {
				MinionAggroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MinionAggroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.getGameUtil().minionTargetInRange(parsedRequest);
			} else if (request.isType("CLICKED_PORTAL")) {
				ClickPortalRequest parsedRequest = gson.fromJson(aMessage.getMessage(), ClickPortalRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.getGameUtil().clickPortal(parsedRequest.getHero_id(), gameServer.getHeroes());
			} else if (request.isType("GET_ABILITIES")) {
				Log.i(TAG, "parsedRequest : " + request.toString());
				CommunicationHandler.sendAbilities(request.getUser_id(), gameServer);
			} else if (request.isType("UPDATE_ABILITY_POSITION")) {
				AbilityPositionRequest abilityPositionRequest = gson.fromJson(aMessage.getMessage(), AbilityPositionRequest.class);
				Log.i(TAG, "parsedRequest : " + request.toString());
				DatabaseUtil.updateAbilityPosition(abilityPositionRequest);
			} else if (request.isType("UPDATE_TALENTS")) {
				TalentRequest talentRequest = gson.fromJson(aMessage.getMessage(), TalentRequest.class);
				Log.i(TAG, "parsedRequest : " + talentRequest.toString());
				DatabaseUtil.addTalentPoints(talentRequest.getHero_id(), talentRequest.talents);
			} else if (request.isType("STOP_HERO")) {
				StopHeroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), StopHeroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				HeroUtil.stopHero(parsedRequest.getHeroId(), gameServer);
			} else if (request.isType("RESTART_LEVEL")) {
				Log.i(TAG, "parsedRequest : " + request.toString());
				gameServer.restartLevel();
			} else if (request.isType("GET_ITEMS")) {
				Log.i(TAG, "parsedRequest : " + request.toString());
				HeroUtil.getHeroItems(GameUtil.getHeroByUserId(request.getUser_id(), gameServer.getHeroes()), true, gameServer);
			} else if (request.isType("UPDATE_ITEM_POSITION")) {
				UpdateHeroItemPositionRequest parsedRequest = gson.fromJson(aMessage.getMessage(), UpdateHeroItemPositionRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.getGameUtil().updateItemPosition(parsedRequest);
			} else if (request.isType("UPDATE_MINION_POSITION")) {
				//Log.i(TAG, "parsedRequest : " + request.toString());
				UpdateMinionPositionRequest parsedRequest = gson.fromJson(aMessage.getMessage(), UpdateMinionPositionRequest.class);
				GameUtil.updateMinionPositions(parsedRequest.getMinions(), gameServer.getMinions());
			} else if (request.isType("SEND_MESSAGE")) {
				SendMessageRequest parsedRequest = gson.fromJson(aMessage.getMessage(), SendMessageRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.addMessage(parsedRequest.getMessage());
			} else if (request.isType("END_GAME")) {
				Log.i(TAG, "Got request to end game, this needs to be changed later so that last on leaving game will end the game as well.");
				endGame();
			}
			notify();
		}
	}

	void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return and deletes the next message from the message queue. If there is
	 * no messages in the queue, falls in sleep until notified by
	 * dispatchMessage method.
	 */
	private synchronized Message getNextMessageFromQueue() throws InterruptedException {
		while (mMessageQueue.size() == 0)
			wait();
		Message message = (Message) mMessageQueue.get(0);
		mMessageQueue.removeElementAt(0);
		return message;
	}

	/**
	 * Sends given message to all clients in the client list. Actually the
	 * message is added to the client sender thread's message queue and this
	 * client sender thread is notified.
	 */
	private synchronized void sendMessageToAllClients(Message aMessage) {
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if ((aMessage.getRecipient() == null || (aMessage.getRecipient() != null && clientInfo.id.equals(aMessage.getRecipient())))
					|| aMessage.getRecipient() != null && aMessage.getRecipient() == -1 && i > 0) {
				clientInfo.clientSender.sendMessage(aMessage);
			}
		}
	}

	/**
	 * Infinitely reads messages from the queue and dispatch them to all clients
	 * connected to the server.
	 */
	public void run() {
		try {
			while (online) {
				Message message = getNextMessageFromQueue();
				sendMessageToAllClients(message);
			}
		} catch (InterruptedException ie) {
			// Thread interrupted. Stop its execution
			setOnline(false);
		}
	}

	public GameServer getGameServer() {
		return gameServer;
	}

	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	public int getClientCount() {
		return mClients.size();
	}

	void setServerId(String id) {
		this.serverId = id;
	}

	public String getServerId() {
		return serverId;
	}

	void endGame() {
		if (getGameServer() != null) {
			getGameServer().endGame();
		}
		for (Object mClient : mClients) {
			ClientInfo clientInfo = (ClientInfo) mClient;
			clientInfo.getClientSender().setServerDispatcher(null);
			clientInfo.getClientSender().setLobbyServerDispatcher(Server.getLobbyServerDispatcher());
			clientInfo.getClientListener().setServerDispatcher(null);
			clientInfo.getClientListener().setLobbyServerDispatcher(Server.getLobbyServerDispatcher());
			Server.getLobbyServerDispatcher().addClient(clientInfo);
			deleteClient(clientInfo);
		}

		Server.getLobbyServerDispatcher().deleteServer(this);
	}

	public Vector getClients() {
		return mClients;
	}

	void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameType() {
		return gameType;
	}

	void setCreatedAt() {
		createdAt = System.currentTimeMillis();
	}

	public long getCreatedAt() {
		return createdAt;
	}
}