package io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class JoinServerRequest extends JsonRequest {
	@SerializedName("character_id")
	public String character_id;

	public String getCharacter_id() {
		return character_id;
	}

	public void setCharacter_id(String character_id) {
		this.character_id = character_id;
	}

	@Override
	public String toString() {
		return "JoinServerRequest{" +
				"character_id='" + character_id + '\'' +
				'}';
	}
}
