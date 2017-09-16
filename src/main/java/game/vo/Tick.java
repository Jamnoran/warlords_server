package game.vo;

/**
 * Created by Eric on 2017-09-15.
 */
public class Tick implements Comparable<Tick>{
	public static int GAME_STATUS = 1;
	public static int HERO_REGEN = 2;
	public static int BUFF = 3;
	public static int DEBUFF = 4;
	public static int MINION_ACTION = 5;
	public static int REQUEST_MINION_POSITION = 6;
	public static int MINION_DEBUFF = 7;

	public long timeToActivate = 0;
	public int typeOfTick = 0;

	public Tick(long timeToActivate, int typeOfTick) {
		this.timeToActivate = timeToActivate;
		this.typeOfTick = typeOfTick;
	}

	@Override
	public int compareTo(Tick tickToCompare) {
		return Long.compare(timeToActivate, tickToCompare.timeToActivate);
	}
}
