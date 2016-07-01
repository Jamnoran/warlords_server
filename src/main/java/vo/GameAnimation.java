package vo;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameAnimation {
	private String animation_type;
	private Integer target_id;
	private Integer source_id;

	public GameAnimation() {
	}

	public GameAnimation(String animation_type, Integer target_id, Integer source_id) {
		this.animation_type = animation_type;
		this.target_id = target_id;
		this.source_id = source_id;
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

	@Override
	public String toString() {
		return "GameAnimation{" +
				"animation_type='" + animation_type + '\'' +
				", target_id=" + target_id +
				", source_id=" + source_id +
				'}';
	}
}
