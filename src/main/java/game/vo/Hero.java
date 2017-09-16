package game.vo;

import game.logging.Log;
import game.util.CalculationUtil;
import game.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class Hero {
	public static final String WARRIOR = "WARRIOR";
	public static final String PRIEST = "PRIEST";
	public static final String WARLOCK = "WARLOCK";
	public static final String ROGUE = "ROGUE";
	private static final String TAG = Hero.class.getSimpleName();

	// General stats
	public Integer id = null;
	private Integer user_id = null;
	private Integer xp = 0;
	private Integer topGameLvl = 0;
	// Need xpToLevelUp as well
	private Integer level = 1;
	private boolean alive = true;
	private float positionX = 0.0f;
	private float positionZ = 0.0f;
	private float positionY = 0.0f;
	private float desiredPositionX = 0.0f;
	private float desiredPositionZ = 0.0f;
	private float desiredPositionY = 0.0f;
	private String class_type = "WARRIOR";


	// Hero stats
	private Integer hp;
	private Integer maxHp;
	private Integer resource;
	private Integer maxResource;
	private float attackRange = 3.0f;
	private transient Integer hpRegen = 0;
	private transient Integer resourceRegen = 0;
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
	private transient float baseAttackSpeed = 2.0f;
	private transient int baseXpForLevel = 1000;
	private transient float xpScale = 0.1f;

	private transient ArrayList<Ability> abilities;
	private transient ArrayList<Talent> talents;
	private ArrayList<Buff> buffs = new ArrayList<>();
	private ArrayList<Buff> deBuffs = new ArrayList<>();

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

	public Integer getTopGameLvl() {
		return topGameLvl;
	}

	public void setTopGameLvl(Integer topGameLvl) {
		this.topGameLvl = topGameLvl;
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

	public Integer getHpRegen() {
		return hpRegen;
	}

	public void setHpRegen(Integer hpRegen) {
		this.hpRegen = hpRegen;
	}

	public float getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}

	public Integer getResourceRegen() {
		return resourceRegen;
	}

	public void setResourceRegen(Integer resourceRegen) {
		this.resourceRegen = resourceRegen;
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

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getDesiredPositionY() {
		return desiredPositionY;
	}

	public void setDesiredPositionY(float desiredPositionY) {
		this.desiredPositionY = desiredPositionY;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
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

	public void generateHeroInformation() {

	}

	public ArrayList<Buff> getBuffs(){
		return buffs;
	}

	public void setBuffs(ArrayList<Buff> buffs){
		this.buffs = buffs;
	}

	public ArrayList<Buff> getDeBuffs() {
		return deBuffs;
	}

	public void setDeBuffs(ArrayList<Buff> deBuffs) {
		this.deBuffs = deBuffs;
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
		float damageLeft = damage;
		// First we need to check if we have a shield on this hero

		if(getBuffs() != null && getBuffs().size() > 0){
			Log.i(TAG, "Got buff : " + getBuffs().size());
			Iterator<Buff> buffIterator = buffs.iterator();
			while (buffIterator.hasNext()) {
				Buff buff = buffIterator.next();

				Log.i(TAG, "Buff type : " + buff.type);
				if(buff.type == Buff.SHIELD){
					float temporaryBuffValue = Math.round(buff.value - damageLeft);
					Log.i(TAG, "Buff amount left after damage: " + temporaryBuffValue);
					if(temporaryBuffValue <= 0){
						damageLeft = Math.round(damageLeft - buff.value);
						buffIterator.remove();
						Log.i(TAG, "Removing buff");
					}else{
						buff.value = Math.round(temporaryBuffValue);
						damageLeft = 0;
					}
				}
			}
		}

		hp = hp - Math.round(damageLeft);
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

	public boolean hasResourceForSpellHeal() {
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
				", topGameLvl=" + topGameLvl +
				", positionX=" + positionX +
				", positionZ=" + positionZ +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionZ=" + desiredPositionZ +
				", class_type='" + class_type + '\'' +
				", hp=" + hp +
				", alive=" + alive +
				", attackRange=" + attackRange +
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
		long attackCD = (long) (baseAttackSpeed * 1000); // Multiplies to get it in milliseconds
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

	public void addExp(int calculatedXP) {
		xp = xp + calculatedXP;
		float xpForLevel = (baseXpForLevel + (level * (baseXpForLevel * xpScale)));
		if(xp >= xpForLevel){
			Log.i(TAG, "Level up!");
			level++;
			xp = xp - Math.round(xpForLevel);
		}
		// Update database
		DatabaseUtil.updateHero(this);
	}


	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`, `top_game_lvl`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() +  "', '" + getTopGameLvl() + "')";
	}

	public String getSqlUpdateQuery() {
		return "UPDATE `heroes` SET `level`=" + getLevel() + ",`xp`=" + getXp() + ",`top_game_lvl`=" + getTopGameLvl() + " WHERE id = " + getId();
	}

	public void regenTick() {
		// Calculate if amount is different based on talents etc.
		Integer hpAmount = hpRegen;
		if(hp >= 1 && hp < maxHp){
			hp = hp + hpAmount;
//			Log.i(TAG, "Hp reg: " + hpAmount);
			if(hp > maxHp){
				hp = maxHp;
			}
		}
		if (hp >= 1 && resourceRegen != null && resource != null) {
			Integer resourceAmount = resourceRegen;
			if(resource < maxResource){
//				Log.i(TAG, "Resource reg: " + resourceAmount);
				resource = resource + resourceAmount;
				if(resource > maxResource){
					resource = maxResource;
				}
			}
		}
	}

	public void setTalents(ArrayList<Talent> talents) {
		this.talents = talents;
	}

	public ArrayList<Talent> getTalents() {
		return talents;
	}

	public void removeBuff(Buff removingBuff) {
		Iterator<Buff> buffIterator = buffs.iterator();
		while (buffIterator.hasNext()) {
			Buff buff = buffIterator.next();
			if(buff.type == removingBuff.type){
				buffIterator.remove();
			}
		}
	}

	public void restoreResource(float totalAmount) {
		if(resource >= 1 && resource < maxResource){
			resource = resource + Math.round(totalAmount);
			if(resource > maxResource){
				resource = maxResource;
			}
		}
	}
}
