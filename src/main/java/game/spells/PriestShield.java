package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.Ability;
import game.vo.Buff;
import game.vo.GameAnimation;
import game.vo.Hero;
import game.vo.classes.Priest;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestShield extends Spell {

	private static final String TAG = PriestShield.class.getSimpleName();

	public PriestShield(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}


	public void execute() {
		if (getTargetFriendlyList().get(0) != null) {
			Hero hero = getTargetFriendlyList().get(0);
			Log.i(TAG, "Target Hero to shield: " + hero.getId());
			// Get shield amount
			Priest priest = (Priest) getHero();
			float shieldAmount = priest.getSpellDamage(getAbility());
			Log.i(TAG, "Shield for this amount : " + shieldAmount);

			int duration = 3000;
			Buff buff = new Buff(hero.getId(), null, Buff.SHIELD, Math.round(shieldAmount), duration, 0, 0);
			Thread buffDurationThread = new Thread(() -> {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				hero.removeBuff(buff);
			});
			buffDurationThread.start();

			hero.getBuffs().add(buff);

			//getGameServer().sendHeroBuff(new Buff(hero.getId(), null, Buff.SHIELD, getAbility().getValue(), 3000));

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("SHIELD", hero.getId(), getHero().getId(), null, 1));
		}

		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));
	}
}
