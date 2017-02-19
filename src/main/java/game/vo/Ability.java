package game.vo;

import game.logging.Log;

/**
 * Created by Jamnoran on 10-Nov-16.
 */
public class Ability {
	private static final String TAG = Ability.class.getSimpleName();
	private int id;
	private String classType;
	private String name;
	private int levelReq;
	private String damageType;
	private String description;
	private String image;
	private int baseDamage;
	private int topDamage;
	private int crittable;
	private String targetType;
	private String timeWhenOffCooldown;
	private int baseCD;
	private transient long millisLastUse = -1000000;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public void setLevelReq(int levelReq) {
		this.levelReq = levelReq;
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public int getTopDamage() {
		return topDamage;
	}

	public void setTopDamage(int topDamage) {
		this.topDamage = topDamage;
	}

	public int getCrittable() {
		return crittable;
	}

	public void setCrittable(int crittable) {
		this.crittable = crittable;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public void setBaseCD(int baseCD) {
		this.baseCD = baseCD;
	}

	public int getBaseCD() {
		return baseCD;
	}

	public long getMillisLastUse() {
		return millisLastUse;
	}

	public void setMillisLastUse(long millisLastUse) {
		this.millisLastUse = millisLastUse;
	}

	public String getTimeWhenOffCooldown() {
		return timeWhenOffCooldown;
	}

	public void setTimeWhenOffCooldown(String timeWhenOffCoolDown) {
		this.timeWhenOffCooldown = timeWhenOffCoolDown;
	}

	@Override
	public String toString() {
		return "Ability{" +
				"id=" + id +
				", classType='" + classType + '\'' +
				", name='" + name + '\'' +
				", levelReq=" + levelReq +
				", damageType='" + damageType + '\'' +
				", description='" + description + '\'' +
				", image='" + image + '\'' +
				", baseDamage=" + baseDamage +
				", topDamage=" + topDamage +
				", crittable=" + crittable +
				", targetType='" + targetType + '\'' +
				", timeWhenOffCooldown='" + timeWhenOffCooldown + '\'' +
				", baseCD=" + baseCD +
				", millisLastUse=" + millisLastUse +
				'}';
	}

	public boolean isAbilityOffCD(long time) {
		Log.i(TAG, "Is ability off cd : " + (getMillisLastUse() + getBaseCD() <= time) + " " + getMillisLastUse() + " +  " + getBaseCD() + " <= " + time);
		return (getMillisLastUse() + getBaseCD() <= time);
	}
}
