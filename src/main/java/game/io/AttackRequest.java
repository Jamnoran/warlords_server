package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class AttackRequest  extends JsonRequest{
	@SerializedName("minion_id")
	public Integer minion_id;
	@SerializedName("time")
	public Long time;

	public Integer getMinion_id() {
		return minion_id;
	}

	public void setMinion_id(Integer minion_id) {
		this.minion_id = minion_id;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "AttackRequest{" +
				"minion_id=" + minion_id +
				", time=" + time +
				'}';
	}
}
