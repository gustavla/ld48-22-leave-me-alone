package com.sweyla.gustavla.alone;

public class MovableItem extends Entity {
	
	public MovableItem(Engine engine) {
		super(engine);
	}
	
	public void takeDamage(int amount) {
		// indestructible as default
	}
	
	public void pickedUp(Entity pickerUpper) {
		// Something should happen here.
		
	}
	
	public boolean onlyHeroCanPickup() {
		return true;
	}

	public void move(int dx, int dy, Entity mover) {
		// The item is being picked up
		if (!onlyHeroCanPickup() || mover == engine.getHero()) {
			// pick up by hero!
			pickedUp(mover);
			
			hp = 0;
		} else {
			super.move(dx, dy, mover);
		}
	}
	
	public boolean canMove(int dx, int dy, Entity mover) {
		if (mover == engine.getHero()) {
			return true;
		} else {
			return super.canMove(dx, dy, mover);
		}
	}
	
	public int getTex() {
		return 16;
	}
}
