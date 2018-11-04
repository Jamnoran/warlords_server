package game.spells.priest;

import game.GameServer;
import game.logging.Log;
import game.spells.Spell;
import game.vo.*;
import game.vo.classes.Priest;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestSmite extends Spell {

	private static final String TAG = PriestSmite.class.getSimpleName();
	private static long animationTime = 400;

	public PriestSmite(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}


	public void execute() {
		if (getTargetEnemyList() != null && getTargetEnemyList().size() >= 1 && !getAbility().isCasting()) {
			getAbility().setCasting(true);
			Log.i(TAG, "Target minion to damage : " + getTargetEnemyList().get(0).getId());

			// Calculate castTime with CDR and talents etc
			getAbility().setCalculatedCastTime(getAbility().getCastTime());

			// Get damage amount
			Priest priest = (Priest) getHero();
			Amount damageAmount = priest.getSpellDamage(this);
			Log.i(TAG, "Damage for this amount : " + damageAmount);
			sendAnimation("SMITE", getTargetEnemyList().get(0).getId());
			Thread animationSender = new Thread(() -> {
				try {
					Thread.sleep(getAbility().getCastTime() - animationTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendAnimation("SMITE_CAST", getTargetEnemyList().get(0).getId());
			});
			animationSender.start();

			Thread castTime = new Thread(() -> {
				try {
					Thread.sleep(getAbility().getCastTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				castTimeCompleted(damageAmount);
			});
			castTime.start();

			// Send castbar information
			getGameServer().sendCastBarInformation(getHero().getId(), getAbility());

		}else{
			Log.i(TAG, "Did not have a target...");
		}
		super.execute();
	}

	private void sendAnimation(String animation, int target) {
		// Add animation to list
		getGameServer().getAnimations().add(new GameAnimation(animation, target, getHero().getId(), null, 2));
	}


	private void castTimeCompleted(Amount amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {
			// Damage target
			damageMinion(getTargetEnemyList().get(0), amount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());
		}
		getAbility().setCasting(false);
	}
}
