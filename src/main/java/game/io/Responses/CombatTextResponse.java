package game.io.Responses;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class CombatTextResponse {
	private String response_type = "COMBAT_TEXT";
	private boolean friendly = true;
	private int idOfTarget = 0;
	private String amount;
	private boolean crit = false;
	private String color;

	public CombatTextResponse() {
	}

	public CombatTextResponse(boolean friendly, int idOfTarget, String amount, boolean crit, String color) {
		this.friendly = friendly;
		this.idOfTarget = idOfTarget;
		this.amount = amount;
		this.crit = crit;
		this.color = color;
	}

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

	public boolean isCrit() {
		return crit;
	}

	public void setCrit(boolean crit) {
		this.crit = crit;
	}

	@Override
	public String toString() {
		return "CombatTextResponse{" +
				"response_type='" + response_type + '\'' +
				", friendly=" + friendly +
				", idOfTarget=" + idOfTarget +
				", amount='" + amount + '\'' +
				", crit=" + crit +
				", color='" + color + '\'' +
				'}';
	}
}
