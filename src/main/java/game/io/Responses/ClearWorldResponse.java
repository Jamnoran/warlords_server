package game.io.Responses;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class ClearWorldResponse {
	private String response_type = "CLEAR_WORLD";

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	@Override
	public String toString() {
		return "ClearWorldResponse{" +
				"response_type='" + response_type + '\'' +
				'}';
	}
}
