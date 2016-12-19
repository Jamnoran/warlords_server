package game;

import game.logging.Log;
import util.CalculationUtil;
import vo.Ability;
import util.DatabaseUtil;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		Log.i(TAG, "Calc: " + CalculationUtil.getRandomInt(5,5));
		Log.i(TAG, "Calc: " + CalculationUtil.getRandomInt(5,5));
		Log.i(TAG, "Calc: " + CalculationUtil.getRandomInt(5,5));

	}
}
