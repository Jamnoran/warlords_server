package game.models.heroes.priest.spells;

import game.GameServer;
import game.logging.Log;
import game.models.abilities.Spell;
import game.models.game.Amount;
import game.models.game.GameAnimation;
import game.models.heroes.Hero;
import game.models.abilities.Ability;
import game.models.heroes.priest.Priest;
import game.models.talents.Talent;
import game.models.vo.Vector3;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class PriestHeal extends Spell {

	private static final String TAG = PriestHeal.class.getSimpleName();

	public PriestHeal(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
		setAmountTalentId(Talent.TALENT_AMOUNT_FLASH_HEAL);
	}

	public void execute() {
		if(!getAbility().isCasting()) {
			try {
				Hero hero;
				if (getTargetFriendlies().size() > 0) {
					hero = getTargetFriendlies().get(0);
				}else{
					hero = getGameServer().getGameUtil().getHeroWithLowestHp();
				}

				Log.i(TAG, "Target Hero to heal : " + hero.getId());

				// Get heal amount
				Priest priest = (Priest) getHero();
				Amount healAmount = priest.getSpellDamage(this);
				Log.i(TAG, "Healing for this amount : " + healAmount);

				// Heal target (don't overheal)
				healHero(hero.getId(), healAmount);

				// Add animation to list
				getGameServer().getAnimations().add(new GameAnimation("HEAL", hero.getId(), getHero().getId(), null, 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Log.i(TAG, "No target to heal");
		}
		super.execute();
	}
}
