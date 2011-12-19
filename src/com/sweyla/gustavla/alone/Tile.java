package com.sweyla.gustavla.alone;

public class Tile {
	static public final int SIZE = 10;
	
	public int tex;
	public int tex2;
	public int transport_to_map;
	public int transport_to_tile_x;
	public int transport_to_tile_y;
	public int transport_facing;
	public boolean walkable;
	public boolean blending;
	
	public double temp;

	public Tile() {
		tex = 0;
		tex2 = -1;
		walkable = true;
		blending = false;
		transport_to_map = -1;
		transport_to_tile_x = 0;
		transport_to_tile_y = 0;
		transport_facing = 0;
	}
	
	public Tile(boolean walkable) {
		this();
		this.walkable = walkable;
	}

	public boolean isWalkable() {
		return walkable;
	}
	
	public void render(Sprite canvas, int x, int y) {
		canvas.blit(Artwork.tiles, x, y, tex);
		if (tex2 >= 0) {
			canvas.blit(Artwork.tiles, x, y, tex2);
		}
	}
}
