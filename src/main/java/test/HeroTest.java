package test;

import game.logging.Log;
import game.util.DatabaseUtil;
import game.vo.ItemStat;
import game.vo.Talent;
import game.vo.classes.Warrior;
import org.junit.Test;
import test.util.HeroHelper;

import java.util.ArrayList;

public class HeroTest {

	private static final String TAG = HeroTest.class.getSimpleName();

	@Test
	public void getItemStat() {
		Warrior warrior = HeroHelper.getWarrior();
		System.out.println("Warrior has this much armor : " + warrior.getItemStat(ItemStat.ARMOR));
		System.out.println("Warrior has this much magic resist : " + warrior.getItemStat(ItemStat.MAGIC_RESIST));
		System.out.println("Warrior has this much damage from items: " + warrior.getItemStat(ItemStat.DAMAGE));
	}

	@Test
	public void addTalent(){
		Warrior warrior = HeroHelper.getWarrior();
		ArrayList<Talent> talents = new ArrayList<>();
		Talent talent = new Talent();
		talent.setHeroId(warrior.getId());
		talent.setTalentId(Talent.TALENT_GENERAL_ARMOR_PENETRATION);
		talent.setPointAdded(1);
		DatabaseUtil.addTalentPoints(warrior.getId(), talents);

		warrior = HeroHelper.getWarrior();
		for (Talent tal : warrior.getTalents()){
			Log.i(TAG, "Warrior talents : " + tal.toString());
		}
	}
}