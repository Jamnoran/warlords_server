package game.models.enemies;

import game.GameServer;
import game.logging.Log;
import game.models.game.Amount;
import game.models.abilities.Buff;
import game.models.game.Threat;
import game.models.game.World;
import game.models.heroes.Hero;
import game.logic.math.CalculationUtil;
import game.util.GameUtil;
import game.util.HeroUtil;

import java.util.ArrayList;

public class Minion {
	public static int ZOMBIE = 1;
	public static int SKELETON = 2;

	private static final String TAG = Minion.class.getSimpleName();
	private Integer id = 1;
	private float positionX = 10.0f;
	private float positionZ = 10.0f;
	private float positionY = 10.0f;
	private float desiredPositionX = 10.0f;
	private float desiredPositionZ = 10.0f;
	private float desiredPositionY = 10.0f;
	private Integer level = 1;
	private float hp = 0;
	private float maxHp = 0;
	private transient GameServer game;
	private int heroTarget = 0;
	private int minionType = 1;

	public transient boolean targetInRangeForAttack = false;
	private transient float baseDamage = 2;
	private transient float damageMultiplier = 1.2f;
	private transient Integer baseHp = 10;
	private transient Integer hpPerLevel = 5;
	private transient Integer timeBetweenAttacks = 3000;
	private transient Long timeLastAttack = null;
	private transient ArrayList<Threat> threats = new ArrayList<Threat>();
	private transient int baseXp = 100;
	private transient float armor = 0;
	private transient float magicResistance = 0;
	private transient float armorPenetration = 0.0f;
	private transient float magicPenetration = 0.0f;
	private ArrayList<Buff> buffs = new ArrayList<>();
	private ArrayList<Buff> deBuffs = new ArrayList<>();

	public Minion(GameServer game) {
		this.game = game;
	}

	public Minion(int id) {
		this.id = id;
	}

	public void generateMinionInformation(float posX, float posZ, float posY){
		setMinionType(CalculationUtil.getRandomInt(1,2));
		setHp(baseHp + (hpPerLevel * level));
		if(minionType == SKELETON){
			setArmor(5);
		}
		setMaxHp(getHp());
		setPositionX(posX);
		setDesiredPositionX(posX);
		setPositionZ(posZ);
		setDesiredPositionZ(posZ);
		setPositionY(posY);
		setDesiredPositionY(posY);
	}

	public void takeAction() {
		if (hp > 0) {
			if (!attackIfHasEnemy()) {
				findNewLocationToWalkTo();
			}
		}
	}

	private boolean attackIfHasEnemy() {
		Integer heroId = getHeroIdWithMostThreat();
		if(heroId != null){
			//Log.i(TAG, "Minion taking action, hp : " + hp + " aggro: " + getHeroIdWithMostThreat());
			if (targetInRangeForAttack) {
				long time = System.currentTimeMillis();
				if (timeLastAttack == null || ((time - timeLastAttack) >= timeBetweenAttacks)) {
					attack(heroId);
				}
			}
			return true;
		}else {
			return false;
		}
	}

	public void attack(Integer heroId) {
		long time = System.currentTimeMillis();
		timeLastAttack = time;
		Amount damage = new Amount(level * (baseDamage * damageMultiplier));
//		Log.i(TAG, "Multiplier on minion damage :" + damage.getAmount() + " from base: " + baseDamage + " and level " + level);
		HeroUtil.attackHero(heroId, damage, id, game);
	}

	private void findNewLocationToWalkTo() {
		Integer heroId = getHeroIdWithMostThreat();
		if(heroId != null){
			if (!targetInRangeForAttack) {
				Hero hero = GameUtil.getHeroById(heroId, game.getHeroes());
				setDesiredPositionX(hero.getPositionX());
				setDesiredPositionZ(hero.getPositionZ());
			}
		}else{
			if (GameUtil.isWorldType(World.CRAWLER, game.getWorldLevel())) {

				float newX = CalculationUtil.getRandomFloat(-4.0f, 3.0f);
				float newZ = CalculationUtil.getRandomFloat(-4.0f, 3.0f);

				double distance = Math.hypot(getPositionX()-newX, getPositionZ()-newZ);

				setDesiredPositionX(getPositionX() + newX);
				setDesiredPositionZ(getPositionZ() + newZ);

				if (distance >= 32.0f) {
					this.game.sendMinionMoveAnimation(getId());
				}
			}
		}
	}

