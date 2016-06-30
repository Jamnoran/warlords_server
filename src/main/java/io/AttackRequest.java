package io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class AttackRequest  extends JsonRequest{
	@SerializedName("minion_id")
	public Integer minion_id;

	public Integer getMinion_id() {
		return minion_id;
	}

	public void setMinion_id(Integer minion_id) {
		this.minion_id = minion_id;
	}

	@Override
	public String toString() {
		return "AttackRequest{" +
				"minion_id=" + minion_id +
				'}';
	}
}
