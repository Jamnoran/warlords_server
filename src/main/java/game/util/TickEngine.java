package game.util;

import game.GameServer;
import game.io.CommunicationHandler;
import game.logging.Log;
import game.models.enemies.Minion;
import game.models.game.Tick;
import game.models.heroes.Hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static game.models.game.Tick.GAME_STATUS;
import static java.lang.Thread.sleep;

public class TickEngine {
	private static final String TAG = TickEngine.class.getSimpleName();
	// Living game world
	private GameServer server;
	private int threadTick = 100;
	private int gameStatusTickTime = 1000;
	private int minionActionTickTime = 500;
	private int heroRegenTickTime = 2000;
	private int requestMinionPositionTickTime = 1000;

	private ArrayList<Tick> ticks = new ArrayList<>();
	private ArrayList<Tick> ticksToAdd = new ArrayList<>();
	private ArrayList<Tick> ticksToRemove = new ArrayList<>();

	public TickEngine(GameServer gameServer) {
		server = gameServer;
	}

	private static final long THREAD_TICK = 100;

	public void startGameTicks() {
		initAllTicks();
		Thread tickThread = new Thread(() -> {
			while (server.isGameRunning()) {
				Collections.sort(ticks);
				ticksToAdd.clear();

				// Check ticks if they should be actioned
				for (Tick tick : ticks) {
					if (System.currentTimeMillis() >= tick.timeToActivate) {
						switch (tick.typeOfTick) {
							case Tick.GAME_STATUS -> gameStatusTick();
							case Tick.MINION_ACTION -> minionActionTick();
							case Tick.HERO_REGEN -> heroRegenTick();
							case Tick.BUFF -> heroBuffTick();
							case Tick.REQUEST_MINION_POSITION -> requestMinionPositionTick();
							case Tick.MINION_DEBUFF -> minionDebuffTick();
							default -> Log.i(TAG, "Activated this tick : " + tick.timeToActivate + " of type " + tick.typeOfTick);
						}
						ticksToRemove.add(tick);
					}
				}

				// Remove all ticks that have been actioned
				ticks.removeAll(ticksToRemove);
				ticksToRemove.clear();

				// Add all ticks wanted to be created to list here so we do not get error from adding inside iterator
				ticks.addAll(ticksToAdd);

				// Wait for next tick
				try {
					Thread.sleep(THREAD_TICK);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		tickThread.start();
	}

	private void initAllTicks() {
		long time = System.currentTimeMillis();
		ticks.add(new Tick((time + gameStatusTickTime), GAME_STATUS));
		ticks.add(new Tick((time + minionActionTickTime), Tick.MINION_ACTION));
		ticks.add(new Tick((time + heroRegenTickTime), Tick.HERO_REGEN));
		ticks.add(new Tick((time + requestMinionPositionTickTime), Tick.REQUEST_MINION_POSITION));
	}

	private void minionDebuffTick() {
		server.getGameUtil().handleMinionDebuffs(server.getMinions(), server.getHeroes());
	}

	private void requestMinionPositionTick() {
		// Send request to a random client to update server with actual position of minions
		CommunicationHandler.sendRequestMinionPosition(server);
		ticksToAdd.add(new Tick((System.currentTimeMillis() + requestMinionPositionTickTime), Tick.HERO_REGEN));
	}

	private void heroBuffTick() {
		Log.i(TAG, "Hero buff");
		server.getGameUtil().heroBuffs(server.getHeroes());
	}

	private void heroRegenTick() {
		for (Hero hero : server.getHeroes()) {
			hero.regenTick();
		}
		ticksToAdd.add(new Tick((System.currentTimeMillis() + heroRegenTickTime), Tick.HERO_REGEN));
	}

	private void minionActionTick() {
		for (Minion minion : server.getMinions()) {
			if (minion.isAlive()) {
				minion.takeAction();
			}
		}
		ticksToAdd.add(new Tick((System.currentTimeMillis() + minionActionTickTime), Tick.MINION_ACTION));
	}

	private void gameStatusTick() {
		server.sendGameStatus();
		ticksToAdd.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), GAME_STATUS));
	}

	public void addTick(Tick tick) {
		ticks.add(tick);
	}
}
