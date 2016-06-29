package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.logging.Log;
import io.CreateCharacterRequest;
import io.JsonRequest;
import vo.Message;

import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class LobbyServerDispatcher extends Thread {
	private static final String TAG = LobbyServerDispatcher.class.getSimpleName();
	private Vector mMessageQueue = new Vector();
	private Vector mClients = new Vector();
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
	public synchronized void addClient(ClientInfo aClientInfo) {
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
		Gson gson = new GsonBuilder().create();

		JsonRequest request = null;
		if (aMessage != null && aMessage.getMessage() != null) {
			request = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
		}

		if (request != null && request.getRequestType() != null) {
			Log.i(TAG, "Got this request" + request.toString());

			if(request.getRequestType().equals("JOIN_SERVER")){
				clientJoinServer(clientInfo);
			}else if (request.getRequestType().equals("CREATE_CHARACTER")){
				CreateCharacterRequest createCharacterRequest = (CreateCharacterRequest) request.getRequest();
				Log.i(TAG, "User is trying to create class: ");
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
		server.setGameServer(new GameServer(server));

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

}
