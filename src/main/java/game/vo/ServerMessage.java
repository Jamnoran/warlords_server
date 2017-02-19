package game.vo;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class ServerMessage {
	private Integer recipient = null;
	private String message = null;

	public ServerMessage() {
		// TODO Auto-generated constructor stub
	}
	public ServerMessage(String message) {
		this.message = message;
	}
	public ServerMessage(Integer recipient, String message) {
		this.message = message;
		this.recipient = recipient;
	}

	public Integer getRecipient() {
		return recipient;
	}
	public void setRecipient(Integer recipient) {
		this.recipient = recipient;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Message{" +
				"recipient=" + recipient +
				", message='" + message + '\'' +
				'}';
	}
}
