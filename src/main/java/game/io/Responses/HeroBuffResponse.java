package game.io.Responses;


import game.io.JsonRequest;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class HeroBuffResponse extends JsonRequest {

	public String response_type = "HERO_BUFF";
	public Integer heroId;
	public Integer minionId;
	public Integer type;
	public float value;
	public long durationMillis;

	public HeroBuffResponse() {
	}

	public HeroBuffResponse(Integer heroId, Integer minionId, Integer type, float value, long durationMillis) {
		this.heroId = heroId;
		this.minionId = minionId;
		this.type = type;
		this.value = value;
		this.durationMillis = durationMillis;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public Integer getMinionId() {
		return minionId;
	}

	public void setMinionId(Integer minionId) {
		this.minionId = minionId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	@Override
	public String toString() {
		return "HeroBuffResponse{" +
				"response_type='" + response_type + '\'' +
				", heroId=" + heroId +
				", minionId=" + minionId +
				", type=" + type +
				", value=" + value +
				", durationMillis=" + durationMillis +
				'}';
	}
}
