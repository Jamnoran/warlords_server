package game;

import game.logging.Log;
import game.util.GameUtil;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Tick;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static java.lang.Thread.sleep;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {

		Sender sender = new Sender();
		sender.setDaemon(true);
		sender.start();
		startGameTicks();

	}


	private static boolean gameRunning = true;
	private static Thread tickThread;
	private static int threadTick = 100;
	private static int gameStatusTickTime = 1000;
	public static ArrayList<Tick> ticks = new ArrayList<>();
	private static void startGameTicks() {
		ticks.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), Tick.GAME_STATUS));
		Log.i(TAG, "Starting new thread");
		tickThread = new Thread(() -> {
			while (gameRunning ) {
				Collections.sort(ticks);

				boolean addNewGameStatus = false;
				// Check ticks if they should be actioned
				Iterator<Tick> iterator = ticks.iterator();
				while (iterator.hasNext()) {
					Tick tick = iterator.next();
					if(System.currentTimeMillis() >= tick.timeToActivate){
						Log.i(TAG, "Activated this tick : " + tick.timeToActivate + " of type " + tick.typeOfTick);

						if(tick.typeOfTick == Tick.GAME_STATUS) {
							addNewGameStatus = true;
						}
						iterator.remove();
					}
				}

				try {
					sleep(threadTick);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (addNewGameStatus) {
					ticks.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), Tick.GAME_STATUS));
				}

			}

		});
		tickThread.start();
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
						ticks.add(new Tick((System.currentTimeMillis() + 200), Tick.BUFF));

						ticks.add(new Tick((System.currentTimeMillis() + 400), Tick.BUFF));
						ticks.add(new Tick((System.currentTimeMillis() + 600), Tick.BUFF));
					}else if (message.equals("2")){
						gameRunning = false;
					}
				}
			} catch (IOException ioe) {
				// Communication is broken
			}
		}

	}
}
