package game.io;


/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class HeroBuffResponse extends JsonRequest{

	private String responseType = "HERO_BUFF";
	private Integer heroId;
	private Integer minionId;
	private Integer type;
	private float value;
	private long durationMillis;

	public HeroBuffResponse() {
	}

	public HeroBuffResponse(Integer heroId, Integer minionId, Integer type, float value, long durationMillis) {
		this.heroId = heroId;
		this.minionId = minionId;
		this.type = type;
		this.value = value;
		this.durationMillis = durationMillis;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
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
				"responseType='" + responseType + '\'' +
				", heroId=" + heroId +
				", minionId=" + minionId +
				", type=" + type +
				", value=" + value +
				", durationMillis=" + durationMillis +
				'}';
	}
}
