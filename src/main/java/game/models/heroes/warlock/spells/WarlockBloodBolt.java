package game.models.heroes.warlock.spells;

import game.GameServer;
import game.logging.Log;
import game.models.abilities.Spell;
import game.models.game.Amount;
import game.models.game.GameAnimation;
import game.models.heroes.Hero;
import game.models.abilities.Ability;
import game.models.heroes.warlock.Warlock;
import game.models.vo.Vector3;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarlockBloodBolt extends Spell {

	private static final String TAG = WarlockBloodBolt.class.getSimpleName();
	private static long animationTime = 1400;

	public WarlockBloodBolt(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}


	public void execute() {
		if (getTargetEnemies() != null && getTargetEnemies().size() >= 1 && !getAbility().isCasting()) {
			getAbility().setCasting(true);
			Log.i(TAG, "Target minion to damage : " + getTargetEnemies().get(0).getId());

			// Calculate castTime with CDR and talents etc
			getAbility().setCalculatedCastTime(getAbility().getCastTime());

			// Get damage amount
			Warlock warlock = (Warlock) getHero();
			Amount damageAmount = warlock.getSpellDamage(this);
			Log.i(TAG, "Damage for this amount : " + damageAmount);
			sendAnimation("BLOOD_BOLT");
//			Thread animationSender = new Thread(() -> {
//				try {
//					Thread.sleep(getAbility().getCastTime() - animationTime);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				sendAnimation("SMITE_CAST");
//			});
//			animationSender.start();

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


	private void sendAnimation(String animation) {
		// Add animation to list
		getGameServer().getAnimations().add(new GameAnimation(animation, getTargetEnemies().get(0).getId(), getHero().getId(), null, 1));
	}


	private void castTimeCompleted(Amount amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {
			// Damage target
			damageMinion(getTargetEnemies().get(0), amount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());
		}
		getAbility().setCasting(false);
	}
}
