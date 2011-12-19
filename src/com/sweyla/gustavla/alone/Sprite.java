package com.sweyla.gustavla.alone;

import java.awt.Rectangle;

public class Sprite {
	private int[] pixmap;
	private int[] zbuffer;
	private int width;
	private int height;
	private int textureSize;
	
	private int cx;
	private int cy;
	
	private int effectColor;
	private int replaceColor1;
	private int replaceColor2;
	private int blit_opacity;
	
	// Use Entity.
	public static final int MIRROR_X = 4;
	public static final int MIRROR_Y = 5; 
	
	public Sprite(int width, int height) {
		this(width, height, false);
	}
	
	public Sprite(int width, int height, boolean use_zbuffer) {
		this.width = width;
		this.height = height;
		this.textureSize = 10;
		pixmap = new int[width * height];
		if (use_zbuffer) {
			zbuffer = new int[width * height];
		} else {
			zbuffer = null;
		}
		cx = cy = 0;
		// Stay away from all black for these
		effectColor = 0x0;
		replaceColor1 = 0x0;
		replaceColor2 = 0x0;
		blit_opacity = 0xFF;
	}
	
	public void setBlitOpacity(int blit_opacity) {
		this.blit_opacity = blit_opacity;
	}
	
	public void setTextureSize(int textureSize) {
		this.textureSize = textureSize;
	}
	
	public void clear() {
		for (int i = 0; i < width * height; ++i) {
			pixmap[i] = 0;
			zbuffer[i] = 0;
		}
	}
	
	public void fill(int color) {
		fill(color, 0, 0, width, height);
	}
	
	public void fill(int color, int x, int y, int w, int h) {
		for (int i = x; i < x+w; ++i) {
			for (int j = y; j < y+h; ++j) {
				//pixmap[i + j * width] = color;
				overPixel(i, j, color);
			}
		}
	}
	
	public void fill(int color, Rectangle r) {
		fill(color, r.x, r.y, r.width, r.height);
	}
	
	public void blit(Sprite sprite, int x, int y) {
		blit(sprite, x, y, -1, 0, 0);
	}
	
	/*
	public void blitAnimation(Sprite sprite, int x, int y, int[] texes, int tick) {
		blit(sprite, x, y, texes[tick%texes.length]);
	}*/
	
	public void blit(Sprite sprite, int x, int y, int tex) {
		blit(sprite, x, y, tex, 0, -1);
	}
	public void blit(Sprite sprite, int x, int y, int tex, int rot, int z) {
		int x0 = 0;
		int y0 = 0;
		int x1 = sprite.getWidth();
		int y1 = sprite.getHeight();
		
		if (tex != -1) {
			x0 = (tex%10) * sprite.textureSize;
			x1 = x0 + sprite.textureSize;
			y0 = (tex/10) * sprite.textureSize;
			y1 = y0 + sprite.textureSize;
		}
		
		int xx, yy;
		for (int i = x0; i < x1; ++i) {
			for (int j = y0; j < y1; ++j) {
				xx = i;
				yy = j;
				if (rot == 1 || rot == 3) {
					yy = y0+(i-x0);
					xx = x1-1-(j-y0);
				}
				if (rot >= 2 && rot <= 3) {
					xx = x1-1-(xx-x0);
					yy = y1-1-(yy-y0);
				}
				// Mirror
				if (rot == MIRROR_X) {
					xx = x1-1-(xx-x0);
				} else if (rot == MIRROR_Y) {
					yy = y1-1-(yy-y0);
				}
				
				int fx = x+i-x0;
				int fy = y+j-y0;
				
				int color = sprite.getPixel(xx, yy);

				overPixel(fx, fy, color, z);
			}
		}
	}
	
	public static int darken(int color, double amount) {
		return (((color>>24)&0xFF)<<24) | ((int)(((color>>16)&0xFF)*amount)<<16) | ((int)(((color>>8)&0xFF)*amount)<<8) | ((int)((color&0xFF)*amount));
	}
	
	public int getZBuffer(int x, int y) {
		if (pixelInside(x, y)) {
			return zbuffer[(x-cx) + (y-cy)*width];
		} else {
			return 0;
		}
	}
	
	public void setZBuffer(int x, int y, int z) {
		if (pixelInside(x, y)) {
			zbuffer[(x-cx) + (y-cy)*width] = z;
		}
	}
	
	private boolean pixelInside(int x, int y) {
		return (x-cx) >= 0 && (x-cx) < width && (y-cy) >= 0 && (y-cy) < height;
	}
	
