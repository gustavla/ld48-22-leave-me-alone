package com.sweyla.gustavla.alone;

public class Stone extends Entity {

	public Stone(Engine engine) {
		super(engine);
		hp = 1;
		radius = 3;
	}
	
	// Drops nothing
	
	public void takeDamage(int amount) {
		if (amount > 1) {
			super.takeDamage(amount);
		}
	}
	
	public int getOffsetZ() {
		return -2;
	}
	
	public int getOffsetY() {
		return -1;
	}

	public int getTex() {
		return 85;
	}
	
	public boolean shouldReset() {
		return false;
	}
}
