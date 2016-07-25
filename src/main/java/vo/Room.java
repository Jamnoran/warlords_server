package vo;

import game.logging.Log;
import util.CalculationUtil;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 11-Jul-16.
 */
public class Room {
//	public static final int INDOOR = 1;
//	public static final int OUTDOOR = 1;


	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;

	private static final String TAG = Room.class.getSimpleName();
	private ArrayList<Obstacle> roomObstacles = new ArrayList<>();
	boolean canGenerateMoreObstacles = true;
	private Obstacle startPosition;
	int temporaryObstacleCount = 0;
	private Integer roomSizeX;
	private Integer roomSizeZ;
	private float xStart;
	private float zStart;


	public ArrayList<Obstacle> generateObstacles(World world, int sizeX, int sizeZ, ArrayList<Integer> doorPositions, boolean startRoom, float xStartPosition, float zStartPosition) {
		float wallHeight = 1.f;
		roomSizeX = sizeX;
		roomSizeZ = sizeZ;
		xStart = xStartPosition;
		zStart = zStartPosition;
		ArrayList<Obstacle> obs = new ArrayList<>();

		// 0 = top, 1 = left, 2 = right, 3 = bottom

		if (doorPositions.contains(TOP) && !startRoom){
			Log.i(TAG, "Top");
			xStart = xStart - (sizeX / 2);
		}else if (doorPositions.contains(LEFT) && !startRoom){
			Log.i(TAG, "left");
			zStart = zStart - (sizeZ / 2);
		}else if (doorPositions.contains(RIGHT) && !startRoom){
			Log.i(TAG, "right");
			xStart = xStart - sizeX;
			zStart = zStart - (sizeZ / 2);
		}else if (doorPositions.contains(BOTTOM) && !startRoom){
			Log.i(TAG, "bottom");
			zStart = zStart - sizeZ;
			xStart = xStart - (sizeX / 2);
		}

		// Check if we should abort this room (if there already is a room here)
		// Check for the door positions
		if(world.hasObstacleOnPosition(xStart + (sizeX / 2), wallHeight , zStart)){
			Log.i(TAG, "Obstacle on one side");
			if(world.hasObstacleOnPosition(xStart, wallHeight , (sizeZ / 2) + zStart)){
				Log.i(TAG, "Obstacle on two sides");
				if(world.hasObstacleOnPosition(sizeX + xStart, wallHeight, (sizeZ / 2) + zStart)){
					Log.i(TAG, "Obstacle on three sides");
					if(world.hasObstacleOnPosition(xStart + (sizeX / 2), wallHeight , zStart + sizeZ)){
						Log.i(TAG, "There is already a room here break it off!");
						return null;
					}
				}
			}
		}


		// Top wall
		boolean shouldGenerateDoor = getValue(TOP, startRoom, doorPositions);
		for (int i = 0 ; i <= sizeX ; i++){
			if(shouldGenerateDoor && ((i == sizeX / 2 || i == (sizeX / 2) - 1) || i == (sizeX / 2) + 1)){
				if (!doorPositions.contains(TOP)){
					if(i == sizeX / 2){
						world.getOpenDoors().add(new Obstacle(i + xStart, wallHeight , zStart, 90, Obstacle.DOOR, null));
					}
				}
//				world.removeObjectOnPosition(i + xStart, wallHeight , zStart);
				obs.add(new Obstacle(i + xStart, wallHeight , zStart, 90, Obstacle.DOOR, null));
			}else{
				obs.add(new Obstacle(i + xStart, wallHeight , zStart, 90, Obstacle.WALL, null));
			}
		}
		// Left wall
		shouldGenerateDoor = getValue(LEFT, startRoom, doorPositions);
		for (int i = 1 ; i <= sizeZ ; i++){
			if(shouldGenerateDoor && ((i == sizeZ / 2 || i == (sizeZ / 2) - 1) || i == (sizeZ / 2) + 1)){
				if (!doorPositions.contains(LEFT)){
					if (i == sizeZ / 2) {
						world.getOpenDoors().add(new Obstacle(xStart, wallHeight , i + zStart, 180, Obstacle.DOOR, null));
					}
				}
//				world.removeObjectOnPosition(xStart, wallHeight , i + zStart);
				obs.add(new Obstacle(xStart, wallHeight , i + zStart, 180, Obstacle.DOOR, null));
			}else{
				obs.add(new Obstacle(xStart, wallHeight , i + zStart, 180, Obstacle.WALL, null));
			}

		}
		// Right wall
		shouldGenerateDoor = getValue(RIGHT, startRoom, doorPositions);
		for (int i = 1 ; i <= sizeZ ; i++){
			if(shouldGenerateDoor && ((i == sizeZ / 2 || i == (sizeZ / 2) - 1) || i == (sizeZ / 2) + 1)){
				if (!doorPositions.contains(RIGHT)){
					if (i == sizeZ / 2) {
						world.getOpenDoors().add(new Obstacle(sizeX + xStart, wallHeight, i + zStart, 270, Obstacle.DOOR, null));
					}
				}
//				world.removeObjectOnPosition(sizeX + xStart, wallHeight, i + zStart);
				obs.add(new Obstacle(sizeX + xStart, wallHeight, i + zStart, 270, Obstacle.DOOR, null));
			}else {
				obs.add(new Obstacle(sizeX + xStart, wallHeight, i + zStart, 270, Obstacle.WALL, null));
			}
		}
		// Bottom wall
		shouldGenerateDoor = getValue(BOTTOM, startRoom, doorPositions);
		for (int i = 1 ; i < sizeX ; i++){
			if(shouldGenerateDoor && ((i == sizeX / 2 || i == (sizeX / 2) - 1) || i == (sizeX / 2) + 1)){
				if (!doorPositions.contains(BOTTOM)){
					if (i == sizeX / 2) {
						world.getOpenDoors().add(new Obstacle(i + xStart, wallHeight , zStart + sizeZ, 0, Obstacle.DOOR, null));
					}
				}
//				world.removeObjectOnPosition(i + xStart, wallHeight , zStart + sizeZ);
				obs.add(new Obstacle(i + xStart, wallHeight , zStart + sizeZ, 0, Obstacle.DOOR, null));
			}else {

				if (!world.locationOccupiedByDoor(new Vector3(i + xStart, wallHeight , zStart + sizeZ))) {
					obs.add(new Obstacle(i + xStart, wallHeight , zStart + sizeZ, 90, Obstacle.WALL, null));
				}
			}
		}

		// Add light
		obs.add(new Obstacle(xStart + (5), 0, zStart + (5), 0, Obstacle.LIGHT, null));

		// Generate some monsters
		//if(!startRoom && world.getServer().getMinionCount() == 0) {
		if(!startRoom){
			world.getServer().spawnMinion(xStart + CalculationUtil.getRandomInt(3, sizeX -3) , zStart + CalculationUtil.getRandomInt(3, sizeZ -3));
		}

		world.getObstacles().addAll(obs);
		return obs;
	}

