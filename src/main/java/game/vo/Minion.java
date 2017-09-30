package game.vo;

import game.GameServer;
import game.logging.Log;
import game.util.CalculationUtil;
import game.util.GameUtil;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Minion {

	private static final String TAG = Minion.class.getSimpleName();
	private Integer id = 1;
	private float positionX = 10.0f;
	private float positionZ = 10.0f;
	private float positionY = 10.0f;
	private float desiredPositionX = 10.0f;
	private float desiredPositionZ = 10.0f;
	private float desiredPositionY = 10.0f;
	private Integer level = 1;
	private Integer hp = null;
	private Integer maxHp = null;
	private transient GameServer game;
	private int heroTarget = 0;
	private int minionType = 1;

	public transient boolean targetInRangeForAttack = false;
	private transient float baseDamage = 5;
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
		setHp(baseHp + (hpPerLevel * level));
		setMaxHp(getHp());
		setPositionX(posX);
		setDesiredPositionX(posX);
		setPositionZ(posZ);
		setDesiredPositionZ(posZ);
		setPositionY(posY);
		setDesiredPositionY(posY);
	}

//	public void startAI(){
//		Thread thread = new Thread(){
//			public void run(){
//				while(hp > 0){
//					takeAction();
//					try {
//						sleep(10000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		thread.start();
//	}

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

	private void attack(Integer heroId) {
		long time = System.currentTimeMillis();
		timeLastAttack = time;
		game.attackHero(heroId, baseDamage, id);
	}

	private void findNewLocationToWalkTo() {
		Integer heroId = getHeroIdWithMostThreat();
		if(heroId != null){
			if (!targetInRangeForAttack) {
				Hero hero = game.getHeroById(heroId);
				setDesiredPositionX(hero.getPositionX());
				setDesiredPositionZ(hero.getPositionZ());
			}
		}else{
			if (GameUtil.isWorldType(GameUtil.DUNGEON_CRAWLER, game.getWorldLevel())) {

				float newX = CalculationUtil.getRandomFloat(-4.0f, 3.0f);
				float newZ = CalculationUtil.getRandomFloat(-4.0f, 3.0f);

				double distance = Math.hypot(getPositionX()-newX, getPositionZ()-newZ);

				setDesiredPositionX(getPositionX() + newX);
				setDesiredPositionZ(getPositionZ() + newZ);

				if (distance >= 32.0f) {
					//Log.i(TAG, "Sending run animation for minion distance" + distance);
					this.game.sendMinionMoveAnimation(getId());
				}
			}
		}
	}

	public void addThreat(Threat threat){
		boolean foundThreat = false;
		for(Threat threatInList : threats){
			if(threat.getHeroId() == threatInList.getHeroId()){
				foundThreat = true;
				threatInList.addThreat(threat);
			}
		}
		if(!foundThreat){
			threats.add(threat);
			heroTarget = threat.getHeroId();
		}
	}

	public Integer getHeroIdWithMostThreat(){
		Threat currentHero = null;
		for(Threat threat : threats){
			if(currentHero == null || (threat.getAmount() > currentHero.getAmount())){
				if (this.game.getHeroById(threat.getHeroId()).getHp() > 0) {
					currentHero = threat;
				}
			}
		}
		if (currentHero != null) {
			heroTarget = currentHero.getHeroId();
			return currentHero.getHeroId();
		} else {
			// Check if a hero is in range, in that case add some threat on that (this will mean the first hero getting close will aggro this minion)
			//return heroInRange();
			return null;
		}
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public float calculateDamageReceived(float damage, float penetration, String damageType) {
		// Calculate if minion has armor or dodge chance
		if(damageType.equals("PHYSICAL")){
			float damageAfterReduction = CalculationUtil.calculateDamageAfterReduction(armor,penetration,damage);
			// damage * (armor )
			return damageAfterReduction;
		}else if(damageType.equals("MAGIC")){
			float damageAfterReduction = CalculationUtil.calculateDamageAfterReduction(magicResistance,penetration,damage);
			return damageAfterReduction;
		}else{
			return damage;
		}
	}

	public boolean takeDamage(float damageAfterMinionCalculation) {
		hp = hp - Math.round(damageAfterMinionCalculation);
		if(hp <= 0){
			// Give xp to all that participated.
			for(Threat threat : threats){
				int calculatedXP = baseXp;
				this.game.getHeroById(threat.getHeroId()).addExp(calculatedXP);
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
}
