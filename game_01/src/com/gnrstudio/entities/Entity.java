package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;

public class Entity {

	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(98, 5, 11, 11);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(0 , 96, 10, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96, 20, 13, 11);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 32, 16, 16);
	public static BufferedImage BUSH_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage RIGHTGUN_EN = Game.spritesheet.getSprite(112, 0, 10, 5);
	public static BufferedImage LEFTGUN_EN = Game.spritesheet.getSprite(134, 0, 10, 5);
	public static BufferedImage DRIGHTGUN_EN = Game.spritesheet.getSprite(112, 80, 10, 5);
	public static BufferedImage DLEFTGUN_EN = Game.spritesheet.getSprite(134, 80, 10, 5);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;

	private BufferedImage sprite;

	private int maskx, masky, mwidth, mheight;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}

	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void tick() {

	}

	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.getHeight() + e1.mheight,
				e1.getWidth() + e1.mwidth);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.getHeight() + e2.mheight,
				e2.getWidth() + e2.mwidth);

		return e1Mask.intersects(e2Mask);
	}

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
//	    g.setColor(Color.red);
//	    g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
	}

}
