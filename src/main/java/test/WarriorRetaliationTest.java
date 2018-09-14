package test;

import game.GameServer;
import game.logging.Log;
import game.spells.warrior.WarriorRetaliation;
import game.vo.Amount;
import game.vo.Buff;
import game.vo.Minion;
import game.vo.Vector3;
import game.vo.classes.Warrior;
import test.util.HeroHelper;
import test.util.MinionHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WarriorRetaliationTest {

	@org.junit.Test
	public void warriorRetaliationTest() {

		long time = System.currentTimeMillis();
		Warrior warrior = HeroHelper.getWarrior();


		GameServer gameServer = new GameServer(null);
		gameServer.getHeroes().add(warrior);

		Minion minion = MinionHelper.getMinion(gameServer);
		gameServer.getMinions().add(minion);

		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(warrior.getId());
		Vector3 position = new Vector3(0,0,0);

		Log.i("Test","Starting spell");
		WarriorRetaliation spell = new WarriorRetaliation(time, warrior, warrior.getAbility(9), gameServer, null, targetFriendly, position);
		if (spell.init()) {
			spell.execute();
		}

		Log.i("Test","Checking if hero has buffs");
		for (Buff buff : warrior.getBuffs()) {
			System.out.println("Buff : " + buff.toString());
		}

		gameServer.attackHero(warrior.getId(), new Amount(10), minion.getId());

		Runnable checkAfterAwhileIfBuffIsThere = () -> {
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Buff buff : warrior.getBuffs()) {
				System.out.println("Buff after a while: " + buff.toString());
			}
			Log.i("Test","Checked if there was any buffs");

			Log.i("Test","Minion hp : " + minion.getHp() + " / " + minion.getMaxHp());

		};
		checkAfterAwhileIfBuffIsThere.run();



	}
}