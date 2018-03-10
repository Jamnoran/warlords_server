package game.util;

import game.logging.Log;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Warlock;
import game.vo.classes.Warrior;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class DatabaseUtil {
	private static final String TAG = DatabaseUtil.class.getSimpleName();
	private static String ip = "warlord.ga";
//	private static String ip = "192.168.0.215";
	private static String port = "8889";
	private static String user = "warlord_clients";
	private static String password = "bosse45&";
	private static Integer countOfRequest = 0;

	public static Hero getHero(Integer id) {
		Hero hero = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM heroes where id = " + id);
				countOfRequest++;
				while (rs.next()) {
					Log.i(TAG, "Class : " + rs.getString("class_type"));
					if(rs.getString("class_type").equals(Hero.WARRIOR)){
						hero = new Warrior();
						Log.i(TAG, "Warrior user");
					}else if(rs.getString("class_type").equals(Hero.PRIEST)){
						Log.i(TAG, "Create priest instead");
						hero = new Priest();
					}else if(rs.getString("class_type").equals(Hero.WARLOCK)){
						Log.i(TAG, "Create warlock instead");
						hero = new Warlock();
					}else{
						Log.i(TAG, "Cant find class: [" + rs.getString("class_type") + "]");
					}
					//Retrieve by column name
					hero.setId(rs.getInt("id"));
					hero.setXp(rs.getInt("xp"));
					hero.setUser_id(rs.getInt("user_id"));
					hero.setClass_type(rs.getString("class_type"));
					hero.setLevel(rs.getInt("level"));
					hero.setTopGameLvl(rs.getInt("top_game_lvl"));
					//Display values
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return hero;
	}


	public static ArrayList<Ability> getAllAbilities(String classType){
		ArrayList<Ability> abilities = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM abilities where class_type LIKE \'" + classType + "\'");
				countOfRequest++;
				while (rs.next()) {
					Ability ability = new Ability();
					//Retrieve by column name
					ability.setId(rs.getInt("id"));
					ability.setName(rs.getString("name"));
					ability.setBaseDamage(rs.getInt("base_damage"));
					ability.setTopDamage(rs.getInt("top_damage"));
					ability.setValue(rs.getInt("value"));
					ability.setClassType(rs.getString("class_type"));
					ability.setCrittable(rs.getInt("crittable"));
					ability.setLevelReq(rs.getInt("level_req"));
					ability.setTargetType(rs.getString("target_type"));
					ability.setCastTime(rs.getLong("cast_time"));
					ability.setImage(rs.getString("image"));
					ability.setDescription(rs.getString("description"));
					ability.setDamageType(rs.getString("damage_type"));
					ability.setBaseCD(rs.getInt("base_cd"));
					ability.setResourceCost(rs.getInt("resource_cost"));
					ability.setRange(rs.getFloat("range"));
					//Display values
					abilities.add(ability);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}

		// Add Auto Attack ability at id 0
		Ability ability = new Ability();
		ability.setId(0);
		ability.setName("Auto Attack");
		ability.setImage("auto");
		abilities.add(ability);

		return abilities;
	}

	public static ArrayList<AbilityPosition> getHeroAbilityPositions(Integer heroId){
		ArrayList<AbilityPosition> abilityPositions = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM hero_ability where hero_id = " + heroId);
				countOfRequest++;
				while (rs.next()) {
					AbilityPosition ability = new AbilityPosition();
					//Retrieve by column name
					ability.setId(rs.getInt("id"));
					ability.setAbilityId(rs.getInt("ability_id"));
					ability.setHeroId(rs.getInt("hero_id"));
					ability.setPosition(rs.getInt("position"));

					//Display values
					abilityPositions.add(ability);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return abilityPositions;
	}


	public static void updateAbilityPosition(Integer heroId, Integer abilityId, Integer position) {
		boolean update = false;
		ArrayList<AbilityPosition> abilityPositions = DatabaseUtil.getHeroAbilityPositions(heroId);
		for(AbilityPosition abilityPosition : abilityPositions){
			if(abilityPosition.getAbilityId() == abilityId){
				update = true;
				break;
			}
		}

		Log.i(TAG, "Updating heroId " + heroId + " ability " + abilityId + " With position : " + position);
		Connection connection = getConnection();
		if (connection != null) {
			try {

				Statement stmt;
				AbilityPosition abilityPosition = new AbilityPosition(heroId, abilityId, position);
				stmt = connection.createStatement();
				if (update) {
					stmt.executeUpdate(abilityPosition.getSqlUpdateQuery());
				} else {
					stmt.executeUpdate(abilityPosition.getSqlInsertQuery());
				}
				countOfRequest++;
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
	}

	public static Hero updateHero(Hero hero) {
		if (hero != null && hero.getId() != null && hero.getId() > 0) {
			Connection connection = getConnection();
			if (connection != null) {
				try {
					Statement stmt = connection.createStatement();
					stmt.executeUpdate(hero.getSqlUpdateQuery());
					countOfRequest++;
					Log.i(TAG, "Update hero_id : " + hero.getId());
					stmt.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				Log.i(TAG, "Failed to make connection!");
			}
		}
		return hero;
	}



	public static ArrayList<Talent> getTalents(){
		ArrayList<Talent> talents = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from talents");
				countOfRequest++;
				while (rs.next()) {
					Talent talent = new Talent();
					//Retrieve by column name
					talent.setTalentId(rs.getInt("id"));
					talent.setDescription(rs.getString("description"));
					talent.setBaseValue(rs.getFloat("base"));
					talent.setScaling(rs.getFloat("scaling"));
					talent.setSpellId(rs.getInt("spell_id"));
					talent.setPosition(rs.getInt("position"));
					talent.setMaxPoints(rs.getInt("max_points"));

					//Display values
					talents.add(talent);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return talents;
	}


	public static ArrayList<Item> getItems(Integer level){
		ArrayList<Item> items = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				String query = "SELECT * from items";
				if(level != null){
					query = query + " where level_req <= " + (level + 5) + " and level_req >=" + (level - 5);
				}
				Log.i(TAG, "Query: " + query);
				ResultSet rs = stmt.executeQuery(query);
				countOfRequest++;
				while (rs.next()) {
					Item item = new Item();
					//Retrieve by column name
					item.setId(rs.getInt("id"));
					item.setName(rs.getString("name"));
					item.setBaseStat(rs.getInt("base_stat"));
					item.setTop(rs.getInt("top"));
					item.setPosition(rs.getString("position"));
					item.setClassType(rs.getString("class"));
					item.setDropRate(rs.getFloat("drop_rate"));
					item.setImage(rs.getString("image"));
					item.setLevelReq(rs.getInt("level_req"));
					item.setRarity(rs.getString("rarity"));

					//Display values
					items.add(item);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return items;
	}

	public static void updateHeroItem(Integer itemId, Integer newPosition, boolean equipped) {
		if (itemId != null && newPosition != null) {
			Connection connection = getConnection();
			if (connection != null) {
				try {
					Statement stmt = connection.createStatement();
					int equippedValue = 0;
					if(equipped){
						equippedValue = 1;
					}
					stmt.executeUpdate("update loot set position_id = " + newPosition + ", equipped = "+ equippedValue +" where id=" + itemId);
					countOfRequest++;
					Log.i(TAG, "Update item : " + itemId + " to new position " + newPosition);
					stmt.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				Log.i(TAG, "Failed to make connection!");
			}
		}
	}


	public static ArrayList<Item> getLoot(int heroId){
		ArrayList<Item> items = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT loot.id,loot.hero_id,loot.item_id, loot.equipped, loot.base,loot.top, loot.position_id, items.name, items.class, items.image, items.position, items.rarity, loot.stat_id_1, loot.stat_id_2, loot.stat_id_3, loot.stat_id_4 from loot, items where hero_id = " + heroId +" and loot.item_id = items.id");
				countOfRequest++;
				while (rs.next()) {
					Item item = new Item();
					//Retrieve by column name
					item.setId(rs.getInt("id"));
					item.setHeroId(rs.getInt("hero_id"));
					item.setItemId(rs.getInt("item_id"));
					item.setName(rs.getString("name"));
					item.setBaseStat(rs.getInt("base_stat"));
					item.setTop(rs.getInt("top"));
					item.setClassType(rs.getString("class"));
					item.setPosition(rs.getString("position"));
					item.setPositionId(rs.getInt("position_id"));
					item.setStatId_1(rs.getLong("stat_id_1"));
					item.setStatId_2(rs.getLong("stat_id_2"));
					item.setStatId_3(rs.getLong("stat_id_3"));
					item.setStatId_4(rs.getLong("stat_id_4"));
					Integer eq = rs.getInt("equipped");
					if(eq == 1){
						item.setEquipped(true);
					}
					item.setImage(rs.getString("image"));

					//Display values
					items.add(item);
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return items;
	}

	public static ArrayList<Talent> getHeroTalents(Integer heroId){
		ArrayList<Talent> talents = getTalents();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, points, talent_id, hero_id FROM warlords.user_talents where hero_id = " + heroId);
				countOfRequest++;
				while (rs.next()) {
					for(Talent talent : talents){
						if (talent.getTalentId() == rs.getInt("talent_id")) {
							//Retrieve by column name
							if (rs.getInt("hero_id") > 0) {
								talent.setId(rs.getInt("id"));
								talent.setHeroId(rs.getInt("hero_id"));
								talent.setPointAdded(rs.getInt("points"));
							}
						}
						talent.setHeroId(heroId);
					}
				}
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return talents;
	}

	public static void addTalentPoints(int heroId, ArrayList<Talent> talents){
		for (Talent talent : talents){
			talent.setHeroId(heroId);
			if (talent.getPointAdded() > 0) {
				addTalentPoint(talent);
			}
		}
	}

	public static Talent addTalentPoint(Talent talent) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				if(talent.getId() == 0){
					stmt.executeUpdate(talent.getSqlInsertQuery());
					countOfRequest++;
					int autoIncKeyFromApi = -1;
					ResultSet rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						autoIncKeyFromApi = rs.getInt(1);
						talent.setId(autoIncKeyFromApi);
					} else {
						// throw an exception from here
						Log.i(TAG, "Could not get user_id");
					}
				}else{
					stmt.executeUpdate(talent.getSqlUpdateQuery());
					countOfRequest++;
					//Log.i(TAG, "Update talent_id : " + talent.getId());
					stmt.close();
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return talent;
	}


	public static Item addLoot(Item item) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(item.getSqlInsertQueryLoot());
				countOfRequest++;
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
					item.setId(autoIncKeyFromApi);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return item;
	}

	public static Item addItem(Item item) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				Log.i(TAG, "[" + item.getSqlInsertQueryItem()+ "]");
				stmt.executeUpdate(item.getSqlInsertQueryItem());
				countOfRequest++;
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
					item.setId(autoIncKeyFromApi);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return item;
	}

	public static ItemStat addItemStat(ItemStat itemStat) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(itemStat.getSqlInsertQuery());
				countOfRequest++;
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
					itemStat.setId(autoIncKeyFromApi);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return itemStat;
	}



	public static Connection getConnection() {
		int tries = 3;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}
		Connection connection = null;
		for(int i = 0 ; i < tries ; i++){
			if(connection == null){
				try {
//					if (!) {
						connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/warlords", user, password);
//					}else{
//						connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:" + port + "/warlords", user, password);
//					}
				} catch (SQLException e) {
					Log.i(TAG, "Connection Failed! Check output console try number : " + i);
					//e.printStackTrace();
					return null;
				}
			}
		}
		return connection;
	}

	public static Integer getCounter(){
		return countOfRequest;
	}

}