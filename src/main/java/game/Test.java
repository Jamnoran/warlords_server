package game;

import game.logging.Log;
import util.CalculationUtil;
import vo.Ability;
import util.DatabaseUtil;
import vo.Hero;
import vo.classes.Warrior;

import java.util.ArrayList;

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
