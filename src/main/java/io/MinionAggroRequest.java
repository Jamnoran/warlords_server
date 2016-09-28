package io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class MinionAggroRequest extends JsonRequest{

	@SerializedName("hero_id")
	private Integer hero_id;
	@SerializedName("minion_id")
	private Integer minion_id;

	public Integer getHero_id() {
		return hero_id;
	}

	public void setHero_id(Integer hero_id) {
		this.hero_id = hero_id;
	}

	public Integer getMinion_id() {
		return minion_id;
	}

	public void setMinion_id(Integer minion_id) {
		this.minion_id = minion_id;
	}

	@Override
	public String toString() {
		return "MinionAggroRequest{" +
				"hero_id=" + hero_id +
				", minion_id=" + minion_id +
				'}';
	}
}
