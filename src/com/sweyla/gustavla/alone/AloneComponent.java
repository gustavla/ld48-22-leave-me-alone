package com.sweyla.gustavla.alone;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AloneComponent extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	final static int WIDTH = 160;
	final static int HEIGHT = 120;
	final static int PIXELSIZE = 3;
	
	private BufferedImage buffer;
	private int[] pixmap;
	private Sprite canvas;
	
	private Engine engine;
	
	public Thread thread;
	public boolean running;
	
	private InputHandler inputHandler;
	
	public AloneComponent() {
		engine = new Engine();
		Dimension d = new Dimension(WIDTH * PIXELSIZE, HEIGHT * PIXELSIZE);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);
		running = false;

		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		DataBufferInt dbi = (DataBufferInt)buffer.getRaster().getDataBuffer();
		
		canvas = new Sprite(WIDTH, HEIGHT, true);
		pixmap = dbi.getData();

		canvas.fill(0xFF00FFFF);
		
		inputHandler = new InputHandler();
		
		addKeyListener(inputHandler);
		addFocusListener(inputHandler);
		addMouseListener(inputHandler);
		addMouseMotionListener(inputHandler);
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		
		running = true;
		thread = new Thread(this);
		thread.run();
	}
	
	public synchronized void stop() {
		if (!running) {
			return;
		}
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double hz = 1.0/60.0;
		boolean ran = false;
		
		double seconds = 0.0;
		int frames = 0;
		long lastWholeSecond = lastTime;
		
		requestFocus();
		while (running) {
			long curTime = System.nanoTime();
			long diff = curTime - lastTime;
			lastTime = curTime;

			//if (diff > 1000000000L) {
				//diff = 1000000000L;
			//}
			seconds += diff/1000000000.0;
			
			ran = false;
			
			while (seconds > hz) {
				tick();
				ran = true;
				seconds -= hz;
				//subtick = subtick%60;
			}
			
			if (ran) {
				render();
				++frames;
				if (curTime - lastWholeSecond > 1000000000L) {
					System.out.println(frames + " fps");
					lastWholeSecond += 1000000000L;
					frames = 0;
				}
			} else {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void tick() {
		engine.tick(inputHandler.getKeys(), hasFocus());
	}
	
	public static int t = 0;
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		// TODO: Needed in the end?
		canvas.clear();
		
		engine.render(canvas, hasFocus());
		
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				pixmap[i + j*WIDTH] = 0xFFFFFF & canvas.getPixel(i, j);
			}
		}
		
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(buffer, 0, 0, WIDTH * PIXELSIZE, HEIGHT * PIXELSIZE, null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		AloneComponent component = new AloneComponent();
		JFrame frame = new JFrame("Leave me alone!");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.CENTER);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		component.start();
	}
}
