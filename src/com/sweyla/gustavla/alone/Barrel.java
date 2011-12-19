package com.sweyla.gustavla.alone;

public class Barrel extends Entity {

	public Barrel(Engine engine) {
		super(engine);
		hp = full_hp = 3;
		radius = 4;
	}
	
	public void doWhenDie() {
		// Drop something
	}

	
	public int getOffsetZ() {
		return -2;
	}
	
	public int getOffsetY() {
		return -1;
	}
	public int getTex() {
		return 84;
	}
	
	public boolean shouldReset() {
		return false;
	}
}
