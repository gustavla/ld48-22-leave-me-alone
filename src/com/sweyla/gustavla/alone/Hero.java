package com.sweyla.gustavla.alone;

public class Hero extends Human {	
	int invincible_counter;
	public static final int INVENTORY_SIZE = 8;
	private int selected_item;
	
	// inventory
	InventoryItem[] inventory = new InventoryItem[INVENTORY_SIZE];
	
	public Hero(Engine engine) {
		super(engine);
		radius = 4;
		moving = false;
		hp = full_hp = 12;
		invincible_counter = 0;
		selected_item = 0;
		money = 1000;
		//color1 = 0xFFE194F0;
		//color2 = 0xFF68573F;
	}
	
	public void setSelectedItemIndex(int selected_item) {
		this.selected_item = Math.max(0, Math.min(INVENTORY_SIZE-1, selected_item));
	}
	
	public int getSelectedItemIndex() {
		return selected_item;
		
	}
	
	public InventoryItem getSelectedItem() {
		return inventory[selected_item];
	}
	
	public InventoryItem[] getInventory() {
		return inventory;
	}
	
	public void sellSelectedItem() {
		if (getSelectedItem() == null || getSelectedItem().price() == -1) {
			return;
		}
		if (getSelectedItem() != null) {
			money += getSelectedItem().price();
			removeInventoryItem(getSelectedItem());
		}
	}
	
	public void packInventoryTight() {
		for (int i=0; i<inventory.length; ++i) {
			if (inventory[i] == null) {
				for (int j=i+1; j<inventory.length; ++j) {
					if (inventory[j] != null) {
						inventory[j-1] = inventory[j];
					}
				}
			}
		}
	}
	
	public boolean addInventoryItem(InventoryItem item) {
		boolean added = false;
		for (int i=0; i<inventory.length; ++i) {
			if (inventory[i] == null) {
				inventory[i] = item;
				added = true;
				break;
			}
		}
		return added;
	}
	
	public void removeInventoryItem(InventoryItem item) {
		for (int i=0; i<inventory.length; ++i) {
			if (inventory[i] == item) {
				inventory[i] = null;
			}
		}
	}
	
	public void takeDamage(int damage) {
		if (invincible_counter == 0) {
			super.takeDamage(damage);
			invincible_counter = 20;
		}
	}
	
	public void tick() {
		super.tick();
		
		if (invincible_counter > 0) {
			invincible_counter--;
		}
	}
	
	public int getAttackTime() {
		return 15;
	}
	
	public int getAttackRefactorTime() {
		return 1;
	}
	
	public void reset() {
		move_dx = 0;
		move_dy = 0;
		moving = false;
		money = 0;
		attack_counter = 0;
		invincible_counter = 0;
		damage_counter = 0;
		attack_refactor_counter = 0;
		hp = full_hp;
	}
	
	public void doWhenDie() {
		engine.resetHeroLocation();
	}
	
	public void useItem() {
		InventoryItem item = getSelectedItem();
		if (item != null) {
			item.use(this);
		}
	}
	
	public int getWeaponType() {
		if (getSelectedItem() != null && Weapon.class.isInstance(getSelectedItem())) {
			return ((Weapon)getSelectedItem()).getType();
		} else {
			return 0;
		}
	}
	
	public boolean hasRoomInInventory() {
		for (int i=0; i<inventory.length; ++i) {
			if (inventory[i] == null) {
				return true;
			}
		}
		return false;
	}
}

