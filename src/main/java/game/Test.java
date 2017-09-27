package game;

import game.io.SpellRequest;
import game.logging.Log;
import game.util.GameUtil;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Tick;
import game.vo.classes.Warlock;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static java.lang.Thread.sleep;
import static sun.swing.MenuItemLayoutHelper.max;

public class Test {

	private static final String TAG = Test.class.getSimpleName();
	private static Hero hero;
	private static GameServer server;
	private static Minion minion;


	public static void main(String[] args) {


		// Armor calculation

		//A - total armor value of all equipped armor pieces
		//P - penetration value of enemy weapon
		//M - final result modifier for armor (0.7 means a 30% reduction in damage)
		//M = (100+P) / (100+A), where M is between 0.2 and 1.
		//M = POWER( MAX(A*(1-P/100),0), 1/1.3 )

		calculateDamageAfterReduction(0, 0, 100);
		calculateDamageAfterReduction(200, 0, 100);
		calculateDamageAfterReduction(200, 80, 100);


		//Sender sender = new Sender();
		//sender.setDaemon(true);
		//sender.start();

		//startTestGame();

	}




	private static double calculateDamageAfterReduction(double resistance, double penetration, double damage) {
		double armorAfterPenetration = resistance * (1 - (penetration / 100));
		double damageMultiplier = damage / ( damage + armorAfterPenetration);
		double totalDamage = damage * damageMultiplier;
		return totalDamage;
	}


	private static void startTestGame() {
		GameServer server = new GameServer(null);
		hero = new Warlock();
		hero.setUser_id(6);
		hero.setClass_type("WARLOCK");
		hero.setId(18);
		hero.setLevel(3);
		hero.generateHeroInformation();
		minion = new Minion(server);
		server.addHero(hero);

		minion.setLevel(1);
		minion.setId(4);
		minion.generateMinionInformation(5, 1, 5);
		server.addMinion(minion);

		SpellRequest req = new SpellRequest();
		req.setHeroId(hero.getId());
		req.setSpell_id(26);
		ArrayList<Integer> target = new ArrayList<>();
		target.add(minion.getId());
		req.setTarget_enemy(target);
		server.spell(req);

		Log.i(TAG, "hero hp left now: " + hero.getHp() + "/" + hero.getMaxHp());
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

					}else if (message.equals("2")){
					}
				}
			} catch (IOException ioe) {
				// Communication is broken
			}
		}

	}
}
