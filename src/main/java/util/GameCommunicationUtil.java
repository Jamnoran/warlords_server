package util;

import game.GameServer;
import io.JoinServerRequest;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameCommunicationUtil {
	private final GameServer server;

	public GameCommunicationUtil(GameServer server) {
		this.server = server;
	}

	public void handleJoinServerRequest(JoinServerRequest parsedRequest) {
		server.characterJoined(parsedRequest.getCharacter_id());
	}

	public void handleGetStatusRequest(JoinServerRequest parsedRequest) {
		server.sendStatusToAllClients();
	}
}
