package game.io;

import game.vo.Ability;
import game.vo.GameAnimation;
import game.vo.Hero;
import game.vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class AbilityStatusResponse {
	private String response_type = "GAME_STATUS";
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
