package game;

import game.logging.Log;
import game.vo.Hero;
import game.vo.Minion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

	private static final String TAG = Test.class.getSimpleName();
	private static Hero hero;
	private static GameServer server;
	private static Minion minion;
	private String randomType;

	public static void main(String[] args) {

//		Hero hero = DatabaseUtil.getHero(14);
////
//		Warrior warr = (Warrior) hero;
////
//		warr.generateHeroInformation();
//
//		ArrayList<Ability> heroAbilities = DatabaseUtil.getAllAbilities(hero.getClass_type());
//		ArrayList<AbilityPosition> abilityPositions = DatabaseUtil.getHeroAbilityPositions(hero.getId());
//		for (AbilityPosition abilityPosition : abilityPositions) {
//			for (Ability ability : heroAbilities) {
//				if (abilityPosition.getAbilityId() == ability.getId()) {
//					ability.setPosition(abilityPosition.getPosition());
//				}
//			}
//		}
//
//		Collections.sort(heroAbilities, new Ability());
//
//		warr.setAbilities(heroAbilities);
//
//		ArrayList<Talent> talents = DatabaseUtil.getHeroTalents(hero.getId());
//		warr.setTalents(talents);
//
//		Log.i(TAG, "Got hero with hp: " + warr.getHp());
//
//		Log.i(TAG, "Get abilities: " + warr.getAbilities().toString());
//
//		Log.i(TAG, "Get talents: " + warr.getTalents().toString());
//
//		SpellUtil util = new SpellUtil();
//		SpellRequest spellReq = new SpellRequest();
//		spellReq.setHeroId(hero.getId());
//		spellReq.setSpell_id(1);
//		ArrayList<Integer> targets = new ArrayList<>();
//		targets.add(1);
//		spellReq.setTarget_enemy(targets);
//		GameServer server = new GameServer(null);
//		server.getHeroes().add(warr);
//		Minion min = new Minion(server);
//		min.generateMinionInformation(0.0f,0.0f,0.0f);
//		server.addMinion(min);
//		util.spell(spellReq, server);
//
//		warr.takeDamage(100,0, "PHYSICAL");
//
//		//Log.i(TAG, "Hero hp after damage " + warr.getHp());
//
		//ArrayList<Item> loot = DatabaseUtil.getLoot(warr.getId());

//		DatabaseUtil.updateHeroItem(2, 4, false);

//		Hero hero = new Hero();
//		hero.setLevel(64);

//		ArrayList<Item> loot = GameUtil.generateLoot(warr);
//		Item item = ItemUtil.generateItem(hero.getLevel(), hero);
//		Log.i(TAG, "Item : " + item.toString());
//		Log.i(TAG, "Got this many items: " + loot.size());
//		for (Item item : loot) {
//			Log.i(TAG, "Item : " + item.toString());
//		}

//		for (int i = 0; i < 5; i++) {
//			Log.i(TAG, "Generate name " + GameUtil.generateItemName(Item.CHEST));
//			Log.i(TAG, "Generate name " + GameUtil.generateItemName(Item.MAIN_HAND));
//		}

		//DatabaseUtil.updateHeroItemPosition(2,3);
		//Log.i(TAG, "Hero hp after damage " + warr.getHp());

//		Item item = ItemUtil.generateItem(10, null);
//		Log.i(TAG, "Generate item : " + item.toString());
	}


	static class Sender extends Thread {

		public Sender(){}

		/**
		 * Until interrupted reads messages from the standard input (keyboard) and
		 * sends them to the chat server through the socket.
		 */
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

				while (!isInterrupted()) {
					String message = in.readLine();
					if(message.equals("1")){
						//server.attackHero(18,10,4);
//						minion.addThreat(new Threat(18, 5f, 5f, 0));
//						minion.targetInRangeForAttack = true;
						Log.i(TAG, "Hero armor : " + hero.getArmor());
					}else if (message.equals("2")){
					}
				}
			} catch (IOException ioe) {
				// Communication is broken
			}
		}

	}
}
