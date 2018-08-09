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
	private Thread tickThread;
	private int threadTick = 100;
	private int gameStatusTickTime = 1000;
	private int minionActionTickTime = 500;
	private int heroRegenTickTime = 2000;
	private int requestMinionPositionTickTime = 1000;

	public ArrayList<Tick> ticks = new ArrayList<>();

	public TickEngine(GameServer gameServer) {
		server = gameServer;
	}

	public void startGameTicks() {
		long time = System.currentTimeMillis();
		ticks.add(new Tick((time + gameStatusTickTime), Tick.GAME_STATUS));
		ticks.add(new Tick((time + minionActionTickTime), Tick.MINION_ACTION));
		ticks.add(new Tick((time + heroRegenTickTime), Tick.HERO_REGEN));
		ticks.add(new Tick((time + requestMinionPositionTickTime), Tick.REQUEST_MINION_POSITION));
		Log.i(TAG, "Starting new thread");
		tickThread = new Thread(() -> {
			while (server.isGameRunning()) {
				Collections.sort(ticks);

				boolean addNewGameStatus = false;
				boolean addMinionAction = false;
				boolean addHeroRegen = false;
				boolean addRequestMinionPosition = false;
				boolean addHeroBuff = false;

				boolean minionDebuffAction = false;

				// Check ticks if they should be actioned
				Iterator<Tick> iterator = ticks.iterator();
				while (iterator.hasNext()) {
					Tick tick = iterator.next();
					if (System.currentTimeMillis() >= tick.timeToActivate) {
						if (tick.typeOfTick != Tick.GAME_STATUS && tick.typeOfTick != Tick.REQUEST_MINION_POSITION && tick.typeOfTick != Tick.HERO_REGEN && tick.typeOfTick != Tick.MINION_ACTION) {
							Log.i(TAG, "Activated this tick : " + tick.timeToActivate + " of type " + tick.typeOfTick);
						}
						if (tick.typeOfTick == Tick.GAME_STATUS) {
							addNewGameStatus = true;
						}
						if (tick.typeOfTick == Tick.MINION_ACTION) {
							addMinionAction = true;
						}
						if (tick.typeOfTick == Tick.HERO_REGEN) {
							addHeroRegen = true;
						}
						if (tick.typeOfTick == Tick.BUFF) {
							addHeroBuff = true;
						}
						if (tick.typeOfTick == Tick.REQUEST_MINION_POSITION) {
							addRequestMinionPosition = true;
						}
						if (tick.typeOfTick == Tick.MINION_DEBUFF) {
							minionDebuffAction = true;
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
					server.sendGameStatus();
					ticks.add(new Tick((System.currentTimeMillis() + gameStatusTickTime), Tick.GAME_STATUS));
				}
				if (addMinionAction) {
					for (Minion minion : server.getMinions()) {
						if (minion.isAlive()) {
							minion.takeAction();
						}
					}
					ticks.add(new Tick((System.currentTimeMillis() + minionActionTickTime), Tick.MINION_ACTION));
				}

				if (minionDebuffAction) {
					server.getGameUtil().minionDebuffs(server.getMinions(), server.getHeroes());
				}

				if (addHeroBuff) {
					Log.i(TAG, "Hero buff");
					server.getGameUtil().heroBuffs(server.getMinions(), server.getHeroes());
				}
				if (addHeroRegen) {
					for (Hero hero : server.getHeroes()) {
						hero.regenTick();
					}
					ticks.add(new Tick((System.currentTimeMillis() + heroRegenTickTime), Tick.HERO_REGEN));
				}

				if (addRequestMinionPosition) {
					// Send request to a random client to update server with actual position of minions
					server.sendRequestMinionPosition();
					ticks.add(new Tick((System.currentTimeMillis() + requestMinionPositionTickTime), Tick.HERO_REGEN));
				}
			}
		});
		tickThread.start();
	}


	public void addTick(Tick tick) {
		ticks.add(tick);
	}

}
