package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class MoveRequest extends JsonRequest{

	@SerializedName("position_x")
	private float positionX = 6.0f;
	@SerializedName("position_z")
	private float positionZ = 5.0f;
	@SerializedName("desired_position_x")
	private float desiredPositionX = 6.0f;
	@SerializedName("desired_position_z")
	private float desiredPositionZ = 5.0f;

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

	public float getDesiredPositionX() {
		return desiredPositionX;
	}

	public void setDesiredPositionX(float desiredPositionX) {
		this.desiredPositionX = desiredPositionX;
	}

	public float getDesiredPositionZ() {
		return desiredPositionZ;
	}

	public void setDesiredPositionZ(float desiredPositionZ) {
		this.desiredPositionZ = desiredPositionZ;
	}

	@Override
	public String toString() {
		return "MoveRequest{" +
				"positionX=" + positionX +
				", positionZ=" + positionZ +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionZ=" + desiredPositionZ +
				'}';
	}
}
