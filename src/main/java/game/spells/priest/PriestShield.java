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
public class PriestShield extends Spell {

	private static final String TAG = PriestShield.class.getSimpleName();

	public PriestShield(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}


	public void execute() {
		if (getTargetFriendlyList().get(0) != null) {
			Hero hero = getTargetFriendlyList().get(0);
			Log.i(TAG, "Target Hero to shield: " + hero.getId());
			// Get shield amount
			Priest priest = (Priest) getHero();
			Amount shieldAmount = priest.getSpellDamage(getAbility());
			Log.i(TAG, "Shield for this amount : " + shieldAmount);

			int duration = 3000;
			Buff buff = new Buff(hero.getId(), null, Buff.SHIELD, Math.round(shieldAmount.getAmount()), duration, "0", 0);
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

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("SHIELD", hero.getId(), getHero().getId(), null, 1));
		}


		super.execute();
	}
}
