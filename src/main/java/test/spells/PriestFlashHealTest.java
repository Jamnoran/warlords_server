package test.spells;

import game.GameServer;
import game.spells.AbilityID;
import game.spells.priest.PriestHeal;
import game.vo.Amount;
import game.vo.Hero;
import game.vo.Vector3;
import game.vo.classes.Priest;
import test.util.GameHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class PriestFlashHealTest {

	@org.junit.Test
	public void priestFlashHealTest() {
		long time = System.currentTimeMillis();
		GameServer server = GameHelper.createWorld(false, true);
		Priest priest = (Priest) GameHelper.getHeroByClass(server, Hero.PRIEST);
		priest.takeDamage(10.0f, 0, Amount.PHYSICAL);
		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(priest.getId());
		Vector3 position = new Vector3(0,0,0);
		// Check that priest has taken damage
		assert (priest.getHp() < priest.getMaxHp());
		PriestHeal spell = new PriestHeal(time,  priest,  priest.getAbility(AbilityID.PRIEST_FLASH_HEAL), server,  null,  targetFriendly,  position);
		if (spell.init()) {
			spell.execute();
		}
		try {
			sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Check damage has been restored
		assert (priest.getHp() == priest.getMaxHp());
	}
}