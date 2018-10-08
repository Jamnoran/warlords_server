package game.vo;

/**
 * Created by Jamnoran on 2017-05-10.
 */
public class Talent {

	public static transient int TALENT_CD_HEROIC_CHARGE = 1;
	public static transient int TALENT_TAUNT_AMOUNT = 2;
	public static transient int TALENT_DEC_RESOURCE_HEROIC_CHARGE = 3;
	public static transient int TALENT_GENERAL_HP = 4;
	public static transient int TALENT_GENERAL_DAMAGE = 5;
	public static transient int TALENT_AMOUNT_FLASH_HEAL = 6;
	public static transient int TALENT_AMOUNT_HOT = 7;
	public static transient int TALENT_GENERAL_INTELLIGENCE = 9;
	public static transient int TALENT_GENERAL_ARMOR = 10;
	public static transient int TALENT_GENERAL_ARMOR_PENETRATION = 11;
	public static transient int TALENT_MAGIC_RESIST = 12;
	public static transient int TALENT_GENERALMAGIC_PENETRATION = 13;

	private int id;
	private int heroId;
	private int spellId;
	private int talentId;
	private String description;
	private float baseValue;
	private float scaling;
	private int pointAdded;
	private int position;
	private int maxPoints;

	public Talent() {
	}

	public Talent(int id, int heroId, int talentId) {
		this.id = id;
		this.heroId = heroId;
		this.talentId = talentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getSpellId() {
		return spellId;
	}

	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}

	public String getDescription() {
		return description;
	}

	public int getTalentId() {
		return talentId;
	}

	public void setTalentId(int talentId) {
		this.talentId = talentId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(float baseValue) {
		this.baseValue = baseValue;
	}

	public float getScaling() {
		return scaling;
	}

	public void setScaling(float scaling) {
		this.scaling = scaling;
	}

	public int getPointAdded() {
		return pointAdded;
	}

	public void setPointAdded(int pointAdded) {
		this.pointAdded = pointAdded;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	@Override
	public String toString() {
		return "Talent{" +
				"id=" + id +
				", heroId=" + heroId +
				", spellId=" + spellId +
				", talentId=" + talentId +
				", description='" + description + '\'' +
				", baseValue=" + baseValue +
				", scaling=" + scaling +
				", pointAdded=" + pointAdded +
				", maxPoints=" + maxPoints +
				'}';
	}

	public String getSqlUpdateQuery() {
		return "UPDATE `user_talents` SET `points`= " + getPointAdded() + " WHERE id = " + getId();
	}

	public String getSqlInsertQuery() {
		return "INSERT into user_talents SET points = " + getPointAdded() + " , hero_id = " + getHeroId()+ ", talent_id = " + getTalentId();
	}


//	public String getSqlInsertQuery() {
//		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`, `top_game_lvl`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() +  "', '" + getTopGameLvl() + "')";
//	}
//
//	public String getSqlUpdateQuery() {
//		return "UPDATE `heroes` SET `level`=" + getLevel() + ",`xp`=" + getXp() + ",`top_game_lvl`=" + getTopGameLvl() + " WHERE id = " + getId();
//	}
}
