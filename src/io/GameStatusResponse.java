package io;

import vo.GameAnimation;
import vo.Champion;
import vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameStatusResponse {
	private String response_type = "GAME_STATUS";
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Champion> champions = new ArrayList<>();
	private ArrayList<GameAnimation> gameAnimations = new ArrayList<>();

	public GameStatusResponse(ArrayList<Minion> minions, ArrayList<Champion> champions) {
		this.minions = minions;
		this.champions = champions;
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

	public ArrayList<Champion> getChampions() {
		return champions;
	}

	public void setChampions(ArrayList<Champion> champions) {
		this.champions = champions;
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
				", champions=" + champions +
				", gameAnimations=" + gameAnimations +
				'}';
	}
}
