package game;

import game.logging.Log;
import io.CreateHeroRequest;
import io.CreateUserRequest;
import io.JoinServerRequest;
import io.JsonRequest;
import util.DatabaseUtil;
import vo.Hero;
import vo.Message;
import vo.Minion;
import vo.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class LobbyServerDispatcher extends Thread {
	private static final String TAG = LobbyServerDispatcher.class.getSimpleName();
	private Vector mMessageQueue = new Vector();
	private static Vector mClients = new Vector();
	private int serverId = 0;

	private static int CLIENTS_PER_SERVER = 3;
	private static int idOfServerCounter = 1;


	private static ArrayList<ServerDispatcher> servers = new ArrayList<ServerDispatcher>();


	private static ServerDispatcher getAvailableServer() {
		// Start warlords.game.ServerDispatcher thread
		if (servers.size() == 0) {
			// First server.
			return createServer();
		} else {
			for(ServerDispatcher server : servers){
				if(server.getClientCount() < CLIENTS_PER_SERVER){
					return server;
				}
			}
			// Could not find any servers with free slots in create a new one
			return createServer();
		}
	}

	private static ServerDispatcher createServer() {
		ServerDispatcher serverDispatcher = new ServerDispatcher();
		serverDispatcher.start();
		serverDispatcher.setServerId(idOfServerCounter);
		servers.add(serverDispatcher);
		idOfServerCounter++;
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
		if (clientIndex != -1){
			mClients.removeElementAt(clientIndex);
		}
		Log.i(TAG, "Client left, size: " + mClients.size());
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
		JsonRequest request = null;
		if (aMessage != null && aMessage.getMessage() != null) {
			request = JsonRequest.parse(aMessage);
		}
		if (request != null && request.getRequestType() != null) {
			Log.i(TAG, "Got this request" + request.toString());
			if(request.getRequestType().equals("JOIN_SERVER")){
				JoinServerRequest joinServerRequest = (JoinServerRequest) request;
				clientInfo.setHeroId(Integer.parseInt(joinServerRequest.getHero_id()));
				clientJoinServer(clientInfo);
			}else if (request.getRequestType().equals("CREATE_HERO")){
				CreateHeroRequest createHeroRequest = (CreateHeroRequest) request;
				Log.i(TAG, "User is trying to create class: " + createHeroRequest.toString());
				Hero hero = DatabaseUtil.createHero(Integer.parseInt(createHeroRequest.getUser_id()), createHeroRequest.getClass_type());
				Log.i(TAG, "Created hero : " + hero.toString());
			}else if (request.getRequestType().equals("CREATE_USER")){
				CreateUserRequest createUserRequest = (CreateUserRequest) request;
				Log.i(TAG, "User is trying to create class: " + createUserRequest.toString());
				User user = DatabaseUtil.createUser(new User(createUserRequest.getUsername(), createUserRequest.getEmail(), createUserRequest.getPassword()));
				Log.i(TAG, "Created user with this is: " + user.getId() + " We need to send that back to client");
				dispatchMessage(new Message(clientInfo.getId(), "{\"response_type\":\"CREATE_USER\", \"user_id\" : \"" + user.getId() + "\"}"));
			}else if (request.getRequestType().equals("GET_HEROES")){
				Log.i(TAG, "User wants his heroes: " + request.toString());
				String heroesJson = "";
				ArrayList<Hero> heroes = DatabaseUtil.getHeroes(Integer.parseInt(request.getUser_id()));
				if (heroes.size() > 0){
					for (Hero hero : heroes){
						if(heroesJson.length() > 2){
							heroesJson = heroesJson + ",";
						}
						heroesJson = heroesJson + "{";

						heroesJson = heroesJson + "\"id\": " + hero.getId() + ", \"level\": " + hero.getLevel() + ", \"class_type\": \"" + hero.getClass_type() + "\"";
						heroesJson = heroesJson + "}";
					}
				}else {
					heroesJson = "{}";
				}
				dispatchMessage(new Message(clientInfo.getId(), "{\"response_type\":\"HEROES\", \"heroes\" : [" + heroesJson + "]}"));
			}
		}

		if (request != null) {
			Log.i(TAG, "JsonRequest: " + request.toString());
			notify();
		}
	}

	private void clientJoinServer(ClientInfo clientInfo) {
		// Get a server that the client can join.
        ServerDispatcher server = getAvailableServer();
		if (server.getGameServer() == null) {
			server.setGameServer(new GameServer(server));
		}

		clientInfo.getClientSender().setServerDispatcher(server);
		clientInfo.getClientSender().setLobbyServerDispatcher(null);
		clientInfo.getClientListener().setServerDispatcher(server);
		clientInfo.getClientListener().setLobbyServerDispatcher(null);

		server.addClient(clientInfo);

		deleteClient(clientInfo);
	}

	/**
	 * @return and deletes the next message from the message queue. If there is
	 *         no messages in the queue, falls in sleep until notified by
	 *         dispatchMessage method.
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
					|| aMessage.getRecipient() != null &&  aMessage.getRecipient() == -1 && i > 0 ) {
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
			if(clientInfo.id <= id){
				id++;
			}
		}
		return id;
	}

	public int getClientCount(){
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
			if(serverDispatcher.getId() == serverToDelete.getId()){
				Log.i(TAG, "Found serverDispatcher to delete : " + serverDispatcher.getId());
				ut.remove();
			}
		}
	}
}
