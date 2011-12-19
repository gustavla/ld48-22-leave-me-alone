package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Entity extends Thing {
	public static final int LEFT = 0;
	public static final int DOWN = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	
	Engine engine;
	
	int facing; // not all entities use this
	
	int gtick;
	
	// half hearts
	int hp;
	int full_hp;
	
	int money;

	// public (who cares!?)
	public int move_dx, move_dy;
	int attack_counter;
	int attack_refactor_counter;
	int damage_counter;
	
	// Entity trying to move this entity
	public Entity mover;
	
	public Entity(Engine engine) {
		super(engine);
		this.engine = engine;
		radius = Tile.SIZE/2;
		facing = DOWN;
		move_dx = move_dy = 0;
		gtick = 0;
		hp = full_hp = 6;
		attack_counter = 0;
		attack_refactor_counter = 0;
		damage_counter = 0;
		money = 0;
	}
	
	public void playDamageSound() {
		
	}
	
	public void takeDamage(int damage) {
		playDamageSound();
		hp -= damage;
		
		if (damage > 0) {
			damage_counter = 10;
		}
		
		if (hp <= 0) {
			doWhenDie();
		}
	}
	
	
	public boolean isAlive() {
		return hp > 0;
	}
	
	void tick() {
		
		if (isAlive()) {
			if (attack_counter > 0) {
				attack_counter--;
			} else {
				// Can't refactor the attack when attacking
				if (attack_refactor_counter > 0) {
					attack_refactor_counter--;
				}
			}
			if (damage_counter > 0) {
				damage_counter--;
			}
		}
	}
	
	public void doWhenDie() {
		// Drop item depending on drop goodness
		int g = getDropGoodness();
		engine.dropRandomItem(pos_x, pos_y, g);
		//Sound.die2.play();
	}
	
	void move(int dx, int dy, Entity mover) {
		pos_x += dx;
		pos_y += dy;
	}
	
	public int getAttackTime() {
		return 18;
	}
	
	public int getAttackRefactorTime() {
		return 60;
	}

	public int getDyingTime() {
		return 0;
	}
	
	public Rectangle getWeaponRectangle() {
		return new Rectangle(0, 0, 0, 0);
	}
	
	public int getAttackDamage() {
		return 1;
	}
	
	public void tryToAttack() {
		if (attack_counter == 0 && attack_refactor_counter == 0) {
			// Attack
			Rectangle r1 = getWeaponRectangle();
			engine.makeDamageInRectangle(r1, getAttackDamage(), this);
			attack_counter = getAttackTime();
			attack_refactor_counter = getAttackRefactorTime();
			playAttackSound();
		}
	}
	
	public void playAttackSound() {
		//Sound.attack.play();
	}

	public void tryToMove(int dx, int dy) {
		move_dx = dx;
		move_dy = dy;
	}

	public boolean wantsToMove() {
		return move_dx != 0 || move_dy != 0;
	}
	
	public boolean isAttacking() {
		return attack_counter > 0;
	}
	
	public boolean collidesWith(Entity e) {
		return (getRectangle().intersects(e.getRectangle()));
	}
	
	public int distanceTo(Entity e) {
		return (int)Math.sqrt(Math.pow(pos_x-e.pos_x, 2) + Math.pow(pos_y-e.pos_y, 2));
	}
	
	public boolean needsCleanup() {
		return hp<=0;
	}
	
	public boolean canMove(int dx, int dy, Entity mover) {
		Rectangle r1 = getRectangle();
		r1.x += dx;
		r1.y += dy;

		// Check collision with other entities
		for (Entity e : engine.getEntities()) {
			if (e != null && e != this && rightDirection(this, e, dx, dy)) {
				Rectangle r2 = e.getRectangle();
				if (r1.intersects(r2)) {
					if (!e.canMove(dx, dy, this)) {
						return false;
					}
				}
			}
		}
		
		// Check collision with walls
		for (int i=-1; i<=engine.curMap().getWidth(); ++i) {
			for (int j=-1; j<=engine.curMap().getHeight(); ++j) {
				Tile tile = engine.curMap().getTile(i, j);
				if (!tile.isWalkable()) {
					Rectangle r2 = engine.curMap().getTileRectangle(i, j);
					if (r1.intersects(r2)) {
						return false;
					}
				}
			}
		}

		return true;
	}
	
	public static boolean rightDirection(Entity e1, Entity e2, int dx, int dy) {
		if (dx > 0) {
			return e2.pos_x > e1.pos_x;
		} else if (dx < 0) {
			return e2.pos_x < e1.pos_x;
		} else if (dy > 0) {
			return e2.pos_y > e1.pos_y;
		} else if (dy < 0) {
			return e2.pos_y < e1.pos_y;
		} else {
			return false;
		}
	}
	
	public void gainHealth(int amount) {
		hp = Math.min(full_hp, hp + amount);
	}

	public int getEffectColor() {
		if (damage_counter == 0) {
			return 0;
		} else {
			return ((damage_counter*30)<<24)|0xFFFFFF;
		}
	}
	
	public Tile getStandingOnTile() {
		return engine.curMap().getTile(pos_x/Tile.SIZE, pos_y/Tile.SIZE);
	}
	
	public void setFacing(int facing) {
		this.facing = facing;
	}
	
	public boolean useParticleDestroy() {
		return true;
	}
	
	public void gainMoney(int value) {
		money += value;
	}

	public void render(Sprite canvas) {
		canvas.setEffectColor(getEffectColor());
		int rot = 0;
		if (facing == Entity.RIGHT) {
			rot = Sprite.MIRROR_X;
		}
		Artwork.character.setReplaceColor1(color1);
		Artwork.character.setReplaceColor2(color2);
		int c = spriteSize()/Tile.SIZE;
		for (int i=0; i<c; ++i) {
			for (int j=0; j<c; ++j) {
				canvas.blit(Artwork.character, pos_x-spriteSize()/2+getOffsetX()+i*Tile.SIZE, pos_y-spriteSize()/2+j*Tile.SIZE+getOffsetY(), getTex()+i+j*10, rot, pos_y+getOffsetZ());
			}
		}
		canvas.setEffectColor(0x0);
	}
	
	public boolean shouldReset() {
		return true;
	}
	
	public int getTilePosX() { 
		return pos_x/Tile.SIZE;
	}
	
	public int getTilePosY() {
		return pos_y/Tile.SIZE;
	}
	
	public int getDropGoodness() {
		return 0;
	}
}

