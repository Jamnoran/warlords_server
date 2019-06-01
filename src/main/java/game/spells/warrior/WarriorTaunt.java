package game.spells.warrior;

import game.GameServer;
import game.logging.Log;
import game.spells.Spell;
import game.vo.*;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorTaunt extends Spell {

	private static final String TAG = WarriorTaunt.class.getSimpleName();
	private boolean initialCast;

	public WarriorTaunt(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}

	public void execute() {
		if (initialCast) {
			// Add animation to list
			Log.i(TAG, "Inital cast of taunt, send animation");
			getGameServer().getAnimations().add(new GameAnimation("TAUNT", 0, getHero().getId(), null, 2));
			getGameServer().sendCastBarInformation(getHero().getId(), getAbility());
		} else {
			Log.i(TAG, "Ability : " + getAbility().toString());
			// Taunt amount
			float scaleAmount = Talent.getTalentAmount(getHero().getTalents(), Talent.TALENT_TAUNT_AMOUNT);
			float tauntAmount = ((float) getAbility().getValue()) * (1 + scaleAmount);
			Log.i(TAG, "Scale amount : " + scaleAmount + " Base : " + getAbility().getValue());

			if (getTargetEnemies() != null) {
				for (Minion minion : getTargetEnemies()) {
					Log.i(TAG, "Target minion to taunt : " + minion.getId());
					float tauntAmountForMinion = tauntAmount + minion.getHighestThreathValue();
					Log.i(TAG, "Taunting for this amount : " + tauntAmountForMinion);
					minion.addThreat(new Threat(getHero().getId(), tauntAmountForMinion, 0, 0));
				}
			}else{
				Log.i(TAG, "Could not find minions to taunt");
			}
			super.execute();
		}
	}

	public void setInitialCast(boolean initialCast) {
		this.initialCast = initialCast;
	}

	public boolean getInitialCast() {
		return initialCast;
	}
}
