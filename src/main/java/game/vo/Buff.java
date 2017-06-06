package game.vo;

/**
 * Created by Eric on 2017-02-17.
 */
public class Buff {
	public static final Integer SPEED = 1;
	public static final Integer SHIELD = 2;

	public Integer heroId;
	public Integer target;
	public Integer type;
	public int value;
	public int duration;

	public Buff(Integer source, Integer target, Integer type, int value, int duration) {
		this.heroId = source;
		this.target = target;
		this.type = type;
		this.value = value;
		this.duration = duration;
	}
}
