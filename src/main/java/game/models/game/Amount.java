package game.models.game;

public class Amount {
	public static String TRUE_DAMAGE = "TRUE";
	public static String PHYSICAL = "PHYSICAL";
	public static String MAGIC = "MAGIC";
	private float amount;
	private boolean crit = false;

	public Amount(float amount) {
		this.amount = amount;
	}

	public Amount(float amount, boolean crit) {
		this.amount = amount;
		this.crit = crit;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public boolean isCrit() {
		return crit;
	}

	public void setCrit(boolean crit) {
		this.crit = crit;
	}

	@Override
	public String toString() {
		return "Amount{" +
				"amount=" + amount +
				", crit=" + crit +
				'}';
	}
}
