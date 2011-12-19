package com.sweyla.gustavla.alone;

public class Coin extends Item {
	public Coin(Engine engine) {
		super(engine);
	}

	int value;
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean pickUp(Entity pickerUpper) {
		pickerUpper.gainMoney(value);
		//Sound.coin.play();
		return true;
	}
	
	public int getTex() {
		int add = 0;
		if (value >= 10) {
			add = 20;
		} else if (value >= 5) {
			add = 10;
		}
		return 24 + add + Engine.animationFrame(2, 30);
	}

}
