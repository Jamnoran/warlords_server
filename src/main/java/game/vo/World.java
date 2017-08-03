package game.vo;

import game.GameServer;
import game.logging.Log;
import game.util.GameUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Jamnoran on 08-Jul-16.
 */
public class World {
	private static final String TAG = World.class.getSimpleName();
	private transient GameServer server;
	private int worldLevel = 1;
	private int worldType = 1; // 1 = normal, 2 = horde, 3 = gauntlet, 4 = tanky? 5 = boss
	private ArrayList<Point> spawnPoints = new ArrayList<>();
	private int seed = 0;


	private transient ArrayList<Obstacle> obstacles = new ArrayList<>();
	private transient ArrayList<Obstacle> openDoors = new ArrayList<>();
	private transient int roomSizeX = 10;
	private transient int roomSizeZ = 10;

	public World generate(GameServer gameServer, int x, int z, int lvl) {
		server = gameServer;
		worldLevel = lvl;
		worldType = worldLevel;

		Random rand = new Random(System.currentTimeMillis());
		seed = rand.nextInt();
		if(seed < 0){
			seed = seed * -1;
		}
		Log.i(TAG, "Seed is set to " + seed);

		return this;
	}

	public void addSpawPoint(Point location){
		spawnPoints.add(location);
	}

	public void addEnemySpawnPoint(Vector3 location){
		spawnPoints.add(new Point(location, Point.ENEMY_POINT));
	}

	public Point getSpawnPoint(Vector3 location){
		for(Point point: spawnPoints){
			if(point.getLocation().getX() == location.getX() && point.getLocation().getZ() == location.getZ()){
				return point;
			}
		}
		return null;
	}



	public GameServer getServer() {
		return server;
	}

	public void setServer(GameServer server) {
		this.server = server;
	}

	public int getWorldType() {
		return worldType;
	}

	public void setWorldType(int worldType) {
		this.worldType = worldType;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public int getWorldLevel() {
		return worldLevel;
	}

	public void setWorldLevel(int worldLevel) {
		this.worldLevel = worldLevel;
	}

	public ArrayList<Point> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(ArrayList<Point> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}

	@Override
	public String toString() {
		return "World{" +
				"worldLevel=" + worldLevel +
//				", sizeX=" + sizeX +
//				", sizeZ=" + sizeZ +
				", worldType=" + worldType +
				", spawnPoints=" + spawnPoints +
				", obstacles=" + obstacles +
				'}';
	}

	public ArrayList<Obstacle> getOpenDoors() {
		return openDoors;
	}

}
