package game.spells;

import game.GameServer;
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
		Buff buff = new Buff(getHero().getId(), null, Buff.SPEED, getAbility().getValue(), 250);
		getHero().getBuffs().add(buff);

		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));

		// Add animation to list
		getGameServer().getAnimations().add(new GameAnimation("CHARGE", 0, getHero().getId(), null));
	}

}
