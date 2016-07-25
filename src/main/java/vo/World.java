package vo;

import game.GameServer;
import game.logging.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jamnoran on 08-Jul-16.
 */
public class World {
	private static final String TAG = World.class.getSimpleName();
	private transient GameServer server;
	private int worldLevel = 1;
	private int worldType = 1; // 1 = normal, 2 = time limit, 3 = horde, 4 = tanky? 5 = boss
	private ArrayList<Vector3> spawnPoints = new ArrayList<>();
	private ArrayList<Obstacle> obstacles = new ArrayList<>();
	private transient ArrayList<Room> rooms = new ArrayList<>();
	private transient ArrayList<Obstacle> openDoors = new ArrayList<>();

	public World generate(GameServer gameServer, int x, int z, int lvl) {
		server = gameServer;
		worldLevel = lvl;
		worldType = worldLevel;
		spawnPoints.add(new Vector3(1.0f, 1.0f, 1.0f));
		spawnPoints.add(new Vector3(3.0f, 1.0f, 1.0f));
		spawnPoints.add(new Vector3(5.0f, 1.0f, 1.0f));
		// 0 = top, 1 = left, 2 = right, 3 = bottom
		ArrayList<Integer> doorList = new ArrayList();
		int roomSizeX = 10;
		int roomSizeZ = 10;

		obstacles.add(new Obstacle(roomSizeX / 2, 0, roomSizeZ / 2, 0, Obstacle.START, null));

		Room startRoom = new Room();
		obstacles.addAll(startRoom.generateObstacles(this, roomSizeX, roomSizeZ, doorList, true, 1.0f,1.0f));


		Log.i(TAG, "Doors open left : " + openDoors.size());
		boolean gotRoomsLeft = true;
		if (openDoors.size() > 0){
			while(gotRoomsLeft){
				doorList.clear();
				if (openDoors.get(0).getRotation() == 0){
					// Correct
					doorList.add(Room.TOP);
				}else if (openDoors.get(0).getRotation() == 90){
					// This is correct
					doorList.add(Room.BOTTOM);
				}else if (openDoors.get(0).getRotation() == 180){
					doorList.add(Room.RIGHT);
				}else if (openDoors.get(0).getRotation() == 270){
					doorList.add(Room.LEFT);
				}
				Room room = new Room();
				boolean successfullyAddedRoom = true;
				if(room.generateObstacles(this, 10, 10, doorList, false, openDoors.get(0).getPositionX(),openDoors.get(0).getPositionZ()) == null){
					successfullyAddedRoom = false;
				}else {
					rooms.add(room);
				}

				Log.i(TAG, "Creating room at position : " + openDoors.get(0));
				openDoors.remove(0);
				if(openDoors.size() == 0){
					if (successfullyAddedRoom) {
						obstacles.addAll(room.addStairs());
					}else{
						obstacles.addAll(rooms.get(rooms.size() - 1).addStairs());
					}
					gotRoomsLeft = false;
				}
			}
		}

//		for (int i = 0; i < 2 ; i++){
//			Room room = new Room();
//			room.generate(obstacles, new Vector3(1.0f, 1.0f, 1.0f), 90, Room.INDOOR);
//		}

		// Maybe clear rooms list to save memory?
		return this;
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

//	public int getSizeX() {
//		return sizeX;
//	}
//
//	public void setSizeX(int sizeX) {
//		this.sizeX = sizeX;
//	}
//
//	public int getSizeZ() {
//		return sizeZ;
//	}
//
//	public void setSizeZ(int sizeZ) {
//		this.sizeZ = sizeZ;
//	}

	public ArrayList<Vector3> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(ArrayList<Vector3> spawnPoints) {
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

	public boolean locationOccupiedByDoor(Vector3 location) {
		for (Obstacle obstacle : obstacles){
			if(obstacle.getPositionX() == location.getX() && obstacle.getPositionZ() == location.getZ()){
				if(obstacle.getType() == Obstacle.DOOR){
					return true;
				}
			}
		}
		return false;
	}

	public void removeObjectOnPosition(float x, float y, float z) {
		Log.i(TAG, "Removing object at position " + x + " x " + z);
		Iterator<Obstacle> ut = obstacles.iterator();
		while (ut.hasNext()) {
			Obstacle obstacle= ut.next();
			if (obstacle.getPositionX() == x && obstacle.getPositionZ() == z) {
				ut.remove();
			}
		}
	}

	public Obstacle getObstacleOnPosition(float x, float y, float z) {
		for(Obstacle obstacle : obstacles){
			if (obstacle.getPositionX() == x && obstacle.getPositionZ() == z) {
				return obstacle;
			}
		}
		return null;
	}

	public boolean hasObstacleOnPosition(float x, float y, float z) {
		Obstacle obstacle = getObstacleOnPosition(x,y,z);
		if(obstacle != null){
			return true;
		}
		return false;
	}
}
