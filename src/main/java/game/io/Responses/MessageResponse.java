package game.io.Responses;

import game.models.server.Message;

/**
 * Created by Jamnoran on 2017-05-17.
 */
public class MessageResponse {
	private Message message;
	private String response_type = "MESSAGE";

	public MessageResponse(Message message) {
		this.message = message;

	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	@Override
	public String toString() {
		return "MessageResponse{" +
				"message=" + message +
				", response_type='" + response_type + '\'' +
				'}';
	}
}
