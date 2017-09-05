package game.vo.classes;

import game.logging.Log;
import game.vo.Hero;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class Warlock extends Hero {

	private static final String TAG = Warlock.class.getSimpleName();

	// Base stats
	private transient Integer hpPerLevel = 10;
	private transient Integer resourcePerLevel = 10;
	private transient Integer strPerLevel = 1;
	private transient Integer intPerLevel = 3;
	private transient Integer dexPerLevel = 1;
	private transient Integer staPerLevel = 2;
	private transient float attackRange = 4.0f;

	public Warlock() {
		super();
	}

	public void generateHeroInformation() {
		setHp(hpPerLevel * getLevel());
		setMaxHp(getHp());
		setResource(resourcePerLevel * getLevel());
		setMaxResource(getResource());
		Log.i(TAG, "Warlock is initialized with stats");
		setStrength(strPerLevel * getLevel());
		setStamina(staPerLevel * getLevel());
		setDexterity(dexPerLevel * getLevel());
		setIntelligence(intPerLevel * getLevel());
		setAttackRange(attackRange);
	}
}
