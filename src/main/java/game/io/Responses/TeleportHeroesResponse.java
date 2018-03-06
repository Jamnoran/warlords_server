package game.io.Responses;

import game.vo.Hero;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Oct-16.
 */
public class TeleportHeroesResponse {

	private String response_type = "TELEPORT_HEROES";
	public ArrayList<Hero> heroes;

	public TeleportHeroesResponse(ArrayList<Hero> heroes) {
		this.heroes = heroes;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}

	public void setHeroes(ArrayList<Hero> heroes) {
		this.heroes = heroes;
	}

	@Override
	public String toString() {
		return "TeleportHeroesResponse{" +
				"heroes=" + heroes +
				'}';
	}
}
