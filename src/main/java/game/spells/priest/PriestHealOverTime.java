package game.spells.priest;

import game.GameServer;
import game.logging.Log;
import game.spells.Spell;
import game.util.GameUtil;
import game.vo.*;
import game.vo.classes.Priest;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestHealOverTime extends Spell {

	private static final String TAG = PriestHealOverTime.class.getSimpleName();
	private Hero heroToHeal;
	private static int healOverTimeDefaultMillisTick =  3000;

	public PriestHealOverTime(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
		getAbility().setDefaultTickMillis(healOverTimeDefaultMillisTick);
		setAmountTalentId(Talent.TALENT_AMOUNT_HOT);
	}

	public void execute() {
		if(!getAbility().isCasting()) {
			getAbility().setCasting(true);
			if (getTargetFriendlies().size() > 0) {
				heroToHeal = getTargetFriendlies().get(0);
			}else{
				heroToHeal = getGameServer().getGameUtil().getHeroWithLowestHp();
			}
			Log.i(TAG, "Healing over time to hero id: " + heroToHeal.getId());

			// Calculate castTime with CDR and talents etc
			getAbility().setCalculatedCastTime(getAbility().getCastTime());

			// Get damage amount
			Priest priest = (Priest) getHero();
			Amount damageAmount = priest.getSpellDamage(this);
			Log.i(TAG, "Healing for this amount : " + damageAmount);

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
			// Heal target
			getAbility().setCasting(false);

			long firstTick = System.currentTimeMillis();

			try {
				if(GameUtil.getHeroById(heroToHeal.getId(), getGameServer().getHeroes()) != null) {
					GameUtil.getHeroById(heroToHeal.getId(), getGameServer().getHeroes()).addBuff(new Buff(getHero().id, heroToHeal.getId(), Buff.HOT, Math.round(amount.getAmount()), getAbility().getDefaultTickMillis(), "" + firstTick, getAbility().getValue()));
					Log.i(TAG, "Added buff healing over time to hero");
				}
			} catch (Exception e) {
				Log.i(TAG, "What do we get nullpointer on here?");
				e.printStackTrace();
			}

			Log.i(TAG, "Added this many ticks to tickengine : " + getAbility().getValue() + " Ability : " + getAbility().toString());
			for (int i = 1 ; i <= getAbility().getValue() ; i++) {
				getGameServer().getTickEngine().addTick(new Tick(firstTick + (i * getAbility().getDefaultTickMillis()), Tick.BUFF));
			}
		}
	}
}
