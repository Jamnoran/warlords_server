package game;

import vo.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientListener extends Thread
{
    private ServerDispatcher mServerDispatcher;
    private ClientInfo mClientInfo;
    private BufferedReader mIn;
 
    public ClientListener(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)
    throws IOException
    {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
 
    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher's queue and notifies the server dispatcher.
     */
    public void run(){
        try {
           while (!isInterrupted()) {
               String message = mIn.readLine();
               if (message == null)
                   break;
               mServerDispatcher.handleClientRequest(new Message(message));
           }
        } catch (IOException ioex) {
           // Problem reading from socket (communication is broken)
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientSender.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
 
}