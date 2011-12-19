package com.sweyla.gustavla.alone;

public class Forest extends Map {
	public String getName() {
		return "FOREST";
	}
	
	public Forest(Engine engine, int width, int height) {
		super(engine, 60, 50);
		// TODO Auto-generated constructor stub
		
		
		for (int i=0; i<getWidth(); ++i) {
			for (int j=0; j<getHeight(); ++j) {
				getTile(i, j).walkable = true;
				getTile(i, j).tex = getMainWalkableTex();
				if (Math.random() > 0.7) {
					getTile(i, j).tex += 1;
				}
				if (Math.random() > 0.7) {
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
				
				System.out.println(i + " " + j + " " + closeness);
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
		int m = 32;
		for (int i=0; i<3; ++i) {
			for (int j=0; j<5; ++j) {
				Tile t = getTile(m-i, j);
				t.walkable = true;
				t.tex = getMainWalkableTex();
				if (i == 1) {
					t.tex = 2;
				}
				if (j == 0) {
					t.transport_to_map = 0;
					t.transport_to_tile_x = 28-i;
					t.transport_to_tile_y = 13;
					t.transport_facing = Entity.UP;
				}
			}
		}
		
		// Place slimes
		
		resetEntities();
				
	}
	
	public double bigEntityProbability() {
		return 0.001;
	}
	
	public double entityProbability() {
		return 0.03;
	}
	
	public Entity newSpawnEntity() {
		return new Slime(engine);
	}
	
	public Entity newBigSpawnEntity() {
		return new BigSlime(engine);
	}

	
	public void resetEntities() {
		super.resetEntities();
		
		for (int i=0; i<getWidth()-1; ++i) {
			for (int j=0; j<getHeight()-1; ++j) {
				int dist = distanceToHero(i, j);
				if (dist > 4) {
					if (getTile(i, j).isWalkable() && getTile(i+1, j).isWalkable() && getTile(i, j+1).isWalkable() && getTile(i+1, j+1).isWalkable()) {
						if (Math.random() > 1.0 - bigEntityProbability()) {
							Entity entity = newBigSpawnEntity();
							if (entity != null) {
								entity.setTilePos(i, j);
								addEntity(entity);
							}
						}
					}
				}
			}
		}

		for (int i=0; i<getWidth(); ++i) {
			for (int j=0; j<getHeight(); ++j) {
				int dist = distanceToHero(i, j);
				if (dist > 3) {
					if (getTile(i, j).isWalkable()) {
						if (Math.random() > 1.0 - entityProbability()) {
							Entity entity = newSpawnEntity();
							if (entity != null) {
								entity.setTilePos(i, j);
								addEntity(entity);
							}
						}
					}
				}
			}
		}
	}
	
	public int getMainWalkableTex() {
		return 3;
	}

	public int getMainWallTex() {
		return 15;
	}
}
