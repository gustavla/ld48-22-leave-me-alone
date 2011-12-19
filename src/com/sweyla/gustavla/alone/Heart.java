package com.sweyla.gustavla.alone;

public class Heart extends Item {

	public Heart(Engine engine) {
		super(engine);
		// TODO Auto-generated constructor stub
	}
	
	public boolean pickUp(Entity pickerUpper) {
		pickerUpper.gainHealth(2);
		///Sound.pickup.play();
		return true;
	}
	
	public int getTex() {
		return 12 + Engine.animationFrame(2, 30) * 2;
	}
}
