package game.vo;

/**
 * Created by Eric on 2017-04-28.
 */
public class Point {
	public static final int SPAWN_POINT = 1;
	public static final int ENEMY_POINT = 2;


	private transient Vector3 location;
	private float posX;
	private float posZ;
	private float posY;
	private int pointType;
	private boolean used = false;

	public Point() {
	}

	public Point(Vector3 loc, int type) {
		location = loc;
		pointType= type;
	}

	public Vector3 getLocation() {
		location = new Vector3(posX, posY, posZ);
		return location;
	}

	public void setLocation(Vector3 location) {
		this.location = location;
	}

	public int getPointType() {
		return pointType;
	}

	public void setPointType(int pointType) {
		this.pointType = pointType;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosZ() {
		return posZ;
	}

	public void setPosZ(float posZ) {
		this.posZ = posZ;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	@Override
	public String toString() {
		return "Point{" +
				"location=" + location +
				", posX=" + posX +
				", posZ=" + posZ +
				", posY=" + posY +
				", pointType=" + pointType +
				", used=" + used +
				'}';
	}
}
