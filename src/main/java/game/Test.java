package game;

import game.logging.Log;
import vo.Ability;
import util.DatabaseUtil;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		ArrayList<Ability> abilities = DatabaseUtil.getAllAbilities("PRIEST");
		for (Ability ab : abilities) {
			Log.i(TAG, "Ability" + ab.toString());
		}
	}
}
