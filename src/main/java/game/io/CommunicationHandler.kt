package game.io

import com.google.gson.Gson
import game.GameServer
import game.io.Responses.AbilitiesResponse
import game.io.Responses.CombatTextResponse
import game.io.Responses.CooldownResponse
import game.io.Responses.RotateTargetResponse
import game.io.Responses.TalentResponse
import game.io.Responses.TeleportHeroesResponse
import game.io.Responses.WorldResponse
import game.logging.Log
import game.models.abilities.Ability
import game.models.server.Message
import game.util.DatabaseUtil
import game.util.GameUtil.Companion.getHeroByUserId

object CommunicationHandler {
    private val TAG = CommunicationHandler::class.java.simpleName

    fun sendTeleportPlayers(server: GameServer) {
        sendRequest(TeleportHeroesResponse(server.heroes), server)
    }

    @JvmStatic
    fun sendRotateTargetResponse(response: RotateTargetResponse, server: GameServer) {
        sendRequest(response, server)
    }

    @JvmStatic
    fun sendCombatText(combatTextResponse: CombatTextResponse, server: GameServer) {
        sendRequest(combatTextResponse, server)
    }

    /**
     * This will be called when a hero joins a game or when requested
     *
     * @param userId
     */
    @JvmStatic
    fun sendAbilities(userId: String, server: GameServer?) {
        val hero = getHeroByUserId(userId, server!!.heroes)
        if (hero != null) {
            val heroAbilities = DatabaseUtil.getAllAbilities(hero.class_type)
            val abilityPositions = DatabaseUtil.getHeroAbilityPositions(hero.getId())
            for (abilityPosition in abilityPositions) {
                for (ability in heroAbilities) {
                    if (abilityPosition.abilityId == ability.id) {
                        ability.position = abilityPosition.position
                    }
                }
            }
            heroAbilities.sortWith(Ability())
            hero.abilities = heroAbilities
            sendRequest(AbilitiesResponse(heroAbilities), server, hero.getId())
        } else {
            Log.i(TAG, "Did not find hero with user id : $userId")
        }
    }

    fun sendTalents(userId: Int, server: GameServer?) {
        val hero = getHeroByUserId("" + userId, server!!.heroes)
        Log.i(TAG, "Sending talents to user")
        if (hero != null) {
            val talents = DatabaseUtil.getHeroTalents(hero.getId())
            hero.talents = talents
            hero.recalculateStats()
            val totalPoints = hero.level
            sendRequest(TalentResponse(talents, totalPoints), server, hero.getId())
        }
    }

    /**
     * Sends the created world down to a specific client
     *
     * @param heroId
     */
    fun sendWorld(heroId: Int, server: GameServer) {
        sendRequest(WorldResponse(server.world), server, heroId)
        Log.i(TAG, "Sending world to hero: $heroId")
    }

    /**
     * Sending CoolDown use down to a specific user
     *
     * @param abi
     * @param heroId
     */
    fun sendCoolDownInformation(abi: Ability?, heroId: Int, server: GameServer) {
        sendRequest(CooldownResponse(abi), server, heroId)
    }

    @JvmStatic
    fun sendRequestMinionPosition(server: GameServer) {
        if (server.server != null && server.server!!.clientCount > 0) {
            sendRequest(JsonResponse("UPDATE_MINION_POSITION", 200), server, server.heroes.random().id)
        }
    }

    private fun sendRequest(data: Any, server: GameServer) {
        val jsonInString = Gson().toJson(data)
        server.server?.dispatchMessage(Message(jsonInString))
    }

    private fun sendRequest(data: Any, server: GameServer, heroId: Int) {
        val jsonInString = Gson().toJson(data)
        server.server?.dispatchMessage(Message(server.getClientIdByHeroId(heroId), jsonInString))
    }
}
