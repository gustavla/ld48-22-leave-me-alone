package com.sweyla.gustavla.alone;


public class NPC extends Human {
	int time_until_next_move;
	int ongoing_move_dx, ongoing_move_dy;
	public boolean friendly;
	private boolean cave_npc;
	Weapon weapon;

	public NPC(Engine engine) {
		super(engine);
		resetTimeUntilNextMove();
		friendly = false;
		cave_npc = false;
		weapon = new Weapon(engine);
		if (Math.random() > 0.5) {
			weapon.setType(0);
		} else {
			weapon.setType(3+(int)(Math.random()*2) + 3*(int)(Math.random()*2));
		}
	}
	
	public void setCaveNPC(boolean cave_npc) {
		this.cave_npc = cave_npc;
	}
	
	public int getAttackRefactorTime() {
		return 3 * 5;
	}

	public void takeDamage(int value) {
		super.takeDamage(value);
		friendly = false;
		
		// Face hero if he's close
		if (engine.getHero() != null && distanceTo(engine.getHero()) < 5*Tile.SIZE) {
			int x = engine.getHero().pos_x;
			int y = engine.getHero().pos_y;
			
			if (Math.abs(x-pos_x) > Math.abs(y-pos_y)) {
				if (x < pos_x) {
					facing = LEFT;
				} else {
					facing = RIGHT;
				}
			} else {
				if (y < pos_y) {
					facing = UP;
				} else {
					facing = DOWN;
				}
			}
			System.out.println("facing = " + facing);
		}
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
				moving = false;
				resetTimeUntilNextMove();
			}
		} else if (--time_until_next_move <= 0 && attack_counter == 0) {
			// Issue a move!
			ongoing_move_dx = (int)(Math.random()*3)-1;
			ongoing_move_dy = (int)(Math.random()*3)-1;
			if (ongoing_move_dx != 0 || ongoing_move_dy != 0) {
				moving = true;
			}
			time_until_next_move = 60;
		}
		if (attack_counter == 0 && !friendly) {
			// Check if player is close by
			if (engine.getHero().distanceTo(this) <= 50) {
				// Try to attack with a certain chance
				if ((int)(Math.random()*40) == 0) {
					tryToAttack();
				}
			}
		}
	}
	
	public void resetTimeUntilNextMove() {
		time_until_next_move = 60 + (int)(Math.random() * 300);
		//time_until_next_move = 10;
	}
	
	public void doWhenDie() {
		super.doWhenDie();
		if (cave_npc) {
			engine.checkMenInCave();
		}
	}
	
	public int getWeaponType() {
		return weapon.getType();	
	}
	
	public InventoryItem getSelectedItem() {
		return weapon;
	}
	
	public int getDropGoodness() {
		return 0; // don't kill humans. It does no good (unless you want to be alone)
	}
}
