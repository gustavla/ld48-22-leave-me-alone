package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Map {
	static public final int MAX_ENTITIES = 300;
	static public final int MAX_ITEMS = 100;
	
	static public final int MAP_TAVERN = 0;
	static public final int MAP_FORREST = 1;
	static public final int MAP_CITY = 2;
	
	private Tile[][] tiles;
	private Entity[] entities = new Entity[MAX_ENTITIES];
	private Item[] items = new Item[MAX_ITEMS];
	
	private int width;
	private int height;
	
	Engine engine;
	
	public Map(Engine engine, int width, int height) {
		this.width = width;
		this.height = height;
		this.engine = engine;
		tiles = new Tile[width][height];
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				tiles[i][j] = new Tile();
			}
		}
	}
	
	public Entity[] getEntities() {
		return entities;
	}
	
	public Item[] getItems() {
		return items;
	}
	
	public void addEntity(Entity entity) {
		// Check first if there will be a collision
		boolean ok = true;
		for (int i=0; i<entities.length; ++i) {
			if (entities[i] != null && entities[i].getRectangle().intersects(entity.getRectangle())) {
				if (entity == engine.getHero()) {
					entities[i] = null;
				} else {
					ok = false;
				}
			}
		}
		
		if (ok) {
			for (int i=0; i<entities.length; ++i) {
				if (entities[i] == null) {
					entities[i] = entity;
					return;
				}
			}
		}
		// If we reached here and we're the hero, kick someone out
		if (entity == engine.getHero()) {
			entities[0] = entity;
		}
	}
	
	public void removeEntity(Entity entity) {
		for (int i=0; i<entities.length; ++i) {
			if (entities[i] == entity) {
				entities[i] = null;
			}
		}
	}
	
	public void addItem(Item item) {
		for (int i=0; i<items.length; ++i) {
			if (items[i] == null) {
				items[i] = item;
				return;
			}
		}
	}
	
	public void addItemWithAnimation(Item item) {
		addItem(item);
		item.initEntryAnimation();
	}
	
	public void removeItem(Item item) {
		for (int i=0; i<items.length; ++i) {
			if (items[i] == item) {
				items[i] = null;
			}
		}
	}
	
	public String getName() {
		return "Scene";
	}
	
	public void resetEntities() {
		// Remove all entities
		Entity[] entities = getEntities();
		for (int i=0; i<entities.length; ++i) {
			if (entities[i] != null && entities[i] != engine.getHero() && entities[i].shouldReset()) {
				entities[i] = null;
			}
		}
	}
	
	// manhattan distance
	public int distanceToHero(int i, int j) {
		if (engine.getHero() == null) {
			return 0;
		} else {
			return Math.abs((i-engine.getHero().getTilePosX())) + Math.abs((j-engine.getHero().getTilePosY()));
		}
	}
	
	public Tile getTile(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return tiles[x][y];
		} else {
			// return non-walkable tile
			return new Tile(false);
		}
	}
	
	public void render(Sprite canvas, int ox, int oy) {
		int tx = ox/Tile.SIZE;
		int ty = oy/Tile.SIZE;
		//for (int i=Math.max(0, tx-8); i<=Math.min(width-1, tx+8); ++i) {
			//for (int j=Math.max(0, ty-8); j<=Math.min(height-1, ty+8); ++j) {
		for (int i=tx-18; i<=tx+18; ++i) {
			for (int j=ty-18; j<=ty+18; ++j) {
				getTile(i, j).render(canvas, i*Tile.SIZE, j*Tile.SIZE);
			}
		}
	}
	
	public Rectangle getTileRectangle(int x, int y) {
		return new Rectangle(x * Tile.SIZE, y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public int getMainWalkableTex() {
		return 2;
	}

	public int getMainWallTex() {
		return 3;
	}
}

