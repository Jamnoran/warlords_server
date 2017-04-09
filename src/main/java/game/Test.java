package game;

import game.logging.Log;
import game.util.DatabaseUtil;
import game.vo.AbilityPosition;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.classes.Warrior;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		//ArrayList<AbilityPosition> data = DatabaseUtil.getHeroAbilityPositions(16);
		//Log.i(TAG, "Data : " + data.toString());

		DatabaseUtil.updateAbilityPosition(13, 8,2);

	}
}
