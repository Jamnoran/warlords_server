package game.io.Requests;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class JoinServerRequest extends JsonRequest {
	@SerializedName("hero_id")
	public int heroId;
	@SerializedName("game_id")
	public String gameId;

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@Override
	public String toString() {
		return "JoinServerRequest{" +
				"heroId=" + heroId +
				", gameId='" + gameId + '\'' +
				'}';
	}
}
