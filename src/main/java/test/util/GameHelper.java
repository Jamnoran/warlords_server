package test.util;

import game.GameServer;
import game.models.heroes.Hero;
import game.models.enemies.Minion;
import game.models.heroes.priest.Priest;
import game.models.heroes.warrior.Warrior;

public class GameHelper {

	public static GameServer createWorld(boolean createWarrior, boolean createPriest){
		GameServer gameServer = new GameServer(null);

		Priest priest;
		if (createPriest) {
			priest = HeroHelper.getPriest();

			gameServer.getHeroes().add(priest);
		}
		Warrior warrior;
		if (createWarrior) {
			warrior = HeroHelper.getWarrior();
			gameServer.getHeroes().add(warrior);
		}

		Minion minion = MinionHelper.getMinion(gameServer);
		gameServer.getMinions().add(minion);

		return gameServer;
	}

	public static Hero getHeroByClass(GameServer server, String classType) {
		for (Hero hero : server.getHeroes()){
			if(hero.getClass_type().equals(classType)){
				return hero;
			}
		}
		return null;
	}

	public static Minion getMinionByPos(GameServer server, int pos) {
		return server.getMinions().get(pos);
	}
}
