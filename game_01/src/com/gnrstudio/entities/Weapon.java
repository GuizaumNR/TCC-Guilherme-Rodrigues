package com.gnrstudio.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.World;

public class Weapon extends Entity {

	private BufferedImage[] WeaponSprite;
	private int frames = 0, maxFrames = 3, index = 0, maxIndex = 2;// maxindex se conta a quantia de frames menos 1
	
	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		WeaponSprite = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			WeaponSprite[i] = Game.spritesheet.getSprite(112 + (i * 16), 48, 10, 16);
//			if(i > 3) {
//				WeaponSprite[i] = Game.spritesheet.getSprite(112 + (i * 16), 71, 10, 9);
//			}
		}
		
	}
	
	public void tick() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
	}
	
	public void render(Graphics g) {
		
		g.drawImage(WeaponSprite[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

	
}