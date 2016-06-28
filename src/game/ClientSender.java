package game;

import vo.Message;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;


public class ClientSender  extends Thread
{
    private Vector mMessageQueue = new Vector();
 
    private ServerDispatcher mServerDispatcher;
    private ClientInfo mClientInfo;
    private PrintWriter mOut;
 
    public ClientSender(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher) throws IOException{
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
 
    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * sendMessage is called by other threads (ServeDispatcher).
     */
    public synchronized void sendMessage(Message aMessage){
        mMessageQueue.add(aMessage);
        notify();
    }
 
    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    private synchronized Message getNextMessageFromQueue() throws InterruptedException{
        while (mMessageQueue.size()==0)
           wait();
        Message message = (Message) mMessageQueue.get(0);
        mMessageQueue.removeElementAt(0);
        return message;
    }
 
    /**
     * Sends given message to the client's socket.
     */
    private void sendMessageToClient(String aMessage){
    	System.out.println("Sending message to client: " + aMessage);
        mOut.println(aMessage);
        mOut.flush();
    }
 
    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */
    public void run(){
        try {
           while (!isInterrupted()) {
        	   Message message = getNextMessageFromQueue();
               sendMessageToClient(message.getMessage());
           }
        } catch (Exception e) {
           // Commuication problem
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientListener.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
 
}