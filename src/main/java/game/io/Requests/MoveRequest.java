package game.io.Requests;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class MoveRequest extends JsonRequest {

	@SerializedName("hero_id")
	private Integer heroId;
	@SerializedName("position_x")
	private float positionX = 6.0f;
	@SerializedName("position_y")
	private float positionY = 6.0f;
	@SerializedName("position_z")
	private float positionZ = 5.0f;
	@SerializedName("desired_position_x")
	private float desiredPositionX = 6.0f;
	@SerializedName("desired_position_y")
	private float desiredPositionY = 6.0f;
	@SerializedName("desired_position_z")
	private float desiredPositionZ = 5.0f;

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
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

	public float getDesiredPositionX() {
		return desiredPositionX;
	}

	public void setDesiredPositionX(float desiredPositionX) {
		this.desiredPositionX = desiredPositionX;
	}

	public float getDesiredPositionY() {
		return desiredPositionY;
	}

	public void setDesiredPositionY(float desiredPositionY) {
		this.desiredPositionY = desiredPositionY;
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
				"heroId=" + heroId +
				", positionX=" + positionX +
				", positionY=" + positionY +
				", positionZ=" + positionZ +
				", desiredPositionX=" + desiredPositionX +
				", desiredPositionY=" + desiredPositionY +
				", desiredPositionZ=" + desiredPositionZ +
				'}';
	}
}
