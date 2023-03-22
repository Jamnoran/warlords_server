package game.models.heroes.warrior.spells;

import game.GameServer;
import game.logging.Log;
import game.models.abilities.Buff;
import game.models.abilities.Spell;
import game.models.game.GameAnimation;
import game.models.heroes.Hero;
import game.models.abilities.Ability;
import game.models.talents.Talent;
import game.models.vo.Vector3;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorCharge extends Spell {

	private static final String TAG = WarriorCharge.class.getSimpleName();
	private int cdTalentId = Talent.TALENT_CD_HEROIC_CHARGE;
	private int costTalentId = Talent.TALENT_DEC_RESOURCE_HEROIC_CHARGE;
	private int CHARGE_DURATION = 300;

	public WarriorCharge(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
		super.setCDTalentId(cdTalentId);
		super.setCostTalentId(costTalentId);
	}

	public void execute() {
		// Send charge ability (set that this hero target is the minion, set movement speed for a period of time)
		if (getTargetEnemies() != null && getTargetEnemies().size() > 0) {
			Buff buff = new Buff(getHero().getId(), null, Buff.SPEED, getAbility().getValue(), CHARGE_DURATION, "0", 0);
			Thread buffDurationThread = new Thread(() -> {
				try {
					Thread.sleep(CHARGE_DURATION);
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
			getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().calculateCooldown(getHero(), this)));

			// Send castbar information
			getGameServer().sendCastBarInformation(getHero().getId(), getAbility());

			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("CHARGE", 0, getHero().getId(), null, 1));
		}else{
			Log.i(TAG, "Hero had no target, canceling ability");
		}
		super.execute();
	}
}
