package game.spells;

import game.GameServer;
import game.logging.Log;
import vo.Ability;
import vo.GameAnimation;
import vo.Hero;
import vo.Minion;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class Spell {

	private static final String TAG = Spell.class.getSimpleName();
	private Ability ability;
	private Hero hero;
	private GameServer gameServer;
	private ArrayList<Integer> targetEnemy;
	private ArrayList<Minion> targetEnemyList;
	private ArrayList<Integer> targetFriendly;
	private ArrayList<Hero> targetFriendlyList;
	private long time;


	public Spell(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly) {
		this.time = time;
		this.hero = hero;
		this.ability = ability;
		this.gameServer = gameServer;
		this.targetEnemy = targetEnemy;
		this.targetFriendly = targetFriendly;
	}

	public boolean init(){
		// Check if has a friendly target
		if(targetFriendly != null){
			targetFriendlyList = new ArrayList<>();
			targetFriendlyList.add(gameServer.getHeroById(targetFriendly.get(0)));
		}else{
			// If not then heal lowest % hp ally
			targetFriendlyList = new ArrayList<>();
			targetFriendlyList.add(gameServer.getHeroWithLowestHp());
		}

		// Remove mana return false if cant use spell (should be handled on client side as well)
		if (targetFriendlyList != null && hero.hasManaForSpellHeal() && ability.isAbilityOffCD(time)) {

			getGameServer().sendCooldownInformation(ability, hero.getId());

			return true;
			// Add threat to all targets close by (of target location or healer location?)
		}else if (targetFriendlyList != null && !hero.hasManaForSpellHeal()){
			Log.i(TAG, "Hero does not have enough mana for use of ability");
			return false;
		}else if (targetFriendlyList != null && !ability.isAbilityOffCD(getTime())){
			Log.i(TAG, "Ability not of cooldown");
			return false;
		}
		return false;
	}

	public void execute() {

	}


	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public GameServer getGameServer() {
		return gameServer;
	}

	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	public ArrayList<Integer> getTargetEnemy() {
		return targetEnemy;
	}

	public void setTargetEnemy(ArrayList<Integer> targetEnemy) {
		this.targetEnemy = targetEnemy;
	}

	public ArrayList<Integer> getTargetFriendly() {
		return targetFriendly;
	}

	public void setTargetFriendly(ArrayList<Integer> targetFriendly) {
		this.targetFriendly = targetFriendly;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public ArrayList<Minion> getTargetEnemyList() {
		return targetEnemyList;
	}

	public void setTargetEnemyList(ArrayList<Minion> targetEnemyList) {
		this.targetEnemyList = targetEnemyList;
	}

	public ArrayList<Hero> getTargetFriendlyList() {
		return targetFriendlyList;
	}

	public void setTargetFriendlyList(ArrayList<Hero> targetFriendlyList) {
		this.targetFriendlyList = targetFriendlyList;
	}
}
