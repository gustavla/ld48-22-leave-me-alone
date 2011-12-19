package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Human extends Entity {
	public static int[] colors1 = {0xFF4488FF, 0xFF77FF99, 0xFFEE3333, 0xFFFF88FF, 0xFF88FFFF, 0xFF444444, 0xFF338899, 0xFFFEFEFE};
	public static int[] colors2 = {0xFFAA8844, 0xFFFFF022, 0xFFFFFF88, 0xFFFFFFFF, 0xFF444444, 0xFFFFFF88};
	/*
		color1 = 0xFF4488FF // blue
	color2 = 0xFFAA8844; // fair
	color1 = 0xFF77FF99; // green
	color2 = 0xFFFFF022; // blonde
	color1 = 0xFFEE3333; // red
	color2 = 0xFFFFFF88; // super blonde
	color1 = 0xFFFF88FF; // purple
	color2 = 0xFFFFFFFF; // white
	color1 = 0xFF88FFFF;
	color2 = 0xFF444444; // dark
	color1 = 0xFF444444;
	color2 = 0xFFFFFF88; // super blonde
	color1 = 0xFF338899;
	color1 = 0xFFFEFEFE;
*/
	
	boolean moving;
	
	public Human(Engine engine) {
		super(engine);
		
		// Randomize colors
		color1 = colors1[(int)(Math.random()*colors1.length)];
		color2 = colors2[(int)(Math.random()*colors2.length)];
		
		hp = full_hp = 20;
	}

	public void render(Sprite canvas) {
		super.render(canvas);
		
		// Render weapon
		if (attack_counter > 0) {
			int px = 0;
			int py = 0;
			int z = 0;
			int t = 5;
			int tex = 19 + getWeaponType()*10 - (attack_counter-1)/(getAttackTime()/3);
			int rot = facing;
			if (rot == RIGHT) {
				rot = Sprite.MIRROR_X;
			}
			
			if (facing == LEFT) {
				px = pos_x-Tile.SIZE-1;
				py = pos_y-Tile.SIZE/2-2;
				z = pos_y;
			} else if (facing == RIGHT) {
				px = pos_x+1;
				py = pos_y-Tile.SIZE/2-2;
				z = pos_y;
			} else if (facing == UP) {
				px = pos_x-Tile.SIZE/2+2;
				py = pos_y-Tile.SIZE-1;
				z = pos_y-1;
			} else if (facing == DOWN) {
		   		px = pos_x-Tile.SIZE/2-3;
				py = pos_y-1;
				z = pos_y+1;
			}
			
			// draw weapon
			canvas.blit(Artwork.character, px, py, tex, rot, z);
		}
	}
	
	public int getTex() {
		int start = 0;
		if (facing == Entity.UP) {
			start = 3;
		} else if (facing == Entity.LEFT || facing == Entity.RIGHT) {
			start = 6;
		}/* else if (facing == Entity.RIGHT) {
			start = 9;
		}*/
		if (moving) {
			start += 1 + Engine.animationFrame(2, 15); 
		}
		return start;
	}

	public Rectangle getWeaponRectangle() {
		int range = 5;
		if (facing == LEFT) {
			return new Rectangle(pos_x-radius-range, pos_y-radius, range, radius*2);
		} else if (facing == RIGHT) {
			return new Rectangle(pos_x+radius, pos_y-radius, range, radius*2);
		} else if (facing == UP) {
			return new Rectangle(pos_x-radius, pos_y-radius-range, radius*2, range);
		} else if (facing == DOWN) {
			return new Rectangle(pos_x-radius, pos_y+radius, radius*2, range);
		} else {
			return super.getWeaponRectangle();
		}
	}
	
	public void tryToMove(int dx, int dy) {
		if (dy < 0) {
			facing = Entity.UP;
		} else if (dy > 0) {
			facing = Entity.DOWN;
		} else if (dx < 0) {
			facing = Entity.LEFT;
		} else if (dx > 0) {
			facing = Entity.RIGHT;
		}
		
		super.tryToMove(dx, dy);
	}
	
	public boolean useParticleDestroy() {
		return true;
	}
	
	public int getWeaponType() {
		return 0;
	}
	
	public int getAttackDamage() {
		if (getSelectedItem() != null) {
			return getSelectedItem().getAttackDamage();
		} else {
			return 0;
		}
	}
	
	public InventoryItem getSelectedItem() {
		return null;
	}
	
	public void playDamageSound() {
		//Sound.humandamage.play();
	}
	
	public void playAttackSound() {
		System.out.println("here");
		//Sound.swing.play();
	}


	public int getDropGoodness() {
		return 1;
	}
}
