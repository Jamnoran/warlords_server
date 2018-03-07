package game;

import game.logging.Log;
import game.util.DatabaseUtil;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.classes.Warrior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class Test {

	private static final String TAG = Test.class.getSimpleName();
	private static Hero hero;
	private static GameServer server;
	private static Minion minion;

	public static void main(String[] args) {
		//Hero hero = DatabaseUtil.getHero(16);


		//Warrior warr = (Warrior) hero;

		//warr.generateHeroInformation();

		//Log.i(TAG, "Got hero with hp: " + warr.getHp());

		//warr.takeDamage(100,0, "PHYSICAL");

		//Log.i(TAG, "Hero hp after damage " + warr.getHp());

	}

	static class Sender extends Thread {

		public Sender(){}

		/**
		 * Until interrupted reads messages from the standard input (keyboard) and
		 * sends them to the chat server through the socket.
		 */
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

				while (!isInterrupted()) {
					String message = in.readLine();
					if(message.equals("1")){
						//server.attackHero(18,10,4);
//						minion.addThreat(new Threat(18, 5f, 5f, 0));
//						minion.targetInRangeForAttack = true;
						Log.i(TAG, "Hero armor : " + hero.getArmor());
					}else if (message.equals("2")){
					}
				}
			} catch (IOException ioe) {
				// Communication is broken
			}
		}

	}
}
