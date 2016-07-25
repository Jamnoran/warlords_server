package vo;

/**
 * Created by Jamnoran on 08-Jul-16.
 */
public class Obstacle {

	public static final int INWARDS = 11;
	public static final int FOLLOW = 12;
	public static final int OUTWARDS = 13;

	public static final int WALL = 1;
	public static final int DOOR = 2;
	public static final int START = 3;
	public static final int STAIRS = 4;
	public static final int LIGHT = 5;

	private transient boolean doorEmptyOtherSide;
	private float positionX;
	private float positionY;
	private float positionZ;
	private float rotation;
	private int type;
	private transient Integer changedRotation = 0;

	public Obstacle(float positionX, float positionY, float positionZ, float rotation, int type, Integer changedRotation) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.positionZ = positionZ;
		this.rotation = rotation;
		this.type = type;
		this.changedRotation = changedRotation;
	}

	public Obstacle(float positionX, float positionY, float positionZ, float rotation, int type, boolean doorEmptyOtherSide) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.positionZ = positionZ;
		this.rotation = rotation;
		this.type = type;
		this.doorEmptyOtherSide = doorEmptyOtherSide;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isDoorEmptyOtherSide() {
		return doorEmptyOtherSide;
	}

	public void setDoorEmptyOtherSide(boolean doorEmptyOtherSide) {
		this.doorEmptyOtherSide = doorEmptyOtherSide;
	}

	public int getChangedRotation() {
		return changedRotation;
	}

	public void setChangedRotation(int changedRotation) {
		this.changedRotation = changedRotation;
	}

	@Override
	public String toString() {
		return "Obstacle{" +
				"doorEmptyOtherSide=" + doorEmptyOtherSide +
				", positionX=" + positionX +
				", positionY=" + positionY +
				", positionZ=" + positionZ +
				", rotation=" + rotation +
				", changedRotation=" + changedRotation +
				", type=" + type +
				'}';
	}
}
