package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Slime extends Monster {
	int type = 0;
	
	public Slime(Engine engine) {
		super(engine);
		setType(0);
	}
	
	public void setType(int type) {
		this.type = type;
		if (type == 0) {
			color1 = 0xFF0080FF;
		} else {
			color1 = 0xFFEE3322;
		}
		
		hp = getFullHP();
	}
	
	public int getFullHP() {
		if (type == 0) {
			return 3;
		} else {
			return 10;
		}
	}

	public int getTex() {
		if (attack_counter > 0) {
			return 9;
		} else {
			return 10 + Engine.animationFrame(2, 60);
		}
	}
	
	public int getAttackDamage() {
		if (type == 0) {
			return 1;
		} else {
			return 2;
		}
	}
	
	public int getMovey() {
		if (type == 0) {
			return 60;
		} else {
			return 80;
		}
		
	}
	
	public int getAttackRefactorTime() {
		if (type == 0) {
			return 100;
		} else {
			return 80;
		}
	}
	
	public void render(Sprite canvas) {
		
		//color1 = 0xFFFF1144; // red
		
		super.render(canvas);
	}
	
	public int getDropGoodness() {
		if (type == 0) {
			return 1;
		} else {
			return 4;
		}
	}
	
	public void tryToAttack() {
		super.tryToAttack();
		
	}
	
	public void playAttackSound() {
		//Sound.slime.play();
	}
}
