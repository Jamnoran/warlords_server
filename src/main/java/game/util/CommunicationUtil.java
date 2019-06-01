package game.util;

import com.google.gson.Gson;
import game.communication.ClientInfo;
import game.GameServer;
import game.io.JsonResponse;
import game.io.Responses.*;
import game.logging.Log;
import game.vo.*;

import java.util.ArrayList;

public class CommunicationUtil {

	private static final String TAG = CommunicationUtil.class.getSimpleName();

	static void sendTeleportPlayers(GameServer server) {
		String jsonInString = new Gson().toJson(new TeleportHeroesResponse(server.getHeroes()));
		server.getServer().dispatchMessage(new Message(jsonInString));
	}

	public static void sendRotateTargetResponse(RotateTargetResponse response, GameServer server){
		String jsonInString = new Gson().toJson(response);
		if (server != null && server.getServer() != null) {
			server.getServer().dispatchMessage(new Message(jsonInString));
		}
	}

	public static void sendCombatText(CombatTextResponse combatTextResponse, GameServer server) {
		String jsonInString = new Gson().toJson(combatTextResponse);
		if (server != null && server.getServer() != null) {
			server.getServer().dispatchMessage(new Message(jsonInString));
		}
	}

	/**
	 * This will be called when a hero joins a game or when requested
	 *
	 * @param userId
	 */
	public static void sendAbilities(String userId, GameServer server) {
		Gson gson = new Gson();
		Hero hero = GameUtil.getHeroByUserId(userId, server.getHeroes());
		if (hero != null) {
			ArrayList<Ability> heroAbilities = DatabaseUtil.getAllAbilities(hero.getClass_type());
			ArrayList<AbilityPosition> abilityPositions = DatabaseUtil.getHeroAbilityPositions(hero.getId());
			for (AbilityPosition abilityPosition : abilityPositions) {
				for (Ability ability : heroAbilities) {
					if (abilityPosition.getAbilityId() == ability.getId()) {
						ability.setPosition(abilityPosition.getPosition());
					}
				}
			}

			heroAbilities.sort(new Ability());

			hero.setAbilities(heroAbilities);
			String jsonInString = gson.toJson(new AbilitiesResponse(heroAbilities));
			if (server != null) {
				server.getServer().dispatchMessage(new Message(server.getClientIdByHeroId(hero.getId()), jsonInString));
			}
		} else {
			Log.i(TAG, "Did not find hero with user id : " + userId);
		}
	}

	public static void sendTalents(Integer userId, GameServer server) {
		Gson gson = new Gson();
		Hero hero = GameUtil.getHeroByUserId("" + userId, server.getHeroes());
		Log.i(TAG, "Sending talents to user");
		if (hero != null) {
			ArrayList<Talent> talents = DatabaseUtil.getHeroTalents(hero.getId());
			hero.setTalents(talents);
			hero.recalculateStats();
			int totalPoints = hero.getLevel();
			String jsonInString = gson.toJson(new TalentResponse(talents, totalPoints));
			if (server != null) {
				server.getServer().dispatchMessage(new Message(server.getClientIdByHeroId(hero.getId()), jsonInString));
			}
		}
	}

	/**
	 * Sends the created world down to a specific client
	 *
	 * @param heroId
	 */
	public static void sendWorld(Integer heroId, GameServer server) {
		String jsonInString = new Gson().toJson(new WorldResponse(server.getWorld()));
		if (server != null) {
			Log.i(TAG, "Sending world to hero: " + heroId);
			server.getServer().dispatchMessage(new Message(server.getClientIdByHeroId(heroId), jsonInString));
		}
	}

	/**
	 * Sending cool down use down to a specific user
	 *
	 * @param abi
	 * @param heroId
	 */
	public static void sendCooldownInformation(Ability abi, Integer heroId, GameServer server) {
		String jsonInString = new Gson().toJson(new CooldownResponse(abi));
		if (server != null) {
			server.getServer().dispatchMessage(new Message(server.getClientIdByHeroId(heroId), jsonInString));
		}
	}

	public static void sendRequestMinionPosition(GameServer server) {
		if (server.getServer() != null && server.getServer().getClientCount() > 0) {
			int positionOfRandomClient = CalculationUtil.getRandomInt(0, (server.getServer().getClientCount() - 1));
			ClientInfo clientInfo = (ClientInfo) server.getServer().getClients().get(positionOfRandomClient);
			server.getServer().dispatchMessage(new Message(clientInfo.getId(), new Gson().toJson(new JsonResponse("UPDATE_MINION_POSITION", 200))));
		}
	}
}
