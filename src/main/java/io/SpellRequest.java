package io;

import com.google.gson.annotations.SerializedName;
import vo.Vector3;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class SpellRequest extends JsonRequest{

	@SerializedName("spell_id")
	private Integer spell_id;
	@SerializedName("target_enemy")
	private Integer target_enemy;
	@SerializedName("target_friendly")
	private Integer target_friendly;
	@SerializedName("target_position_x")
	private float target_position_x;
	@SerializedName("target_position_z")
	private float target_position_z;

	public Integer getSpell_id() {
		return spell_id;
	}

	public void setSpell_id(Integer spell_id) {
		this.spell_id = spell_id;
	}

	public Integer getTarget_enemy() {
		return target_enemy;
	}

	public void setTarget_enemy(Integer target_enemy) {
		this.target_enemy = target_enemy;
	}

	public Integer getTarget_friendly() {
		return target_friendly;
	}

	public void setTarget_friendly(Integer target_friendly) {
		this.target_friendly = target_friendly;
	}

	public float getTarget_position_x() {
		return target_position_x;
	}

	public void setTarget_position_x(float target_position_x) {
		this.target_position_x = target_position_x;
	}

	public float getTarget_position_z() {
		return target_position_z;
	}

	public void setTarget_position_z(float target_position_z) {
		this.target_position_z = target_position_z;
	}

	@Override
	public String toString() {
		return "SpellRequest{" +
				"spell_id=" + spell_id +
				", target_enemy=" + target_enemy +
				", target_friendly=" + target_friendly +
				", target_position_x=" + target_position_x +
				", target_position_z=" + target_position_z +
				'}';
	}
}
