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
	private float desiredPositionX = 10.0f;
	private float desiredPositionZ = 10.0f;
	private Integer level = 1;
	private Integer hp = null;
	private Integer maxHp = null;
	private transient GameServer game;
	private int heroTarget = 0;

	public transient boolean targetInRangeForAttack = false;
	private transient float baseDamage = 5;
	private transient Integer baseHp = 10;
	private transient Integer hpPerLevel = 5;
	private transient Integer timeBetweenAttacks = 1200;
	private transient Long timeLastAttack = null;
	private transient ArrayList<Threat> threats = new ArrayList<Threat>();
	private transient int baseXp = 100;

	public Minion(GameServer game) {
		this.game = game;
	}

	public Minion(int id) {
		this.id = id;
	}

	public void generateMinionInformation(float posX, float posZ){
		setHp(baseHp + (hpPerLevel * level));
		setMaxHp(getHp());

		if (posX == 0.0f)
		{
			posX = (CalculationUtil.getRandomFloat(5, 15));
		}
		if(posZ == 0.0f){
			posZ = (CalculationUtil.getRandomFloat(5, 15));
		}

		setPositionX(posX);
		setDesiredPositionX(posX);
		setPositionZ(posZ);
		setDesiredPositionZ(posZ);
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
//			Log.i(TAG, "Minion tries to find new location to walk to");
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
		if (!GameUtil.isWorldType(GameUtil.GAUNTLET, game.getWorldLevel())) {

			float newX = CalculationUtil.getRandomFloat(-1.0f, 0.0f);
			float newZ = CalculationUtil.getRandomFloat(-1.0f, 0.0f);
//			float newX = CalculationUtil.getRandomFloat(-2.0f, 1.0f);
//			float newZ = CalculationUtil.getRandomFloat(-2.0f, 1.0f);

			double distance = Math.hypot(getDesiredPositionX()-newX, getDesiredPositionZ()-newZ);

			setDesiredPositionX(getDesiredPositionX() + newX);
			setDesiredPositionZ(getDesiredPositionZ() + newZ);

			if (distance >= 32.0f) {
				Log.i(TAG, "Sending run animation for minion distance" + distance);
				this.game.sendMinionMoveAnimation(getId());
			}
		}

		//Log.i(TAG, "Walking to new position " + getDesiredPositionX() + " x " + getDesiredPositionZ());
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void move() {
		if (positionX <= 15f) {
			positionX = positionX + 1.0f;
			positionZ = positionZ + 1.0f;
		}else {
			positionX = 1.0f;
			positionZ = 1.0f;
		}
	}

	public float calculateDamageReceived(float damage) {
		// Calculate if minion has armor or dodge chance
		return damage;
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
}
