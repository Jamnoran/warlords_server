package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class StopHeroRequest extends JsonRequest{

	@SerializedName("hero_id")
	private Integer heroId;

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	@Override
	public String toString() {
		return "StopHeroRequest{" +
				"heroId=" + heroId +
				'}';
	}
}
