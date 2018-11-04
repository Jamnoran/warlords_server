package game.vo;

import game.logging.Log;
import game.spells.Spell;
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
	private int xpForLevel = 1000;
	private transient int baseXpForScaling = 1000;
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
	private float hp;
	private float maxHp;
	private float resource;
	private float maxResource;
	private float attackRange = 3.0f;
	private float armor = 0;
	private float magicResistance = 0;
	private float armorPenetration = 0.0f;
	private float magicPenetration = 0.0f;
	private transient float hpRegen = 0;
	private transient float resourceRegen = 0;
	private Integer strength;
	private Integer intelligence;
	private Integer stamina;
	private Integer dexterity;
	private Integer baseAttackDamage = 2;
	private Integer baseMaxAttackDamage = 4;
	private transient float attackStrScaling = 0.1f;
	private transient float attackIntScaling = 0.1f;
	private transient float criticalMultiplier = 2.0f;
	private transient float criticalChance = 0.25f;
	private transient boolean stairsPressed = false;
	private transient long timeLastAuto = 0;
	private transient float baseAttackSpeed = 2.0f;
	private transient float xpScale = 0.1f;

	private transient ArrayList<Ability> abilities;
	private transient ArrayList<Talent> talents;
	private transient ArrayList<Item> items;
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

	public float getResource() {
		return resource;
	}

	public void setResource(float resource) {
		this.resource = resource;
	}

	public float getMaxResource() {
		return maxResource;
	}

	public void setMaxResource(float maxResource) {
		this.maxResource = maxResource;
	}

	public float getHpRegen() {
		return hpRegen;
	}

	public void setHpRegen(float hpRegen) {
		this.hpRegen = hpRegen;
	}

	public float getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}

	public float getResourceRegen() {
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

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(float maxHp) {
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

	public int getXpForLevel() {
		return xpForLevel;
	}

	public void setXpForLevel(int xpForLevel) {
		this.xpForLevel = xpForLevel;
	}

	public float getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(float criticalChance) {
		this.criticalChance = criticalChance;
	}

	public long getTimeLastAuto() {
		return timeLastAuto;
	}

	public void setTimeLastAuto(long timeLastAuto) {
		this.timeLastAuto = timeLastAuto;
	}

	public float getBaseAttackSpeed() {
		return baseAttackSpeed;
	}

	public void setBaseAttackSpeed(float baseAttackSpeed) {
		this.baseAttackSpeed = baseAttackSpeed;
	}

	public float getArmorPenetration() {
		return armorPenetration;
	}

	public void setArmorPenetration(float armorPenetration) {
		this.armorPenetration = armorPenetration;
	}

	public float getMagicPenetration() {
		return magicPenetration;
	}

	public void setMagicPenetration(float magicPenetration) {
		this.magicPenetration = magicPenetration;
	}

	public ArrayList<Buff> getBuffs(){
		return buffs;
	}

	public void addBuff(Buff buff){
		buffs.add(buff);
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

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public void heal(Amount healAmount) {
		hp = hp + Math.round(healAmount.getAmount());
		if(hp > maxHp){
			hp = maxHp;
		}
	}

	public float getHpFromTalents(){
		float hpAmount = Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_HP);
		return hpAmount;
	}

	public float calculateDamageReceived(Amount damage) {
		float newDamage = damage.getAmount();
		return newDamage;
	}

	public boolean takeDamage(float damage, float penetration, String damageType) {
		float damageLeft = calculateDamageReceived(damage, penetration, damageType);
		// First we need to check if we have a shield on this hero
		if(getBuffs() != null && getBuffs().size() > 0){
			Iterator<Buff> buffIterator = buffs.iterator();
			while (buffIterator.hasNext()) {
				Buff buff = buffIterator.next();

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

	public float calculateDamageReceived(float damage, float penetration, String damageType) {
		// Calculate if minion has armor or dodge chance
		if(damageType.equals("PHYSICAL")){
			return CalculationUtil.calculateDamageAfterReduction(getArmor(), penetration, damage);
		}else if(damageType.equals("MAGIC")){
			return CalculationUtil.calculateDamageAfterReduction(getMagicResistance(), penetration, damage);
		}else{
			return damage;
		}
	}

	public Amount getSpellDamage(Spell spell) {
		Amount damage;
		float multiplier;
		if(spell.getAbility().getDamageType().equals("MAGIC")){
			multiplier = (1 + (getIntelligence() * attackIntScaling));
		}else{ // PHYSICAL
			multiplier = (1 + (getStrength() * attackStrScaling));
		}
		int talentAmount = Math.round(Talent.getTalentAmount(getTalents(), spell.getAmountTalentId()));
		damage = new Amount(CalculationUtil.getRandomInt((spell.getAbility().getBaseDamage() + talentAmount), (spell.getAbility().getTopDamage() + talentAmount)) * multiplier);
		if(spell.getAbility().getCrittable() == 1 && checkIfCritical()){
			Log.i(TAG, "Critical hit");
			damage.setAmount(damage.getAmount() * criticalMultiplier);
			damage.setCrit(true);
		}
		return damage;
	}

	public Amount getAttackDamage() {
		Amount damage = new Amount(CalculationUtil.getRandomInt(baseAttackDamage + Math.round(getBaseDamageFromTalents()), baseMaxAttackDamage + Math.round(getBaseDamageFromTalents())) * (1 + (getStrength() * attackStrScaling)));
		if(checkIfCritical()){
			Log.i(TAG, "Critical hit");
			damage.setAmount(damage.getAmount() * criticalMultiplier);
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

	public float getArmor() {
		this.armor = calculateArmor();
		return armor;
	}

	public float calculateArmor() {
		float armorCalculation = 0;
		// Check base stats

		// Check items equipped
		armorCalculation = armorCalculation + getItemStat(ItemStat.ARMOR);

		// Check talents
		if(getTalents() != null){
			for(Talent talent : getTalents()){
				if(talent.getTalentId() == Talent.TALENT_GENERAL_ARMOR){
					armorCalculation = armorCalculation + talent.getBaseValue() + (talent.getScaling() * talent.getPointAdded());
				}
			}
		}
		setArmor(armorCalculation);
		return armorCalculation;
	}

	public void setArmor(float armor) {
		this.armor = armor;
	}

	public float getMagicResistance() {
		this.magicResistance = calculateMagicResist();
		return magicResistance;
	}
	public float getBaseDamageFromTalents(){
		return Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_DAMAGE);
	}

	public float calculateMagicResist() {
		float mrCalculation = 0;
		// Check base stats

		// Check items equipped
		mrCalculation = mrCalculation + getItemStat(ItemStat.MAGIC_RESIST);

		// Check talents
		if(getTalents() != null){
			for(Talent talent : getTalents()){
				if(talent.getTalentId() == Talent.TALENT_GENERAL_MAGIC_RESIST){
					mrCalculation = mrCalculation + talent.getBaseValue() + (talent.getScaling() * talent.getPointAdded());
				}
			}
		}
		setMagicResistance(mrCalculation);
		return mrCalculation;
	}

	public void setMagicResistance(float magicResistance) {
		this.magicResistance = magicResistance;
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
			Log.i(TAG, "Setting timeoffcd to : " + getAbility(0).getTimeWhenOffCooldown());
			return true;
		}else{
			//Log.i(TAG, "Time until next attack : " + timeWhenNextAttackIsReady);
		}
		return false;
	}

	public void addExp(int calculatedXP) {
		xp = xp + calculatedXP;
		xpForLevel = Math.round(baseXpForScaling + (level * (this.baseXpForScaling * xpScale)));
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
		float hpAmount = hpRegen;
		if(hp >= 1 && hp < maxHp){
			hp = hp + hpAmount;
//			Log.i(TAG, "Hp reg: " + hpAmount);
			if(hp > maxHp){
				hp = maxHp;
			}
		}
		if (hp >= 1) {
			float resourceAmount = resourceRegen;
			if(resource < maxResource){
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


	public float getPenetration(String damageType) {
		if(damageType.equals("PHYSICAL")){
			return calculateArmorPenetration();
		}else if (damageType.equals("MAGIC")){
			return calculateMagicPenetration();
		}
		return 0;
	}

	public void recalculateStats() {
		calculateArmor();
		calculateMagicResist();
	}

	public void checkForRetaliation(Minion minion) {
		if(getBuffs() != null && getBuffs().size() > 0){
			for (Buff buff: getBuffs()) {
				if(buff.type == Buff.RETALIATION){
					minion.takeDamage(buff.getValue());
				}
			}
		}
	}


	public float getItemStat(String value) {
		float itemStat = 0f;
		if(getItems() != null) {
//			Log.i(TAG, "Checking items of character, has this many items : " + getItems().size());
			for (Item item : getItems()) {
//				Log.i(TAG, "Checking item : " + item.toString());
				if (item.isEquipped()) {
					for (ItemStat stat: item.getStats()) {
						if(stat.getType().equals(value)){
							if(value.equals(ItemStat.DAMAGE)){
								itemStat = itemStat + CalculationUtil.getRandomInt(stat.getBaseStat(), stat.getTop());
							}else {
								itemStat = itemStat + stat.getBaseStat();
							}
						}
					}
					if(value.equals(ItemStat.DAMAGE)){
						if(item.getPosition().equals(Item.MAIN_HAND) || item.getPosition().equals(Item.OFF_HAND)){
							itemStat = itemStat + item.getBaseStat();
						}
					}else{
						if(!item.getPosition().equals(Item.MAIN_HAND) && !item.getPosition().equals(Item.OFF_HAND)){
							if (value.equals(ItemStat.ARMOR)) {
								itemStat = itemStat + item.getBaseStat();
							}
						}
					}
				}
			}
		}
		return itemStat;
	}

	public Integer getHpPerLevel() {
		return null;
	}

	public Integer getStrPerLevel() {
		return null;
	}

	public Integer getIntPerLevel() {
		return null;
	}

	public Integer getDexPerLevel() {
		return null;
	}

	public Integer getStaPerLevel() {
		return null;
	}


	private float calculateArmorPenetration() {
		return getArmorPenetration() + Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_ARMOR_PENETRATION);
	}

	private float calculateMagicPenetration() {
		return getArmorPenetration() + Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_MAGIC_PENETRATION);
	}

	public void updateStats() {
		calculateArmor();
		Log.i(TAG, "Hero has calculated armor value : " + armor);
		calculateMagicResist();
		setStrength(getStrPerLevel() * getLevel());
		setStamina(getStaPerLevel() * getLevel());
		setDexterity(getDexPerLevel() * getLevel());
		setIntelligence(getIntPerLevel() * getLevel() + Math.round(Talent.getTalentAmount(getTalents(), Talent.TALENT_GENERAL_INTELLIGENCE)));
		setHpRegen(hpRegen);

		Log.i(TAG, "Hero has updated stats : " + this.toString());
	}


	@Override
	public String toString() {
		return "Hero{" +
				"id=" + id +
				", user_id=" + user_id +
				", xp=" + xp +
				", xpForLevel=" + xpForLevel +
				", baseXpForScaling=" + baseXpForScaling +
				", topGameLvl=" + topGameLvl +
				", level=" + level +
				", alive=" + alive +
				", positionX=" + positionX +
				", positionZ=" + positionZ +
				", positionY=" + positionY +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionZ=" + desiredPositionZ +
				", desiredPositionY=" + desiredPositionY +
				", class_type='" + class_type + '\'' +
				", hp=" + hp +
				", maxHp=" + maxHp +
				", resource=" + resource +
				", maxResource=" + maxResource +
				", attackRange=" + attackRange +
				", armor=" + armor +
				", magicResistance=" + magicResistance +
				", armorPenetration=" + armorPenetration +
				", magicPenetration=" + magicPenetration +
				", hpRegen=" + hpRegen +
				", resourceRegen=" + resourceRegen +
				", strength=" + strength +
				", intelligence=" + intelligence +
				", stamina=" + stamina +
				", dexterity=" + dexterity +
				", baseAttackDamage=" + baseAttackDamage +
				", baseMaxAttackDamage=" + baseMaxAttackDamage +
				", attackStrScaling=" + attackStrScaling +
				", attackIntScaling=" + attackIntScaling +
				", criticalMultiplier=" + criticalMultiplier +
				", criticalChance=" + criticalChance +
				", stairsPressed=" + stairsPressed +
				", timeLastAuto=" + timeLastAuto +
				", baseAttackSpeed=" + baseAttackSpeed +
				", xpScale=" + xpScale +
				", abilities=" + abilities +
				'}';
	}
}
