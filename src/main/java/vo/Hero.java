package vo;

public class Hero {
	public Integer id = null;
	public Integer user_id = null;
	public Integer xp = 0;
	public Integer level = 1;
	public Double positionX = 6.0d;
	public Double positionY = 5.0d;
	public String class_type = "WARRIOR";

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
				", user_id=" + user_id +
				", xp=" + xp +
				", level=" + level +
				", positionX=" + positionX +
				", positionY=" + positionY +
				", class_type='" + class_type + '\'' +
				'}';
	}

	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() + "')";
	}
}
