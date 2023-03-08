package test.spells;

import game.GameServer;
import game.spells.AbilityID;
import game.spells.warrior.WarriorRetaliation;
import game.util.HeroUtil;
import game.vo.Amount;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Vector3;
import game.vo.classes.Warrior;
import test.util.GameHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WarriorRetaliationTest {

	@org.junit.Test
	public void warriorRetaliationTest() {
		long time = System.currentTimeMillis();
		GameServer server = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(server, Hero.WARRIOR);
		Minion minion = GameHelper.getMinionByPos(server, 0);

		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(warrior.getId());
		Vector3 position = new Vector3(0,0,0);

		assert (warrior.getBuffs().size() == 0);
		WarriorRetaliation spell = new WarriorRetaliation(time, warrior, warrior.getAbility(AbilityID.WARRIOR_RETALLIATION), server, null, targetFriendly, position);
		if (spell.init()) {
			spell.execute();
		}

		// Assert hero has buff
		assert (warrior.getBuffs().size() > 0);

		HeroUtil.attackHero(warrior.getId(), new Amount(10), minion.getId(), server);

		Runnable checkAfterAwhileIfBuffIsThere = () -> {
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Assert buff is gone
			assert (warrior.getBuffs().size() == 0);

			// Assert that minion has taken damage
			assert (minion.getHp() < minion.getMaxHp());

		};
		checkAfterAwhileIfBuffIsThere.run();
	}
}