package game.vo;

/**
 * Created by Jamnoran on 2017-05-10.
 */
public class Talent {

	private int id;
	private int heroId;
	private int spellId;
	private int talentId;
	private String description;
	private float base;
	private float scaling;
	private int pointAdded;

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

	public float getBase() {
		return base;
	}

	public void setBase(float base) {
		this.base = base;
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

	@Override
	public String toString() {
		return "Talent{" +
				"id=" + id +
				", heroId=" + heroId +
				", spellId=" + spellId +
				", talentId=" + talentId +
				", description='" + description + '\'' +
				", base=" + base +
				", scaling=" + scaling +
				", pointAdded=" + pointAdded +
				'}';
	}

	public String getSqlUpdateQuery() {
		return "UPDATE `talents` SET `points`=" + getPointAdded() + " WHERE id = " + getId();
	}


//	public String getSqlInsertQuery() {
//		return "INSERT INTO `warlords`.`heroes` (`id`, `user_id`, `xp`, `level`, `class_type`, `top_game_lvl`) VALUES (NULL, '" + getUser_id() + "', '" + getXp() + "', '" + getLevel() + "', '" + getClass_type() +  "', '" + getTopGameLvl() + "')";
//	}
//
//	public String getSqlUpdateQuery() {
//		return "UPDATE `heroes` SET `level`=" + getLevel() + ",`xp`=" + getXp() + ",`top_game_lvl`=" + getTopGameLvl() + " WHERE id = " + getId();
//	}
}
