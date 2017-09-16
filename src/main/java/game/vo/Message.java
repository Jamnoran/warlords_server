package game.vo;

public class Message {
	private Integer recipient = null;
	private String sender = null;
	private String message = null;
	
	public Message() {
	}
	public Message(String message) {
		this.message = message;
	}

	public Message(Integer recipient , String message) {
		this.message = message;
		this.recipient = recipient;
	}

	public Message(Integer recipient, String sender, String message) {
		this.message = message;
		this.sender = sender;
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

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return "Message{" +
				"recipient=" + recipient +
				", sender='" + sender + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
