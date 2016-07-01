package game;

import game.logging.Log;
import util.CalculationUtil;
import vo.GameAnimation;
import vo.Hero;
import vo.Minion;
import vo.classes.Warrior;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	private static ArrayList<Minion> minions = new ArrayList<>();
	private static ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();

	public static void main(String[] args) {
		Warrior warrior = new Warrior();
		warrior.generateHeroInformation();
		heroes.add(warrior);

		Minion minion = new Minion();
		minion.generateMinionInformation();
		minions.add(minion);


		handleAttack(warrior, minion);
		handleAttack(warrior, minion);


		Log.i(TAG, "Heroes: " + heroes.toString());
		Log.i(TAG, "Minions:" + minions.toString());

	}

	private static void handleAttack(Hero hero, Minion minion) {
		float damage = hero.getAttackDamage();
		Log.i(TAG, "Hero attack damage: " + damage);
		float damageAfterMinionCalculation =  minion.calculateDamageReceived(damage);
		Log.i(TAG, "Damage after minion calculation: " + damageAfterMinionCalculation);
		Log.i(TAG, "Minion died: " + minion.takeDamage(damageAfterMinionCalculation));
	}


//		Log.i(TAG, "Saving information to Mysql!");
//		String classType = "WARRIOR";
//
//		Integer id = 1;
//		User user = DatabaseUtil.getUser(id);
//		Log.i(TAG, "Got user from database : " + user.toString());
////		DatabaseUtil.createUser(user);
//
////		Hero hero = DatabaseUtil.createHero(id, classType);
////		Log.i(TAG, "Created hero : " + hero.toString());
//		ArrayList<Hero> heroes = DatabaseUtil.getHeroes(id);
//		for(Hero hero: heroes){
//			Log.i(TAG, "Hero: "+ hero.toString());
//		}v
}
