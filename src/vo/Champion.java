package vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Champion {
	public Integer id = null;
	public Double positionX = 6.0d;
	public Double positionY = 5.0d;
	public Integer championClass = 1; // 1 = Warrior

	public Champion(String character_id) {
		id = Integer.parseInt(character_id);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getPositionX() {
		return positionX;
	}
	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}
	public Double getPositionY() {
		return positionY;
	}
	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}
	public Integer getChampionClass() {
		return championClass;
	}
	public void setChampionClass(Integer championClass) {
		this.championClass = championClass;
	}

}
