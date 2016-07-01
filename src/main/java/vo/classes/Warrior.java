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
	private transient Integer strPerLevel = 3;
	private transient Integer intPerLevel = 1;
	private transient Integer dexPerLevel = 1;
	private transient Integer staPerLevel = 2;

	public Warrior() {
		super();
	}

	public void generateHeroInformation() {
		setHp(hpPerLevel * getLevel());
		setMaxHp(getHp());
		Log.i(TAG, "Warrior is initialized with stats");
		setStrength(strPerLevel * getLevel());
		setStamina(staPerLevel * getLevel());
		setDexterity(dexPerLevel * getLevel());
		setIntelligence(intPerLevel * getLevel());
	}

}
