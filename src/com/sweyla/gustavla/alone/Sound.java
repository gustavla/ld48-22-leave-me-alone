package com.sweyla.gustavla.alone;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	public Clip clip;
	
	//public static Sound attack = loadSound("/sounds/attack.wav");
	/*
	public static Sound attack = loadSound("/sounds/attack.wav");
	public static Sound coin = loadSound("/sounds/coin.wav");
	public static Sound damage = loadSound("/sounds/damage.wav");
	public static Sound die = loadSound("/sounds/die.wav");
	public static Sound die2 = loadSound("/sounds/die2.wav");
	public static Sound pickup = loadSound("/sounds/pickup.wav");
	public static Sound pickup2 = loadSound("/sounds/pickup2.wav");
	public static Sound potion = loadSound("/sounds/potion.wav");
	public static Sound potion2 = loadSound("/sounds/potion2.wav");
	public static Sound slime = loadSound("/sounds/slime.wav");
	public static Sound humandamage = loadSound("/sounds/humandamage.wav");
	public static Sound monsterdamage = loadSound("/sounds/monsterdamage.wav");
	public static Sound swing = loadSound("/sounds/swing.wav");
	*/
	
	//public static Sound walk_stone = loadSound("/sounds/walk-stone.wav");
	//public static Sound blur = loadSound("/sounds/blur.wav");
	
	public static Sound loadSound(String fileName) {
		Sound sound = new Sound();
		try {
			sound.clip = (Clip) AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
			sound.clip.open(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sound;
	}
	
	public void play() {
		// Has been turned off
		/*
		try {
			if (clip != null) {
				new Thread(new Runnable() {
					public void run() {
						synchronized (clip) {
							try {
								clip.stop();
								clip.setFramePosition(0);
								clip.start();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

}
