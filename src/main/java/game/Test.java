package game;

import game.vo.Hero;
import game.vo.classes.Warrior;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		Warrior warrior = new Warrior();
		warrior.setLevel(1);
		warrior.setId(5);
		warrior.setUser_id(8);
		warrior.setClass_type("WARRIOR");
		warrior.setXp(200);

		warrior.generateHeroInformation();

		warrior.setHp(warrior.getHp() - 5);

		Hero hero = warrior;

		hero.regenTick();
	}
}
