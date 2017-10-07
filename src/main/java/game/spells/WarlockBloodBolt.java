package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.Ability;
import game.vo.GameAnimation;
import game.vo.Hero;
import game.vo.classes.Warlock;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarlockBloodBolt extends Spell {

	private static final String TAG = WarlockBloodBolt.class.getSimpleName();

	public WarlockBloodBolt(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if (getTargetEnemyList() != null && getTargetEnemyList().size() >= 1 && !getAbility().isCasting()) {
			Log.i(TAG, "Target minion to damage : " + getTargetEnemyList().get(0).getId());

			// Get damage amount
			Warlock warlock = (Warlock) getHero();
			float damageAmount = warlock.getSpellDamage(getAbility());
			Log.i(TAG, "Damage for this amount : " + damageAmount);
			Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
			// Damage target
			damageMinion(getTargetEnemyList().get(0), damageAmount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());
			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("BLOOD_BOLT", 0, getHero().getId(), null, 1));
		}

		super.execute();
	}

}
