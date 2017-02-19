package game.vo;

public class Message {
	private Integer recipient = null;
	private String message = null;
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	public Message(String message) {
		this.message = message;
	}
	public Message(Integer recipient, String message) {
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
