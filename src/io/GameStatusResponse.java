package io;

import vo.Animation;
import vo.Champion;
import vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameStatusResponse {
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Champion> champions = new ArrayList<>();
	private ArrayList<Animation> animations = new ArrayList<>();

	public GameStatusResponse(ArrayList<Minion> minions, ArrayList<Champion> champions) {
		this.minions = minions;
		this.champions = champions;
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

	public ArrayList<Animation> getAnimations() {
		return animations;
	}

	public void setAnimations(ArrayList<Animation> animations) {
		this.animations = animations;
	}

	@Override
	public String toString() {
		return "GameStatusResponse{" +
				"minions=" + minions +
				", champions=" + champions +
				", animations=" + animations +
				'}';
	}
}
