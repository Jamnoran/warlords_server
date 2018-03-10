package game.io.Requests;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;
import game.vo.Point;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class SpawnPointsRequest extends JsonRequest {
	@SerializedName("hero_id")
	public Integer heroId;
	@SerializedName("points")
	public ArrayList<Point> points;


	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "SpawnPointsRequest{" +
				"heroId=" + heroId +
				", points=" + points +
				'}';
	}
}
