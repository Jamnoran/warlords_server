package game.util

import game.GameServer
import game.io.CommunicationHandler
import game.io.Requests.MinionAggroRequest
import game.io.Requests.UpdateHeroItemPositionRequest
import game.io.Responses.CombatTextResponse
import game.logging.Log
import game.logic.math.CalculationUtil
import game.models.*
import game.models.abilities.Buff
import game.models.enemies.Minion
import game.models.game.Amount
import game.models.game.Point
import game.models.game.Threat
import game.models.game.World
import game.models.heroes.Hero
import game.models.heroes.priest.Priest
import game.models.heroes.warlock.Warlock
import game.models.heroes.warrior.Warrior
import game.models.talents.Talent
import game.models.items.Item
import game.models.vo.Vector3
import java.util.*

/**
 * Created by Eric on 2017-02-02.
 */
class GameUtil {
    var gameServer: GameServer? = null

    private fun startHordeMinionSpawner(server: GameServer) {
        val thread = Thread {
            while (server.hordeMinionsLeft > 0) {
                val point = getRandomEnemySpawnPoint(server)
                val minion = server.spawnMinion(point.posX, point.posZ, point.posY)
                minion.desiredPositionX = 0f
                minion.desiredPositionZ = 0f
                server.hordeMinionsLeft = server.hordeMinionsLeft - 1
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    private fun getRandomEnemySpawnPoint(server: GameServer): Point {
        var point: Point? = null
        while (point == null) {
            val randomPos = CalculationUtil.getRandomInt(0, server.world!!.spawnPoints.size - 1)
            point = server.world!!.spawnPoints[randomPos]
            Log.i(
                TAG,
                "Trying to find a random spawn point at pos:  " + randomPos + " its of type : " + point.pointType
            )
            if (point.pointType == Point.SPAWN_POINT) {
                point = null
            }
        }
        return point
    }

    fun minionTargetInRange(parsedRequest: MinionAggroRequest) {
        if (gameServer != null && parsedRequest.minion_id != null) {
            val minion = getMinionById(parsedRequest.minion_id, gameServer!!.minions)
            if (minion != null && parsedRequest.hero_id > 0) {
//				Log.i(TAG, "Target is in range for an attack");
                minion.targetInRangeForAttack = true
            } else {
                Log.i(TAG, "Target is out of range for an attack")
                if (minion != null) {
                    minion.targetInRangeForAttack = false
                }
            }
        } else {
            Log.i(TAG, "Something wrong, either gameserver is null or minion_id is")
            if (gameServer == null) {
                Log.i(TAG, "GameServer is null")
            }
        }
    }

    /**
     * Goes through the list of heroes and returning the hero that has the lowest hp
     *
     * @return hero
     */
    val heroWithLowestHp: Hero?
        get() {
            var targetHero: Hero? = null
            if (gameServer != null) {
                for (lowestHpHero in gameServer!!.heroes) {
                    if (targetHero == null) {
                        targetHero = lowestHpHero
                    } else if (lowestHpHero.hp < targetHero.hp) {
                        targetHero = lowestHpHero
                    }
                }
            }
            return targetHero
        }

    private fun setTalents(hero: Hero, talents: ArrayList<Talent>) {
        hero.talents = talents
    }

    fun updateItemPosition(request: UpdateHeroItemPositionRequest) {
        Log.i(TAG, "Updated item " + request.itemId + " To pos: " + request.newPosition)
        DatabaseUtil.updateHeroItem(request.itemId.toLong(), request.newPosition, request.equipped)
        gameServer?.heroes?.let {
            getHeroById(request.heroId, it)
                ?.updateStats()
        }
        gameServer?.sendGameStatus()
    }

    fun dealDamageToMinion(hero: Hero?, minion: Minion, damage: Float) {
        if (minion.takeDamage(damage)) {
            Log.i(TAG, "Found minion to attack : " + minion.id + " Minion died!!!")
            gameServer!!.minionDied(hero!!.getId(), minion.id)
            // Send stop movement to all attacking this minion
            HeroUtil.stopHero(hero.id, gameServer)
        } else {
            Log.i(TAG, "Found minion to attack : " + minion.id + " new hp is: " + minion.hp)
            minion.addThreat(Threat(hero!!.getId(), 0.0f, damage, 0.0f))
        }
    }

    /**
     * A hero has clicked a portal, check that all have then start new level
     *
     * @param heroId
     */
    fun clickPortal(heroId: Int, heroes: ArrayList<Hero>) {
        Log.i(TAG, "This hero has clicked on the stairs $heroId")
        val hero = getHeroById(heroId, heroes)
        hero?.setStairsPressed()
        var startNewGame = true
        for (heroInList in heroes) {
            if (!heroInList.isStairsPressed) {
                startNewGame = false
            }
        }
        // Start new level
        if (startNewGame) {
            gameServer?.let{ server ->
                server.gameLevel = server.gameLevel + 1
                server.startLevel()
            }
        } else {
            Log.i(TAG, "Not all heroes has clicked the portal")
        }
    }

    /**
     * When a player join he needs to have a hero, this makes sure his hero join as the correct class etc.
     *
     * @param hero
     */
    fun addHero(hero: Hero, heroes: ArrayList<Hero?>) {
        gameServer?.sendWorldOperation(hero.getId())
        if (hero.isClass(Hero.WARRIOR)) {
            Log.i(TAG, "Added a warrior")
            val warrior = hero as Warrior
            warrior.generateHeroInformation()
            heroes.add(warrior)
        } else if (hero.isClass(Hero.PRIEST)) {
            Log.i(TAG, "Added a priest")
            val priest = hero as Priest
            priest.generateHeroInformation()
            heroes.add(priest)
        } else if (hero.isClass(Hero.WARLOCK)) {
            Log.i(TAG, "Added a warlock")
            val warlock = hero as Warlock
            warlock.generateHeroInformation()
            heroes.add(warlock)
        }
        Log.i(TAG, "Hero joined with this user id: " + hero.user_id + " characters in game: " + heroes.size)
        gameServer?.sendGameStatus()
        CommunicationHandler.sendAbilities("" + hero.user_id, gameServer)
        CommunicationHandler.sendTalents(hero.user_id, gameServer)
        HeroUtil.getHeroItems(hero, true, gameServer)
        gameServer?.setGameStarted()
    }

    fun addSpawnPoints(points: ArrayList<Point>) {
        var pointAdded = false
        Log.i(TAG, "Got this many spawnpoints : " + points.size)
        for (point in points) {
            if (gameServer?.world?.getSpawnPoint(point.location) == null) {
                Log.i(TAG, "Adding point of type : " + point.pointType + " position: " + point.location.toString())
                if (point.pointType == Point.SPAWN_POINT) {
                    pointAdded = true
                    gameServer?.world?.addSpawPoint(point)
                } else if (point.pointType == Point.ENEMY_POINT) {
                    gameServer?.world?.addSpawPoint(point)
                    if (gameServer?.world?.worldType != World.HORDE) {
                        gameServer?.spawnMinion(point.posX, point.posZ, point.posY)
                    }
                }
            }
        }
        if (gameServer?.world?.worldType == World.HORDE) {
            gameServer?.let{
                startHordeMinionSpawner(it)
            }
        }
        if (pointAdded) {
            Log.i(TAG, "Sending new spawn location for heroes")
            gameServer?.let{ gameServer ->
                gameServer.heroes.let{ heroes ->
                    for (hero in heroes) {
                        gameServer.world?.let{ world ->
                            val location = getFreeStartPosition(world)
                            hero.positionX = location!!.x
                            hero.positionZ = location.z
                            hero.positionY = location.y
                            hero.desiredPositionY = location.y
                            hero.desiredPositionX = location.x
                            hero.desiredPositionZ = location.z
                            Log.i(
                                TAG,
                                "Setting new location for hero " + hero.getId() + " " + hero.positionX + "x" + hero.positionZ + "y" + hero.positionY
                            )
                        }
                    }
                }
                CommunicationHandler.sendTeleportPlayers(gameServer)
            }
        }
    }

    fun handleMinionDebuffs(minions: ArrayList<Minion>, heroes: ArrayList<Hero>) {
        for (minion in minions.filter { it.isAlive }) {
            if (minion.deBuffs.isNotEmpty()) {
                val iterator = minion.deBuffs.iterator()
                while (iterator.hasNext()) {
                    val debuff = iterator.next()
                    val tickTimeMillis = debuff.tickTime.toLongOrNull() ?: continue
                    if (System.currentTimeMillis() >= tickTimeMillis) {
                        debuff.tickTime = (tickTimeMillis + debuff.duration).toString()
                        handleDebuffEffect(debuff, minion, heroes)
                        debuff.ticks--
                        if (debuff.ticks == 0) {
                            iterator.remove()
                        }
                    }
                }
                Log.i(TAG, "Minion has ${minion.deBuffs.size} debuffs remaining")
            }
        }
    }

    private fun handleDebuffEffect(debuff: Buff, minion: Minion, heroes: ArrayList<Hero>) {
        when (debuff.type) {
            Buff.DOT -> {
                val hero = getHeroById(debuff.heroId, heroes) ?: return
                dealDamageToMinion(hero, minion, debuff.value.toFloat())
                gameServer?.let { server ->
                    CommunicationHandler.sendCombatText(
                        CombatTextResponse(false, minion.id, debuff.value.toString(), false, COLOR_DAMAGE),
                        server
                    )
                }
            }
            else -> {
                // Do nothing, since the debuff type is not recognized
            }
        }
    }

    fun heroBuffs(heroes: ArrayList<Hero>) {
        Log.i(TAG, "Got tick to go through hero buffs")
        for (hero in heroes) {
            if (hero.buffs.size > 0) {
                val iterator = hero.buffs.iterator()
                while (iterator.hasNext()) {
                    val buff = iterator.next()
                    Log.i(TAG, "Hero [" + hero.getId() + "] got buffs : " + hero.buffs)
                    if (buff.type == Buff.HOT) {
                        Log.i(TAG, "Healed for " + buff.value)
                        hero.heal(Amount(buff.value.toFloat()))
                        gameServer?.let { server ->
                            CommunicationHandler.sendCombatText(
                                CombatTextResponse(
                                    true,
                                    hero.getId(),
                                    "" + buff.value,
                                    false,
                                    COLOR_HEAL
                                ), server
                            )
                        }
                        buff.ticks--
                        if (buff.ticks == 0) {
                            iterator.remove()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = GameUtil::class.java.simpleName
        const val COLOR_DAMAGE = "#FFFFFFFF"
        const val COLOR_HEAL = "#FF00FF00"
        const val COLOR_CRIT = "#FFFF0000"
        private const val itemsForEachLevel = 5
        @JvmStatic
		fun isWorldType(type: Int, worldLevel: Int): Boolean {
            return type == getWorldTypeFromLevel(worldLevel)
        }

        @JvmStatic
		fun getWorldTypeFromLevel(worldLevel: Int): Int {
            var worldType = worldLevel % 5
            if (worldType == 4 || worldType == 0) {
                worldType = World.CRAWLER
            }
            return worldType
        }

        @JvmStatic
		fun generateLoot(hero: Hero?): ArrayList<Item> {
            val loot = ArrayList<Item>()
            var items: ArrayList<Item>? = null
            if (hero != null) {
                items = DatabaseUtil.getItems(hero.level)
            }
            if (items == null) {
                items = ArrayList()
            }
            // Check if there is enough items to roll otherwise create new items
            if (items.size < itemsForEachLevel) {
                Log.i(
                    TAG,
                    "Not enough items to loot through, generate a couple more. " + items.size + " of " + itemsForEachLevel
                )
                for (i in 0 until itemsForEachLevel - items.size) {
                    val item = ItemUtil.generateItem(hero!!.level, hero)
                    Log.i(TAG, "Generated item cause there was not enough in database.")
                    items.add(item)
                    DatabaseUtil.addItem(item)
                }
            }

            // Roll if hero got these items
            for (item in items) {
                val rate = CalculationUtil.getRandomInt(0, 1000)
                Log.i(TAG, "Drop rate : " + rate + " /" + Math.round(item.dropRate * 1000))
                if (rate <= item.dropRate * 1000) {
                    item.generateInfo(true)
                    item.heroId = hero?.getId()?.toLong() ?: 0
                    DatabaseUtil.addLoot(item)
                    loot.add(item)
                }
            }
            return loot
        }

        @JvmStatic
		fun getHeroByUserId(user_id: String, heroes: ArrayList<Hero>): Hero? {
            for (hero in heroes) {
                if (hero.user_id != null && hero.user_id == user_id.toInt()) {
                    return hero
                }
            }
            return null
        }

        @JvmStatic
		fun getHeroById(heroId: Int, heroes: ArrayList<Hero>): Hero? {
            for (hero in heroes) {
                if (hero.getId() == heroId) {
                    return hero
                }
            }
            return null
        }

        @JvmStatic
		fun getMinionById(minionId: Int, minions: ArrayList<Minion>): Minion? {
            for (minion in minions) {
                if (minion.id == minionId) {
                    return minion
                }
            }
            return null
        }

        fun removeMinion(minionId: Int, minions: ArrayList<Minion>) {
            val ut = minions.iterator()
            while (ut.hasNext()) {
                val minion = ut.next()
                if (minion.id == minionId) {
                    ut.remove()
                    removeMinion(minion.id, minions)
                }
            }
        }

        @JvmStatic
		fun minionAggro(parsedRequest: MinionAggroRequest, minions: ArrayList<Minion>) {
            val minion = getMinionById(parsedRequest.minion_id, minions)
            if (minion != null && minion.heroIdWithMostThreat == null) {
                Log.i(
                    TAG,
                    "This minion had no aggro, add towards this hero [" + parsedRequest.hero_id + "] Since first to see it"
                )
                minion.addThreat(
                    Threat(
                        parsedRequest.hero_id,
                        Threat.inRangeThreath,
                        0f,
                        0f
                    )
                )
            } else {
                if (minion != null) {
                    Log.i(TAG, "Minion had aggro : " + minion.heroIdWithMostThreat)
                } else {
                    Log.i(TAG, "Did not find minion with id : " + parsedRequest.minion_id)
                }
            }
        }

        @JvmStatic
		fun updateMinionPositions(updatedMinions: ArrayList<Minion>, minions: ArrayList<Minion>) {
            // TODO: Do this more efficient
            for (minion in updatedMinions) {
                for (gameMinion in minions) {
                    if (minion.id == gameMinion.id) {
                        gameMinion.positionX = minion.positionX
                        gameMinion.positionY = minion.positionY
                        gameMinion.positionZ = minion.positionZ
                    }
                }
            }
        }

        /**
         * Loops through the different spawn points that's available to start on
         * @param world
         */
        fun getFreeStartPosition(world: World): Vector3? {
            world.spawnPoints?.shuffle(Random(System.nanoTime()))
            for (point in world.spawnPoints) {
                if (!point.isUsed && point.pointType == Point.SPAWN_POINT) {
                    val spawnPoint = point.location
                    point.isUsed = true
                    Log.i(TAG, "Found spawnpoint to use: $point")
                    return spawnPoint
                }
            }
            return null
        }
    }
}