	private boolean getValue(int i, boolean startRoom, ArrayList<Integer> doorPositions) {
		if (doorPositions.contains(i)){
			return true;
		}
		if(startRoom){
			return true;
		}
		if(CalculationUtil.rollDice(20)){
			return true;
		}
		return false;
	}



	public ArrayList<Obstacle> addStairs() {
		ArrayList<Obstacle> obs = new ArrayList<>();
		// If last room then create stairs down to next level
		obs.add(new Obstacle(xStart + (5), 0, zStart + (5), 0, Obstacle.STAIRS, null));
		return obs;
	}

























	// Auto generation, not complete
	public ArrayList<Obstacle> generate(ArrayList<Obstacle> obstacles, Vector3 startPosition, int roomOrientation, int typeOfRoom) {
		canGenerateMoreObstacles = true;
		Obstacle lastObstacle = new Obstacle(startPosition.getX(), 3 , startPosition.getZ(), roomOrientation, Obstacle.DOOR, true);
		this.startPosition = lastObstacle;
		this.roomObstacles.add(lastObstacle);
		while(canGenerateMoreObstacles){
			lastObstacle = generateNextObstacle(lastObstacle);
			this.roomObstacles.add(lastObstacle);
		}

		obstacles.addAll(this.roomObstacles);
//		for (Obstacle obstacle: this.roomObstacles) {
			//Log.i(TAG, "Obstacle : " + Math.round(obstacle.getPosition_x()) + "x" + Math.round(obstacle.getPosition_z()) + " ori:" + obstacle.getRotation());
//			obstacles.add(obstacle);
//		}

		// Return this rooms obstacles if something needs to be done with it
		return this.roomObstacles;
	}

	public ArrayList<Obstacle> getObstacles() {
		return roomObstacles;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.roomObstacles = obstacles;
	}

