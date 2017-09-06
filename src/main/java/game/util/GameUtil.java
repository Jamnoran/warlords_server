package game.util;

/**
 * Created by Eric on 2017-02-02.
 */
public class GameUtil {
	public static int DUNGEON_CRAWLER = 1;
	public static int HORDE = 2;
	public static int GAUNTLET = 3;
	public static int BOSS = 0;

	public static boolean isWorldType(int type, int worldLevel) {
		if(type == worldLevel){
			return true;
		}
		return false;
	}

	public static int getWorldTypeFromLevel(int worldLevel) {
		int worldType = worldLevel % 5;
		if(worldType == 3 || worldType == 4 || worldType == 0 ){
			worldType = DUNGEON_CRAWLER;
		}
		return worldType;
	}
}
