package vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class GameUpdate {
	private String typeOfAction = null;
	private ArrayList<Minion> minions = null;
	private ArrayList<Champion> champions = null;
	
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

	public ArrayList<Champion> getChampions() {
		return champions;
	}

	public void setChampions(ArrayList<Champion> champions) {
		this.champions = champions;
	}
	
	public void addChampion(Champion champion) {
		if(champions == null){
			champions = new ArrayList<Champion>();
		}
		champions.add(champion);
	}

	
	
	
	public String getGson(){
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
	}

	public Integer getChampionPosiotion(Integer id) {
		int position = 0;
		for (Champion champion : champions) {
			if(champion.getId() == id){
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
