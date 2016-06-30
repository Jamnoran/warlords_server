package game;

import game.logging.Log;
import io.JoinServerRequest;
import io.JsonRequest;
import vo.Message;

import java.util.Vector;

public class ServerDispatcher extends Thread {
	private static final String TAG = ServerDispatcher.class.getSimpleName();

	private Vector mMessageQueue = new Vector();
	private Vector mClients = new Vector();
    private int serverId = 0;

	private GameServer gameServer;

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
		JsonRequest request = JsonRequest.parse(gameServer, aMessage);
        if (request != null) {
	        Log.i(TAG, "JsonRequest: " + request.toString());

	        if(request.isType("CHARACTER_ACTION")){
		        JoinServerRequest parsedRequest = (JoinServerRequest) request;
		        gameServer.getGameCommunicationUtil().handleJoinServerRequest(parsedRequest);
	        }else if(request.isType("GET_STATUS")){
		        JoinServerRequest parsedRequest = (JoinServerRequest) request;
		        Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
		        gameServer.getGameCommunicationUtil().handleGetStatusRequest(parsedRequest);
	        }
            notify();
        }
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

	public GameServer getGameServer() {
		return gameServer;
	}

	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
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