package game.vo;

/**
 * Created by Eric on 2017-04-09.
 */
public class AbilityPosition {
	private int id;
	private int heroId;
	private int abilityId;
	private int position;

	public AbilityPosition() {
	}

	public AbilityPosition(Integer hId, Integer aId, Integer pos) {
		heroId = hId;
		abilityId = aId;
		position = pos;
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

	public int getAbilityId() {
		return abilityId;
	}

	public void setAbilityId(int abilityId) {
		this.abilityId = abilityId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getSqlUpdateQuery() {
		return "UPDATE `hero_ability` SET `position`=" + getPosition() + " WHERE hero_id = " + getHeroId() + " and ability_id = " + getAbilityId();
	}

	public String getSqlInsertQuery() {
		return "INSERT into `hero_ability` SET `position`=" + getPosition() + ", hero_id = " + getHeroId() + ", ability_id = " + getAbilityId();
	}


	@Override
	public String toString() {
		return "AbilityPosition{" +
				"id=" + id +
				", heroId=" + heroId +
				", abilityId=" + abilityId +
				", position=" + position +
				'}';
	}

}
