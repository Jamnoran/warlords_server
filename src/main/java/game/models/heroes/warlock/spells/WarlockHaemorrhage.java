package game.models.heroes.warlock.spells;

import game.GameServer;
import game.logging.Log;
import game.models.abilities.Buff;
import game.models.abilities.Spell;
import game.models.game.Amount;
import game.models.game.GameAnimation;
import game.models.game.Tick;
import game.models.heroes.Hero;
import game.models.abilities.Ability;
import game.models.vo.Vector3;
import game.util.GameUtil;
import game.models.heroes.warlock.Warlock;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarlockHaemorrhage extends Spell {

	private static final String TAG = WarlockHaemorrhage.class.getSimpleName();

	public WarlockHaemorrhage(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
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
			getGameServer().sendCastBarInformation(getHero().getId(), getAbility());

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("HAEMORRHAGE", getTargetEnemies().get(0).getId(), getHero().getId(), null, 1));
			getGameServer().sendGameStatus();
		}else{
			Log.i(TAG, "Cant use spell cos we dont have target or we are casting already " + getAbility().isCasting());
		}
		super.execute();
	}

	private void castTimeCompleted(Amount amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {

			damageMinion(getTargetEnemies().get(0), amount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());

			if(getTargetEnemies().get(0).isAlive()){
				long firstTick = System.currentTimeMillis();

				try {
					if(GameUtil.getMinionById(getTargetEnemies().get(0).getId(), getGameServer().getMinions()) != null) {
						GameUtil.getMinionById(getTargetEnemies().get(0).getId(), getGameServer().getMinions()).addDebuff(new Buff(getHero().id, getTargetEnemies().get(0).getId(), Buff.DOT, Math.round(amount.getAmount()), getAbility().getDefaultTickMillis(), "" + firstTick, getAbility().getValue()));
					}
				} catch (Exception e) {
					Log.i(TAG, "What do we get nullpointer on here?");
					e.printStackTrace();
				}

				for (int i = 0 ; i < getAbility().getValue() ; i++) {
					getGameServer().getTickEngine().addTick(new Tick(firstTick + (i * getAbility().getDefaultTickMillis()), Tick.MINION_DEBUFF));
				}
			}
			getAbility().setCasting(false);
		}
	}
}
