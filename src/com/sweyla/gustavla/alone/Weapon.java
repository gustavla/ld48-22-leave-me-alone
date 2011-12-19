package com.sweyla.gustavla.alone;

public class Weapon extends InventoryItem {
	public static final int BUY_PRICES[] = {-1, 200, 400, 2, 10, 100, 2, 10, 100};
	public static final int SELL_PRICES[] = {-1, 100, 200, 1, 5, 50, 1, 5, 50};
	public static final int DAMAGES[] = {1, 7, 10, 2, 3, 5, 2, 3, 5};
	public static final String[] NAMES = {
		"STICK",
		"GOLD",
		"DIAMOND",
		"WOOD",
		"STEEL",
		"GOLD",
		"WOOD",
		"STEEL",
		"GOLD"
	};

	int type;
	
	public Weapon(Engine engine) {
		super(engine);
		type = 0;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public int getTex() {
		return 16 + type * 10;
	}
	
	public int getAttackDamage() {
		return DAMAGES[type];
	}
	
	public int price() {
		return SELL_PRICES[type];
	}
	
	public int shopPrice() {
		return BUY_PRICES[type];
	}
	
	public String getName() {
		return NAMES[type];
	}
	
	public void use(Hero hero) {
		hero.tryToAttack();
	}
}
