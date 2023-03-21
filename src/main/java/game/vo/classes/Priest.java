package game.vo.classes;

import game.vo.Hero;
import game.vo.Talent;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class Priest extends Hero {

	private static final String TAG = Priest.class.getSimpleName();

	// Base stats
	private transient Integer hpPerLevel = 10;
	private transient Integer resourcePerLevel = 10;
	private transient Integer strPerLevel = 1;
	private transient Integer intPerLevel = 3;
	private transient Integer dexPerLevel = 1;
	private transient Integer staPerLevel = 2;
	private transient Integer resourceRegen = 1;
	private transient Integer hpRegen = 1;
	private transient float attackRange = 4.0f;

	public Priest() {
		super();
	}

	public void generateHeroInformation() {
		setHp((hpPerLevel * getLevel()) + getHpFromTalents());
		setMaxHp(getHp());
		setResource(resourcePerLevel * getLevel());
		setMaxResource(getResource());
		setStrength(strPerLevel * getLevel());
		setStamina(staPerLevel * getLevel());
		setDexterity(dexPerLevel * getLevel());
		setIntelligence(intPerLevel * getLevel() + Math.round(Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_INTELLIGENCE)));
		setHpRegen(hpRegen);
		setResourceRegen(resourceRegen);
		setAttackRange(attackRange);
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
