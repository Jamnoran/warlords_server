package game.util;

import game.vo.Item;

public class ItemUtil {


	public static Item generateItem(int level){
		Item item = new Item();

		item.setLevelReq(level);
		item.setPosition(getRandomType());

		item = generateRarity(item);

		item.setName(generateName(item));

		item.setImage(generateImage(item));

		item.setBaseStat(getBaseStats(level, item));
		item.setTop(getBaseStats(item.getBaseStat(), item));


		item.generateInfo();
		return item;
	}

	private static String generateImage(Item item) {
		String image = null;
		if(item.getPosition().equals(Item.MAIN_HAND)){
			image = "Sword";
		}else if(item.getPosition().equals(Item.OFF_HAND)){
			image = "Shield";
		}else if (item.getPosition().equals(Item.HEAD)){
			image = "Helmet";
		}else if (item.getPosition().equals(Item.SHOULDERS)){
			image = "Shoulders";
		}else if (item.getPosition().equals(Item.CHEST)){
			image = "Armor";
		}else if (item.getPosition().equals(Item.LEGS)){
			image = "Leggings";
		}else if (item.getPosition().equals(Item.BOOTS)){
			image = "Boots";
		}
		return image;
	}

	private static Item generateRarity(Item item) {
		int chance = CalculationUtil.getRandomInt(0,100);
		if(chance < 1){
			item.setRarity(Item.LEGENDARY);
			item.setDropRate(0.01f);
		}else if(chance < 5){
			item.setRarity(Item.RARE);
			item.setDropRate(0.05f);
		}else if (chance < 20){
			item.setRarity(Item.UNCOMMON);
			item.setDropRate(0.1f);
		}else{
			item.setRarity(Item.COMMON);
			item.setDropRate(0.2f);
		}
		return item;
	}

	private static String generateName(Item item) {
		String name = null;
		if(item.getPosition().equals(Item.MAIN_HAND)){
			name = "Sword";
		}else if(item.getPosition().equals(Item.OFF_HAND)){
			name = "Shield";
		}else if (item.getPosition().equals(Item.HEAD)){
			name = "Helmet";
		}else if (item.getPosition().equals(Item.SHOULDERS)){
			name = "Shoulders";
		}else if (item.getPosition().equals(Item.CHEST)){
			name = "Armor";
		}else if (item.getPosition().equals(Item.LEGS)){
			name = "Leggings";
		}else if (item.getPosition().equals(Item.BOOTS)){
			name = "Boots";
		}
		int random = CalculationUtil.getRandomInt(0,3);
		switch (random){
			case(0):
				name = name + " of doom";
				break;
			case(1):
				name = name + " of death";
				break;
			case(2):
				name = name + " of fluff";
				break;
			case(3):
				name = name + " of iron";
				break;
		}

		return name;
	}

	private static int getBaseStats(int base, Item item) {
		if(item.getRarity().equals(Item.LEGENDARY)){
			base = (int) (base * 1.3);
		}else if (item.getRarity().equals(Item.RARE)){
			base = (int) (base * 1.1);
		}else if (item.getRarity().equals(Item.UNCOMMON)){
			base = (int) (base * 1.05);
		}
		if(item.getPosition().equals(Item.MAIN_HAND)){
			double highest = (base + (base * 0.5));
			int high = (int) highest;
			return CalculationUtil.getRandomInt(base, high);
		}else{
			double highest = (base + (base * 0.5));
			int high = (int) highest;
			return CalculationUtil.getRandomInt(base, high);
		}
	}

	private static String getRandomType() {
		int random = CalculationUtil.getRandomInt(0,6);
		switch (random){
			case(0):
				return Item.MAIN_HAND;
			case(1):
				return Item.OFF_HAND;
			case(2):
				return Item.HEAD;
			case(3):
				return Item.SHOULDERS;
			case(4):
				return Item.CHEST;
			case(5):
				return Item.LEGS;
			case(6):
				return Item.BOOTS;
		}
		return null;
	}


}
