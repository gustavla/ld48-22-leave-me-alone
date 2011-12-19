package com.sweyla.gustavla.alone;

public class Potion extends InventoryItem {

	public Potion(Engine engine) {
		super(engine);
		color1 = 0xFFFF0000;
	}

	public void use(Hero hero) {
		hero.hp = Math.min(hero.hp + 10, hero.full_hp);
		// destroy item
		hero.removeInventoryItem(this);
		//Sound.potion2.play();
	}
	
	public int getTex() {
		return 33;
	}
	
	public int price() {
		return 10;
	}
	
	public int shopPrice() {
		return 20;
	}
	
	public String getName() {
		return "POTION";
	}
}
