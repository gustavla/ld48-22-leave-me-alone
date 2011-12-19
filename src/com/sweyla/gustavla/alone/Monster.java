package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Monster extends Entity {
	int time_until_next_move;
	int ongoing_move_dx, ongoing_move_dy;

	public Monster(Engine engine) {
		super(engine);
		radius = 4;
		ongoing_move_dx = ongoing_move_dy = 0;
		resetTimeUntilNextMove();
	}
	
	public int getAttackRange() {
		return 10;
	}
	
	public Rectangle getWeaponRectangle() {
		return new Rectangle(pos_x - getAttackRange(), pos_y - getAttackRange(), 2*getAttackRange(), 2*getAttackRange());
	}
	
	public void tick() {
		super.tick();
		
		// Handle movement
		if (ongoing_move_dx != 0 || ongoing_move_dy != 0) {
			if (Engine.gtick%2==0) {
				tryToMove(ongoing_move_dx, ongoing_move_dy);
			}
			if (--time_until_next_move <= 0) {
				ongoing_move_dx = ongoing_move_dy = 0;
				resetTimeUntilNextMove();
			}
		} else if (--time_until_next_move <= 0 && attack_counter == 0) {
			// Issue a move!
			ongoing_move_dx = (int)(Math.random()*3)-1;
			ongoing_move_dy = (int)(Math.random()*3)-1;
			//ongoing_move_dx = -1;
			//ongoing_move_dy = 0;
			time_until_next_move = 10;
		} else {
			// Check if player is close by
			if (engine.getHero().distanceTo(this) <= 20) {
				// Try to attack with a certain chance
				if ((int)(Math.random()*10) == 0) {
					tryToAttack();
				}
			}
		}
	}
	
	public void playDamageSound() {
		//Sound.monsterdamage.play();
	}
	
	public int getAttackDamage() {
		return 1;
	}
	
	public int getMovey() {
		return 60;
	}
	
	public int getAttackey() {
		return 50;
	}
	
	public void resetTimeUntilNextMove() {
		time_until_next_move = getMovey() + (int)(Math.random() * getMovey()*10);
		//time_until_next_move = 10;
	}

	public boolean useParticleDestroy() {
		return true;
	}
	
	public void render(Sprite canvas) {
		super.render(canvas);
		
		int b = 6;
		if (isAttacking()) {
			int c = spriteSize()/Tile.SIZE;
			for (int i=-c; i<=c; ++i) {
				for (int j=-c; j<=c; ++j) {
					if (i == -c || i == c || j == -c || j == c) {
						canvas.setBlitOpacity(0x55);
						canvas.blit(Artwork.character, pos_x+b*i-Tile.SIZE/2, pos_y+b*j-Tile.SIZE/2, 73, (int)(Engine.gtick*6 + i * 3212 + j*31)%6, pos_y+Tile.SIZE*j);
						canvas.setBlitOpacity(0xFF);
					}
				}
			}
		}

	}
}
