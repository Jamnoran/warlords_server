package game.communication;

import game.vo.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientListener extends Thread {
	private LobbyServerDispatcher lobbyServerDispatcher;
    private ServerDispatcher serverDispatcher;
    private ClientInfo clientInfo;
    private BufferedReader in;
 
    public ClientListener(ClientInfo aClientInfo, LobbyServerDispatcher lobbyServerDispatcher) throws IOException
    {
        clientInfo = aClientInfo;
	    this.lobbyServerDispatcher = lobbyServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

	public ServerDispatcher getServerDispatcher() {
		return serverDispatcher;
	}

	public void setServerDispatcher(ServerDispatcher mServerDispatcher) {
		this.serverDispatcher = mServerDispatcher;
	}

	public LobbyServerDispatcher getLobbyServerDispatcher() {
		return lobbyServerDispatcher;
	}

	public void setLobbyServerDispatcher(LobbyServerDispatcher lobbyServerDispatcher) {
		this.lobbyServerDispatcher = lobbyServerDispatcher;
	}

	/**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run(){
        try {
           while (!isInterrupted()) {
               String message = in.readLine();
               if (message == null)
                   break;
	           if (serverDispatcher != null) {
		           serverDispatcher.handleClientRequest(new Message(message));
	           } else {
		           lobbyServerDispatcher.handleClientRequest(clientInfo, new Message(message));
	           }
           }
        } catch (IOException ioex) {
           // Problem reading from socket (communication is broken)
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        clientInfo.clientSender.interrupt();
	    if (serverDispatcher != null) {
		    serverDispatcher.deleteClient(clientInfo);
	    }
	    if (lobbyServerDispatcher != null) {
		    lobbyServerDispatcher.deleteClient(clientInfo);
	    }
    }
 
}