package game.models.abilities;

/**
 * Created by Eric on 2017-02-17.
 */
public class Buff {
	public static final Integer SPEED = 1;
	public static final Integer SHIELD = 2;
	public static final Integer DOT = 3;
	public static final Integer HOT = 4;
	public static final Integer RETALIATION = 5;

	public Integer heroId = 0;
	public Integer target = 0;
	public int type = 0;
	public int value = 0;
	public int duration = 0;
	public String tickTime = "0";
	public int ticks = 0;

	public Buff(Integer source, Integer target, Integer type, int value, int duration, String tickTime, int ticks) {
		this.heroId = source;
		this.target = target;
		this.type = type;
		this.value = value;
		this.duration = duration;
		this.tickTime = tickTime;
		this.ticks = ticks;
	}

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTickTime() {
		return tickTime;
	}

	public void setTickTime(String tickTime) {
		this.tickTime = tickTime;
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	@Override
	public String toString() {
		return "Buff{" +
				"heroId=" + heroId +
				", target=" + target +
				", type=" + type +
				", value=" + value +
				", duration=" + duration +
				", tickTime='" + tickTime + '\'' +
				", ticks=" + ticks +
				'}';
	}
}
