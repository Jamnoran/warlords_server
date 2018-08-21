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
public class PriestHot extends Spell {

	private static final String TAG = PriestHot.class.getSimpleName();

	public PriestHot(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}


	public void execute() {
		if(getTargetFriendlyList() != null && getTargetFriendlyList().size() > 0) {
			try {
				for (Hero hero : getTargetFriendlyList()) {

					Log.i(TAG, "Target Hero to heal : " + hero.getId());

					// Get heal amount
					Priest priest = (Priest) getHero();
					Amount healAmount = priest.getSpellDamage(getAbility());
					Log.i(TAG, "Healing for this amount : " + healAmount);

					// Heal target (don't overheal)

					long firstTick = System.currentTimeMillis();

					if(GameUtil.getHeroById(hero.getId(), getGameServer().getHeroes()) != null) {
						GameUtil.getHeroById(hero.getId(), getGameServer().getHeroes()).getBuffs().add(new Buff(getHero().id, getTargetEnemy().get(0), Buff.HOT, Math.round(healAmount.getAmount()), getAbility().getDefaultTickMillis(), "" + firstTick, getAbility().getValue()));
					}

					// Add animation to list
					getGameServer().getAnimations().add(new GameAnimation("HEAL_OVER_TIME", hero.getId(), getHero().getId(), null, 2));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Log.i(TAG, "No target to heal");
		}
		super.execute();
	}



	public void castTimeCompleted(Amount amount){
		Log.i(TAG, "Ability cast time is complete, time to do rest [" + getAbility().getName() + "]");
		if (getAbility().isCasting()) {

			damageMinion(getTargetEnemyList().get(0), amount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());

			long firstTick = System.currentTimeMillis();

			try {
				if(GameUtil.getMinionById(getTargetEnemy().get(0), getGameServer().getMinions()) != null) {
					GameUtil.getMinionById(getTargetEnemy().get(0), getGameServer().getMinions()).addDebuff(new Buff(getHero().id, getTargetEnemy().get(0), Buff.DOT, Math.round(amount.getAmount()), getAbility().getDefaultTickMillis(), "" + firstTick, getAbility().getValue()));
				}
			} catch (Exception e) {
				Log.i(TAG, "What do we get nullpointer on here?");
				e.printStackTrace();
			}

			for (int i = 0 ; i < getAbility().getValue() ; i++) {
				getGameServer().getTickEngine().addTick(new Tick(firstTick + (i * getAbility().getDefaultTickMillis()), Tick.MINION_DEBUFF));
			}
			getAbility().setCasting(false);
		}
	}
}
