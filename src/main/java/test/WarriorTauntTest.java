package test;

import game.GameServer;
import game.logging.Log;
import game.spells.warrior.WarriorTaunt;
import game.vo.Minion;
import game.vo.Vector3;
import game.vo.classes.Priest;
import game.vo.classes.Warrior;
import org.junit.Test;
import test.util.HeroHelper;
import test.util.MinionHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WarriorTauntTest {

	@Test
	public void taunt(){
		long time = System.currentTimeMillis();
		Warrior warrior = HeroHelper.getWarrior();

		Priest priest = HeroHelper.getPriest();

		GameServer gameServer = new GameServer(null);
		gameServer.getHeroes().add(warrior);
		gameServer.getHeroes().add(priest);

		Minion minion = MinionHelper.getMinion(gameServer);
		gameServer.getMinions().add(minion);

		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(warrior.getId());
		Vector3 position = new Vector3(0,0,0);

		gameServer.attack(priest.getId(), minion.getId(), System.currentTimeMillis());

		try {
			sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Log.i("Test","Minion threat table: " + minion.getHeroIdWithMostThreat());
		Log.i("Test", "Amount was : " + minion.getHighestThreathValue());


		Log.i("Test","Starting spell taunt");
		ArrayList<Integer> minions = new ArrayList<>();
		minions.add(minion.getId());
		WarriorTaunt spell = new WarriorTaunt(time, warrior, warrior.getAbility(8), gameServer, minions, targetFriendly, position);
		if (spell.init()) {
			spell.execute();
		} else {
			Log.i("Test", "Could not send spell, probably because of mana or cd");
		}

		try {
			sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Log.i("Test","Minion threath table: " + minion.getHeroIdWithMostThreat());


	}

}