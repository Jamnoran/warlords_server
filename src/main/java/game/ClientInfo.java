package game;

import java.net.Socket;

public class ClientInfo {
    public static final int LOBBY = 1;
    public static final int GAME = 2;
    public static final int CLIENT = 3;

    public Socket mSocket = null;
    public ClientListener clientListener = null;
    public ClientSender clientSender = null;
    public Integer id = null;
    public Integer heroId = null;
    private Integer typeOfConnect = null;

    public Socket getmSocket() {
        return mSocket;
    }

    public void setmSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }

    public ClientListener getClientListener() {
        return clientListener;
    }

    public void setClientListener(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    public ClientSender getClientSender() {
        return clientSender;
    }

    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeroId() {
        return heroId;
    }

    public void setHeroId(Integer heroId) {
        this.heroId = heroId;
    }

    public Integer getTypeOfConnect() {
        return typeOfConnect;
    }

    public void setTypeOfConnect(Integer typeOfConnect) {
        this.typeOfConnect = typeOfConnect;
    }
}
