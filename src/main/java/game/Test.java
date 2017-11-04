package game;

import game.io.SpellRequest;
import game.logging.Log;
import game.util.DatabaseUtil;
import game.util.GameUtil;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Threat;
import game.vo.Tick;
import game.vo.classes.Warlock;
import game.vo.classes.Warrior;

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
