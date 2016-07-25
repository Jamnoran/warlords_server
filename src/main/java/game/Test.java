package game;

import game.logging.Log;
import util.CalculationUtil;
import vo.*;
import vo.classes.Warrior;

import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	private static ArrayList<Minion> minions = new ArrayList<>();
	private static ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();

	public static void main(String[] args) {
//		Room room = new Room();
//		room.generate(new ArrayList<>(), new Vector3(1.0f, 1.0f, 1.0f), 90, Room.INDOOR);
		CalculationUtil.rollDice(10);
	}

}
