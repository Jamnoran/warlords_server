package game.vo;

import java.util.ArrayList;

/**
 * Created by Eric on 2017-03-05.
 */
public class ServerList {
	private ArrayList<Server> lobby;
	private ArrayList<Server> game;

	public ArrayList<Server> getLobby() {
		return lobby;
	}

	public void setLobby(ArrayList<Server> lobby) {
		this.lobby = lobby;
	}

	public ArrayList<Server> getGame() {
		return game;
	}

	public void setGame(ArrayList<Server> game) {
		this.game = game;
	}

	@Override
	public String toString() {
		return "ServerList{" +
				"lobby=" + lobby +
				", game=" + game +
				'}';
	}
}
