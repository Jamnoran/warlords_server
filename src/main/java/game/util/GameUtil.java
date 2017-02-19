package game.util;

/**
 * Created by Eric on 2017-02-02.
 */
public class GameUtil {
	public static int DUNGEON_CRAWLER = 1;
	public static int GAUNTLET = 2;
	public static int BOSS = 5;

	public static boolean isWorldType(int type, int worldLevel) {
		if(type == worldLevel){
			return true;
		}
		return false;
	}
}
