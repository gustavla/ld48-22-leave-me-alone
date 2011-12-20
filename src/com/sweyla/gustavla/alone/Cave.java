package com.sweyla.gustavla.alone;

public class Cave extends Map {

	public String getName() {
		return "ME-TIME CAVE";
	}
	
	public Cave(Engine engine) {
		super(engine, 30, 30);

		for (int i=0; i<getWidth(); ++i) {
			for (int j=0; j<getHeight(); ++j) {
				getTile(i, j).walkable = true;
				getTile(i, j).tex = getMainWalkableTex();
				if (Math.random() > 0.95) {
					getTile(i, j).tex += 2;
				} else
				if (Math.random() > 0.95) {
					getTile(i, j).tex += 10;
				}
			}
		}
		
		// Edges
		for (int i=0; i<getWidth(); ++i) {
			for (int j=0; j<getHeight(); ++j) {
				// closeness
				int closeness = Math.min(i, Math.min(getWidth()-1-i, Math.min(j, getHeight()-1-j)));
				
				boolean edge = false;
				
				if (closeness <= 1) {
					edge = true;
				}
				if (edge) {
					getTile(i, j).walkable = false;
					getTile(i, j).tex = getMainWallTex();
				}
			}
		}
		
		// open up to the city
		for (int i=0; i<2; ++i) {
			Tile t = getTile(15, 29-i);
			t.walkable = true;
			t.tex = getMainWalkableTex();
			if (i == 0) {
				t.transport_to_map = 0;
				t.transport_to_tile_x = 29;
				t.transport_to_tile_y = 6;
				t.transport_facing = Entity.DOWN;
			}
		}		
	}
	
	public void resetEntities() {
		super.resetEntities();
		
		int left = 50;
		
		for (int i = 2; i<getWidth()-2; ++i) {
			for (int j=2; j<getHeight()-4; ++j) {
				if (getTile(i, j).isWalkable()) {
					// Create NPC
					if (Math.random() > 0.95 && left > 0) {
						spawnEntity(i, j);
						left--;
					}
				}
			}
		}
	}
	
	public void spawnEntity(int x, int y) {
		//double which = Math.random();
		if (true) {
			NPC npc = new NPC(engine);
			npc.setTilePos(x, y);
			npc.setCaveNPC(true);
			addEntity(npc);
		}
	}
	
	public int getMainWalkableTex() {
		return 20;
	}
	
	public int getMainWallTex() {
		return 21;
	}
}
