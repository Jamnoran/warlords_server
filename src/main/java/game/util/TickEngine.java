package game.util;

import game.GameServer;
import game.logging.Log;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Tick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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

	public TickEngine(GameServer gameServer) {
		server = gameServer;
	}

	public void startGameTicks() {
		initAllTicks();
		Thread tickThread = new Thread(() -> {
			while (server.isGameRunning()) {
				Collections.sort(ticks);
				ticksToAdd.clear();
				// Check ticks if they should be actioned
				Iterator<Tick> iterator = ticks.iterator();
				while (iterator.hasNext()) {
					Tick tick = iterator.next();
					if (System.currentTimeMillis() >= tick.timeToActivate) {
						if (tick.typeOfTick != Tick.GAME_STATUS && tick.typeOfTick != Tick.REQUEST_MINION_POSITION && tick.typeOfTick != Tick.HERO_REGEN && tick.typeOfTick != Tick.MINION_ACTION) {
							Log.i(TAG, "Activated this tick : " + tick.timeToActivate + " of type " + tick.typeOfTick);
						}
						if (tick.typeOfTick == Tick.GAME_STATUS) {
							gameStatusTick();
						}
						if (tick.typeOfTick == Tick.MINION_ACTION) {
							minionActionTick();
						}
						if (tick.typeOfTick == Tick.HERO_REGEN) {
							heroRegenTick();
						}
						if (tick.typeOfTick == Tick.BUFF) {
							heroBuffTick();
						}
						if (tick.typeOfTick == Tick.REQUEST_MINION_POSITION) {
							requestMinionPositionTick();
						}
						if (tick.typeOfTick == Tick.MINION_DEBUFF) {
							minionDebuffTick();
						}
						iterator.remove();
					}
				}

				// Add all ticks wanted to be created to list here so we do not get error from adding inside iterator
				ticks.addAll(ticksToAdd);

				// Wait for next tick
				try {
					sleep(threadTick);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		tickThread.start();
	}

	private void initAllTicks() {
		long time = System.currentTimeMillis();
		ticks.add(new Tick((time + gameStatusTickTime), Tick.GAME_STATUS));
		ticks.add(new Tick((time + minionActionTickTime), Tick.MINION_ACTION));
		ticks.add(new Tick((time + heroRegenTickTime), Tick.HERO_REGEN));
		ticks.add(new Tick((time + requestMinionPositionTickTime), Tick.REQUEST_MINION_POSITION));
	}

	private void minionDebuffTick() {
		server.getGameUtil().minionDebuffs(server.getMinions(), server.getHeroes());
	}

	private void requestMinionPositionTick() {
		// Send request to a random client to update server with actual position of minions
		CommunicationUtil.sendRequestMinionPosition(server);
		ticksToAdd.add(new Tick((System.currentTimeMillis() + requestMinionPositionTickTime), Tick.HERO_REGEN));
	}

	private void heroBuffTick() {
		Log.i(TAG, "Hero buff");
		server.getGameUtil().heroBuffs(server.getMinions(), server.getHeroes());
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
		ticksToAdd.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), Tick.GAME_STATUS));
	}

	public void addTick(Tick tick) {
		ticks.add(tick);
	}
}
