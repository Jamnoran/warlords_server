package game.models.heroes.warlock;

import game.models.heroes.Hero;
import game.models.talents.Talent;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class Warlock extends Hero {

	private static final String TAG = Warlock.class.getSimpleName();

	// Base stats
	private transient Integer hpPerLevel = 10;
	private transient Integer strPerLevel = 1;
	private transient Integer intPerLevel = 3;
	private transient Integer dexPerLevel = 1;
	private transient Integer staPerLevel = 2;
	private transient Integer hpRegen = 1;
	private transient float attackRange = 8.0f;

	public Warlock() {
		super();
	}

	public void generateHeroInformation() {
		setHp((hpPerLevel * getLevel()) + getHpFromTalents());
		setMaxHp(getHp());
		setStrength(strPerLevel * getLevel());
		setStamina(staPerLevel * getLevel());
		setDexterity(dexPerLevel * getLevel());
		setIntelligence(intPerLevel * getLevel() + Math.round(Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_INTELLIGENCE)));
		setAttackRange(attackRange);
		setHpRegen(hpRegen);
		calculateArmor();
		calculateMagicResist();
	}
	public Integer getHpPerLevel() {
		return hpPerLevel;
	}

	public Integer getStrPerLevel() {
		return strPerLevel;
	}

	public Integer getIntPerLevel() {
		return intPerLevel;
	}

	public Integer getDexPerLevel() {
		return dexPerLevel;
	}

	public Integer getStaPerLevel() {
		return staPerLevel;
	}

}
