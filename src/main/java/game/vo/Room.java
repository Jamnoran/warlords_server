package game.vo;

import game.logging.Log;
import game.util.CalculationUtil;

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
	public int doorGenerationValue = 20;


	private boolean getValue(int i, boolean startRoom, ArrayList<Integer> doorPositions, int percentage) {
		if (doorPositions != null) {
			if (doorPositions.contains(i)){
				return true;
			}
		}
		if(startRoom){
			return true;
		}
		if(CalculationUtil.rollDice(percentage)){
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
