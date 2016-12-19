package vo;

import game.logging.Log;
import util.CalculationUtil;

import java.util.ArrayList;

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
	private float positionZ = 5.0f;
	private float desiredPositionX = 6.0f;
	private float desiredPositionZ = 5.0f;
	private String class_type = "WARRIOR";
	private transient ArrayList<Ability> abilities;


	// Hero stats
	private Integer hp;
	private Integer maxHp;
	private Integer resource;
	private Integer maxResource;
	private transient Integer strength;
	private transient Integer intelligence;
	private transient Integer stamina;
	private transient Integer dexterity;
	private transient Integer baseAttackDamage = 2;
	private transient Integer baseMaxAttackDamage = 4;
	private transient float attackStrScaling = 0.1f;
	private transient float attackIntScaling = 0.1f;
	private transient float criticalMultiplier = 2.0f;
	private transient float criticalChance = 0.25f;
	private transient boolean stairsPressed = false;
	private transient long timeLastAuto = 0;
	private transient float baseAttackSpeed = 1.0f;

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

	public Integer getResource() {
		return resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}

	public Integer getMaxResource() {
		return maxResource;
	}

	public void setMaxResource(Integer maxResource) {
		this.maxResource = maxResource;
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

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

	public float getDesiredPositionX() {
		return desiredPositionX;
	}

	public void setDesiredPositionX(float desiredPositionX) {
		this.desiredPositionX = desiredPositionX;
	}

	public float getDesiredPositionZ() {
		return desiredPositionZ;
	}

	public void setDesiredPositionZ(float desiredPositionZ) {
		this.desiredPositionZ = desiredPositionZ;
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

	public void setStairsPressed() {
		stairsPressed = true;
	}

	public boolean isStairsPressed() {
		return stairsPressed;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		if (this.abilities == null) {
			this.abilities = new ArrayList<>();
		}
		this.abilities = abilities;
	}

	public void addAbility(Ability ability){
		this.abilities.add(ability);
	}

	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() + "')";
	}

	public void generateHeroInformation() {

	}


	public void heal(float healAmount) {
		hp = hp + Math.round(healAmount);
		if(hp > maxHp){
			hp = maxHp;
		}
	}

	public float calculateDamageReceived(float damage) {
		float newDamage = damage;
		return newDamage;
	}

	public boolean takeDamage(float damage) {
		hp = hp - Math.round(damage);
		if(hp <= 0){
			return true;
		}
		return false;
	}

	public float getSpellDamage(Ability ability) {
		float multiplier;
		if(ability.getDamageType().equals("MAGIC")){
			multiplier = (1 + (getIntelligence() * attackIntScaling));
		}else{ // PHYSICAL
			multiplier = (1 + (getStrength() * attackStrScaling));
		}
		float damage = CalculationUtil.getRandomInt(ability.getBaseDamage(), ability.getTopDamage()) * multiplier;
		if(ability.getCrittable() == 1 && checkIfCritical()){
			Log.i(TAG, "Critical hit");
			damage = damage * criticalMultiplier;
		}
		return damage;
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

	public boolean hasManaForSpellHeal() {
		return true;
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
				", positionZ=" + positionZ +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionZ=" + desiredPositionZ +
				", class_type='" + class_type + '\'' +
				", hp=" + hp +
				", maxHp=" + maxHp +
				", strength=" + strength +
				", intelligence=" + intelligence +
				", stamina=" + stamina +
				", dexterity=" + dexterity +
				", baseAttackDamage=" + baseAttackDamage +
				", baseMaxAttackDamage=" + baseMaxAttackDamage +
				", attackStrScaling=" + attackStrScaling +
				", criticalMultiplier=" + criticalMultiplier +
				", criticalChance=" + criticalChance +
				'}';
	}

	public void setStartPosition(Vector3 freeStartPosition) {
		positionX = freeStartPosition.getX();
		positionZ = freeStartPosition.getZ();
	}

	public Ability getAbility(Integer spellId) {
		for (Ability ability : getAbilities()){
			if(ability.getId() == spellId){
				return ability;
			}
		}
		return null;
	}

	public boolean readyForAutoAttack(long time) {
		// TODO : Take into account items for attack speed and talents etc
		long attackCD = (long) (baseAttackSpeed * 1000);
		long timeWhenNextAttackIsReady = timeLastAuto + attackCD;
		if(time >= timeWhenNextAttackIsReady){
			Log.i(TAG, "Ready for attack, seconds since last attack : " + ((time - timeWhenNextAttackIsReady) / 1000));
			timeLastAuto = time;
			getAbility(0).setTimeWhenOffCooldown("" + (time + attackCD));
			return true;
		}else{
			Log.i(TAG, "Time until next attack : " + timeWhenNextAttackIsReady);
		}
		return false;
	}
}
