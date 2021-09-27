package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;

public class BulletShoot extends Entity{

	private int dx;
	private int dy;
	private double spd;
	
	private BufferedImage BulletShootSprite;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		BulletShootSprite = Game.spritesheet.getSprite(96, 32, 13, 11);
		
	}
	
	public void tick() {
		x += dx * spd;
		y += dy * spd;
		
		
	}
	  public void render(Graphics g) {
		  //g.drawImage(BulletShootSprite,96, 32, 1, 4, null)
		  
		  g.setColor(Color.YELLOW);
		  g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	  }
}
