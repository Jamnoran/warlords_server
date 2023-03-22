package test.util;

import game.models.heroes.Hero;
import game.models.abilities.Ability;
import game.models.abilities.AbilityPosition;
import game.models.talents.Talent;
import game.models.items.Item;
import game.util.DatabaseUtil;
import game.models.heroes.priest.Priest;
import game.models.heroes.warrior.Warrior;

import java.util.ArrayList;
import java.util.Collections;

public class HeroHelper {

	public static Warrior getWarrior(){
		Hero hero = DatabaseUtil.getHero(14);
		Warrior warrior = (Warrior) hero;

		warrior.setAbilities(getAbilities(warrior));

		warrior.setTalents(getTalents(warrior));

		warrior.setItems(getItems(warrior));

		warrior.generateHeroInformation();

		return warrior;
	}

	public static Priest getPriest(){
		Hero hero = DatabaseUtil.getHero(13);
		Priest priest = (Priest) hero;

		priest.setAbilities(getAbilities(priest));

		priest.setTalents(getTalents(priest));

		priest.setItems(getItems(priest));

		priest.generateHeroInformation();
		return priest;
	}

	private static ArrayList<Talent> getTalents(Hero hero) {
		ArrayList<Talent> talents = DatabaseUtil.getHeroTalents(hero.getId());
		return talents;
	}

	public static ArrayList<Ability> getAbilities(Hero hero) {
		ArrayList<Ability> heroAbilities = DatabaseUtil.getAllAbilities(hero.getClass_type());
		ArrayList<AbilityPosition> abilityPositions = DatabaseUtil.getHeroAbilityPositions(hero.getId());
		for (AbilityPosition abilityPosition : abilityPositions) {
			for (Ability ability : heroAbilities) {
				if (abilityPosition.getAbilityId() == ability.getId()) {
					ability.setPosition(abilityPosition.getPosition());
				}
			}
		}

		Collections.sort(heroAbilities, new Ability());

		return heroAbilities;
	}


	private static ArrayList<Item> getItems(Hero hero) {
		return DatabaseUtil.getLoot(hero.getId());
	}
}
