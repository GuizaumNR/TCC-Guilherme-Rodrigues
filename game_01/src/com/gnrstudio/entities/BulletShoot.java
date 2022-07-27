package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.World;

public class BulletShoot extends Entity {

	private double dx;
	private double dy;
	private double spd = 4;

	private BufferedImage BulletShootSprite;

	private int life = 30, curLife = 0; // tempo de "vida" das balas
	
	int z = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);

		BulletShootSprite = Game.spritesheet.getSprite(96, 32, 3, 1);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		if(World.isFreeDynamic((int)(x + dx * spd),(int) (y + dy * spd), 3, 1)) {
		x += dx * spd;
		y += dy * spd;
		}else {
			destroySelf();
		}
		curLife++;
		if (curLife == life) {
			Game.bullets.remove(this); // removendo essa bala
			return; // sempre se da um return qunado se remove o proprio objeto
		}
		
	}

	public void destroySelf() {
		Game.bullets.remove(this);
		World.generateParticles(100,(int) x,(int) y);
	}



	public void render(Graphics g) {
		g.drawImage(BulletShootSprite, this.getX() - Camera.x, this.getY() - Camera.y, 3, 1, null);


//		 g.setColor(Color.BLUE); 
//		 g.fillRect(this.getX() + 3 - Camera.x, this.getY() + 1 - Camera.y, 3, 1);
	}
}
