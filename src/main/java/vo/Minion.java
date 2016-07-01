package vo;

public class Minion {

	private Integer id = 1;
	private float positionX = 10.0f;
	private float positionY = 10.0f;
	private float desiredPositionX = 10.0f;
	private float desiredPositionY = 10.0f;
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

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
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

	public void move() {
		if (positionX <= 15f) {
			positionX = positionX + 1.0f;
			positionY = positionY + 1.0f;
		}else {
			positionX = 1.0f;
			positionY = 1.0f;
		}
	}

}
