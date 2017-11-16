package game.spells;

import game.GameServer;
import game.io.CombatTextResponse;
import game.io.RotateTargetResponse;
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
	private final Vector3 position;
	private long time;


	public Spell(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemy, ArrayList<Integer> targetFriendly, Vector3 position) {
		this.time = time;
		this.hero = hero;
		this.ability = ability;
		this.gameServer = gameServer;
		this.targetEnemy = targetEnemy;
		this.targetFriendly = targetFriendly;
		this.position = position;
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
			Log.i(TAG, "Has not enough resources");
			return false;
		}

		// Return false if not off cd
		if(!ability.isAbilityOffCD(time)){
			Log.i(TAG, "Ability not of cooldown");
			return false;
		}

		// Return false if no target (Check ability target type)
		if(!checkTargetTypeIsCorrect()){
			Log.i(TAG, "Sent up wrong target type, we should cancel ability");
			return false;
		}

		//getGameServer().sendCooldownInformation(ability, hero.getId());
		Log.i(TAG, "Spell is ok to cast");
		return true;
	}

	private boolean checkTargetTypeIsCorrect() {
		// Send rotation
		RotateTargetResponse rotationResponse = new RotateTargetResponse();
		if (getAbility().getTargetType().equals("SINGLE") || getAbility().getTargetType().equals("DOT")){
			if(targetEnemyList == null || targetEnemyList.size() == 0)
				return false;
			rotationResponse.setFriendly(false);
			rotationResponse.setIdOfTarget(targetEnemyList.get(0).getId());
		} else if (getAbility().getTargetType().equals("SINGLE_FRIENDLY") || getAbility().getTargetType().equals("HOT")) {
			if(targetFriendlyList.size() == 0)
				return false;
			rotationResponse.setIdOfTarget(targetFriendlyList.get(0).getId());
		} else if (getAbility().getTargetType().equals("AOE")) {
			// It's valid to send a AOE spell without a target, it might not do anything but it should be possible
			rotationResponse.setFriendly(false);
			rotationResponse.setTargetPosition(position);
		} else if (getAbility().getTargetType().equals("CONE")) {
			// It's valid to send a CONE spell without a target, it might not do anything but it should be possible
			rotationResponse.setFriendly(false);
			rotationResponse.setTargetPosition(position);
		} else if(getAbility().getTargetType().equals("SINGLE_FRIENDLY_NOT_SELF")){
			if(targetFriendlyList.size() == 0 || (targetFriendlyList.size() == 1 && targetFriendlyList.get(0).getId() != getHero().getId()))
				return false;
			rotationResponse.setIdOfTarget(targetFriendlyList.get(0).getId());
		}
		// Send the rotation to all clients
		getGameServer().sendRotateTargetResponse(rotationResponse);
		Log.i(TAG, "Send this rotation message : " + rotationResponse.toString());
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
			if(hero.getResource() >= resourceCostCost){
				Log.i(TAG, "Has resources");
				return true;
			}else{
				Log.i(TAG, "Mana cost : " + resourceCostCost + " Mana left : " + hero.getResource());
			}

		}
		return false;
	}

	public void execute() {
		Log.i(TAG, "Execute done on spell ");
		if(hero.getClass_type().equals("WARLOCK")){
			float abilityCost = hero.getMaxHp() * (((float)ability.getResourceCost())/ 100); // Cost is in %
			if(hero.getHp() > Math.round(abilityCost)){
				Log.i(TAG, "Has hp enough for spell hp cost:  "  + abilityCost);
				// TODO: Need to have check if ability can cause lethal damage
				hero.takeDamage(Math.round(abilityCost), 0, "TRUE");
			}
		}else{
			float resourceCostCost = ability.getResourceCost();
			Log.i(TAG, "Has resources");
			hero.setResource(hero.getResource() - resourceCostCost);
			if(hero.getResource() < 0){
				hero.setResource(0);
			}
		}

		setSpellCooldown();
		// Send castbar information
		getGameServer().sendCastBarInformation(getAbility());
	}

	public void setSpellCooldown(){
		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().getBaseCD()));
		// TODO: This should we be able remove
		getGameServer().sendGameStatus();
	}

	public void damageMinion(Minion minion, Amount damageAmount, float penetration, String damageType) {
		float totalDamageAfterReduction = Math.round(minion.calculateDamageReceived(damageAmount, penetration, damageType));
		gameServer.sendCombatText(new CombatTextResponse(false, minion.getId(), "" + totalDamageAfterReduction, damageAmount.isCrit(), "#FFFF0000"));
		if (minion.takeDamage(totalDamageAfterReduction)) {
			Log.i(TAG, "Found minion to attack : " + minion.getId() + " new hp is: " + minion.getHp());
			gameServer.minionDied(hero.getId(), minion.getId());
		}else {
			minion.addThreat(new Threat(hero.getId(), 0.0f, totalDamageAfterReduction, 0.0f));
		}
	}


	public void healHero(Integer heroId, Amount amount) {
		gameServer.getHeroById(heroId).heal(amount);
		gameServer.sendCombatText(new CombatTextResponse(true, hero.getId(), "" + amount.getAmount(), amount.isCrit(), "#FF00FF00"));
	}

	public void damageHero(Integer heroId, float amount){
		float totalAmount = Math.round(amount);
		gameServer.getHeroById(heroId).takeDamage(totalAmount, 0, "TRUE");
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
