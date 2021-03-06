package game;

import game.logging.Log;
import game.vo.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class OldServer {

	private static final String TAG = OldServer.class.getSimpleName();
	// Configuration;
    static int portNumber = 2055;
    private static int MAX_GAMES = 100;
    private int gameCount = 0;

	private static LobbyServerDispatcher lobbyServerDispatcher;


	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.i(TAG, "warlords.game.OldServer is up and running!");

		lobbyServerDispatcher = new LobbyServerDispatcher();
		lobbyServerDispatcher.start();
		// Open server socket for listening
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			Log.i(TAG, "Warlords started on port " + portNumber);
		} catch (IOException se) {
			Log.i(TAG, "Can not start listening on port " + portNumber);
			se.printStackTrace();
			System.exit(-1);
		}

		// Accept and handle client connections
		while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
			Log.i(TAG, "Incoming connection");

            // Create the client
            ClientInfo clientInfo = createClient(socket, lobbyServerDispatcher);

			lobbyServerDispatcher.addClient(clientInfo);
            // Add client to server
//            server.addClient(clientInfo);
			Log.i(TAG, "Added client to server : " + lobbyServerDispatcher.getServerId() + " That now has this many players: " + lobbyServerDispatcher.getClientCount());

            // Dispatch message to all client that a client has joined
			lobbyServerDispatcher.dispatchMessage(new Message(null, "{\"response_type\":\"SERVER_INFO\", \"clients\" : \"" + lobbyServerDispatcher.getClientCount() + "\"}"));
		}
	}

    private static ClientInfo createClient(Socket socket, LobbyServerDispatcher server) {
        try {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.mSocket = socket;
            clientInfo.id = server.getNextClientId();
            ClientListener clientListener = new ClientListener(clientInfo, server);
            ClientSender clientSender = new ClientSender(clientInfo, server);
            clientInfo.clientListener = clientListener;
            clientInfo.clientSender = clientSender;
            clientListener.start();
            clientSender.start();
            return clientInfo;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

	public static LobbyServerDispatcher getLobbyServerDispatcher() {
		return lobbyServerDispatcher;
	}
}
