package game.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class GameUpdate {
	private String typeOfAction = null;
	private ArrayList<Minion> minions = null;
	private ArrayList<Hero> heros = null;
	
	public String getTypeOfAction() {
		return typeOfAction;
	}
	
	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}
	
	public void setMinions(ArrayList<Minion> minions) {
		this.minions = minions;
	}
	
	public ArrayList<Minion> getMinions() {
		return minions;
	}

	public void addMinion(Minion minion) {
		minions.add(minion);
	}

	public ArrayList<Hero> getHeros() {
		return heros;
	}

	public void setHeros(ArrayList<Hero> heros) {
		this.heros = heros;
	}
	
	public void addChampion(Hero hero) {
		if(heros == null){
			heros = new ArrayList<Hero>();
		}
		heros.add(hero);
	}

	
	
	
	public String getGson(){
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
	}

	public Integer getChampionPosiotion(Integer id) {
		int position = 0;
		for (Hero hero : heros) {
			if(hero.getId() == id){
				return position;
			}
			position++;
		}
		return null;
	}

	public int getNextIdForMinion() {
		int id = 0;
		for (Minion minion : minions) {
			if(minion.getId() >= id){
				id = minion.getId();
			}
		}
		id++;
		return id;
	}
}
