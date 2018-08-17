package game;

import game.io.WebserviceCommunication;
import game.logging.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Eric on 2017-03-03.
 */
public class Server {
	private static final String TAG = Server.class.getSimpleName();
	// Configuration;
	static int portNumber = 2065; //2055
	public static boolean local = false;
	private static LobbyServerDispatcher lobbyServerDispatcher;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.i(TAG, "Server is up and running!");

		// Register to webservice that we are a game server
		if (!local) {
			try {
				WebserviceCommunication.sendGameServerOnline(portNumber);
			} catch (Exception e) {
				Log.i(TAG, "Could not register this server as online");
			}
		}

		// Open up connections for players to connect
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
			Log.i(TAG, "Added client to server : " + lobbyServerDispatcher.getServerId() + " size : " + lobbyServerDispatcher.getClientCount());
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