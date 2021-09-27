package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;

public class BulletShoot extends Entity{

	private int dx;
	private int dy;
	private double spd = 4;
	
	private BufferedImage BulletShootSprite;
	
	private int life = 10, curLife = 0; //tempo de "vida" das balas
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		
		BulletShootSprite = Game.spritesheet.getSprite(96, 32, 13, 11);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if(curLife == life) { 
			Game.bullets.remove(this); //removendo essa bala
			return; // sempre se da um return qunado se remove o proprio objeto
		}
	}
	  public void render(Graphics g) {
		  //g.drawImage(BulletShootSprite,96, 32, 1, 4, null);
		  
		  g.setColor(Color.YELLOW);
		  g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	  }
}
