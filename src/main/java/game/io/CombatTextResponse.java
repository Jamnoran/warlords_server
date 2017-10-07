package game.io;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class CombatTextResponse {
	private String response_type = "ROTATE_TARGET";
	private boolean friendly = true;
	private int idOfTarget = 0;
	private String amount;
	private String color;

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public int getIdOfTarget() {
		return idOfTarget;
	}

	public void setIdOfTarget(int idOfTarget) {
		this.idOfTarget = idOfTarget;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "CombatTextResponse{" +
				"response_type='" + response_type + '\'' +
				", friendly=" + friendly +
				", idOfTarget=" + idOfTarget +
				", amount='" + amount + '\'' +
				", color='" + color + '\'' +
				'}';
	}
}
