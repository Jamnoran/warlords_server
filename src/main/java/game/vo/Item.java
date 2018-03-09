package game.vo;

import game.util.CalculationUtil;

public class Item {
	private int id;
	private int heroId;
	private int itemId;
	private String name;
	private String position;
	private String image;
	private String classType;
	private String rarity;
	private int baseStat;
	private int top;
	private transient float dropRate;
	private int levelReq;
	private int statId_1;
	private int statId_2;
	private int statId_3;
	private int statId_4;
	private boolean equipped = false;

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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public int getBaseStat() {
		return baseStat;
	}

	public void setBaseStat(int baseStat) {
		this.baseStat = baseStat;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public float getDropRate() {
		return dropRate;
	}

	public void setDropRate(float dropRate) {
		this.dropRate = dropRate;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public void setLevelReq(int levelReq) {
		this.levelReq = levelReq;
	}

	public int getStatId_1() {
		return statId_1;
	}

	public void setStatId_1(int statId_1) {
		this.statId_1 = statId_1;
	}

	public int getStatId_2() {
		return statId_2;
	}

	public void setStatId_2(int statId_2) {
		this.statId_2 = statId_2;
	}

	public int getStatId_3() {
		return statId_3;
	}

	public void setStatId_3(int statId_3) {
		this.statId_3 = statId_3;
	}

	public int getStatId_4() {
		return statId_4;
	}

	public void setStatId_4(int statId_4) {
		this.statId_4 = statId_4;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", heroId=" + heroId +
				", itemId=" + itemId +
				", name='" + name + '\'' +
				", position='" + position + '\'' +
				", image='" + image + '\'' +
				", classType='" + classType + '\'' +
				", rarity='" + rarity + '\'' +
				", baseStat=" + baseStat +
				", top=" + top +
				", dropRate=" + dropRate +
				", levelReq=" + levelReq +
				", statId_1=" + statId_1 +
				", statId_2=" + statId_2 +
				", statId_3=" + statId_3 +
				", statId_4=" + statId_4 +
				", equipped=" + equipped +
				'}';
	}

	public String getSqlInsertQuery() {
		return "INSERT into loot SET hero_id = " + getHeroId() + " , item_id = " + getItemId()+ ", baseStat = " + getBaseStat() + ", top = " + getTop();
	}

	public void generateInfo() {
		setBaseStat(CalculationUtil.getRandomInt(getBaseStat(), getTop()));
		setItemId(getId());
	}
}
