package vo;

public class Hero {
	public Integer id = null;
	public Integer userId = null;
	public Integer xp = 0;
	public Integer level = 1;
	public Double positionX = 6.0d;
	public Double positionY = 5.0d;
	public String class_type = "WARRIOR";

	public Hero(Integer userId) {
		this.userId = userId;
	}

	public Hero(String userId) {
		this.userId = Integer.parseInt(userId);
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
	public Double getPositionX() {
		return positionX;
	}
	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}
	public Double getPositionY() {
		return positionY;
	}
	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	public String getClass_type() {
		return class_type;
	}

	public void setClass_type(String class_type) {
		this.class_type = class_type;
	}

	@Override
	public String toString() {
		return "Hero{" +
				"id=" + id +
				", userId=" + userId +
				", xp=" + xp +
				", level=" + level +
				", positionX=" + positionX +
				", positionY=" + positionY +
				", class_type='" + class_type + '\'' +
				'}';
	}

	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`) VALUES (NULL, '" + getUserId() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() + "')";
	}
}
