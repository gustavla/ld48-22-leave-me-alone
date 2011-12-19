package com.sweyla.gustavla.alone;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Engine {
	
	static public final int NUM_MAPS = 7;
	static public final int MAX_PARTICLES = 400;
	
	static public final int MAP_CAVE = 3;
	static public final int MAP_SHOP = 6;
	
	static public final int GAME_STATE_START = 0;
	static public final int GAME_STATE_INTRO1 = 1;
	static public final int GAME_STATE_INTRO2 = 5;
	static public final int GAME_STATE_INTRO3 = 6;
	static public final int GAME_STATE_INGAME = 2;
	static public final int GAME_STATE_HELP = 3;
	static public final int GAME_STATE_ABOUT = 4;
	
	static String[] options = {"NEW GAME", "HOW TO PLAY", "ABOUT"};
	
	private Map[] maps = null;
	private Particle[] particles = new Particle[MAX_PARTICLES];
	private int particle_cursor = 0;
	private int current_map;
	private Hero hero;
	private boolean the_end;
	private int the_end_counter;
	private int game_state = GAME_STATE_START;
	private int menu_option = 0;
	private boolean show_inventory = false;
	private int shop_menu_option = 0;
	private int buy_option = 0;
	private boolean buying = false;
	private String shop_error;
	private int shop_error_counter = 0;
	
	public static final int[] SHOP_ORDER = {0, 3, 6, 4, 7, 5, 8, 1, 2};
	
	private InventoryItem[] shop_items = null;
	
	public String scene_str;
	int scene_str_counter;
	int reset_counter;
	
	// Global tick that can be accessed by animations
	static public int gtick = 0;
	
	public Engine() {
		// Shop items
		shop_items = new InventoryItem[9];
		
		shop_items[0] = new Potion(this);
		
		for (int i=1; i<9; ++i) {
			Weapon weapon = new Weapon(this);
			weapon.setType(i);
			shop_items[i] = weapon;
		}
		
		
		loadMaps();
	}
	
	
	public void resetHeroLocation() { resetHeroLocation(false); }
	public void resetHeroLocation(boolean immediately) {
		if (immediately) {
			__resetHeroLocation();
		} else {
			reset_counter = 120;
		}
	}
	
	public void __resetHeroLocation() {
		curMap().removeEntity(hero);
		current_map = 4;
		hero.reset();
		curMap().addEntity(hero);
		hero.setTilePos(4, 1);
		hero.facing = Entity.DOWN;
	}
	
	public void loadMaps() {
		scene_str_counter = 0;
		maps = new Map[NUM_MAPS];
		
		maps[0] = new City(this, 0, 0);
		maps[1] = new Forest(this, 30, 30);
		maps[2] = new DarkForest(this, 30, 30);
		maps[3] = new Cave(this);
		maps[4] = new Inn(this);
		maps[5] = new HouseLong(this, 0, 0);
		maps[6] = new Shop(this, 0, 0);
		
		
		current_map = 4;
		// Create hero
		hero = new Hero(this);
		hero.setTilePos(4, 10);
		//hero.setTilePos(4, 4);
		curMap().addEntity(hero);
		resetHeroLocation(true);
		//hero.money = 0;
		//resetHeroLocation();

		// Basic weapon
		Weapon weapon2 = new Weapon(this);
		//weapon2.setTilePos(25, 8);
		weapon2.setType(0);
		getHero().addInventoryItem(weapon2);

		/*
		for (int i=0; i<9; ++i) {
			Potion weapon = new Potion(this);
			weapon.setTilePos(20+i, 10);
			//weapon.setType(i);
			curMap().addItem(weapon);
		}
		
		Heart heart = new Heart(this);
		heart.setTilePos(20, 10);
		curMap().addItem(heart);
		*/
		
		/*
		for (int i=0; i<maps[0].getWidth(); ++i) {
			for (int j=0; j<maps[0].getHeight(); ++j) {
				
				if (i < 2 || j < 2 || i > maps[0].getWidth()-3 || j > maps[0].getHeight()-3) {
					maps[0].getTile(i, j).tex = 1;
					maps[0].getTile(i, j).walkable = false;
				} else {
					maps[0].getTile(i, j).tex = 2;
				}
			}
		}
		
		maps[0].getTile(5, 8).tex = 2;
		maps[0].getTile(5, 8).walkable = true;
		maps[0].getTile(5, 8).transport_to_map = 1;
		maps[0].getTile(5, 8).transport_to_tile_x = 15;
		maps[0].getTile(5, 8).transport_to_tile_y = 2;
		maps[0].getTile(5, 8).transport_facing = Entity.DOWN;
		
		
		
		Slime slime = new Slime(this);
		slime.setTilePos(7, 8);
		//curMap().addEntity(slime);
		
		Slime slime2 = new Slime(this);
		slime2.setTilePos(7, 10);
		//curMap().addEntity(slime2);

		
		if (false) {
			for (int i = 0; i<10; ++i) {
				for (int j=  0; j<10; ++j) {
					Slime npc = new Slime(this);
					npc.setTilePos(4+i*1, 7+j*1);
					curMap().addEntity(npc);
				}
			}
		}
		Item item = new Item(this);
		item.setTilePos(5, 8);
		curMap().addEntity(item);
		
		Heart heart = new Heart(this);
		heart.setTilePos(7, 6);
		curMap().addEntity(heart);

		//Tile t = maps[0].getTile(0, 0);
		//t.transport_to_map = 1;
		*/
		
		curMap().resetEntities();
		//resetHeroLocation(true);
	}
	
	public void tick(boolean[] keys, boolean has_focus) {
		if (has_focus) {
			if (game_state == GAME_STATE_INGAME) {
				tick_ingame(keys, has_focus);
			} else if (game_state == GAME_STATE_START) {
				if (keys[KeyEvent.VK_UP]) {
					keys[KeyEvent.VK_UP] = false;
					menu_option = Math.max(0, menu_option-1);
				} else if (keys[KeyEvent.VK_DOWN]) {
					keys[KeyEvent.VK_DOWN] = false;
					menu_option = Math.min(options.length-1, menu_option+1);
				} else if (keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_ENTER]) {
					keys[KeyEvent.VK_SPACE] = false;
					keys[KeyEvent.VK_ENTER] = false;
					if (menu_option == 0) {
						game_state = GAME_STATE_INTRO1;
					} else if (menu_option == 1) {
						game_state = GAME_STATE_HELP;
					} else {
						game_state = GAME_STATE_ABOUT;
					}
				}
				
			} else if (game_state == GAME_STATE_INTRO1) {
				if (keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE]){
					keys[KeyEvent.VK_SPACE] = false;
					keys[KeyEvent.VK_ENTER] = false;
					game_state = GAME_STATE_INTRO2;
				} else if (keys[KeyEvent.VK_BACK_SPACE]) {
					keys[KeyEvent.VK_BACK_SPACE] = false;
					game_state = GAME_STATE_START;
				}
			} else if (game_state == GAME_STATE_INTRO2) {
				if (keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE]){
					keys[KeyEvent.VK_SPACE] = false;
					keys[KeyEvent.VK_ENTER] = false;
					game_state = GAME_STATE_INTRO3;
				} else if (keys[KeyEvent.VK_BACK_SPACE]) {
					keys[KeyEvent.VK_BACK_SPACE] = false;
					game_state = GAME_STATE_INTRO1;
				}
			} else if (game_state == GAME_STATE_INTRO3) {
				if (keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE]){
					keys[KeyEvent.VK_SPACE] = false;
					keys[KeyEvent.VK_ENTER] = false;
					game_state = GAME_STATE_INGAME;
				} else if (keys[KeyEvent.VK_BACK_SPACE]) {
					keys[KeyEvent.VK_BACK_SPACE] = false;
					game_state = GAME_STATE_INTRO2;
				}
			} else if (game_state == GAME_STATE_HELP || game_state == GAME_STATE_ABOUT) {
				if (keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE]){
					keys[KeyEvent.VK_SPACE] = false;
					keys[KeyEvent.VK_ENTER] = false;
					game_state = GAME_STATE_START;
				}
			}
		}
	}
	
	public void tick_ingame(boolean[] keys, boolean has_focus) {
		if (!the_end) {
			if (current_map == MAP_SHOP) {
				tick_shop(keys, has_focus);
			} else {
				tick_world(keys, has_focus);
			}
		}
	}
	
	public void tick_world(boolean[] keys, boolean has_focus) {

		if (keys[KeyEvent.VK_I] || keys[KeyEvent.VK_ENTER]) {
			keys[KeyEvent.VK_I] = false;
			keys[KeyEvent.VK_ENTER] = false;
			
			show_inventory = !show_inventory;
			return;
		}

		// Hero movement
		int dx = 0;
		int dy = 0;
		
		if (show_inventory) {
			if (keys[KeyEvent.VK_LEFT]) {
				keys[KeyEvent.VK_LEFT] = false;
				hero.setSelectedItemIndex(hero.getSelectedItemIndex()-1);
			}
			if (keys[KeyEvent.VK_RIGHT]) {
				keys[KeyEvent.VK_RIGHT] = false;
				hero.setSelectedItemIndex(hero.getSelectedItemIndex()+1);
			}
			
			// Selling it?
			if (keys[KeyEvent.VK_S]) {
				keys[KeyEvent.VK_S]= false; 
				hero.sellSelectedItem();
			}
		} else {
			if (keys[KeyEvent.VK_LEFT]) {
				dx -= 1;
			}
			if (keys[KeyEvent.VK_RIGHT]) {
				dx += 1;
			} 
			if (keys[KeyEvent.VK_UP]) {
				dy -= 1;
			} 
			if (keys[KeyEvent.VK_DOWN]) {
				dy += 1;
			}
			

			if (reset_counter > 0) {
				reset_counter--;
				if (reset_counter <= 0) {
					__resetHeroLocation();
					return;
				}
			}
			if (scene_str_counter > 0) {
				scene_str_counter--;
			}
			if (the_end_counter > 0) {
				the_end_counter--;
				if (the_end_counter <= 0) {
					the_end = true;
				}
			}

			gtick++;
			
			Entity[] entities = curMap().getEntities();
			

			if (keys[KeyEvent.VK_SPACE]) {
				InventoryItem item = getHero().getSelectedItem();
				if (item != null) {
					hero.useItem();
				}
				//hero.tryToAttack();
			}
			
			// Check if the player is standing on a teleport
			
			Tile t = hero.getStandingOnTile();
			if (t.transport_to_map != -1) {
				curMap().removeEntity(hero);
				current_map = t.transport_to_map;
				curMap().addEntity(hero);
				hero.setTilePos(t.transport_to_tile_x, t.transport_to_tile_y);
				hero.setFacing(t.transport_facing);
				curMap().resetEntities();
				scene_str = curMap().getName();
				scene_str_counter = 60;
				
				// fugly stuff (a few more hours to go)
				leaving = false;
				shop_menu_option = 0;
				buy_option = 0;
				buying = false;
				shop_error_counter = 0;
			}

			if (dx != 0 || dy != 0) {
				hero.moving = true;
				if (Engine.gtick%2 == 0) {
					hero.tryToMove(dx, dy);
				}
			} else {
				hero.moving = false;
			}

			for (int i=0; i<entities.length; ++i) {
				if (entities[i] != null && entities[i].needsCleanup()) {
					// Convert to particles!
					if (entities[i].useParticleDestroy()) {
						int ss = entities[i].spriteSize();
						double vx, vy, vz;
						for (int x=0; x<ss; ++x) {
							for (int y=0; y<ss; ++y) {
								Artwork.character.setReplaceColor1(entities[i].color1);
								Artwork.character.setReplaceColor2(entities[i].color2);
								int c = Artwork.character.getPixel(x, y, entities[i].getTex());
								
								vx = (x-ss/2)/40.0 + (Math.random()-0.5)/10.0;
								vy = (y-ss/2)/40.0 + (Math.random()-0.5)/10.0;
								vz = 0.9 + (Math.random()-0.5)/10.0;
								Particle particle = new Particle(c, entities[i].pos_x-ss/2+x, entities[i].pos_y+ss/2, ss+2-y, vx, vy, vz);
								addParticle(particle);
							}
						}
					}
					
					entities[i] = null;
				}
			}
			
			// Check if item is being picked up
			Rectangle r1 = hero.getRectangle();
			Item[] items = curMap().getItems();
			for (int i=0; i<items.length; ++i) {
				if (items[i] != null && items[i].canPickup()) {
					if (r1.intersects(items[i].getRectangle())) {
						// PICK UP!
						if (items[i].pickUp(hero)) {
							items[i] = null;
						}
					}
				}
			}
			
			// Tick all entities
			for (Entity e : entities) {
				if (e != null) {
					e.tick();
					
					if (e.wantsToMove()) {
						moveEntity(e, null, e.move_dx, e.move_dy);
						e.tryToMove(0, 0);
					}
				}
			}
			
			// ITEMS
			for (Item item : items) {
				if (item != null) {
					item.tick();
				}
			}
			
			// PARTICLES
			for (int i = 0; i < particles.length; ++i) {
				if (particles[i] != null && particles[i].needsCleanup()) {
					particles[i] = null;
				}
			}
			
			for (Particle p : particles) {
				if (p != null) {
					p.tick();
				}
			}
		}
	}
	
	boolean leaving = false;
	public void tick_shop(boolean[] keys, boolean has_focus) {
		if (shop_error_counter > 0) {
			shop_error_counter--;
			return;
		}
		if (leaving) {
			hero.tryToMove(0, 1);
			tick_world(keys, has_focus);
		} else if (!buying) {
			if (keys[KeyEvent.VK_BACK_SPACE]) {
				keys[KeyEvent.VK_BACK_SPACE] = false;
				leaving = true;
				return;
			}
			
			if (keys[KeyEvent.VK_UP]) {
				keys[KeyEvent.VK_UP] = false;
				shop_menu_option = Math.max(0, shop_menu_option-1);
			}
			if (keys[KeyEvent.VK_DOWN]) {
				keys[KeyEvent.VK_DOWN] = false;
				shop_menu_option = Math.min(2, shop_menu_option+1);
			}
			
			if (keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE]) {
				keys[KeyEvent.VK_ENTER] = false;
				keys[KeyEvent.VK_SPACE] = false;
			
				if (shop_menu_option == 2) {
					leaving = true;
				} else if (shop_menu_option == 1) {
					// Sleep
					boolean b1 = getHero().hp != getHero().full_hp;
					boolean b2 = getHero().money >= 5;
					if (b1 && b2) {
						getHero().money -= 5;
						getHero().hp = getHero().full_hp;
						// TODO: Play sound for fuck's sake
					} else if (!b1) {
						shop_error = "NO NEED!";
						shop_error_counter = 60;
					} else if (!b2) {
						shop_error = "COSTS 5 COINS!";
						shop_error_counter = 60;
					}
				} else if (shop_menu_option == 0) {
					buying = true;
				}
			}
		} else if (buying) {
			if (keys[KeyEvent.VK_BACK_SPACE]) {
				keys[KeyEvent.VK_BACK_SPACE] = false;
				buying = false;;
				return;
			}
			
			if (keys[KeyEvent.VK_UP]) {
				keys[KeyEvent.VK_UP] = false;
				buy_option = Math.max(0, buy_option-1);
			}
			if (keys[KeyEvent.VK_DOWN]) {
				keys[KeyEvent.VK_DOWN] = false;
				
				buy_option = Math.min(shop_items.length, buy_option+1);
			}
			
			if (keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_SPACE]) {
				keys[KeyEvent.VK_ENTER] = false;
				keys[KeyEvent.VK_SPACE] = false;
				
				// Buy this item! (or return)
				if (buy_option == 0) {
					buying = false;
				} else {
					InventoryItem item = shop_items[SHOP_ORDER[buy_option-1]];
					String error = "";
					boolean b1 = item.shopPrice() <= getHero().money;
					boolean b2 = getHero().hasRoomInInventory();
					if (b1 && b2) {
						getHero().money -= item.shopPrice();
						getHero().addInventoryItem(item);
					} else if (!b1) {
						shop_error = "NEED MORE MONEY!";
						shop_error_counter = 60;
					} else if (!b2) {
						shop_error = "NO MORE ROOM!";
						shop_error_counter = 60;
					}
				}
			}
		}
	}
	
	public void addParticle(Particle particle) {
		particles[(particle_cursor++)%MAX_PARTICLES] = particle;
	}
	
	// dx/dy should probably not be more than 1.
	public void moveEntity(Entity movee, Entity mover, int dx, int dy) {
		if (movee.canMove(dx, 0, mover) && dx != 0) {
			cascadeMoveEntity(movee, mover, dx, 0, Map.MAX_ENTITIES);
		}
		if (movee.canMove(0, dy, mover) && dy != 0) {
			cascadeMoveEntity(movee, mover, 0, dy, Map.MAX_ENTITIES);
		}
	}
	
	public void cascadeMoveEntity(Entity movee, Entity mover, int dx, int dy, int limit) {
		movee.move(dx, dy, mover);
		for (Entity e : curMap().getEntities()) {
			if (e != null && e != movee && e != mover) {
				// maybe change to manually
				if (movee.collidesWith(e) && limit > 0) {
					// the movee becomes the mover
					cascadeMoveEntity(e, movee, dx, dy, limit-1);
				}
			}
		}
	}
	
	// Resolves collisions
	/*
	public void checkCollision(Entity entity) {
		Rectangle r1 = entity.getRectangle(); 
		// Loop through all the wall this player might be in
		// (do all for now)
		
		for (int i=0; i<curMap().getWidth(); ++i) {
			for (int j=0; j<curMap().getHeight(); ++j) {
				Tile tile = curMap().getTile(i, j);
				if (!tile.isWalkable()) {
					Rectangle r2 = curMap().getTileRectangle(i, j);
					if (r1.intersects(r2)) {
						//System.out.println(r1 + " " + r2);
						System.out.println("INTERSECT " + i + " " + j);
						// Check what direction resolves it most easily
					}
				}
			}
		}
	}
	*/
	
	public Map curMap() {
		return maps[current_map];
	}
	
	public void render(Sprite canvas, boolean has_focus) {
		if (game_state == GAME_STATE_INGAME) {
			render_ingame(canvas, has_focus);
		} else if (game_state == GAME_STATE_START) {
			canvas.setEffectColor(0xFF00AAFF);
			Artwork.printText(canvas, "LEAVE ME ALONE!", canvas.getWidth()/2, 20, true);
			canvas.setEffectColor(0);
			
			
			for (int i=0; i<options.length; ++i) {
				String str = options[i];
				canvas.setEffectColor(0);
				if (i == menu_option) {
					str = "$" + str + " ";
					canvas.setEffectColor(0x44DDDD00);
				}
				Artwork.printText(canvas, str, canvas.getWidth()/2, canvas.getHeight()/2+i*18, true);
				canvas.setEffectColor(0);
			}
		} else if (game_state == GAME_STATE_INTRO1) {
			String strs[] = {
					"YOU LOVE BEING",
					"ALONE IN YOUR",
					"ME-TIME CAVE.",
					"",
					"NEXT"
			};
			render_text_screen(canvas, strs, 4);			
		} else if (game_state == GAME_STATE_INTRO2) {
			String strs[] = {
					"KILL ALL MEN",
					"IN THERE TO",
					"RECLAIM IT!",
					"",
					"NEXT"
			};
			render_text_screen(canvas, strs, 4);
		} else if (game_state == GAME_STATE_INTRO3) {
			String strs[] = {
					"IF YOU EXIT THE",
					"CAVE, ALL MEN",
					"WILL COME BACK!",
					"",
					"START THE GAME"
			};
			render_text_screen(canvas, strs, 4);
		} else if (game_state == GAME_STATE_HELP) {
			String strs[] = {
				"@#$%  MOVE",
				"SPACE USE",
				"ENTER INVENTORY",
				"",
				"GO BACK"
			};
			render_text_screen(canvas, strs, 4);
		} else if (game_state == GAME_STATE_ABOUT) {
			String[] strs = {
					"CREATED BY",
					"GUSTAV LARSSON",
					"IN 48 HOURS FOR",
					"LUDUM DARE 22",
					"GO BACK"
			};
			render_text_screen(canvas, strs, 4);
		}
	}
	
	public void render_text_screen(Sprite canvas, String[] strs, int highlight) {
		int x = 12;
		int y = 16;
		int dy = 20;
		for (int i=0; i<strs.length; ++i) {
			String str = strs[i];
			if (i == highlight) {
				str = "$" + str + " ";
				canvas.setEffectColor(0x44DDDD00);
			}
			Artwork.printText(canvas, str, x, y+dy*i, false);
			canvas.setEffectColor(0);
		}


	}
	
	public void render_ingame(Sprite canvas, boolean has_focus) {
		int cx = hero.pos_x-canvas.getWidth()/2;
		int cy = hero.pos_y-canvas.getHeight()/2;
		
		// Camera boundaries
		if (curMap().getWidth()*Tile.SIZE > canvas.getWidth()) {
			cx = Math.max(cx, 0);
			cx = Math.min(cx, curMap().getWidth()*Tile.SIZE - canvas.getWidth());
		}
		if (curMap().getHeight()*Tile.SIZE > canvas.getHeight()) {
			cy = Math.max(cy, 0);
			cy = Math.min(cy, curMap().getHeight()*Tile.SIZE - canvas.getHeight());
		}
		
		canvas.setCursor(cx, cy);
		
		curMap().render(canvas, hero.pos_x, hero.pos_y);
		
		Entity[] entities = curMap().getEntities();
		for (Entity e : entities) {
			if (e != null) {
				e.render(canvas);
				
				// TEMP 
				// Render weapon rectangle if attacking
				if (false && e.isAttacking()) {
					canvas.fill(0x44FF0000, e.getWeaponRectangle());
				}
			}
		}
		
		for (Item item : curMap().getItems()) {
			if (item != null) {
				item.render(canvas);
			}
		}
		
		for (Particle p : particles) {
			if (p != null) {
				p.render(canvas);
			}
		}
		//hero.render(canvas, ox, oy);
		
		canvas.setCursor(0, 0);
		
		// Render GUI
		renderGUI(canvas, has_focus);
	}
	
	public void renderGUI(Sprite canvas, boolean has_focus) {
		// hearts
		for (int i=0; i<hero.full_hp/2; ++i) {
			int tex;
			if (i < hero.hp/2) {
				tex = 14;
			} else if (i == hero.hp/2) {
				if (hero.hp%2 == 1) {
					tex = 15;
				} else {
					tex = 13;
				}
			} else {
				tex = 13;
			}
			canvas.blit(Artwork.character, i*(Tile.SIZE-2), canvas.getHeight()-Tile.SIZE, tex);
		}
		/*if (hero.hp%2 == 1) {
			canvas.blit(Artwork.character, (hero.hp/2)*(Tile.SIZE-2), canvas.getHeight()-Tile.SIZE, 15);
		} 
		*/
		
		int ix, iy;
		int buf = 3;
		ix = canvas.getWidth()-Tile.SIZE-buf;
		iy = canvas.getHeight()-Tile.SIZE-buf;
		// Selected item
		canvas.fill(0x55000000, ix-1, iy-1, Tile.SIZE+2, Tile.SIZE+2);
		canvas.fill(0x55000000, ix-2, iy-2, Tile.SIZE+4, Tile.SIZE+4);

		if (getHero().getSelectedItem() != null) {
			canvas.blit(Artwork.character, ix, iy, getHero().getSelectedItem().getTex());
		}
		
		// Money
		canvas.blit(Artwork.character, 2, 2, 23);
		Artwork.printText(canvas, String.format("%d", hero.money), 4 + Tile.SIZE, 3, false);
		
		if (the_end) {
			canvas.fill(0x66000000, 0, 0, canvas.getWidth(), canvas.getHeight());
			Artwork.printText(canvas, "THE END", canvas.getWidth()/2, canvas.getHeight()/2-5, true);
		} else if (current_map == MAP_SHOP) {
			render_shop(canvas);
		} else if (show_inventory) {
			render_inventory(canvas);
		} else if (!has_focus) {
			canvas.fill(0x66000000, 0, 0, canvas.getWidth(), canvas.getHeight());
			Artwork.printText(canvas, "PAUSED", canvas.getWidth()/2, canvas.getHeight()/2-5, true);
		} else if (the_end_counter > 0) {
			String text = "ALL TO MYSELF!";
			if (the_end_counter > 120) {
				text = "FINALLY...";
			}
			
			Artwork.printText(canvas, text, canvas.getWidth()/2, canvas.getHeight()/2-5, true);
		} else if (scene_str_counter > 0) {
			Artwork.printText(canvas, scene_str, canvas.getWidth()/2, canvas.getHeight()/2-5, true);
		}
		//Artwork.printText(canvas, "I AM ALONE,YOU?", canvas.getWidth()/2, canvas.getHeight()/2-5, true);
	}
	
	public void render_shop(Sprite canvas) {
		canvas.fill(0x66000000, 0, 0, canvas.getWidth(), canvas.getHeight());
		
		// Selection
		if (shop_error_counter > 0) {
			Artwork.printText(canvas, shop_error, canvas.getWidth()/2, canvas.getHeight()/2-10, true);
		} else if (!buying) {
			// show menu
			String[] strs = {"BUY", "SLEEP", "LEAVE"};
			for (int i=0; i<strs.length; ++i) {
				String str = strs[i];
				if (i == shop_menu_option) {
					str = "$" + str + " ";
					canvas.setEffectColor(0x44DDDD00);
				}
				Artwork.printText(canvas, str, canvas.getWidth()/2, 30 + 18 * i, true);
				canvas.setEffectColor(0);
			}
		} else {
			int x = 20;
			int y = canvas.getHeight()/2-40 - buy_option * 12;
			
			y += 12;
			for (int i=-1; i<shop_items.length; ++i) {
				String str;
				
				if (i == -1) {
					str = "GO BACK";
		
				} else {
					str = String.format("%7s %3d", shop_items[SHOP_ORDER[i]].getName(), shop_items[SHOP_ORDER[i]].shopPrice());
				}
				
				
				if (i+1 == buy_option) {
					str = "$" + str;
					canvas.setEffectColor(0x44DDDD00);
				} else {
					str = " "+str;
				}
				Artwork.printText(canvas,  str, x, y, false);
				canvas.setEffectColor
				
				
				(0);	
				if (i >= 0) {
					canvas.blit(Artwork.character, canvas.getWidth()-x-10, y-2, shop_items[SHOP_ORDER[i]].getTex());
				}
				
				y += 12;
			}
		}
		
	}
	
	public void render_inventory(Sprite canvas) {
		canvas.fill(0x66000000, 0, 0, canvas.getWidth(), canvas.getHeight());
		Artwork.printText(canvas, "INVENTORY", canvas.getWidth()/2, canvas.getHeight()/2-45, true);
		
		InventoryItem[] inventory = getHero().getInventory();
		int bb = 6;
		int between = (Tile.SIZE+bb);
		int buf = 7;
		int x0 = canvas.getWidth()/2 + (-inventory.length/2)*between-buf;
		int y0 = 35;
		canvas.fill(0x99000000, x0, y0, inventory.length*between + 2*buf - bb, between + 2*buf - bb);
		
		for (int i=0; i<inventory.length; ++i) {
			int x = x0 + buf + (i)*between;
			int y = y0 + buf;
			if (i == getHero().getSelectedItemIndex()) {
				canvas.fill(0x33FF0000, x-2, y-2, Tile.SIZE+4, Tile.SIZE+4);
				canvas.fill(0xFF000000, x-1, y-1, Tile.SIZE+2, Tile.SIZE+2);
			}
			

			if (inventory[i] != null) {
				canvas.blit(Artwork.character, x, y, inventory[i].getTex());
			}
		}
		
		InventoryItem item = getHero().getSelectedItem();
		if (item != null && item.price() != -1) {
			Artwork.printText(canvas, String.format("PRICE %d", item.price()), canvas.getWidth()/2, canvas.getHeight()/2+10, true);
			//canvas.setEffectColor(0x55333333);
			Artwork.printText(canvas, "PRESS S TO SELL", canvas.getWidth()/2, canvas.getHeight()/2+40, true);
			//canvas.setEffectColor(0);
		}
	}

	public void makeDamageInRectangle(Rectangle r1, int damage, Entity except_entity) {
		for (Entity e : curMap().getEntities()) {
			if (e != null && e != except_entity && e.getRectangle().intersects(r1)) {
				e.takeDamage(damage);
			}
		}
	}
	
	public void dropRandomItem(int pos_x, int pos_y, int goodness) {
		if (goodness > 0) {
			if (Math.random() > 0.5) {
				// Drop coin
				dropCoin(pos_x, pos_y, goodness + (int)(Math.random() * goodness));
			} else {
				// What's the probability of dropping something?
				if (Math.random() < goodness/10.0) {
					if (Math.random() > 0.9) {
						dropPotion(pos_x, pos_y);
					} else if (Math.random() > 0.8) {
						// Drop weapon (this is rare)
						dropRandomWeapon(pos_x, pos_y, goodness);
					} else {
						// Drop heart
						dropHeart(pos_x, pos_y);
						
						// Drop potion?
					}
				}
			}
		}
	}
	
	public void dropHeart(int pos_x, int pos_y) {
		Heart heart = new Heart(this);
		heart.setPos(pos_x, pos_y);
		curMap().addItemWithAnimation(heart);
	}
	
	public void dropPotion(int pos_x, int pos_y) {
		Potion potion = new Potion(this);
		potion.setPos(pos_x, pos_y);
		curMap().addItemWithAnimation(potion);
	}
	
	public void dropCoin(int pos_x, int pos_y, int value) {
		Coin coin = new Coin(this);
		coin.setPos(pos_x, pos_y);
		coin.setValue(value);
		curMap().addItemWithAnimation(coin);
	}
	
	public void dropRandomWeapon(int pos_x, int pos_y, int goodness) {
		int type = 3;
		for (int i=0; i<2; ++i) {
			if (goodness >= 5 && Math.random() < goodness/40.0) {
				type += 1;
			}
		}
		type += (int)(Math.random()*2) * 3;
		Weapon weapon = new Weapon(this);
		weapon.setType(type);
		weapon.setPos(pos_x, pos_y);
		curMap().addItemWithAnimation(weapon);
	}
	
	public Hero getHero() {
		return hero;
	}
	
	public Entity[] getEntities() {
		return curMap().getEntities();
	}
	
	public static int animationFrame(int frames, int ticks_per_frame) {
		return (Engine.gtick%(frames*ticks_per_frame))/ticks_per_frame;
	}
	
	public void checkMenInCave() {
		if (current_map == MAP_CAVE && getHero().hp > 0) {
			int count = 0;
			for (Entity e : curMap().getEntities()) {
				if (e != null && e != getHero() && e.hp > 0) {
					count++;
					System.out.println(e);
				}
			}
			
			if (count == 0) {
				initTheEnd();
			}
		}
	}
	
	public void initTheEnd() {
		the_end_counter = 240;
	}
}
