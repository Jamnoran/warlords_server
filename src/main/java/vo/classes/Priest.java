package vo.classes;

import game.logging.Log;
import util.CalculationUtil;
import vo.Hero;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class Priest extends Hero {

	private static final String TAG = Priest.class.getSimpleName();

	// Base stats
	private transient Integer hpPerLevel = 10;
	private transient Integer strPerLevel = 1;
	private transient Integer intPerLevel = 3;
	private transient Integer dexPerLevel = 1;
	private transient Integer staPerLevel = 2;
	private transient Integer resourcePerLevel = 10;

	public Priest() {
		super();
	}

	public void generateHeroInformation() {
		setHp(hpPerLevel * getLevel());
		setMaxHp(getHp());
		Log.i(TAG, "Priest is initialized with stats");
		setStrength(strPerLevel * getLevel());
		setStamina(staPerLevel * getLevel());
		setDexterity(dexPerLevel * getLevel());
		setIntelligence(intPerLevel * getLevel());
		setResource(resourcePerLevel * getLevel());
		setMaxResource(getResource());
	}
}
