package game;

import game.logging.Log;
import util.CalculationUtil;
import vo.Ability;
import util.DatabaseUtil;
import vo.Hero;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		Hero hero = new Hero();
		hero.setLevel(1);
		hero.setId(5);
		hero.setUser_id(8);
		hero.setClass_type("WARRIOR");
		hero.setXp(200);
		DatabaseUtil.updateHero(hero);

	}
}
