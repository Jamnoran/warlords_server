package game.util;

import com.google.gson.Gson;
import game.GameServer;
import game.io.Requests.MoveRequest;
import game.io.Responses.CombatTextResponse;
import game.io.Responses.HeroItemsResponse;
import game.io.Responses.StopHeroResponse;
import game.logging.Log;
import game.vo.*;

import java.util.ArrayList;

public class HeroUtil {
	private static final String TAG = HeroUtil.class.getSimpleName();

	public static void stopHero(Integer heroId, GameServer gameServer) {
		Hero hero = GameUtil.getHeroById(heroId, gameServer.getHeroes());
		hero.setDesiredPositionX(hero.getPositionX());
		hero.setDesiredPositionZ(hero.getPositionZ());
		Log.i(TAG, "Send stop hero to heroId : " + heroId);
		String jsonInString = new Gson().toJson(new StopHeroResponse(heroId));
		if (gameServer.getServer() != null) {
			gameServer.getServer().dispatchMessage(new Message(jsonInString));
		}

		gameServer.getAnimations().add(new GameAnimation("HERO_IDLE", null, heroId, null, 0));
		gameServer.sendGameStatus();
	}


	public static void heroMove(MoveRequest parsedRequest, GameServer gameServer) {
		//Log.i(TAG, "User wants to move : " + parsedRequest.getHeroId());
		Hero usersHero = GameUtil.getHeroById(parsedRequest.getHeroId(), gameServer.getHeroes());
		if (usersHero != null) {
			usersHero.setPositionX(parsedRequest.getPositionX());
			usersHero.setPositionY(parsedRequest.getPositionY());
			usersHero.setPositionZ(parsedRequest.getPositionZ());
			usersHero.setDesiredPositionX(parsedRequest.getDesiredPositionX());
			usersHero.setDesiredPositionY(parsedRequest.getDesiredPositionY());
			usersHero.setDesiredPositionZ(parsedRequest.getDesiredPositionZ());
			//Log.i(TAG, "Hero : " + usersHero.toString());

			gameServer.getAnimations().add(new GameAnimation("HERO_RUN", null, usersHero.getId(), null, 0));
		}
		gameServer.sendGameStatus();
	}

	public static ArrayList<Item> getHeroItems(Hero hero, boolean sendToClient, GameServer gameServer) {
		ArrayList<Item> items = DatabaseUtil.getLoot(hero.getId());
		hero.setItems(items);
		if(sendToClient){
			HeroItemsResponse response = new HeroItemsResponse(items);
			if (gameServer.getServer() != null) {
				String json = new Gson().toJson(response);
				Log.i(TAG, "Sending these items to client : " + json);
				gameServer.getServer().dispatchMessage(new Message(gameServer.getClientIdByHeroId(hero.getId()), json));
			}
		}
		return items;
	}

	public static void heroIdle(MoveRequest parsedRequest, GameServer gameServer) {
		Hero usersHero = GameUtil.getHeroById(parsedRequest.getHeroId(), gameServer.getHeroes());
		gameServer.getAnimations().add(new GameAnimation("HERO_IDLE", null, usersHero.getId(), null, 0));
	}

	/**
	 * This method is called when a minion attacks a hero.
	 *
	 * @param heroId
	 * @param damage
	 * @param minionId
	 */
	public static void attackHero(Integer heroId, Amount damage, Integer minionId, GameServer gameServer) {
		//Log.i(TAG, "Minion : " + minionId + " attacked hero: " + heroId);
		Hero hero = GameUtil.getHeroById(heroId, gameServer.getHeroes());
		if (hero != null) {
			gameServer.getAnimations().add(new GameAnimation("MINION_ATTACK", heroId, minionId, null, 0));
			gameServer.sendGameStatus();

			final float fDamage = hero.calculateDamageReceived(damage);
			Thread thread = new Thread() {
				public void run() {
					int timeAfterAnimationHasStartedToDamageIsDealt = 1100;
					try {
						sleep(timeAfterAnimationHasStartedToDamageIsDealt);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Minion min = GameUtil.getMinionById(minionId, gameServer.getMinions());
					if (min != null && min.getHp() > 0) {
						hero.takeDamage(fDamage, min.getArmorPenetration(), Amount.PHYSICAL);

						// Check if hero has retaliation buff
						hero.checkForRetaliation(min);

						CommunicationUtil.sendCombatText(new CombatTextResponse(true, hero.getId(), "" + damage.getAmount(), damage.isCrit(), GameUtil.COLOR_CRIT), gameServer);
						if (hero.getHp() <= 0) {
							Log.i(TAG, "Hero died, send death animation to client");
							int numbersAlive = 0;
							for (Hero listHero : gameServer.getHeroes()) {
								if (listHero.getHp() > 0) {
									numbersAlive++;
								}
							}
							if (numbersAlive == 0) {
								Log.i(TAG, "Nobody is alive, send endgame screen");
							}
						}
					}
					gameServer.sendGameStatus();
				}
			};
			thread.start();
		}else {
			Log.i(TAG, "Could not find hero");
		}
	}


	/**
	 * This method is called when a hero wants to attack a minion,
	 *
	 * @param heroId
	 * @param minionId
	 */
	public static void attack(Integer heroId, Integer minionId, long timeForAttackRequest, GameServer gameServer) {
		Hero hero = GameUtil.getHeroById(heroId, gameServer.getHeroes());
		if (hero != null && hero.readyForAutoAttack(timeForAttackRequest)) {
			Minion minion = GameUtil.getMinionById(minionId, gameServer.getMinions());
			if (minion != null) {
				gameServer.getAnimations().add(new GameAnimation("ATTACK", minionId, hero.id, null, 0));
				gameServer.sendGameStatus();

				Thread thread = new Thread() {
					public void run() {
						int timeAfterAnimationHasStartedToDamageIsDealt = 500;
						try {
							sleep(timeAfterAnimationHasStartedToDamageIsDealt);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						Log.i(TAG, "Found hero that's attacking : " + hero.getClass_type() + " hp of minion is : " + minion.getHp());
						Amount amount = hero.getAttackDamage();
						float totalDamage = Math.round(minion.calculateDamageReceived(amount, hero.getPenetration(Amount.PHYSICAL), Amount.PHYSICAL));
						gameServer.getGameUtil().dealDamageToMinion(hero, minion, totalDamage);

						CommunicationUtil.sendCombatText(new CombatTextResponse(false, minion.getId(), "" + amount.getAmount(), amount.isCrit(), GameUtil.COLOR_DAMAGE), gameServer);

						//sendCooldownInformation(hero.getAbility(0), hero.getId());

						Log.i(TAG, "Minion size now: " + gameServer.getMinions().size());
						// Send updated status a while after animation is sent for mapping to animation hitting minion.
						gameServer.sendGameStatus();
						gameServer.sendCastBarInformation(hero.getId(), hero.getAbility(0));
					}
				};
				thread.start();
			} else {
				Log.i(TAG, "Hero is trying to attack a minion that is already dead or non existing");
			}
		}
	}
}
