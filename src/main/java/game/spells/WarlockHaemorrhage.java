package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.*;
import game.vo.classes.Warlock;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarlockHaemorrhage extends Spell {

	private static final String TAG = WarlockHaemorrhage.class.getSimpleName();

	public WarlockHaemorrhage(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if (getTargetEnemyList() != null && getTargetEnemyList().size() >= 1 && !getAbility().isCasting()) {
			getAbility().setCasting(true);
			Log.i(TAG, "Target minion to damage : " + getTargetEnemyList().get(0).getId());

			// Calculate castTime with CDR and talents etc
			getAbility().setCalculatedCastTime(getAbility().getCastTime());

			// Get damage amount
			Warlock warlock = (Warlock) getHero();
			float damageAmount = warlock.getSpellDamage(getAbility());
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

			// Set the cooldown for this ability
			getAbility().setMillisLastUse(getTime());
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));

			// Send castbar information
			getGameServer().sendCastBarInformation(getAbility());

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("HAEMORRHAGE", 0, getHero().getId(), null, 1));
			getGameServer().sendGameStatus();
		}else{
			Log.i(TAG, "Cant use spell cos we dont have target or we are casting already " + getAbility().isCasting());
		}
	}


	public void castTimeCompleted(float amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {
			damageMinion(getTargetEnemyList().get(0), amount);

			long firstTick = System.currentTimeMillis();

			try {
				getGameServer().getMinionById(getTargetEnemy().get(0)).addDebuff(new Buff(getHero().id, getTargetEnemy().get(0), Buff.DOT, Math.round(amount), getAbility().getDefaultTickMillis(), firstTick, getAbility().getValue()));
			} catch (Exception e) {
				Log.i(TAG, "What do we get nullpointer on here?");
				e.printStackTrace();
			}

			for (int i = 0 ; i < getAbility().getValue() ; i++) {
				getGameServer().addTick(new Tick(firstTick + (i * getAbility().getDefaultTickMillis()), Tick.MINION_DEBUFF));
			}

			// Set the cooldown for this ability
			getAbility().setMillisLastUse(getTime());
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));
			//getGameServer().sendGameStatus();
			getAbility().setCasting(false);
		}else{
			Log.i(TAG, "Not doing ability since value is : " + getAbility().getValue());
		}
	}
}