	public void addThreat(Threat threat){
		boolean foundThreat = false;
		for(Threat threatInList : threats){
			if(threat.getHeroId().equals(threatInList.getHeroId())){
				foundThreat = true;
				threatInList.addThreat(threat);
			}
		}
		if(!foundThreat){
			threat.addThreat(threat);
			threats.add(threat);
			heroTarget = threat.getHeroId();
		}
	}

	public Integer getHeroIdWithMostThreat(){
		Threat currentHero = null;
		for(Threat threat : threats){
			if(currentHero == null || (threat.getAmount() > currentHero.getAmount())){
				if (GameUtil.getHeroById(threat.getHeroId(), game.getHeroes()).getHp() > 0) {
					currentHero = threat;
				}
			}
		}
		if (currentHero != null) {
			heroTarget = currentHero.getHeroId();
			return currentHero.getHeroId();
		} else {
			// Check if a hero is in range, in that case add some threat on that (this will mean the first hero getting close will aggro this minion)
			return null;
		}
	}

	public ArrayList<Threat> getThreats() {
		return threats;
	}

	public void setThreats(ArrayList<Threat> threats) {
		this.threats = threats;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}


	public float getPositionX() {
		return positionX;
	}

	public float getDesiredPositionX() {
		return desiredPositionX;
	}

	public void setDesiredPositionX(float desiredPositionX) {
		this.desiredPositionX = desiredPositionX;
	}

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

	public float getDesiredPositionZ() {
		return desiredPositionZ;
	}

	public void setDesiredPositionZ(float desiredPositionZ) {
		this.desiredPositionZ = desiredPositionZ;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

	public void setBuffs(ArrayList<Buff> buffs) {
		this.buffs = buffs;
	}

	public ArrayList<Buff> getDeBuffs() {
		return deBuffs;
	}

	public void setDeBuffs(ArrayList<Buff> deBuffs) {
		this.deBuffs = deBuffs;
	}

	public float getArmor() {
		return armor;
	}

	public void setArmor(float armor) {
		this.armor = armor;
	}

	public float getMagicResistance() {
		return magicResistance;
	}

	public void setMagicResistance(float magicResistance) {
		this.magicResistance = magicResistance;
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

	public int getMinionType() {
		return minionType;
	}

	public void setMinionType(int minionType) {
		this.minionType = minionType;
	}

	public float calculateDamageReceived(Amount damage, float penetration, String damageType) {
		// Calculate if minion has armor or dodge chance
		if(damageType.equals("PHYSICAL")){
			float damageAfterReduction = CalculationUtil.calculateDamageAfterReduction(armor,penetration,damage.getAmount());
			// damage * (armor )
			return damageAfterReduction;
		}else if(damageType.equals("MAGIC")){
			float damageAfterReduction = CalculationUtil.calculateDamageAfterReduction(magicResistance,penetration,damage.getAmount());
			return damageAfterReduction;
		}else{
			return damage.getAmount();
		}
	}

	public boolean takeDamage(float damageAfterMinionCalculation) {
		hp = hp - Math.round(damageAfterMinionCalculation);
		if(hp <= 0){
			// Give xp to all that participated.
			for(Threat threat : threats){
				int calculatedXP = baseXp;
				// We should make xp amount less if the minions level is not close to heroes
				GameUtil.getHeroById(threat.getHeroId(), game.getHeroes()).addExp(calculatedXP);
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Minion{" +
				"id=" + id +
				", positionX=" + positionX +
				", positionZ=" + positionZ +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionZ=" + desiredPositionZ +
				", level=" + level +
				", hp=" + hp +
				", maxHp=" + maxHp +
				", hpPerLevel=" + hpPerLevel +
				'}';
	}

	public boolean isAlive() {
		if(hp > 0){
			return true;
		}
		return false;
	}

	public void addDebuff(Buff debuff) {
		deBuffs.add(debuff);
	}

	public float getHighestThreathValue() {
		for(Threat threat : threats){
			if(threat.getHeroId().equals(getHeroIdWithMostThreat())){
				Log.i(TAG, "Highest amount of threat was : " + threat.getAmount());
				return threat.getAmount();
			}
		}
		return 0;
	}
}
