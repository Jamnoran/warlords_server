package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class ClickPortalRequest extends JsonRequest{

	@SerializedName("hero_id")
	private Integer hero_id;


	public Integer getHero_id() {
		return hero_id;
	}

	public void setHero_id(Integer hero_id) {
		this.hero_id = hero_id;
	}





	@Override
	public String toString() {
		return "MinionAggroRequest{" +
				"hero_id=" + hero_id +
				'}';
	}
}
