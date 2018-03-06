package game.io.Requests;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;
import game.vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 2017-08-10.
 */
public class UpdateHeroItemPositionRequest extends JsonRequest {

	@SerializedName("hero_id")
	private Integer heroId;
	@SerializedName("item_id")
	private Integer itemId;
	@SerializedName("new_position")
	private Integer newPosition;

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(Integer newPosition) {
		this.newPosition = newPosition;
	}

	@Override
	public String toString() {
		return "UpdateHeroItemPositionRequest{" +
				"heroId=" + heroId +
				", itemId=" + itemId +
				", newPosition=" + newPosition +
				'}';
	}
}
