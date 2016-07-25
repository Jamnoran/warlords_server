package vo;

import game.GameServer;
import game.logging.Log;
import util.CalculationUtil;

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

	private transient float baseDamage = 5;
	private transient Integer hpPerLevel = 10;
	private transient ArrayList<Threat> threats = new ArrayList<Threat>();

	public Minion(GameServer game) {
		this.game = game;
	}

	public Minion(int id) {
		this.id = id;
	}

	public void generateMinionInformation(float posX, float posZ){
		setHp(hpPerLevel * level);
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


	public void startAI(){
		Thread thread = new Thread(){
			public void run(){
				while(hp > 0){
					if (hp > 0) {
						findNewLocationToWalkTo();
						attackIfHasEnemy();
					}
					try {
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	private void attackIfHasEnemy() {
		Integer heroId = getHeroIdWithMostThreat();
		if(heroId != null){
			attack(heroId);
		}
	}

	private void attack(Integer heroId) {
		game.attackHero(heroId, baseDamage, id);
	}

	private void findNewLocationToWalkTo() {
		//setDesiredPositionX(getDesiredPositionX() + CalculationUtil.getRandomFloat(-1.0f, 1.0f));
		//setDesiredPositionZ(getDesiredPositionZ() + CalculationUtil.getRandomFloat(-1.0f, 1.0f));

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
		}
	}

	public Integer getHeroIdWithMostThreat(){
		Threat currentHero = null;
		for(Threat threat : threats){
			if(currentHero == null || (threat.getAmount() > currentHero.getAmount())){
				currentHero = threat;
			}
		}
		if (currentHero != null) {
			return currentHero.getHeroId();
		} else {
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
