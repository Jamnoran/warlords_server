package test.game;

import game.GameServer;
import game.logging.Log;
import game.models.heroes.warrior.spells.WarriorRetaliation;
import game.util.TickEngine;
import game.models.heroes.Hero;
import game.models.enemies.Minion;
import game.models.game.Tick;
import game.models.vo.Vector3;
import game.models.heroes.warrior.Warrior;
import org.junit.Test;
import test.util.GameHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.junit.Assert.fail;

public class TickEngineTest {

	@Test
	public void startGameTicks() {
		long time = System.currentTimeMillis();
		GameServer gameServer = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(gameServer, Hero.WARRIOR);
		Minion minion = GameHelper.getMinionByPos(gameServer, 0);

		TickEngine te = new TickEngine(gameServer);
		te.startGameTicks();


		Vector3 position = new Vector3(0,0,0);
		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(warrior.getId());
		ArrayList<Integer> minions = new ArrayList<>();
		minions.add(minion.getId());
		Log.i("TickEngineTest","Starting spell");
		WarriorRetaliation spell = new WarriorRetaliation(time, warrior, warrior.getAbility(9), gameServer, null, targetFriendly, position);
		if (spell.init()) {
			spell.execute();
		} else{
			fail("Hero could not cast spell");
		}
		assert(warrior.getBuffs().size() > 0);

		Tick tick = new Tick(System.currentTimeMillis() + 50, Tick.BUFF);
		te.addTick(tick);
		try {
			sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameServer.endGame();
	}
}