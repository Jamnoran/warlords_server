package game.vo;

/**
 * Created by Jamnoran on 08-Jul-16.
 */
public class Vector3 {
	private float x;
	private float y;
	private float z;

	public Vector3(float v, float v1, float v2) {
		x = v;
		y = v1;
		z = v2;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Vector3{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
