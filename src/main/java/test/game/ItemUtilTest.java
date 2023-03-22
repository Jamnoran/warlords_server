package test.game;

import game.logging.Log;
import game.logic.math.CalculationUtil;
import game.util.DatabaseUtil;
import game.util.GameUtil;
import game.models.heroes.Hero;
import game.models.items.Item;
import game.models.heroes.warrior.Warrior;
import org.junit.Test;
import test.util.HeroHelper;

import java.util.ArrayList;

public class ItemUtilTest {

	private static final String TAG = ItemUtilTest.class.getSimpleName();

	@Test
	public void lootEnemiesUntilGetItem() {
		Warrior hero = HeroHelper.getWarrior();

		ArrayList<Item> loot = lootRoll(hero);
		int timesLooted = 0;
		while (loot.size() == 0) {
			loot = lootRoll(hero);
			timesLooted++;
		}
		Log.i(TAG, "Got this many items: " + loot.size() + " After times looted enemy : " + timesLooted);
		for (Item item : loot) {
			Log.i(TAG, "Item : " + item.toString());
		}
	}

	private ArrayList<Item> lootRoll(Hero hero){
		return GameUtil.generateLoot(hero);
	}

	@Test
	public void equipItem(){
		Warrior warrior = HeroHelper.getWarrior();
		int itemPosToEquip = CalculationUtil.getRandomInt(0, (warrior.getItems().size() - 1));
//		Log.i(TAG, "ItemPos to equip "+ itemPosToEquip + " from size " + warrior.getItems().size());
		Item equipItem = warrior.getItems().get(itemPosToEquip);
		for (Item item : warrior.getItems()) {
//			Log.i(TAG, "Item warrior has [" + item.getName() + "] Position [" + item.getPosition() + "] Equipped [" + item.isEquipped() + "]");
			if(item.getPosition().equals(equipItem.getPosition())){
				item.setEquipped(false);
				item.setPositionId(CalculationUtil.getRandomInt(0,32));
				DatabaseUtil.updateHeroItem(item.getId(), item.getPositionId(), item.isEquipped());
			}
		}
		equipItem.setEquipped(true);
		equipItem.setPositionId(-1);
		DatabaseUtil.updateHeroItem(equipItem.getId(), equipItem.getPositionId(), equipItem.isEquipped());

		getItemsHeroHasEquipped();
	}


	@Test
	public void getItemsHeroHasEquipped(){
		Warrior warrior = HeroHelper.getWarrior();
		boolean hasItemsEquipped = false;
		for (Item item : warrior.getItems()) {
			if (item.isEquipped()) {
				Log.i(TAG, "Item warrior has [" + item.getName() + "] Position [" + item.getPosition() + "] Equipped [" + item.isEquipped() + "]");
				hasItemsEquipped = true;
			}
		}
		if(!hasItemsEquipped){
			Log.i(TAG, "Hero had no items equipped");
		}
	}
	@Test
	public void getItemsHeroHas(){
		Warrior warrior = HeroHelper.getWarrior();
		for (Item item : warrior.getItems()) {
			Log.i(TAG, "Item warrior has [" + item.getName() + "] Position [" + item.getPosition() + "] Equipped [" + item.isEquipped() + "]" + " Extra stats " + item.getStats().toString());
		}
	}
}