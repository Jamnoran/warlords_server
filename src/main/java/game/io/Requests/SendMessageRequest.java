package game.io.Requests;

import game.vo.Message;

/**
 * Created by Jamnoran on 2017-05-17.
 */
public class SendMessageRequest {
	private Message message;
	private String request_type = "SEND_MESSAGE";

	public SendMessageRequest(Message message) {
		this.message = message;

	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getRequestType() {
		return request_type;
	}

	public void setRequestType(String request_type) {
		this.request_type = request_type;
	}

	@Override
	public String toString() {
		return "MessageResponse{" +
				"message=" + message +
				", request_type='" + request_type + '\'' +
				'}';
	}
}
