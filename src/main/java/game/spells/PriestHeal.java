package game.spells;

import game.GameServer;
import game.logging.Log;
import vo.Ability;
import vo.GameAnimation;
import vo.Hero;
import vo.classes.Priest;

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
		for (Hero hero : getTargetFriendlyList()) {
			Log.i(TAG, "Target Hero to heal : " + hero.getId());
			// Get heal amount
			Priest priest = (Priest) getHero();
			float healAmount = priest.getSpellHealAmount();
			Log.i(TAG, "Healing for this amount : " + healAmount);

			// Heal target (don't overheal)
			hero.heal(healAmount);

			// Set the cooldown for this ability
			getAbility().setMillisLastUse(getTime());
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));


			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("HEAL", hero.getId(), hero.getId(), null));
		}
	}
}
