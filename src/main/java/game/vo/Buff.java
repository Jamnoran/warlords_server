package game.vo;

/**
 * Created by Eric on 2017-02-17.
 */
public class Buff {
	public static final Integer SPEED = 1;
	public static final Integer SHIELD = 2;
	public static final Integer DOT = 3;

	public Integer heroId = 0;
	public Integer target = 0;
	public int type = 0;
	public int value = 0;
	public int duration = 0;
	public long tickTime = 0;
	public int ticks = 0;

	public Buff(Integer source, Integer target, Integer type, int value, int duration, long tickTime, int ticks) {
		this.heroId = source;
		this.target = target;
		this.type = type;
		this.value = value;
		this.duration = duration;
		this.tickTime = tickTime;
		this.ticks = ticks;
	}
}
