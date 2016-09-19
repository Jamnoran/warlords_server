package game;

import game.logging.Log;
import util.CalculationUtil;
import vo.*;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	private static ArrayList<Minion> minions = new ArrayList<>();
	private static ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();

	public static void main(String[] args) {

		Vector3 onePoint = new Vector3(1f,0f,1f);
//		Vector3 twoPoint = new Vector3(2f,0f,2f);

//		double distance = Point2D.distance(onePoint.getX(), onePoint.getZ(), twoPoint.getX(), twoPoint.getY());

		for (int i = 0 ; i <= 10 ; i++) {
			float addingValue = CalculationUtil.getRandomFloat(-1.0f, 0.0f);
			Log.i(TAG, "random float is: " + addingValue);
			onePoint.setX(onePoint.getX() + addingValue);
			Log.i(TAG, "Pos x is: " + onePoint.getX());
		}
	}
}
