package game.util;

import game.logging.Log;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Warrior;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class DatabaseUtil {
	private static final String TAG = DatabaseUtil.class.getSimpleName();
	private static String ip = "192.168.0.191";
	private static String user = "ErCa";
	private static String password = "test";


	public static User createUser(User user) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(user.getSqlInsertQuery());
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				Log.i(TAG, "Got user_id : " + autoIncKeyFromApi);
				user.setId(autoIncKeyFromApi);
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Failed to make connection!");
		}
		return user;
	}

	public static User getUser(Integer id) {
		User user = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, username, email, password FROM users where id = " + id);
				while (rs.next()) {
					user = new User();
					//Retrieve by column name
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
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
		return user;
	}

	public static User getUser(String email, String password) {
		User user = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, username, email, password FROM users where email = \"" + email + "\" and password = \"" + password + "\"");
				while (rs.next()) {
					user = new User();
					//Retrieve by column name
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
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
		return user;
	}


	public static Hero createHero(Integer userId, String classType) {
		Connection connection = getConnection();
		Hero hero = new Hero(userId);
		hero.setClass_type(classType.trim());
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(hero.getSqlInsertQuery());
				int autoIncKeyFromApi = -1;
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					autoIncKeyFromApi = rs.getInt(1);
				} else {
					// throw an exception from here
					Log.i(TAG, "Could not get user_id");
				}
				Log.i(TAG, "Got hero_id : " + autoIncKeyFromApi);
				hero.setId(autoIncKeyFromApi);
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

	public static Hero getHero(Integer id) {
		Hero hero = null;
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM heroes where id = " + id);
				while (rs.next()) {
					Log.i(TAG, "Class : " + rs.getString("class_type"));
					if(rs.getString("class_type").equals(Hero.WARRIOR)){
						hero = new Warrior();
						Log.i(TAG, "Warrior user");
					}else if(rs.getString("class_type").equals(Hero.PRIEST)){
						Log.i(TAG, "Create priest instead");
						hero = new Priest();
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
					ability.setImage(rs.getString("image"));
					ability.setDescription(rs.getString("description"));
					ability.setDamageType(rs.getString("damage_type"));
					ability.setBaseCD(rs.getInt("base_cd"));
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
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(hero.getSqlUpdateQuery());
				Log.i(TAG, "Update hero_id : " + hero.getId());
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



	public static ArrayList<Talent> getTalents(){
		ArrayList<Talent> talents = new ArrayList<>();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from talents");
				while (rs.next()) {
					Talent talent = new Talent();
					//Retrieve by column name
					talent.setTalentId(rs.getInt("id"));
					talent.setDescription(rs.getString("description"));
					talent.setBaseValue(rs.getFloat("base"));
					talent.setScaling(rs.getFloat("scaling"));
					talent.setSpellId(rs.getInt("spell_id"));
					talent.setPosition(rs.getInt("position"));

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


	public static ArrayList<Talent> getHeroTalents(Integer heroId){
		ArrayList<Talent> talents = getTalents();
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, points, talent_id, hero_id FROM warlords.user_talents where hero_id = " + heroId);
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

	public static void addTalentPoints(ArrayList<Talent> talents){
		for (Talent talent : talents){
			addTalentPoint(talent);
		}
	}

	public static Talent addTalentPoint(Talent talent) {
		Connection connection = getConnection();
		if (connection != null) {
			try {
				Statement stmt = connection.createStatement();
				if(talent.getId() == 0){
					stmt.executeUpdate(talent.getSqlInsertQuery());
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
					Log.i(TAG, "Update talent_id : " + talent.getId());
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






	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}
		try {
			return DriverManager.getConnection("jdbc:mysql://" + ip + ":9996/warlords", user, password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
	}

}