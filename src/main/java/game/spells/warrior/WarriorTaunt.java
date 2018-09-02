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

	public WarriorTaunt(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}

	public void execute() {
		// Taunt amount
		float scaleAmount = getScaleFromTalents();
		float tauntAmount = getAbility().getBaseDamage() * (1 + scaleAmount);

		if (getTargetEnemyList() != null) {
			for (Minion minion : getTargetEnemyList()) {
				Log.i(TAG, "Target minion to taunt : " + minion.getId());
				Log.i(TAG, "Taunting for this amount : " + tauntAmount);

				minion.addThreat(new Threat(getHero().getId(), tauntAmount, 0, 0));
			}
		}

		// Add animation to list
		getGameServer().getAnimations().add(new GameAnimation("TAUNT", 0, getHero().getId(), null, 2));

		super.execute();
	}

	private float getScaleFromTalents() {
		int scaleId = 2;
		float scaleAmount = 0.0f;
		for(Talent talent : getHero().getTalents()){
			if(talent.getId() == scaleId){
				scaleAmount = scaleAmount + talent.getScaling() * talent.getPointAdded();
			}
		}
		return scaleAmount;
	}
}