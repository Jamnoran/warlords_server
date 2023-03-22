package game.models.heroes.warrior.spells;

import game.GameServer;
import game.logging.Log;
import game.models.abilities.Spell;
import game.models.enemies.Minion;
import game.models.game.Amount;
import game.models.game.GameAnimation;
import game.models.heroes.Hero;
import game.models.abilities.Ability;
import game.models.heroes.warrior.Warrior;
import game.models.vo.Vector3;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorCleave extends Spell {

	private static final String TAG = WarriorCleave.class.getSimpleName();
	private boolean initialCast = false;

	public WarriorCleave(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}

	public void execute() {
		Log.i(TAG, "Ability : " + getAbility().toString() + " Initial cast " + initialCast);
		if (initialCast) {
			// Add animation to list
			getGameServer().getAnimations().add(new GameAnimation("CLEAVE", 0, getHero().getId(), getPosition(), 1));
			getGameServer().sendCastBarInformation(getHero().getId(), getAbility());
		}else{
			if(getTargetEnemies() != null){
				for (Minion minion : getTargetEnemies()) {
					Log.i(TAG, "Target minion to damage : " + minion.getId());
					// Get damage amount
					Warrior warrior = (Warrior) getHero();
					Amount damageAmount = warrior.getSpellDamage(this);
					Log.i(TAG, "Damage for this amount : " + damageAmount);

					// Damage target
					damageMinion(minion, damageAmount, getHero().getPenetration(getAbility().getDamageType()), getAbility().getDamageType());
				}
			}
			super.execute();
		}
	}

	public void setInitialCast(boolean initialCast) {
		this.initialCast = initialCast;
	}
}
