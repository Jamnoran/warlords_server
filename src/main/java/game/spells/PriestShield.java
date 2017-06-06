package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.Ability;
import game.vo.Buff;
import game.vo.GameAnimation;
import game.vo.Hero;
import game.vo.classes.Priest;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestShield extends Spell {

	private static final String TAG = PriestShield.class.getSimpleName();

	public PriestShield(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if(getTargetFriendly().get(0) != null){
			Hero hero = getTargetFriendlyList().get(0);
			Log.i(TAG, "Target Hero to shield: " + hero.getId());
			// Get shield amount
			Priest priest = (Priest) getHero();
			float shieldAmount = priest.getSpellDamage(getAbility());
			Log.i(TAG, "Shield for this amount : " + shieldAmount);

			// Heal target (don't overheal)
			getGameServer().sendHeroBuff(hero.getId(), null, Buff.SHIELD, getAbility().getValue(), 3000);

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("SHIELD", hero.getId(), getHero().getId(), null));
		}

		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));
	}
}