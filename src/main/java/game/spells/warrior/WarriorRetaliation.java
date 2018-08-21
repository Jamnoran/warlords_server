package game.spells.warrior;

import game.GameServer;
import game.logging.Log;
import game.spells.Spell;
import game.vo.*;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class WarriorRetaliation extends Spell {

	private static final String TAG = WarriorRetaliation.class.getSimpleName();

	public WarriorRetaliation(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		super(time, hero, ability, gameServer, targetEnemy, targetFriendly, position);
	}

	public void execute() {
		// Taunt amount
		float scaleAmount = getScaleFromTalents();
		float tauntAmount = getAbility().getBaseDamage() * (1 + scaleAmount);
		getAbility().setValue(3000);
		Buff buff = new Buff(getHero().getId(), null, Buff.RETALIATION, Math.round(tauntAmount), getAbility().getValue(), null, 0);

		getHero().addBuff(buff);

		Thread buffDurationThread = new Thread(() -> {
			try {
				Thread.sleep(buff.getDuration());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getHero().removeBuff(buff);
			getGameServer().sendGameStatus();
		});
		buffDurationThread.start();

		// Add animation to list
		getGameServer().getAnimations().add(new GameAnimation("RETALIATION", 0, getHero().getId(), null, 2));

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
