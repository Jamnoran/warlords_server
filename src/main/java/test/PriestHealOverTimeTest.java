package test;

import game.GameServer;
import game.spells.PriestHealOverTime;
import game.util.DatabaseUtil;
import game.vo.Ability;
import game.vo.AbilityPosition;
import game.vo.Hero;
import game.vo.Vector3;
import game.vo.classes.Priest;

import java.util.ArrayList;
import java.util.Collections;

public class PriestHealOverTimeTest {

	@org.junit.Test
	public void castTimeCompleted() {

		long time = System.currentTimeMillis();
		Hero hero = DatabaseUtil.getHero(13);
		Priest priest = (Priest) hero;

		priest.generateHeroInformation();

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

		priest.setAbilities(heroAbilities);

		GameServer gameServer = new GameServer(null);
		ArrayList<Integer> targetFriendly = new ArrayList<>();
		targetFriendly.add(priest.getId());
		Vector3 position = new Vector3(0,0,0);
		PriestHealOverTime spell = new PriestHealOverTime( time,  hero,  priest.getAbility(5), gameServer,  null,  targetFriendly,  position);
		spell.execute();
	}
}