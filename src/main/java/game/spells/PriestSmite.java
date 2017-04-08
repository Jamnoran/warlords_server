package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.Ability;
import game.vo.GameAnimation;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.classes.Priest;
import game.vo.classes.Warrior;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestSmite extends Spell {

	private static final String TAG = PriestSmite.class.getSimpleName();

	public PriestSmite(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if (getTargetEnemyList() != null && getTargetEnemyList().size() >= 1) {
			Log.i(TAG, "Target minion to damage : " + getTargetEnemyList().get(0).getId());
			// Get damage amount
			Priest priest = (Priest) getHero();
			float damageAmount = priest.getSpellDamage(getAbility());
			Log.i(TAG, "Damage for this amount : " + damageAmount);

			// Damage target
			damageMinion(getTargetEnemyList().get(0), damageAmount);

			// Set the cooldown for this ability
			getAbility().setMillisLastUse(getTime());
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("SMITE", 0, getHero().getId(), null));
		}
	}

}
