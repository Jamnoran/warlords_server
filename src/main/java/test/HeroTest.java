package test;

import game.vo.ItemStat;
import game.vo.classes.Warrior;
import org.junit.Test;
import test.util.HeroHelper;

public class HeroTest {

	private static final String TAG = HeroTest.class.getSimpleName();

	@Test
	public void getItemStat() {
		Warrior warrior = HeroHelper.getWarrior();
		System.out.println("Warrior has this much armor : " + warrior.getItemStat(ItemStat.ARMOR));
		System.out.println("Warrior has this much magic resist : " + warrior.getItemStat(ItemStat.MAGIC_RESIST));
		System.out.println("Warrior has this much damage from items: " + warrior.getItemStat(ItemStat.DAMAGE));
	}
}