package game.io;

import game.vo.Item;

import java.util.ArrayList;

public class HeroItemsResponse extends JsonRequest {
	public String response_type = "HERO_ITEMS";
	public ArrayList<Item> items = null;

	public HeroItemsResponse() {
	}

	public HeroItemsResponse(ArrayList<Item> items) {
		this.items = items;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "HeroItemsResponse{" +
				"response_type='" + response_type + '\'' +
				", items=" + items +
				'}';
	}
}
