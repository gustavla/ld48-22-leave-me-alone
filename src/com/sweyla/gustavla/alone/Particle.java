package com.sweyla.gustavla.alone;

public class Particle {
	int color;
	double x, y, z;
	// velocity
	double vx, vy, vz;
	int life;
	
	public Particle(int color, int start_x, int start_y, int start_z, double vx, double vy, double vz) {
		this.color = color;
		x = (double)start_x;
		y = (double)start_y;
		z = (double)start_z;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		life = 63;
	}
	
	public void tick() {
		x += vx;
		y += vy;
		z += vz;
		vz -= 0.1;
		if (z < 0) {
			vz *= -0.3;
			z = -z;
		}
		life--;
	}
	
	public void render(Sprite canvas) {
		int c = color;
		c = ((( (int)((double)((color>>24)&0xFF)/0xFF)*life*4&0xFF )<<24))|(color&0xFFFFFF);
		canvas.overPixel((int)x, (int)(y-z), c, (int)(y-z));
	}
	
	public boolean needsCleanup() {
		return life <= 0;
	}
}