	public int getPixel(int x, int y) {
		return getPixel(x, y, 0, 0);
	}
	public int getPixel(int x, int y, int ox, int oy) {
		if (pixelInside(ox+x, oy+y)) {
			int color = pixmap[(ox+x-cx) + (oy+y-cy)*width];
			
			// Handle replace colors
			if (replaceColor1 != 0) {
				if (color == 0xFF8EFCCA) {
					color = replaceColor1;
				}
				if (color == 0xFF31FBA9) {
					color = darken(replaceColor1, 0.8);
				} else if (color == 0xFF25D592) {
					color = darken(replaceColor1, 0.6);
				} else if (color == 0xFF189C6B) {
					color = darken(replaceColor1, 0.35);
				}
			}
			
			if (replaceColor2 != 0) {
				if (color == 0xFF0092FF) {
					color = replaceColor2;
				} else if (color == 0xFF0080E1) {
					color = darken(replaceColor2, 0.8);
				}
			}

			return color;
		} else {
			return 0x00000000;
		}
	}
	
	public int getPixel(int x, int y, int tex) {
		int ox = (tex%10) * 10;
		int oy = (tex/10) * 10;
		return getPixel(x, y, ox, oy);
	}
	
	public void setPixel(int x, int y, int color) {
		if (pixelInside(x, y)) {
			pixmap[(x-cx) + (y-cy)*width] = color;
		}
	}
	
	public void setCursor(int cx, int cy) {
		this.cx = cx;
		this.cy = cy;
	}
	
	public void setPixmap(int[] pixmap) {
		if (pixmap == null || pixmap.length != width * height) {
			System.err.println("setPixmap rejected because of mismatched size!");
		} else {
			this.pixmap = pixmap;
		}
	}
	
	public int[] getPixmap() {
		return pixmap;
	}
	
	public void overPixel(int x, int y, int color) {
		overPixel(x, y, color, -1);
	}

	/*
	 * 			if (effectColor != 0) {
				overPixel(fx, fy, effectColor);
			}

	 */
	
	public void overPixel(int x, int y, int color, int z) {
		__overPixel(x, y, color, z);
		if (effectColor != 0 && ((color>>24)&0xFF) != 0) {
			__overPixel(x, y, effectColor, z);
		}
	}
	
	public void __overPixel(int x, int y, int color, int z) {
		boolean draw = false;
		if (((color>>24)&0xFF) != 0 && pixelInside(x, y)) {
			if (zbuffer != null && z != -1) {
				if (z >= getZBuffer(x, y)) {
					setZBuffer(x, y, z);
					draw = true;
				}
			} else {
				draw = true;
			}
		}
		if (draw) {			
			int alpha = (color>>24)&0xFF;
			
			if (blit_opacity != 0xFF) {
				alpha = (int)((double)alpha*blit_opacity/255.0);
				System.out.println("YES");
			}


			
			if (alpha == 0xFF) {
				setPixel(x, y, color);
			} else if (alpha == 0) {
				// do nothing
			} else {
				//System.out.println(alpha);
				int c = getPixel(x, y);
				double r0 = ((c>>16)&0xFF)/255.0;
				double g0 = ((c>>8 )&0xFF)/255.0;
				double b0 = ((c>>0 )&0xFF)/255.0;
				double a0 = ((c>>24)&0xFF)/255.0;
				
				double r1 = ((color>>16)&0xFF)/255.0;
				double g1 = ((color>>8 )&0xFF)/255.0;
				double b1 = ((color>>0 )&0xFF)/255.0;
				double a1 = (alpha)/255.0;
				
				

				
				double r2 = r1 * a1 + r0 * a0 * (1 - a1);
				double g2 = g1 * a1 + g0 * a0 * (1 - a1);
				double b2 = b1 * a1 + b0 * a0 * (1 - a1);
				double a2 = a1 + a0 * (1 - a1);
				
				int result = ((int)(a2*0xFF)<<24)|((int)(r2*0xFF)<<16)|((int)(g2*0xFF)<<8)|((int)(b2*0xFF));
				setPixel(x, y, result);
			}
		}
	}
	
	public int getChannel(int x, int y, int channel) {
		int c = getPixel(x, y);
		return (c>>(8*(2-channel)))&0xFF;
	}
	
	public void setPixelRGB(int x, int y, int r, int g, int b) {
		setPixel(x, y, ((int)(0xFF)<<24)|((int)(r)<<16)|((int)(g)<<8)|((int)(b)));
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public int getEffectColor() { return effectColor; }
	public void setEffectColor(int effectColor) {
		this.effectColor = effectColor;
	}
	
	public int getReplaceColor1() { return replaceColor1; }
	public void setReplaceColor1(int color) { replaceColor1 = color; }
	
	public int getReplaceColor2() { return replaceColor2; }
	public void setReplaceColor2(int color) { replaceColor2 = color; }
}
