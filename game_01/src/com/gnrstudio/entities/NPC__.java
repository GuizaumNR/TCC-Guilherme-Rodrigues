package com.gnrstudio.entities;

import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;

public class NPC__ extends Entity{

	public NPC__(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();

		int x_ = (int) x;
		int y_ = (int) y;

		if (Math.abs(xPlayer - x_) < 35 && Math.abs(y_ - yPlayer) < 35) {
			for (int i = 0; i < Game.entities.size(); i++) {
				Entity atual = Game.entities.get(i);
				if (atual instanceof NPC__) {
					if (Entity.isColidding(this, atual)) {
							Game.player.life = 1;
						Game.entities.remove(atual);
						}
					}
				}
			}
		}
	}

