package com.sweyla.gustavla.alone;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Artwork {
	public static Sprite character = loadSprite("/sprites/character.png", 10);
	public static Sprite tiles = loadSprite("/sprites/tiles.png", 10);
	public static Sprite alphanumeric = loadSprite("/sprites/alphabet.png", 10);
	
	public static Sprite loadSprite(String filename, int textureSize) {
		BufferedImage img;
		try {
			img = ImageIO.read(Artwork.class.getResource(filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Sprite sprite = new Sprite(img.getWidth(), img.getHeight());
		
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), sprite.getPixmap(), 0, img.getWidth());
		
		int[] pixmap = sprite.getPixmap();
		for (int i=0; i<pixmap.length; ++i) {
			int c = pixmap[i];
			if (c == 0xffff40ff) {
				pixmap[i] = 0x0;
			}
		}
		
		sprite.setTextureSize(textureSize);
		return sprite;
	}
	
	public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?0123456789-+(@#$%[ ";
	
	// Print text
	public static void printText(Sprite canvas, String str, int x, int y, boolean center) {
		int old_effect = canvas.getEffectColor();
		
		for (int relief=0; relief<2; ++relief) {
			if (relief == 0) {
				canvas.setEffectColor(0xAA000000);
			} else {
				canvas.setEffectColor(old_effect);
			}
			
			int cx = x;
			if (center) {
				cx -= 9*str.length()/2;
			}
			for (int i=0; i<str.length(); ++i) {
				int index = alphabet.indexOf(str.charAt(i));
				if (index != -1) {
					canvas.blit(alphanumeric, cx, y-relief, index);
				}
					
				cx += 9;
			}
		}
	}
}
