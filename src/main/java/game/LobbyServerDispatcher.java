package game;

import com.google.gson.Gson;
import game.io.*;
import game.logging.Log;
import game.util.DatabaseUtil;
import game.vo.Hero;
import game.vo.Message;
import game.vo.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class LobbyServerDispatcher extends Thread {
	private static final String TAG = LobbyServerDispatcher.class.getSimpleName();
	private Vector mMessageQueue = new Vector();
	private static Vector mClients = new Vector();
	private int serverId = 0;

	private static int gameCounter = 0;

	private static ArrayList<ServerDispatcher> servers = new ArrayList<ServerDispatcher>();


	private static ServerDispatcher getServer(String id) {
		// Start warlords.game.ServerDispatcher thread
		if (servers.size() == 0) {
			return null;
		} else {
			for (ServerDispatcher server : servers) {
				if (server.getServerId().equals(id)) {
					return server;
				}
			}
		}
		return null;
	}

	private static ServerDispatcher createServer(String gameType) {
		ServerDispatcher serverDispatcher = new ServerDispatcher();
		serverDispatcher.start();

		// Could not find any servers with free slots in create a new one
		UUID uniqueKey = UUID.randomUUID();
		serverDispatcher.setServerId(uniqueKey.toString());
		serverDispatcher.setGameType(gameType);
		serverDispatcher.setCreatedAt();
		servers.add(serverDispatcher);
		gameCounter++;
		Log.i(TAG, "This is game sever number : " + gameCounter);
		return serverDispatcher;
	}


	/**
	 * Adds given client to the server's client list.
	 */
	public static synchronized void addClient(ClientInfo aClientInfo) {
		mClients.add(aClientInfo);
	}

	/**
	 * Deletes given client from the server's client list if the client is in
	 * the list.
	 */
	public synchronized void deleteClient(ClientInfo aClientInfo) {
		int clientIndex = mClients.indexOf(aClientInfo);
		if (clientIndex != -1) {
			mClients.removeElementAt(clientIndex);
			Log.i(TAG, "Client left, size: " + mClients.size());
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

	public synchronized void handleClientRequest(ClientInfo clientInfo, Message aMessage) {
		Gson gson = new Gson();
		JsonRequest request = null;
		if (aMessage != null && aMessage.getMessage() != null) {
			request = JsonRequest.parse(aMessage);
		}
		if (request != null && request.getRequestType() != null) {
			Log.i(TAG, "Got this request" + request.toString());
			if (request.getRequestType().equals("JOIN_GAME")) {
				JoinServerRequest joinServerRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				if (joinServerRequest.getHeroId() > 0 && joinServerRequest.getGameId() != null) {
					clientInfo.setHeroId(joinServerRequest.getHeroId());
					clientJoinServer(clientInfo, joinServerRequest.getGameId());
				} else {
					Log.i(TAG, "Could not game heroid or gameid : " + joinServerRequest.toString());
				}
				Log.i(TAG, "Done with join_game");
			} else if (request.getRequestType().equals("GAME_SLOT_AVAILABLE")) {
				GameSlotRequest gameSlotRequest = gson.fromJson(aMessage.getMessage(), GameSlotRequest.class);

				// TODO: Go through all servers and remove games that's older than 10 minutes and have no players

				ServerDispatcher serverDispatcher = createServer(gameSlotRequest.getGameType());
				dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new GameSlotResponse(true, serverDispatcher.getServerId()))));

				Log.i(TAG, "Sending back we got room! (" + servers.size() + "/100)");
			}
		}

		if (request != null) {
			Log.i(TAG, "JsonRequest: " + request.toString());
			notify();
		}
	}

	private void clientJoinServer(ClientInfo clientInfo, String serverId) {
		// Get a server that the client can join.
		ServerDispatcher server = getServer(serverId);

		if(server == null){
			// This means we will create a local game (its debug mode)
			server = createServer("CUSTOM");
		}

		// TODO: Check if its time to start game
		if (server.getGameServer() == null) {
			server.setGameServer(new GameServer(server));
		}

		clientInfo.getClientSender().setServerDispatcher(server);
		clientInfo.getClientSender().setLobbyServerDispatcher(null);
		clientInfo.getClientListener().setServerDispatcher(server);
		clientInfo.getClientListener().setLobbyServerDispatcher(null);

		server.addClient(clientInfo);

		deleteClient(clientInfo);

		Log.i(TAG, "Client joined server that has this many players now [" + server.getClientCount() + "] Clients left in limbo [" + getClientCount() + "]");
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

	public Integer getNextClientId() {
		int id = 1;
		for (int i = 0; i < mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if (clientInfo.id <= id) {
				id++;
			}
		}
		return id;
	}

	public int getClientCount() {
		return mClients.size();
	}

	public void setServerId(int id) {
		this.serverId = id;
	}

	public int getServerId() {
		return serverId;
	}

	public static void deleteServer(ServerDispatcher serverToDelete) {
		Iterator<ServerDispatcher> ut = servers.iterator();
		while (ut.hasNext()) {
			ServerDispatcher serverDispatcher = ut.next();
			if (serverDispatcher.getId() == serverToDelete.getId()) {
				Log.i(TAG, "Found serverDispatcher to delete : " + serverDispatcher.getId());
				ut.remove();
			}
		}
	}
}
