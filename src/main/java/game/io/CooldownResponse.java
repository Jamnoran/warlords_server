package game.io;

import game.vo.Ability;

/**
 * Created by Jamnoran on 15-Nov-16.
 */
public class CooldownResponse {
	private String response_type = "COOLDOWN";
	private Ability ability;

	public CooldownResponse(Ability abi) {
		ability = abi;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}

	@Override
	public String toString() {
		return "CooldownResponse{" +
				"response_type='" + response_type + '\'' +
				", ability=" + ability +
				'}';
	}
}
