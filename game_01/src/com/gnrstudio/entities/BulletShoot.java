package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;

public class BulletShoot extends Entity{

	private double dx;
	private double dy;
	private double spd = 4;
	
	private BufferedImage BulletShootSprite;
	
	private int life = 30, curLife = 0; //tempo de "vida" das balas
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		
		BulletShootSprite = Game.spritesheet.getSprite(96, 32, 4, 1);
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
		
//		if(collidingWall()) {
//		destroySelf();
//		}
		
		
	}
	
//	public void destroySelf() {
//		Game.bullets.remove(this);
//		
//	}
//	
//	public boolean collidingWall() {
//		for(int i = 0; i < Game.entities.size(); i++) {
//			Entity e = Game.entities.get(i);
//			if(e instanceof BulletShoot) {
//				if(Entity.isColidding(this, e)) {
//					return true;
//				}
//			}
//		}
//		return false;
//		
//	}
	
	  public void render(Graphics g) {
		  g.drawImage(BulletShootSprite,this.getX() - Camera.x, this.getY() - Camera.y, 6, 1 , null);
		  
//		  g.setColor(Color.YELLOW);
//		  g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	  }
}
