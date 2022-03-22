package com.gnrstudio.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 64, 16, 16);
	public static BufferedImage TILE_FLOOR2 = Game.spritesheet.getSprite(0, 5*16, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(0, 96, 16, 16);
	public static BufferedImage TILE_WALL2 = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage TILE_TOMB = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage TILE_TOMB2 = Game.spritesheet.getSprite(16, 80, 16, 16);
	
	private BufferedImage sprite;
	public int x;
	public int y;
	
	Tile(int x, int y, BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		
	}
}
