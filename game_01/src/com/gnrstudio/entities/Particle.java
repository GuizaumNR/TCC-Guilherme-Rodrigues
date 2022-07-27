package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;

public class Particle extends Entity{
	
	public int lifeTime = 20;
	public int curLife = 0;
	
	public int spd = 2;
	public double dx = 0;
	public double dy = 0;

	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
	}

	public void tick() {
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if(curLife == lifeTime) {
			Game.entities.remove(this);
		}
	}
	public void render(Graphics g) {
		 g.setColor(Color.YELLOW); 
		 g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}
