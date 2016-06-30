package vo.classes;

import game.logging.Log;
import vo.Hero;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class Warrior extends Hero {

	private static final String TAG = Warrior.class.getSimpleName();
	// Base stats
	private transient Integer hpPerLevel = 20;

	public Warrior() {
		super();
	}

	public void generateHeroInformation() {
		setHp(hpPerLevel * getLevel());
		setMaxHp(getHp());
		Log.i(TAG, "Warrior is initialized with stats");
	}

}
