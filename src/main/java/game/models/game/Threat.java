package game.models.game;

/**
 * Created by Jamnoran on 14-Jul-16.
 */
public class Threat {
	private Integer heroId;
	private float amount = 0.0f;
	private float damage = 0.0f;
	private float healingDoneWithinVision = 0.0f;
	private float damageMultiplier = 1.0f;
	private float healingMultiplier = 0.5f;
	public static float inRangeThreath = 2.0f;

	public Threat(Integer heroId, float amount, float damage, float healingDoneWithinVision) {
		this.heroId = heroId;
		this.amount = amount;
		this.damage = damage;
		this.healingDoneWithinVision = healingDoneWithinVision;
	}

	public Integer getHeroId() {
		return heroId;
	}

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getHealingDoneWithinVision() {
		return healingDoneWithinVision;
	}

	public void setHealingDoneWithinVision(float healingDoneWithinVision) {
		this.healingDoneWithinVision = healingDoneWithinVision;
	}

	@Override
	public String toString() {
		return "Threat{" +
				"heroId=" + heroId +
				", amount=" + amount +
				", damage=" + damage +
				", healingDoneWithinVision=" + healingDoneWithinVision +
				'}';
	}

	public void addThreat(Threat threat) {
		if(threat.getDamage() > 0){
			setAmount(getAmount() + (threat.getDamage() * damageMultiplier));
		}else if (threat.getHealingDoneWithinVision() > 0){
			setAmount(getAmount() + (threat.getHealingDoneWithinVision() * healingMultiplier));
		}else{
			setAmount(getAmount() + threat.getAmount());
		}
	}
}
