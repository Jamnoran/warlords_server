package game.models.game

import game.models.game.Tick

/**
 * Created by Eric on 2017-09-15.
 */
class Tick(timeToActivate: Long, typeOfTick: Int) : Comparable<Tick> {
    @JvmField
	var timeToActivate: Long = 0
    @JvmField
	var typeOfTick = 0

    init {
        this.timeToActivate = timeToActivate
        this.typeOfTick = typeOfTick
    }

    override fun compareTo(tickToCompare: Tick): Int {
        return java.lang.Long.compare(timeToActivate, tickToCompare.timeToActivate)
    }

    companion object {
		const val GAME_STATUS = 1
        const val HERO_REGEN = 2
        const val BUFF = 3
        const val DEBUFF = 4
        const val  MINION_ACTION = 5
        const val  REQUEST_MINION_POSITION = 6
        const val MINION_DEBUFF = 7
    }
}