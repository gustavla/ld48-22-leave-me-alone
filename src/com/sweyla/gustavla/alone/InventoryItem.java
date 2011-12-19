package com.sweyla.gustavla.alone;

public class InventoryItem extends Item {

	public InventoryItem(Engine engine) {
		super(engine);
	}

	public int price() {
		return 1;
	}
	
	public int shopPrice() {
		return 2;
	}
	
	public boolean pickUp(Entity pickerUpper) {
		if (pickerUpper != engine.getHero()) {
			return false;
		}
		
		return engine.getHero().addInventoryItem(this);
	}
	
	public int getAttackDamage() {
		return 0;
	}
	
	public String getName() {
		return "";
	}
	
	public void use(Hero hero) {
		
	}
}
