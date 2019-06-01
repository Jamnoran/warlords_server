package test.spells;

import game.GameServer;
import game.logging.Log;
import game.spells.AbilityID;
import game.spells.warrior.WarriorTaunt;
import game.util.HeroUtil;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Vector3;
import game.vo.classes.Priest;
import game.vo.classes.Warrior;
import org.junit.Test;
import test.util.GameHelper;
import test.util.HeroHelper;
import test.util.MinionHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WarriorTauntTest {

	@Test
	public void warriorTauntTest(){
		long time = System.currentTimeMillis();
		GameServer gameServer = GameHelper.createWorld(true, true);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(gameServer, Hero.WARRIOR);
		Priest priest = (Priest) GameHelper.getHeroByClass(gameServer, Hero.PRIEST);
		Minion minion = GameHelper.getMinionByPos(gameServer, 0);

		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(warrior.getId());
		Vector3 position = new Vector3(0,0,0);

		HeroUtil.attack(priest.getId(), minion.getId(), System.currentTimeMillis(), gameServer);

		try {
			sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Assert that priest has highest threat before taunt
		assert (minion.getHeroIdWithMostThreat() == priest.getId());

		ArrayList<Integer> minions = new ArrayList<>();
		minions.add(minion.getId());
		WarriorTaunt spell = new WarriorTaunt(time, warrior, warrior.getAbility(AbilityID.WARRIOR_TAUNT), gameServer, minions, targetFriendly, position);
		if (spell.init()) {
			spell.execute();
		}

		try {
			sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Assert that warrior has highest threat after taunt
		assert (minion.getHeroIdWithMostThreat().equals(warrior.getId()));
	}
}