package game;

import vo.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    // Configuration;
    static int portNumber = 2055;
    private static int CLIENTS_PER_SERVER = 3;
    private static int idOfServerCounter = 1;


    private static ArrayList<ServerDispatcher> servers = new ArrayList<ServerDispatcher>();


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("warlords.game.Server is up and running!");
		// Open server socket for listening
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Warlords started on port " + portNumber);
		} catch (IOException se) {
			System.err.println("Can not start listening on port " + portNumber);
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
            System.out.println("Incoming connection");

            // Get a server that the client can join.
            ServerDispatcher server = getAvailableServer();
			server.setGameServer(new GameServer(server));

            // Create the client
            ClientInfo clientInfo = createClient(socket, server);

            // Add client to server
            server.addClient(clientInfo);
            System.out.println("Added client to server : " + server.getServerId() + " That now has this many players: " + server.getClientCount());

            // Dispatch message to all client that a client has joined
            server.dispatchMessage(new Message(null, "{ \"clients\" : \"" + server.getClientCount() + "\"}"));
		}
	}

    private static ClientInfo createClient(Socket socket, ServerDispatcher server) {
        try {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.mSocket = socket;
            clientInfo.id = server.getNextClientId();
            ClientListener clientListener = new ClientListener(clientInfo, server);
            ClientSender clientSender = new ClientSender(clientInfo, server);
            clientInfo.mClientListener = clientListener;
            clientInfo.mClientSender = clientSender;
            clientListener.start();
            clientSender.start();
            return clientInfo;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

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
}
