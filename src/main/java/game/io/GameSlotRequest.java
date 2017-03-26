package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 28-Oct-16.
 */
public class GameSlotRequest  extends JsonRequest {
	@SerializedName("game_type")
	public String gameType;

	public GameSlotRequest(String gameType) {
		this.gameType = gameType;
	}


	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	@Override
	public String toString() {
		return "JoinServerRequest{" +
				"gameType='" + gameType + '\'' +
				'}';
	}
}
