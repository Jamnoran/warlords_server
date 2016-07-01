package io;

import vo.GameAnimation;
import vo.Hero;
import vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameStatusResponse {
	private String response_type = "GAME_STATUS";
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> gameAnimations = new ArrayList<>();

	public GameStatusResponse(ArrayList<Minion> minions, ArrayList<Hero> heroes, ArrayList<GameAnimation> gameAnimations) {
		this.minions = minions;
		this.heroes = heroes;
		this.gameAnimations = gameAnimations;
	}


	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public ArrayList<Minion> getMinions() {
		return minions;
	}

	public void setMinions(ArrayList<Minion> minions) {
		this.minions = minions;
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}

	public void setHeroes(ArrayList<Hero> heroes) {
		this.heroes = heroes;
	}

	public ArrayList<GameAnimation> getGameAnimations() {
		return gameAnimations;
	}

	public void setGameAnimations(ArrayList<GameAnimation> gameAnimations) {
		this.gameAnimations = gameAnimations;
	}

	@Override
	public String toString() {
		return "GameStatusResponse{" +
				"minions=" + minions +
				", heroes=" + heroes +
				", gameAnimations=" + gameAnimations +
				'}';
	}
}
