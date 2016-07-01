package vo;

public class Hero {
	public Integer id = null;
	private Integer user_id = null;
	private Integer xp = 0;
	private Integer level = 1;
	private float positionX = 6.0f;
	private float positionY = 5.0f;
	private float desiredPositionX = 6.0f;
	private float desiredPositionY = 5.0f;
	private String class_type = "WARRIOR";
	private Integer hp;
	private Integer maxHp;

	public static String WARRIOR = "WARRIOR";
	public static String PRIEST = "PRIEST";

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


	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() + "')";
	}

	public void generateHeroInformation() {

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
