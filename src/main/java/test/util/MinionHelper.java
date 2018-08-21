package test.util;

import game.GameServer;
import game.util.DatabaseUtil;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Warrior;

import java.util.ArrayList;
import java.util.Collections;

public class MinionHelper {

	public static Minion getMinion(GameServer server ) {
		Minion minion = new Minion(server);
		minion.generateMinionInformation(0f,0f,0f);
		return minion;
	}
}
