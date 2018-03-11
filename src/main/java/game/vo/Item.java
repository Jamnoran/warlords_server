package game.vo;

import game.util.CalculationUtil;
import game.util.ItemUtil;

import java.util.ArrayList;

public class Item {
	public static final String LEGENDARY = "LEGENDARY";
	public static final String RARE = "RARE";
	public static final String UNCOMMON = "UNCOMMON";
	public static final String COMMON = "COMMON";

	public static String MAIN_HAND = "MAIN_HAND";
	public static String OFF_HAND = "OFF_HAND";
	public static String HEAD = "HEAD";
	public static String SHOULDERS = "SHOULDERS";
	public static String CHEST = "CHEST";
	public static String LEGS = "LEGS";
	public static String BOOTS = "BOOTS";


	private long id;
	private long heroId;
	private long itemId;
	private String name;
	private String position;
	private String image;
	private String classType;
	private String rarity;
	private int baseStat;
	private int top;
	private transient float dropRate;
	private int levelReq;
	private long statId_1;
	private long statId_2;
	private long statId_3;
	private long statId_4;
	private boolean equipped = false;
	private Integer positionId;
	private ArrayList<ItemStat> stats = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHeroId() {
		return heroId;
	}

	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
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

	public long getStatId_1() {
		return statId_1;
	}

	public void setStatId_1(long statId_1) {
		this.statId_1 = statId_1;
	}

	public long getStatId_2() {
		return statId_2;
	}

	public void setStatId_2(long statId_2) {
		this.statId_2 = statId_2;
	}

	public long getStatId_3() {
		return statId_3;
	}

	public void setStatId_3(long statId_3) {
		this.statId_3 = statId_3;
	}

	public long getStatId_4() {
		return statId_4;
	}

	public void setStatId_4(long statId_4) {
		this.statId_4 = statId_4;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public ArrayList<ItemStat> getStats() {
		return stats;
	}

	public void setStats(ArrayList<ItemStat> stats) {
		this.stats = stats;
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
				", positionId=" + positionId +
				", stats=" + stats +
				'}';
	}
	public String getSqlInsertQueryItem() {
		return "INSERT into items SET name = \"" + getName()+ "\", class=\""+getClassType()+"\", position=\""+getPosition()+"\",image=\""+getImage()+"\",drop_rate="+getDropRate()+", level_req="+getLevelReq()+", rarity=\""+getRarity()+"\", base_stat = " + getBaseStat() + ", top = " + getTop();
	}

	public String getSqlInsertQueryLoot() {
		return "INSERT into loot SET hero_id = " + getHeroId() + " , item_id = " + getItemId() + ", stat_id_1 = " + getStatId_1() + ", stat_id_2 = " + getStatId_2() + ", stat_id_3 = " + getStatId_3() + ", stat_id_4 = " + getStatId_4() + ", base_stat = " + getBaseStat() + ", top = " + getTop();
	}

	/**
	 * This is done when item is generated and added to users inventory
	 */
	public void generateInfo(boolean generateStat) {
		setBaseStat(CalculationUtil.getRandomInt(getBaseStat(), getTop()));

		if (generateStat) {
			// Generate extra item stats
			Item item = ItemUtil.generateExtraStats(this);
			setStatId_1(item.getStatId_1());
			setStatId_2(item.getStatId_2());
			setStatId_3(item.getStatId_3());
			setStatId_4(item.getStatId_4());
			setItemId(getId());

			setPositionId(-1);
		}
	}
}
