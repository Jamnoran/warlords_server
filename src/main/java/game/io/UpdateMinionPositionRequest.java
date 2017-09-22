package game.io;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;
import game.vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 2017-08-10.
 */
public class UpdateMinionPositionRequest  extends JsonRequest {

	@SerializedName("hero_id")
	private Integer heroId;
	@SerializedName("minions")
	private ArrayList<Minion> minions;

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public ArrayList<Minion> getMinions() {
		return minions;
	}

	public void setMinions(ArrayList<Minion> minions) {
		this.minions = minions;
	}

	@Override
	public String toString() {
		return "UpdateMinionPositionRequest{" +
				"heroId=" + heroId +
				", minions=" + minions +
				", requestType='" + requestType + '\'' +
				", user_id='" + user_id + '\'' +
				'}';
	}
}
