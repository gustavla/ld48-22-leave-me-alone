package com.sweyla.gustavla.alone;

public class Item extends Thing {
	int pos_x, pos_y;
	int entry_counter;
	double y;
	double vy;

	public Item(Engine engine) {
		super(engine);
	}
	
	public void tick() {
		if (entry_counter > 0) {
			if (--entry_counter == 0) {
				y = 0.0;
			} else {
				vy += 0.3;
				y -= vy;
				if (y < 0.0) {
					vy *= -0.5;
					y = -y;
				}
			}
		}
	}
	
	public int getOffsetX() {
		return 0;
	}
	
	public int getOffsetY() {
		return -2 - (int)y;
	}
	
	public int getOffsetZ() {
		return -2;
	}
	
	public boolean pickUp(Entity pickerUpper) {
		return true;
	}
	
	public void initEntryAnimation() {
		entry_counter = 60;
		y = 0.0;
		vy = 5.0;
	}
	
	public boolean canPickup() {
		return (entry_counter <= 40 && pickUppable());
	}
	
	// If false, it's just something for decoration
	public boolean pickUppable() {
		return true;
	}
}
