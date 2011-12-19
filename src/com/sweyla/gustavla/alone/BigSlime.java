package com.sweyla.gustavla.alone;

public class BigSlime extends Slime {

	public BigSlime(Engine engine) {
		super(engine);
		radius = Tile.SIZE - 2; 
	}
	
	public int getFullHP() {
		if (type == 0) {
			return 15;
		} else {
			return 40;
		}
	}
	
	public int spriteSize() {
		return Tile.SIZE * 2;
	}
	
	public int getOffsetZ() {
		return 3;
	}
	
	public int getAttackRange() {
		return 16;
	}
	
	public boolean canMove(int dx, int dy, Entity mover) {
		if (mover == null) {
			return super.canMove(dx, dy, mover);
		} else {
			// Too big for the hero to move.
			return false;
		}
	} 

	public int getTex() {
		if (attack_counter > 0) {
			return 60;
		} else {
			return 20 + Engine.animationFrame(2, 30) * 20;
		}
	}
	
	public int getAttackDamage() {
		return super.getAttackDamage()*2;

	}

	public int getDropGoodness() {
		if (type == 0) {
			return 5;
		} else {
			return 20;
		}
	}

}
