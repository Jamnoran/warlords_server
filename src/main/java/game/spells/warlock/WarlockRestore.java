package game.spells.warlock;

import game.GameServer;
import game.logging.Log;
import game.spells.Spell;
import game.vo.*;
import game.vo.classes.Warlock;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarlockRestore extends Spell {

	private static final String TAG = WarlockRestore.class.getSimpleName();

	public WarlockRestore(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}


	public void execute() {
		if (getTargetEnemies() != null && getTargetFriendlies().size() >= 1 && !getAbility().isCasting() && getTargetFriendlies().get(0).getId() != getHero().getId()) {
			getAbility().setCasting(true);
			Log.i(TAG, "Target hero to restore: " + getTargetFriendlies().get(0).getId());

			// Calculate castTime with CDR and talents etc
			getAbility().setCalculatedCastTime(getAbility().getCastTime());

			// Get damage amount
			Warlock warlock = (Warlock) getHero();
			float hpCost = getHero().getMaxHp() * (getAbility().getResourceCost() / 100);
			Amount damageAmount = warlock.getSpellDamage(this);
			Log.i(TAG, "Damage for this amount : " + damageAmount);

			Thread castTime = new Thread(() -> {
				try {
					Thread.sleep(getAbility().getCastTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				castTimeCompleted(damageAmount, hpCost);
			});
			castTime.start();

			// Send castbar information
			getGameServer().sendCastBarInformation(getHero().getId(), getAbility());

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("RESTORE", 0, getHero().getId(), null, 1));
		}
		super.execute();
	}


	public void castTimeCompleted(Amount amount, float hpCost){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {

			damageHero(getHero().id, hpCost);

			restoreResources(getTargetFriendlies().get(0).getId(), amount.getAmount());
			getAbility().setCasting(false);
		}
	}

}
