package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.*;
import game.vo.classes.Warrior;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorCleave extends Spell {

	private static final String TAG = WarriorCleave.class.getSimpleName();

	public WarriorCleave(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if (getTargetEnemyList() != null) {
			for (Minion minion : getTargetEnemyList()) {
				Log.i(TAG, "Target minion to damage : " + minion.getId());
				// Get damage amount
				Warrior warrior = (Warrior) getHero();
				float damageAmount = warrior.getSpellDamage(getAbility());
				Log.i(TAG, "Damage for this amount : " + damageAmount);

				// Damage target
				damageMinion(minion, damageAmount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());

			}
			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("CLEAVE", 0, getHero().getId(), null, 1));
		}
		super.execute();
	}

}
