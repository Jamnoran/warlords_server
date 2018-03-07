package game.io.Requests;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;

/**
 * Created by Eric on 2017-04-09.
 */
public class AbilityPositionRequest  extends JsonRequest {
	@SerializedName("hero_id")
	public Integer heroId;
	@SerializedName("ability_id")
	public Integer abilityId;
	@SerializedName("position")
	public Integer position;

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public Integer getAbilityId() {
		return abilityId;
	}

	public void setAbilityId(Integer abilityId) {
		this.abilityId = abilityId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "AbilityPositionRequest{" +
				"heroId=" + heroId +
				", abilityId=" + abilityId +
				", position=" + position +
				'}';
	}
}
