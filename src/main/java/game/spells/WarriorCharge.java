package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.Buff;
import game.vo.*;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorCharge extends Spell {

	private static final String TAG = WarriorCharge.class.getSimpleName();

	public WarriorCharge(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly);
	}

	public void execute() {
		// Send charge ability (set that this hero target is the minion, set movement speed for a period of time)
		if (getTargetEnemy() != null && getTargetEnemy().size() > 0) {
			int duration = 250;
			Buff buff = new Buff(getHero().getId(), null, Buff.SPEED, getAbility().getValue(), duration, "0", 0);
			Thread buffDurationThread = new Thread(() -> {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getHero().removeBuff(buff);
				getGameServer().sendGameStatus();
				Log.i(TAG, "Removed buff, now have these many buffs left : " + getHero().getBuffs().size());
			});
			buffDurationThread.start();

			getHero().getBuffs().add(buff);


			// Set the cooldown for this ability
			getAbility().setMillisLastUse(getTime());
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));


			// Send castbar information
			getGameServer().sendCastBarInformation(getAbility());

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("CHARGE", 0, getHero().getId(), null, 1));
		}else{
			Log.i(TAG, "Hero had no target, canceling ability");
		}
		super.execute();
	}

}
