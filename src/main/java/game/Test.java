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

		//DatabaseUtil.updateAbilityPosition(13, 8,2);

		int highestLevel = 14;
		int levelDivider = 5;

		int levelToPlay = highestLevel - (highestLevel % levelDivider) + 1;
		Log.i(TAG, "Level to play : " + levelToPlay);

	}
}
