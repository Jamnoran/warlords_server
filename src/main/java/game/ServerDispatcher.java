package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.logging.Log;
import game.io.*;
import game.util.DatabaseUtil;
import game.vo.Hero;
import game.vo.Message;
import game.io.SendMessageRequest;
import io.UpdateMinionPositionRequest;

import java.util.Vector;

public class ServerDispatcher extends Thread {
	private static final String TAG = ServerDispatcher.class.getSimpleName();

	private Vector mMessageQueue = new Vector();
	private Vector mClients = new Vector();
	private String serverId;

	private GameServer gameServer;
	private String gameType;

	private long createdAt = 0;

	/**
	 * Adds given client to the server's client list.
	 */
	public synchronized void addClient(ClientInfo aClientInfo) {
		mClients.add(aClientInfo);
		getClientsHero(aClientInfo.getHeroId());
	}

	private void getClientsHero(Integer heroId) {
		Log.i(TAG, "Add hero with id: " + heroId);
		Hero hero = DatabaseUtil.getHero(heroId);
		if (hero != null && hero.id > 0) {
			Log.i(TAG, "Got this hero: " + hero.toString());
			gameServer.addHero(hero);
		} else {
			Log.i(TAG, "Could not find hero");
		}
	}

	/**
	 * Deletes given client from the server's client list if the client is in
	 * the list.
	 */
	public synchronized void deleteClient(ClientInfo aClientInfo) {
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
	 * (warlords.game.ClientListener) when a message is arrived.
	 */
	public synchronized void dispatchMessage(Message aMessage) {
		mMessageQueue.add(aMessage);
		notify();
	}

	public synchronized void handleClientRequest(Message aMessage) {
		Gson gson = new GsonBuilder().create();
		JsonRequest request = JsonRequest.parse(aMessage);
		if (request != null) {
			if (!request.isType("UPDATE_MINION_POSITION") && !request.isType("MOVE")) {
				Log.i(TAG, "JsonRequest: " + request.toString());
			}
			if (request.isType("GET_STATUS")) {
				JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.sendGameStatus();
			} else if (request.isType("SPAWN_POINTS")) {
				SpawnPointsRequest parsedRequest = gson.fromJson(aMessage.getMessage(), SpawnPointsRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.addSpawnPoints(parsedRequest.getPoints());
			} else if (request.isType("ATTACK")) {
				AttackRequest parsedRequest = gson.fromJson(aMessage.getMessage(), AttackRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.attack(parsedRequest.getHeroId(), parsedRequest.getMinion_id(), parsedRequest.getTime());
			} else if (request.isType("MOVE")) {
				MoveRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MoveRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.heroMove(parsedRequest);
			} else if (request.isType("SPELL")) {
				SpellRequest parsedRequest = gson.fromJson(aMessage.getMessage(), SpellRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.spell(parsedRequest);
			} else if (request.isType("MINION_AGGRO")) {
				MinionAggroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MinionAggroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.minionAggro(parsedRequest);
			} else if (request.isType("MINION_TARGET_IN_RANGE")) {
				MinionAggroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MinionAggroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.minionTargetInRange(parsedRequest);
			} else if (request.isType("CLICKED_PORTAL")) {
				ClickPortalRequest parsedRequest = gson.fromJson(aMessage.getMessage(), ClickPortalRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.clickPortal(parsedRequest.getHero_id());
			} else if (request.isType("GET_ABILITIES")) {
				Log.i(TAG, "parsedRequest : " + request.toString());
				gameServer.sendAbilities(request.getUser_id());
			} else if (request.isType("UPDATE_ABILITY_POSITION")) {
				AbilityPositionRequest abilityPositionRequest = gson.fromJson(aMessage.getMessage(), AbilityPositionRequest.class);
				Log.i(TAG, "parsedRequest : " + request.toString());
				gameServer.updateAbilityPosition(abilityPositionRequest);
			} else if (request.isType("UPDATE_TALENTS")) {
				TalentRequest talentRequest = gson.fromJson(aMessage.getMessage(), TalentRequest.class);
				Log.i(TAG, "parsedRequest : " + talentRequest.toString());
//				gameServer.updateAbilityPosition(abilityPositionRequest);
				DatabaseUtil.addTalentPoints(talentRequest.talents);
			} else if (request.isType("STOP_HERO")) {
				StopHeroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), StopHeroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.stopHero(parsedRequest.getHeroId());
			} else if (request.isType("RESTART_LEVEL")) {
				Log.i(TAG, "parsedRequest : " + request.toString());
				gameServer.restartLevel();
			} else if (request.isType("UPDATE_MINION_POSITION")) {
				//Log.i(TAG, "parsedRequest : " + request.toString());
				UpdateMinionPositionRequest parsedRequest = gson.fromJson(aMessage.getMessage(), UpdateMinionPositionRequest.class);
				gameServer.updateMinionPositions(parsedRequest.getMinions());
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
			if ((aMessage.getRecipient() == null || (aMessage.getRecipient() != null && clientInfo.id == aMessage.getRecipient()))
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
			while (true) {
				Message message = getNextMessageFromQueue();
				sendMessageToAllClients(message);
			}
		} catch (InterruptedException ie) {
			// Thread interrupted. Stop its execution
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

	public void setServerId(String id) {
		this.serverId = id;
	}

	public String getServerId() {
		return serverId;
	}

	public void endGame() {
		if (getGameServer() != null) {
			getGameServer().endGame();
		}
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
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

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameType() {
		return gameType;
	}

	public void setCreatedAt() {
		createdAt = System.currentTimeMillis();
	}

	public long getCreatedAt() {
		return createdAt;
	}
}