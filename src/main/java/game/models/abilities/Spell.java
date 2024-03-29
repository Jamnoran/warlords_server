package game.models.abilities;

import game.GameServer;
import game.io.Responses.CombatTextResponse;
import game.io.Responses.RotateTargetResponse;
import game.logging.Log;
import game.models.game.Amount;
import game.models.enemies.Minion;
import game.models.game.Threat;
import game.models.vo.Vector3;
import game.models.heroes.Hero;
import game.logic.math.CalculationUtil;
import game.io.CommunicationHandler;
import game.util.GameUtil;

import java.util.ArrayList;

/**
 * Created by Jamnoran on 28-Nov-16.
 */
public class Spell {

	private static final String TAG = Spell.class.getSimpleName();
	private Ability ability;
	private Hero hero;
	private GameServer gameServer;
	private ArrayList<Minion> targetEnemies;
	private ArrayList<Hero> targetFriendlies;
	private final Vector3 position;
	private long time;
	private int CDTalentId;
	private int costTalentId;
	private int amountTalentId;

	public Spell(long time, Hero hero, Ability ability, GameServer gameServer, ArrayList<Integer> targetEnemies, ArrayList<Integer> targetFriendlies, Vector3 position) {
		this.time = time;
		this.hero = hero;
		this.ability = ability;
		this.gameServer = gameServer;
		this.targetEnemies = CalculationUtil.getMinionListById(targetEnemies, gameServer);
		this.targetFriendlies = CalculationUtil.getHeroListById(targetFriendlies, gameServer);
		this.position = position;
	}

	public boolean init(){
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
			if(targetEnemies == null || targetEnemies.size() == 0)
				return false;
			rotationResponse.setFriendly(false);
			rotationResponse.setIdOfTarget(targetFriendlies.get(0).getId());
		} else if (getAbility().getTargetType().equals("SINGLE_FRIENDLY") || getAbility().getTargetType().equals("HOT")) {
			if(targetFriendlies == null || targetFriendlies.get(0) == null)
				return false;
			rotationResponse.setIdOfTarget(targetFriendlies.get(0).getId());
		} else if (getAbility().getTargetType().equals("AOE")) {
			// It's valid to send a AOE spell without a target, it might not do anything but it should be possible
			rotationResponse.setFriendly(false);
			rotationResponse.setTargetPosition(position);
		} else if (getAbility().getTargetType().equals("CONE")) {
			// It's valid to send a CONE spell without a target, it might not do anything but it should be possible
			rotationResponse.setFriendly(false);
			rotationResponse.setTargetPosition(position);
		} else if(getAbility().getTargetType().equals("SINGLE_FRIENDLY_NOT_SELF")){
			if(targetFriendlies.size() == 0 || (targetFriendlies.size() == 1 && targetFriendlies.get(0).getId() != getHero().getId()))
				return false;
			rotationResponse.setIdOfTarget(targetFriendlies.get(0).getId());
		}
		// Send the rotation to all clients
		CommunicationHandler.sendRotateTargetResponse(rotationResponse, getGameServer());
		Log.i(TAG, "Send this rotation message : " + rotationResponse.toString());
		return true;
	}

	private boolean checkForResourses(Hero hero, Ability ability) {
		float resCost = ability.calculateResourceCost(hero, this);
		if(hero.getClass_type().equals("WARLOCK")){
			float abilityCost = hero.getMaxHp() * (resCost / 100);
			if(hero.getHp() > Math.round(abilityCost)){
				Log.i(TAG, "Has hp enough for spell");
				return true;
			}
		}else{
			if(hero.getResource() >= resCost){
				Log.i(TAG, "Has resources");
				return true;
			}else{
				Log.i(TAG, "Mana cost : " + resCost + " Mana left : " + hero.getResource());
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
				hero.takeDamage(Math.round(abilityCost), 0, Amount.TRUE_DAMAGE);
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
		getGameServer().sendCastBarInformation(hero.getId(), getAbility());
	}

	public void setSpellCooldown(){
		// Set the cooldown for this ability
		getAbility().setMillisLastUse(getTime());
		getAbility().setTimeWhenOffCooldown("" + (getTime() + getAbility().calculateCooldown(getHero(), this)));
//		 TODO: This should we be able remove
//		getGameServer().sendGameStatus();
	}

	public void damageMinion(Minion minion, Amount damageAmount, float penetration, String damageType) {
		float totalDamageAfterReduction = Math.round(minion.calculateDamageReceived(damageAmount, penetration, damageType));
		CommunicationHandler.sendCombatText(new CombatTextResponse(false, minion.getId(), "" + totalDamageAfterReduction, damageAmount.isCrit(), GameUtil.COLOR_DAMAGE), gameServer);
		if (minion.takeDamage(totalDamageAfterReduction)) {
			gameServer.minionDied(hero.getId(), minion.getId());
		}else {
			minion.addThreat(new Threat(hero.getId(), 0.0f, totalDamageAfterReduction, 0.0f));
		}
	}


	public void healHero(Integer heroId, Amount amount) {
		GameUtil.getHeroById(heroId, gameServer.getHeroes()).heal(amount);
		CommunicationHandler.sendCombatText(new CombatTextResponse(true, hero.getId(), "" + amount.getAmount(), amount.isCrit(), GameUtil.COLOR_HEAL), gameServer);
	}

	public void damageHero(Integer heroId, float amount){
		float totalAmount = Math.round(amount);
		GameUtil.getHeroById(heroId, gameServer.getHeroes()).takeDamage(totalAmount, 0, Amount.TRUE_DAMAGE);
	}

	public void restoreResources(Integer heroId, float amount) {
		float totalAmount = Math.round(amount);
		GameUtil.getHeroById(heroId, gameServer.getHeroes()).restoreResource(totalAmount);
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setCDTalentId(int CDTalentId) {
		this.CDTalentId = CDTalentId;
	}

	public int getCDTalentId() {
		return CDTalentId;
	}

	public int getCostTalentId() {
		return costTalentId;
	}

	public void setCostTalentId(int costTalentId) {
		this.costTalentId = costTalentId;
	}

	public int getAmountTalentId() {
		return amountTalentId;
	}

	public void setAmountTalentId(int amountTalentId) {
		this.amountTalentId = amountTalentId;
	}

	public Vector3 getPosition() {
		return position;
	}

	public ArrayList<Minion> getTargetEnemies() {
		return targetEnemies;
	}

	public void setTargetEnemies(ArrayList<Minion> targetEnemies) {
		this.targetEnemies = targetEnemies;
	}

	public ArrayList<Hero> getTargetFriendlies() {
		return targetFriendlies;
	}

	public void setTargetFriendlies(ArrayList<Hero> targetFriendlies) {
		this.targetFriendlies = targetFriendlies;
	}
}
