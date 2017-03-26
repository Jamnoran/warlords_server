package game.io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eric on 2017-03-10.
 */
public class RegisterGameServerResponse extends JsonResponse {
	@SerializedName("id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RegisterLobbyResponse{" +
				", id='" + id + '\'' +
				'}';
	}
}
