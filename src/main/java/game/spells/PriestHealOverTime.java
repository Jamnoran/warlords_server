package game.spells;

import game.GameServer;
import game.logging.Log;
import game.util.GameUtil;
import game.vo.*;
import game.vo.classes.Priest;
import game.vo.classes.Warlock;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestHealOverTime extends Spell {

	private static final String TAG = PriestHealOverTime.class.getSimpleName();

	public PriestHealOverTime(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
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
			Amount damageAmount = priest.getSpellDamage(getAbility());
			Log.i(TAG, "Damage for this amount : " + damageAmount);

			Thread castTime = new Thread(() -> {
				try {
					Thread.sleep(getAbility().getCalculatedCastTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				castTimeCompleted(damageAmount);
			});
			castTime.start();

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("HEAL_OVER_TIME", 0, getHero().getId(), null, 1));
		}

		super.execute();
	}

	public void castTimeCompleted(Amount amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {
			// Damage target
			healHero(getTargetFriendly().get(0), amount);
			getAbility().setCasting(false);

			long firstTick = System.currentTimeMillis();

			try {
				if(GameUtil.getHeroById(getTargetFriendly().get(0), getGameServer().getHeroes()) != null) {
					GameUtil.getHeroById(getTargetFriendly().get(0), getGameServer().getHeroes()).addBuff(new Buff(getHero().id, getTargetEnemy().get(0), Buff.HOT, Math.round(amount.getAmount()), getAbility().getDefaultTickMillis(), "" + firstTick, getAbility().getValue()));
				}
			} catch (Exception e) {
				Log.i(TAG, "What do we get nullpointer on here?");
				e.printStackTrace();
			}

			for (int i = 0 ; i < getAbility().getValue() ; i++) {
				getGameServer().getTickEngine().addTick(new Tick(firstTick + (i * getAbility().getDefaultTickMillis()), Tick.BUFF));
			}
		}
	}
}
