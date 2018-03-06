package game.io.Responses;

import game.vo.Ability;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 10-Nov-16.
 */
public class AbilitiesResponse{
	private String response_type = "ABILITIES";
	private ArrayList<Ability> abilities;

	public AbilitiesResponse(ArrayList<Ability> allAbilities) {
		this.abilities = allAbilities;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}

	@Override
	public String toString() {
		return "AbilitiesResponse{" +
				"response_type='" + response_type + '\'' +
				", abilities=" + abilities +
				'}';
	}
}
