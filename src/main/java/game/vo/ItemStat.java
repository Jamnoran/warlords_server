package game.vo;

public class ItemStat {

	public static final String ARMOR = "ARMOR";
	public static final String MAGIC_RESIST = "MAGIC_RESIST";
	public static final String CRIT = "CRIT";
	public static final String LIFE_STEAL = "LIFE_STEAL";
	public static final String HP = "HP";

	private long id;
	private String name;
	private String type;
	private int baseStat;
	private int top;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSqlInsertQuery() {
		return "INSERT into item_stats SET name=\"" + getName() + "\" , type=\"" + getType()+ "\", baseStat = " + getBaseStat() + ", top = " + getTop();
	}

	@Override
	public String toString() {
		return "ItemStat{" +
				"id=" + id +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", baseStat=" + baseStat +
				", top=" + top +
				'}';
	}
}
