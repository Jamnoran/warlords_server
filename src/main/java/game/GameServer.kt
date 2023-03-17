package game

import com.google.gson.Gson
import game.GameServer
import game.communication.ClientInfo
import game.communication.ServerDispatcher
import game.io.Requests.SpellRequest
import game.io.Responses.AbilityStatusResponse
import game.io.Responses.ClearWorldResponse
import game.io.Responses.GameStatusResponse
import game.io.Responses.MessageResponse
import game.logging.Log
import game.util.CommunicationUtil
import game.util.GameUtil
import game.util.SpellUtil
import game.util.TickEngine
import game.vo.*
import java.util.function.Consumer

/**
 * Created by Jamnoran on 27-Jun-16.
 */
class GameServer(server: ServerDispatcher?) {
    var isGameRunning = true
        private set
    private var gameStarted = false
    var server: ServerDispatcher?
    private var minionCount = 0
    var hordeMinionsLeft = 0
    val minions = ArrayList<Minion>()
    val heroes = ArrayList<Hero>()
    var animations = ArrayList<GameAnimation>()
    val allMessages = ArrayList<Message>()
    var world: World? = null
        private set
    @JvmField
	var gameLevel = 1
    private val spellUtil: SpellUtil
    val gameUtil: GameUtil
    val tickEngine: TickEngine

    val worldLevel: Int
        get() = world?.worldLevel ?: 0

    init {
        Log.i(TAG, "Game server is initilized, creating world")
        this.server = server
        createWorld()
        tickEngine = TickEngine(this@GameServer)
        tickEngine.startGameTicks()
        spellUtil = SpellUtil()
        gameUtil = GameUtil()
        gameUtil.gameServer = this
    }

    /**
     * Creates the world and all in it, minions, walls, lighting, stairs up and down.
     */
    private fun createWorld() {
        world = World().generate(this, 100, 100, gameLevel)
        hordeMinionsLeft = HORDE_MINIONS
    }

    /**
     * Send the world to a specific hero, this is done when the client joins the game
     *
     * @param heroIdToSend
     */
    fun sendWorldOperation(heroIdToSend: Int) {
        val thread: Thread = object : Thread() {
            override fun run() {
                CommunicationUtil.sendWorld(heroIdToSend, this@GameServer)
            }
        }
        thread.start()
    }

    /**
     * Spawns a minion at a specific point in the world, this is called from the World creation
     *
     * @param posX
     * @param posZ
     */
    fun spawnMinion(posX: Float, posZ: Float, posY: Float): Minion {
        minionCount++
        val minion = Minion(this)
        minion.id = minionCount
        minion.level = gameLevel
        minion.generateMinionInformation(posX, posZ, posY)
        minions.add(minion)
        Log.i(TAG, "Minions spawned : $minion")
        sendGameStatus()
        return minion
    }

    fun startLevel() {
        clearWorld()
        sendClearWorld()
        Log.i(TAG, "World is cleared. spawning a new world with a higher level")
        createWorld()
        for (heroLoop in heroes) {
            sendWorldOperation(heroLoop.getId())
        }
    }

    /**
     * Clear the world on server
     */
    private fun clearWorld() {
        minions.clear()
        world?.spawnPoints = null
        world = null
        for (hero in heroes) {
            hero.desiredPositionY = 0.0f
            hero.desiredPositionX = 0.0f
            hero.desiredPositionZ = 0.0f
            hero.positionY = 0.0f
            hero.positionX = 0.0f
            hero.positionZ = 0.0f
        }
    }

    /**
     * Send message to clients that they should clear their local world
     */
    private fun sendClearWorld() {
        server?.dispatchMessage(Message(Gson().toJson(ClearWorldResponse())))
    }

    /**
     * Sends the Game status down to the clients (this needs to improve that only new information is being sent, now everything is being sent)
     */
    fun sendGameStatus() {
        val response = GameStatusResponse(minions, heroes, animations)
        if (world?.isWorldType(World.HORDE) == true) {
            response.totalMinionsLeft = hordeMinionsLeft
        }
        server?.dispatchMessage(Message(Gson().toJson(response)))
        clearSentAnimations()
    }

    fun sendCastBarInformation(heroId: Int, ability: Ability?) {
        val jsonInString = Gson().toJson(AbilityStatusResponse(ability))
        server?.dispatchMessage(Message(getClientIdByHeroId(heroId), jsonInString))
    }

    /**
     * Removes all animations in list, this is done after animations have been sent to clients so we don't send them multiple times
     */
    private fun clearSentAnimations() {
        animations.clear()
    }

    fun sendMinionMoveAnimation(minionId: Int?) {
        animations.add(GameAnimation("MINION_RUN", null, minionId, null, 0))
    }

    fun endGame() {
        isGameRunning = false
    }

    fun restartLevel() {
        resetHeroes()
        startLevel()
    }

    private fun resetHeroes() {
        heroes.forEach(Consumer { obj: Hero -> obj.generateHeroInformation() })
    }

    fun minionDied(heroId: Int, minionId: Int?) {
        animations.add(GameAnimation("MINION_DIED", minionId, heroId, null, 0))

        heroes.forEach { hero ->
            GameUtil.generateLoot(hero)
        }
    }

    /**
     * Util method to get a client by his hero id, good to have if needing to send specified message to that client instead of to all clients
     *
     * @param heroId
     * @return
     */
    fun getClientIdByHeroId(heroId: Int): Int? {
        server?.clients?.let{ clients ->
            for (i in clients.indices) {
                val clientInfo = server!!.clients[i] as ClientInfo
                if (clientInfo.getHeroId() == heroId) {
                    return clientInfo.getId()
                }
            }
        }
        return null
    }

    fun sendSpell(parsedRequest: SpellRequest?) {
        spellUtil.spell(parsedRequest, this)
    }

    fun setGameStarted() {
        if (!gameStarted) {
            gameStarted = true
        }
    }

    fun addMessage(message: Message) {
        allMessages.add(message)
        val jsonInString = Gson().toJson(MessageResponse(message))
        server?.dispatchMessage(Message(jsonInString))
    }

    companion object {
        private val TAG = GameServer::class.java.simpleName
        private const val HORDE_MINIONS = 10
    }
}