	private Obstacle generateNextObstacle(Obstacle lastObstacle) {
		// Steps:

		// The closer we get to startPosition the higher chance we should have to actually connect to it.
		// Get parameters for generation
		// Chance for obstacle to follow same path
		int cFollow = 80;
		// Chance for obstacle to go inwards in to room
		int cInwards = 4;

		// Chance for obstacle to go outwards and expand room
		int cOutwards = 2;
		// Chance for obstacle to be a door
		int cDoor = 0;

		// Rules:
		// Obstacle cant change direction when there is less than X obstacles between them
		for(int i = 1; i < 3 ; i++){
			if ((roomObstacles.size() - i) > 0) {
				Obstacle checkIfChangedRotation = roomObstacles.get(roomObstacles.size() - i);
				if (checkIfChangedRotation != null) {
					if(checkIfChangedRotation.getChangedRotation() == Obstacle.INWARDS){
						cInwards = 0;
						cOutwards = 0;
					} else if(checkIfChangedRotation.getChangedRotation() == Obstacle.OUTWARDS){
						cInwards = 0;
						cOutwards = 0;
					}
				}
			}
		}
		// Obstacle cant go to direction that's already occupied

		// Obstacle should have higher and higher chance of going to startPoint the more obstacle placed and closer it gets to it


		int totalChance = cFollow + cInwards + cOutwards + cDoor;
		int random = CalculationUtil.getRandomInt(0, totalChance);
		float posX = lastObstacle.getPositionX();
		float posZ = lastObstacle.getPositionZ();
		float orientation = lastObstacle.getRotation();

		Integer changedRotation = null;
		if(random <= cFollow){
			Log.i(TAG, "Obstacle follow  " + Math.round(lastObstacle.getRotation()));
			if (Math.round(lastObstacle.getRotation()) == 90) {
				posZ = lastObstacle.getPositionZ() + 1.0f;
			} else if(Math.round(lastObstacle.getRotation()) == 270){
				posZ = lastObstacle.getPositionZ() - 1.0f;
			}else if (Math.round(lastObstacle.getRotation()) == 0) {
				posX = lastObstacle.getPositionX() - 1.0f;
			} else if(Math.round(lastObstacle.getRotation()) == 180){
				posX = lastObstacle.getPositionX() + 1.0f;
			}
			changedRotation = Obstacle.FOLLOW;
		}else if (random <= cInwards + cFollow){
			Log.i(TAG, "Obstacle inwards");
			if (Math.round(lastObstacle.getRotation()) == 90) {
				posX = lastObstacle.getPositionX() + 1.0f;
			} else if(Math.round(lastObstacle.getRotation()) == 270){
				posX = lastObstacle.getPositionX() - 1.0f;
			}else if (Math.round(lastObstacle.getRotation()) == 0) {
				posZ = lastObstacle.getPositionZ() + 1.0f;
			} else if(Math.round(lastObstacle.getRotation()) == 180){
				posZ = lastObstacle.getPositionZ() - 1.0f;
			}
			orientation = orientation + 90.0f;
			changedRotation = Obstacle.INWARDS;
		}else if (random <= cOutwards + cInwards + cFollow){
			Log.i(TAG, "Obstacle outwards");
			if (Math.round(lastObstacle.getRotation()) == 90) {
				posX = lastObstacle.getPositionX() - 1.0f;
			} else if(Math.round(lastObstacle.getRotation()) == 270){
				posX = lastObstacle.getPositionX() + 1.0f;
			}
			if (Math.round(lastObstacle.getRotation()) == 0) {
				posZ = lastObstacle.getPositionZ() - 1.0f;
			} else if(Math.round(lastObstacle.getRotation()) == 180){
				posZ = lastObstacle.getPositionZ() + 1.0f;
			}
			orientation = orientation - 90.0f;
			changedRotation = Obstacle.OUTWARDS;
		}else if (random <= cDoor + cOutwards + cInwards + cFollow){
			Log.i(TAG, "Obstacle door");
		}
		if (Math.round(orientation) == -90){
			orientation = 270.0f;
		}else if(Math.round(orientation) == 360){
			orientation = 0.0f;
		}

		if (temporaryObstacleCount == 50) {
			// Check if we are connected to startPosition
			canGenerateMoreObstacles = false;
		} else {
			temporaryObstacleCount++;
		}

		Obstacle obstacle = new Obstacle(posX, 3 , posZ, orientation, Obstacle.WALL, changedRotation);
		//Log.i(TAG, "Last Obstacle : " + Math.round(lastObstacle.getPosition_x()) + "x" + Math.round(lastObstacle.getPosition_z()) + " ori:" + lastObstacle.getRotation());
		Log.i(TAG, "New Obstacle : " + Math.round(obstacle.getPositionX()) + "x" + Math.round(obstacle.getPositionZ()) + " ori:" + obstacle.getRotation());
		// Return false when cant generate no more obstacles
		return obstacle;
	}

}
