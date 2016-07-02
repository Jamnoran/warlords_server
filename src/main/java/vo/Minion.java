package vo;

import util.CalculationUtil;

public class Minion {

	private Integer id = 1;
	private float positionX = 10.0f;
	private float positionZ = 10.0f;
	private float desiredPositionX = 10.0f;
	private float desiredPositionZ = 10.0f;
	private Integer level = 1;
	private Integer hp = null;
	private Integer maxHp = null;

	private transient Integer hpPerLevel = 10;

	public Minion() {
		
	}

	public Minion(int id) {
		this.id = id;
	}

	public void generateMinionInformation(){
		setHp(hpPerLevel * level);
		setMaxHp(getHp());

		float posX = (CalculationUtil.getRandomFloat(5, 15));
		float posZ = (CalculationUtil.getRandomFloat(5, 15));
		setPositionX(posX);
		setDesiredPositionX(posX);
		setPositionZ(posZ);
		setDesiredPositionZ(posZ);
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
