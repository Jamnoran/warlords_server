package game.io.Responses;

import game.models.abilities.Ability;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class AbilityStatusResponse {
	private String response_type = "ABILITY_STATUS";
	private Ability ability = new Ability();

	public AbilityStatusResponse(Ability abi) {
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
		return "AbilityStatusResponse{" +
				"response_type='" + response_type + '\'' +
				", ability=" + ability +
				'}';
	}
}
