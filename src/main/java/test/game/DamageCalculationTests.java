package test.game;

import game.GameServer;
import game.logging.Log;
import game.util.HeroUtil;
import game.vo.Amount;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.Talent;
import game.vo.classes.Warrior;
import org.junit.Test;
import test.util.GameHelper;

import static java.lang.Thread.sleep;

public class DamageCalculationTests {

	@Test
	public void testArmor(){
		Log.i("Test", "Testing armor for heroes");
		GameServer server = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(server, Hero.WARRIOR);
		for(int i = 1 ; i <= 3 ; i++){
			Minion minion = GameHelper.getMinionByPos(server, 0);
			minion.setLevel(i * 5);
			minion.generateMinionInformation(0.0f,0.0f,0.0f);
			minion.attack(warrior.getId());
			try {
				sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.i("Test", "Minion done attacking HP [" + warrior.getHp() + "/" + warrior.getMaxHp() + "]");
		}
	}

	@Test
	public void testMR(){
		GameServer server = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(server, Hero.WARRIOR);
		warrior.takeDamage(10.0f, 0.0f, Amount.MAGIC);
		Log.i("Test", "Testing MR[" + warrior.getHp() + "/" + warrior.getMaxHp() + "]");
		warrior.takeDamage(10.0f, 10.0f, Amount.MAGIC);
		Log.i("Test", "Testing MR[" + warrior.getHp() + "/" + warrior.getMaxHp() + "]");
		warrior.takeDamage(10.0f, 30.0f, Amount.MAGIC);
		Log.i("Test", "Testing MR[" + warrior.getHp() + "/" + warrior.getMaxHp() + "]");
		warrior.takeDamage(10.0f, 100.0f, Amount.MAGIC);
		Log.i("Test", "Testing MR[" + warrior.getHp() + "/" + warrior.getMaxHp() + "]");
	}

	@Test
	public void testAttackMinion(){
		GameServer server = GameHelper.createWorld(true, false);
		Warrior warrior = (Warrior) GameHelper.getHeroByClass(server, Hero.WARRIOR);

		Minion minion = GameHelper.getMinionByPos(server, 0);
		HeroUtil.attack(warrior.getId(), minion.getId(), System.currentTimeMillis(), server);
		try {
			sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i("Test", "Done");
	}
}
