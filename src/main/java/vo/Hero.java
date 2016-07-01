package vo;

import game.logging.Log;
import util.CalculationUtil;

public class Hero {
	public static final String WARRIOR = "WARRIOR";
	public static final String PRIEST = "PRIEST";
	private static final String TAG = Hero.class.getSimpleName();

	// General stats
	public Integer id = null;
	private Integer user_id = null;
	private Integer xp = 0;
	private Integer level = 1;
	private float positionX = 6.0f;
	private float positionY = 5.0f;
	private float desiredPositionX = 6.0f;
	private float desiredPositionY = 5.0f;
	private String class_type = "WARRIOR";


	// Hero stats
	private Integer hp;
	private Integer maxHp;
	private transient Integer strength;
	private transient Integer intelligence;
	private transient Integer stamina;
	private transient Integer dexterity;
	private transient Integer baseAttackDamage = 2;
	private transient Integer baseMaxAttackDamage = 4;
	private transient float attackStrScaling = 0.1f;
	private transient float criticalMultiplier = 2.0f;
	private transient float criticalChance = 0.25f;

	public Hero() {
	}

	public Hero(Integer user_id) {
		this.user_id = user_id;
	}

	public Hero(String user_id) {
		this.user_id = Integer.parseInt(user_id);
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getXp() {
		return xp;
	}

	public void setXp(Integer xp) {
		this.xp = xp;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public float getPositionX() {
		return positionX;
	}
	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}
	public float getPositionY() {
		return positionY;
	}
	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getDesiredPositionX() {
		return desiredPositionX;
	}

	public void setDesiredPositionX(float desiredPositionX) {
		this.desiredPositionX = desiredPositionX;
	}

	public float getDesiredPositionY() {
		return desiredPositionY;
	}

	public void setDesiredPositionY(float desiredPositionY) {
		this.desiredPositionY = desiredPositionY;
	}

	public String getClass_type() {
		return class_type;
	}

	public void setClass_type(String class_type) {
		this.class_type = class_type;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(Integer maxHp) {
		this.maxHp = maxHp;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Integer getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(Integer intelligence) {
		this.intelligence = intelligence;
	}

	public Integer getStamina() {
		return stamina;
	}

	public void setStamina(Integer stamina) {
		this.stamina = stamina;
	}

	public Integer getDexterity() {
		return dexterity;
	}

	public void setDexterity(Integer dexterity) {
		this.dexterity = dexterity;
	}

	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() + "')";
	}

	public void generateHeroInformation() {

	}

	public float getAttackDamage() {
		float damage = CalculationUtil.getRandomInt(baseAttackDamage, baseMaxAttackDamage) * (1 + (getStrength() * attackStrScaling));
		if(checkIfCritical()){
			Log.i(TAG, "Critical hit");
			damage = damage * criticalMultiplier;
		}
		return damage;
	}

	private boolean checkIfCritical() {
		int randomNumber = CalculationUtil.getRandomInt(0,10000);
		if(randomNumber <= (criticalChance * 10000)){
			return true;
		}
		return false;
	}

	public boolean isClass(String classCheck){
		if(getClass_type().equals(classCheck)){
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Hero{" +
				"id=" + id +
				", user_id=" + user_id +
				", xp=" + xp +
				", level=" + level +
				", positionX=" + positionX +
				", positionY=" + positionY +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionY=" + desiredPositionY +
				", class_type='" + class_type + '\'' +
				", hp=" + hp +
				", maxHp=" + maxHp +
				'}';
	}

}
