package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.Ability;
import game.vo.GameAnimation;
import game.vo.Hero;
import game.vo.classes.Priest;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestHeal extends Spell {

	private static final String TAG = PriestHeal.class.getSimpleName();

	public PriestHeal(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if(getTargetFriendlyList() != null && getTargetFriendlyList().size() > 0) {
			try {
				for (Hero hero : getTargetFriendlyList()) {

					Log.i(TAG, "Target Hero to heal : " + hero.getId());

					// Get heal amount
					Priest priest = (Priest) getHero();
					float healAmount = priest.getSpellDamage(getAbility());
					Log.i(TAG, "Healing for this amount : " + healAmount);

					// Heal target (don't overheal)
					hero.heal(healAmount);

					// Add animation to list
					getGameServer().getAnimations().add(new GameAnimation("HEAL", hero.getId(), getHero().getId(), null, 2));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Set the cooldown for this ability
			getAbility().setMillisLastUse(getTime());
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));
		}else{
			Log.i(TAG, "No target to heal");
		}
	}
}
