package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class JoinServerRequest extends JsonRequest {
	@SerializedName("hero_id")
	public String hero_id;

	public String getHero_id() {
		return hero_id;
	}

	public void setHero_id(String hero_id) {
		this.hero_id = hero_id;
	}

	@Override
	public String toString() {
		return "JoinServerRequest{" +
				"hero_id='" + hero_id + '\'' +
				'}';
	}
}
