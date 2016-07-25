package vo;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameAnimation {
	private String animation_type;
	private Integer target_id;
	private Integer source_id;
	private float position_x = 0.0f;
	private float position_y = 0.0f;
	private float position_z = 0.0f;

	public GameAnimation() {
	}

	public GameAnimation(String animation_type, Integer target_id, Integer source_id, Vector3 position) {
		this.animation_type = animation_type;
		this.target_id = target_id;
		this.source_id = source_id;
		if (position != null) {
			this.position_x = position.getX();
			this.position_y = position.getY();
			this.position_z = position.getZ();
		}
	}

	public String getAnimation_type() {
		return animation_type;
	}

	public void setAnimation_type(String animation_type) {
		this.animation_type = animation_type;
	}

	public Integer getTarget_id() {
		return target_id;
	}

	public void setTarget_id(Integer target_id) {
		this.target_id = target_id;
	}

	public Integer getSource_id() {
		return source_id;
	}

	public void setSource_id(Integer source_id) {
		this.source_id = source_id;
	}

	public float getPosition_x() {
		return position_x;
	}

	public void setPosition_x(float position_x) {
		this.position_x = position_x;
	}

	public float getPosition_y() {
		return position_y;
	}

	public void setPosition_y(float position_y) {
		this.position_y = position_y;
	}

	public float getPosition_z() {
		return position_z;
	}

	public void setPosition_z(float position_z) {
		this.position_z = position_z;
	}

	@Override
	public String toString() {
		return "GameAnimation{" +
				"animation_type='" + animation_type + '\'' +
				", target_id=" + target_id +
				", source_id=" + source_id +
				", position_x=" + position_x +
				", position_y=" + position_y +
				", position_z=" + position_z +
				'}';
	}
}
