package game.spells;

import game.GameServer;
import game.logging.Log;
import game.vo.*;

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
		if(targetFriendly != null && targetFriendly.size() > 0 && targetFriendly.get(0) > 0){
			targetFriendlyList = new ArrayList<>();
			targetFriendlyList.add(gameServer.getHeroById(targetFriendly.get(0)));
		}else{
			// Auto set the lowest hp friendly target otherwise
			targetFriendlyList = new ArrayList<>();
			targetFriendlyList.add(gameServer.getHeroWithLowestHp());
		}
		// Fix from single target to the list of targets
		if(targetEnemy != null && targetEnemy.size() > 0){
			targetEnemyList = new ArrayList<>();
			for(Integer minionId : targetEnemy){
				Minion min = gameServer.getMinionById(minionId);
				if (min != null) {
					targetEnemyList.add(min);
				}else{
					Log.i(TAG, "Could not get minion by this id : " + minionId);
				}
			}
		}

		// Return false if not has resources
		if(!checkForResourses(hero, getAbility())){
			Log.i(TAG, "Has enough resources");
			return false;
		}

		// Return false if not off cd
		if(!ability.isAbilityOffCD(time)){
			Log.i(TAG, "Ability not of cooldown");
			return false;
		}

		// Return false if no target (Check ability target type)

		getGameServer().sendCooldownInformation(ability, hero.getId());
		Log.i(TAG, "Spell is ok to cast");
		return true;
	}

	private boolean checkForResourses(Hero hero, Ability ability) {
		if(hero.getClass_type().equals("WARLOCK")){
			float abilityCost = hero.getMaxHp() * (ability.getResourceCost() / 100);
			if(hero.getHp() > Math.round(abilityCost)){
				Log.i(TAG, "Has hp enough for spell");
				return true;
			}
		}else{
			int resourceCostCost = ability.getResourceCost();
			if(hero.getResource() > resourceCostCost){
				Log.i(TAG, "Has resourses");
				return true;
			}
		}
		return false;
	}

	public void execute() {
		Log.i(TAG, "Execute done on spell ");
		if(hero.getClass_type().equals("WARLOCK")){
			float abilityCost = hero.getMaxHp() * (((float)ability.getResourceCost())/ 100);
			if(hero.getHp() > Math.round(abilityCost)){
				Log.i(TAG, "Has hp enough for spell hp cost:  "  + abilityCost);
				hero.takeDamage(Math.round(abilityCost));
			}
		}else{
			int resourceCostCost = ability.getResourceCost();
			Log.i(TAG, "Has resourses");
			hero.setResource(hero.getResource() - resourceCostCost);
			if(hero.getResource() < 0){
				hero.setResource(0);
			}
		}
	}

	public void setSpellCooldown(boolean clearSetCasting){
		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));
		getGameServer().sendGameStatus();
		if (clearSetCasting) {
			getAbility().setCasting(false);
		}
	}

	public void damageMinion(Minion minion, float damageAmount) {
		float totalDamage = Math.round(minion.calculateDamageReceived(damageAmount));
		if (minion.takeDamage(totalDamage)) {
			Log.i(TAG, "Found minion to attack : " + minion.getId() + " new hp is: " + minion.getHp());
			gameServer.minionDied(hero.getId(), minion.getId());
			gameServer.removeMinion(minion.getId());
		}else {
			minion.addThreat(new Threat(hero.getId(), 0.0f, totalDamage, 0.0f));
		}
	}


	public void healHero(Integer heroId, float amount) {
		float totalAmount = Math.round(amount);
		gameServer.getHeroById(heroId).heal(totalAmount);
	}

	public void damageHero(Integer heroId, float amount){
		float totalAmount = Math.round(amount);
		gameServer.getHeroById(heroId).takeDamage(totalAmount);
	}


	public void restoreResources(Integer heroId, float amount) {
		float totalAmount = Math.round(amount);
		gameServer.getHeroById(heroId).restoreResource(totalAmount);
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
