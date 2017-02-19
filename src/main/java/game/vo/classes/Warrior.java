package game.vo.classes;

import game.logging.Log;
import game.vo.Hero;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class Warrior extends Hero {

	private static final String TAG = Warrior.class.getSimpleName();

	// Base stats
	private transient Integer resource = 100;
	private transient Integer hpPerLevel = 20;
	private transient Integer strPerLevel = 3;
	private transient Integer intPerLevel = 1;
	private transient Integer dexPerLevel = 1;
	private transient Integer staPerLevel = 2;
	private transient Integer hpRegen = 1;
	private transient Integer resourceRegen = 2;
	private transient float attackRange = 2.0f;

	public Warrior() {
		super();
	}

	public void generateHeroInformation() {
		setHp(hpPerLevel * getLevel());
		setResource(resource);
		setMaxResource(getResource());
		setMaxHp(getHp());
		Log.i(TAG, "Warrior is initialized with stats");
		setStrength(strPerLevel * getLevel());
		setStamina(staPerLevel * getLevel());
		setDexterity(dexPerLevel * getLevel());
		setIntelligence(intPerLevel * getLevel());
		setHpRegen(hpRegen);
		setResourceRegen(resourceRegen);
		setAttackRange(attackRange);
	}

}
