package test.game;

import game.GameServer;
import game.logging.Log;
import game.models.enemies.Minion;
import game.models.game.Amount;
import game.models.heroes.Hero;
import game.models.talents.Talent;
import game.models.items.ItemStat;
import game.util.DatabaseUtil;
import game.util.HeroUtil;
import game.models.heroes.warrior.Warrior;
import org.junit.Test;
import test.util.GameHelper;
import test.util.HeroHelper;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class HeroTest {

	private static final String TAG = HeroTest.class.getSimpleName();

	@Test
	public void getItemStat() {
		Warrior warrior = HeroHelper.getWarrior();
		System.out.println("Warrior has this much armor : " + warrior.getItemStat(ItemStat.ARMOR));
		System.out.println("Warrior has this much magic resist : " + warrior.getItemStat(ItemStat.MAGIC_RESIST));
		System.out.println("Warrior has this much damage from items: " + warrior.getItemStat(ItemStat.DAMAGE));

		warrior.updateStats();
		System.out.println("Stats are updated");
	}

	@Test
	public void addTalent(){
		Warrior warrior = HeroHelper.getWarrior();
		ArrayList<Talent> talents = new ArrayList<>();
		Talent talent = new Talent();
		talent.setHeroId(warrior.getId());
		talent.setTalentId(Talent.TALENT_GENERAL_HP);
		talent.setPointAdded(1);
		talents.add(talent);
		DatabaseUtil.addTalentPoints(warrior.getId(), talents);

		warrior = HeroHelper.getWarrior();
		for (Talent tal : warrior.getTalents()){
			Log.i(TAG, "Warrior talents : " + tal.toString());
		}
	}

	@Test
	public void stopHeroTest(){
		Warrior warrior = HeroHelper.getWarrior();
		GameServer server = GameHelper.createWorld(true, false);
		HeroUtil.stopHero(warrior.id, server);
	}

	@Test
	public void HeroAttackTest(){
		GameServer server = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(server, Hero.WARRIOR);
		Minion minion = GameHelper.getMinionByPos(server, 0);
		HeroUtil.attack(warrior.getId(), minion.getId(), System.currentTimeMillis(), server);
		try {
			sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void AttackedHeroTest(){
		GameServer server = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(server, Hero.WARRIOR);
		Minion minion = GameHelper.getMinionByPos(server, 0);
		//Integer heroId, Amount damage, Integer minionId, GameServer gameServer
		Log.i(TAG, "Hero hp : " + warrior.getHp());
		HeroUtil.attackHero(warrior.id, new Amount(10f, false), minion.getId(), server);
		try {
			sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "Hero hp after attack: " + warrior.getHp());
	}
}