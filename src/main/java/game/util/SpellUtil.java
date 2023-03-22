package game.util;

import game.GameServer;
import game.io.Requests.SpellRequest;
import game.logging.Log;
import game.models.heroes.priest.spells.PriestHeal;
import game.models.heroes.priest.spells.PriestHealOverTime;
import game.models.heroes.priest.spells.PriestShield;
import game.models.heroes.priest.spells.PriestSmite;
import game.models.heroes.warlock.spells.WarlockBloodBolt;
import game.models.heroes.warlock.spells.WarlockDrainLife;
import game.models.heroes.warlock.spells.WarlockHaemorrhage;
import game.models.heroes.warlock.spells.WarlockRestore;
import game.models.heroes.warrior.spells.WarriorCharge;
import game.models.heroes.warrior.spells.WarriorCleave;
import game.models.heroes.warrior.spells.WarriorTaunt;
import game.models.heroes.Hero;
import game.models.heroes.priest.Priest;
import game.models.heroes.warlock.Warlock;
import game.models.heroes.warrior.Warrior;

public class SpellUtil {

    private static final String TAG = SpellUtil.class.getSimpleName();
    private GameServer gameServer = null;

    /**
     * Important method that handles the spells sent up from clients that a hero wants to initiate a spell
     * This method takes as parameters
     * heroId
     * spellId
     * targetFriendly
     * targetEnemy
     * Location
     *
     * @param parsedRequest
     */
    public void spell(SpellRequest parsedRequest, GameServer gServer) {
        gameServer = gServer;
        Log.i(TAG, "Handle spell " + parsedRequest.toString());
        Hero hero = GameUtil.getHeroById(parsedRequest.getHeroId(), gameServer.getHeroes());
        switch (parsedRequest.getSpell_id()) {
            case 8:
                Log.i(TAG, "Warrior used taunt!");
                warriorTaunt((Warrior) hero, parsedRequest);
                break;
            case 7:
                Log.i(TAG, "Warrior used cleave!");
                warriorCleave((Warrior) hero, parsedRequest);
                break;
            case 1:
                Log.i(TAG, "Warrior used Charge!");
                warriorCharge((Warrior) hero, parsedRequest);
                break;
            case 2:
                Log.i(TAG, "Priest used Heal!");
                priestHeal((Priest) hero, parsedRequest);
                break;
            case 3:
                Log.i(TAG, "Priest used Smite!");
                priestSmite((Priest) hero, parsedRequest);
                break;
            case 6:
                Log.i(TAG, "Priest used Shield!");
                priestShield((Priest) hero, parsedRequest);
                break;
            case 5:
                Log.i(TAG, "Priest used Heal over time!");
                priesHealOverTime((Priest) hero, parsedRequest);
                break;
            case 26:
                Log.i(TAG, "Warlock used drain life!");
                warlockDrain((Warlock) hero, parsedRequest);
                break;
            case 27:
                Log.i(TAG, "Warlock used haemorrhage!");
                warlockHaemorrhage((Warlock) hero, parsedRequest);
                break;
            case 28:
                Log.i(TAG, "Warlock used restore!");
                warlockRestore((Warlock) hero, parsedRequest);
                break;
            case 29:
                Log.i(TAG, "Warlock used restore!");
                warlockBloodBolt((Warlock) hero, parsedRequest);
                break;
            default:
                Log.i(TAG, "Did not find spell with id: " + parsedRequest.getSpell_id());
                break;
        }
        gServer.sendGameStatus();
    }



    // All spells


    //          Priest


    private void priestHeal(Priest hero, SpellRequest parsedRequest) {
        PriestHeal spell = new PriestHeal(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of resource or cd");
        }
    }

    private void priestSmite(Priest hero, SpellRequest parsedRequest) {
        PriestSmite spell = new PriestSmite(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void priestShield(Priest hero, SpellRequest parsedRequest) {
        PriestShield spell = new PriestShield(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void priesHealOverTime(Priest hero, SpellRequest parsedRequest) {
        PriestHealOverTime spell = new PriestHealOverTime(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void priesAOEHeal(Priest hero, SpellRequest parsedRequest) {

    }

    private void priesBuff(Priest hero, SpellRequest parsedRequest) {

    }


    //          Warrior
    private void warriorCleave(Warrior hero, SpellRequest parsedRequest) {
        WarriorCleave spell = new WarriorCleave(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        spell.setInitialCast(parsedRequest.isInitialCast());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void warriorTaunt(Warrior hero, SpellRequest parsedRequest) {
        WarriorTaunt spell = new WarriorTaunt(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        spell.setInitialCast(parsedRequest.isInitialCast());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void warriorCharge(Warrior hero, SpellRequest parsedRequest) {
        WarriorCharge spell = new WarriorCharge(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void warriorSlam(Warrior hero, SpellRequest parsedRequest) {

    }

    private void warriorBarricade(Warrior hero, SpellRequest parsedRequest) {

    }

    private void warriorBuff(Warrior hero, SpellRequest parsedRequest) {

    }

    // 		Warlock

    private void warlockDrain(Warlock hero, SpellRequest parsedRequest) {
        WarlockDrainLife spell = new WarlockDrainLife(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void warlockHaemorrhage(Warlock hero, SpellRequest parsedRequest) {
        WarlockHaemorrhage spell = new WarlockHaemorrhage(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void warlockRestore(Warlock hero, SpellRequest parsedRequest) {
        WarlockRestore spell = new WarlockRestore(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

    private void warlockBloodBolt(Warlock hero, SpellRequest parsedRequest) {
        WarlockBloodBolt spell = new WarlockBloodBolt(parsedRequest.getTime(), hero, hero.getAbility(parsedRequest.getSpell_id()), gameServer, parsedRequest.getTarget_enemy(), parsedRequest.getTarget_friendly(), parsedRequest.getVector());
        if (spell.init()) {
            spell.execute();
        } else {
            Log.i(TAG, "Could not send spell, probably because of mana or cd");
        }
    }

}
