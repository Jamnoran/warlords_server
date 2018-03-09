package game;

import game.logging.Log;
import game.util.DatabaseUtil;
import game.util.GameUtil;
import game.util.ItemUtil;
import game.vo.Hero;
import game.vo.Item;
import game.vo.Minion;
import game.vo.classes.Warrior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Test {

	private static final String TAG = Test.class.getSimpleName();
	private static Hero hero;
	private static GameServer server;
	private static Minion minion;
	private String randomType;

	public static void main(String[] args) {
		Hero hero = DatabaseUtil.getHero(16);
//
		Warrior warr = (Warrior) hero;
//
		warr.generateHeroInformation();
//
		//Log.i(TAG, "Got hero with hp: " + warr.getHp());
//
//		warr.takeDamage(100,0, "PHYSICAL");
//
//		//Log.i(TAG, "Hero hp after damage " + warr.getHp());
//
//		ArrayList<Item> loot = DatabaseUtil.getLoot(warr.getId());
//
		ArrayList<Item> loot = GameUtil.generateLoot(warr);
		Log.i(TAG, "Got this many items: " + loot.size());
		for (Item item : loot) {
			Log.i(TAG, "Item : " + item.toString());
		}

		//DatabaseUtil.updateHeroItemPosition(2,3);
		//Log.i(TAG, "Hero hp after damage " + warr.getHp());

//		Item item = ItemUtil.generateItem(10, null);
//		Log.i(TAG, "Generate item : " + item.toString());
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
