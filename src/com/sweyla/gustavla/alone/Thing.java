package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Thing {
	int pos_x, pos_y;
	int radius;
	Engine engine;

	public int color1;
	public int color2;
	

	public Thing(Engine engine) {
		this.engine = engine;
		pos_x = pos_y = 0;
		radius = 3;
		color1 = 0;
		color2 = 0;
	}

	public void setPos(int pos_x, int pos_y) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}
	
	public int spriteSize() {
		return Tile.SIZE;
	}
	
	public void setTilePos(int tile_x, int tile_y) {
		// Place in the middle of this tile
		pos_x = tile_x * Tile.SIZE + spriteSize()/2;
		pos_y = tile_y * Tile.SIZE + spriteSize()/2;
	}
	
	public int getPosX() { return pos_x; }
	public int getPosY() { return pos_y; }
	
	public Rectangle getRectangle() {
		return new Rectangle(pos_x-radius, pos_y-radius, 2*radius, 2*radius);
	}
	
	public void render(Sprite canvas) {
		canvas.setEffectColor(getEffectColor());
		Artwork.character.setReplaceColor1(color1);
		Artwork.character.setReplaceColor2(color2);
		canvas.blit(Artwork.character, pos_x-Tile.SIZE/2+getOffsetX(), pos_y-Tile.SIZE/2+getOffsetY(), getTex(), 0, pos_y+getOffsetZ());
		canvas.setEffectColor(0x0);
	}
	
	public int getOffsetX() {
		return 0;
	}
	
	public int getOffsetY() {
		return -2;
	}
	
	public int getOffsetZ() {
		return 0;
	}
	
	public int getTex() {
		return 99;
	}
	
	public int getEffectColor() {
		return 0;
	}
}
