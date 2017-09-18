package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.*;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorTaunt extends Spell {

	private static final String TAG = WarriorTaunt.class.getSimpleName();

	public WarriorTaunt(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}

	public void execute() {
		// Taunt amount
		float tauntAmount = getAbility().getBaseDamage();

		if (getTargetEnemyList() != null) {
			for (Minion minion : getTargetEnemyList()) {
				Log.i(TAG, "Target minion to taunt : " + minion.getId());
				Log.i(TAG, "Taunting for this amount : " + tauntAmount);

				minion.addThreat(new Threat(getHero().getId(), tauntAmount, 0, 0));
			}
		}
		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));

		// Send castbar information
		getGameServer().sendCastBarInformation(getAbility());

		// Add animation to list
		getGameServer().getAnimations().add(new GameAnimation("TAUNT", 0, getHero().getId(), null, 2));
	}

}
