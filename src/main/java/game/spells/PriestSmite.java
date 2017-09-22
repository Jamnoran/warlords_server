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
public class PriestSmite extends Spell {

	private static final String TAG = PriestSmite.class.getSimpleName();

	public PriestSmite(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if (getTargetEnemyList() != null && getTargetEnemyList().size() >= 1 && !getAbility().isCasting()) {
			getAbility().setCasting(true);
			Log.i(TAG, "Target minion to damage : " + getTargetEnemyList().get(0).getId());

			// Calculate castTime with CDR and talents etc
			getAbility().setCalculatedCastTime(getAbility().getCastTime());

			// Get damage amount
			Priest priest = (Priest) getHero();
			float damageAmount = priest.getSpellDamage(getAbility());
			Log.i(TAG, "Damage for this amount : " + damageAmount);

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
			getGameServer().sendCastBarInformation(getAbility());

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("SMITE", 0, getHero().getId(), null, 2));
		}else{
			Log.i(TAG, "Did not have a target...");
		}
		super.execute();
	}


	public void castTimeCompleted(float amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {
			// Damage target
			damageMinion(getTargetEnemyList().get(0), amount);
		}
		getAbility().setCasting(false);
	}
}
