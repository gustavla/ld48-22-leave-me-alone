package com.sweyla.gustavla.alone;

public class DarkForest extends Forest {

	public String getName() {
		return "DARK FOREST";
	}
	
	public DarkForest(Engine engine, int width, int height) {
		super(engine, 60, 50);
		
		for (int i=0; i<getWidth(); ++i) {
			for (int j=0; j<getHeight(); ++j) {
				getTile(i, j).walkable = true;
				getTile(i, j).tex = getMainWalkableTex();
				if (Math.random() > 0.95) {
					getTile(i, j).tex += 1;
				}
				if (Math.random() > 0.95) {
					getTile(i, j).tex += 1;
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
				} else {
					edge = (Math.random()>closeness/4.0);
				}
				
				if (edge) {
					getTile(i, j).walkable = false;
					getTile(i, j).tex = getMainWallTex();
				}
			}
		}
		
		// open up to the city
		int m = 40;
		for (int i=0; i<5; ++i) {
			for (int j=0; j<3; ++j) {
				Tile t = getTile(i, m+j);
				t.walkable = true;
				t.tex = getMainWalkableTex();
				if (j == 1) {
					t.tex = 32;
				}
				if (i == 0) {
					t.transport_to_map = 0;
					t.transport_to_tile_x = 31;
					t.transport_to_tile_y = 8+j;
					t.transport_facing = Entity.UP;
				}
			}
		}
		
		// Place slimes
		
		resetEntities();
				
	}
	
	public double bigEntityProbability() {
		return 0.01;
	}
	
	public double entityProbability() {
		return 0.01;
	}
	
	public Entity newSpawnEntity() {
		Slime entity = new Slime(engine);
		entity.setType(1);
		return (Entity)entity;
	}
	
	public Entity newBigSpawnEntity() {
		BigSlime e = new BigSlime(engine);
		if (Math.random() > 0.9) {
			e.setType(1);
		}
		return e;
	}
	
	public int getMainWalkableTex() {
		return 23;
	}

	public int getMainWallTex() {
		return 35;
	}
}
