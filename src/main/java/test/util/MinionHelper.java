package test.util;

import game.GameServer;
import game.vo.Minion;

public class MinionHelper {

	public static Minion getMinion(GameServer server ) {
		Minion minion = new Minion(server);
		minion.generateMinionInformation(0f,0f,0f);
		return minion;
	}
}
