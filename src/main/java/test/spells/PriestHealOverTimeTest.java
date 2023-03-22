package test.spells;

import game.GameServer;
import game.models.abilities.AbilityID;
import game.models.heroes.priest.spells.PriestHealOverTime;
import game.models.game.Amount;
import game.models.heroes.Hero;
import game.models.vo.Vector3;
import game.models.heroes.priest.Priest;
import test.util.GameHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class PriestHealOverTimeTest {

	@org.junit.Test
	public void priestHealOverTimeTest() {
		long time = System.currentTimeMillis();
		GameServer server = GameHelper.createWorld(false, true);
		Priest priest = (Priest) GameHelper.getHeroByClass(server, Hero.PRIEST);
		priest.takeDamage(30.0f, 0, Amount.PHYSICAL);
		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(priest.getId());
		Vector3 position = new Vector3(0,0,0);
		// Check that priest has taken damage
		assert (priest.getHp() < priest.getMaxHp());
		PriestHealOverTime spell = new PriestHealOverTime(time,  priest,  priest.getAbility(AbilityID.PRIEST_HEAL_OVER_TIME), server,  null,  targetFriendly,  position);
		if (spell.init()) {
			spell.execute();
		}
		try {
			// Need long time to check since this is a HOT
			sleep(12000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Check damage has been restored
		assert (priest.getHp() == priest.getMaxHp());
	}
}