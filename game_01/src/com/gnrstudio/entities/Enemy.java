package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.World;

public class Enemy extends Entity {
	private double speed = 1;

	private int maskX = 8, maskY = 8, maskW = 15, maskH = 16;

	private int frames = 0, maxFrames = 3, index = 0, maxIndex = 2;// maxindex se conta a quantia de frames menos 1

	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;

	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		rightEnemy = new BufferedImage[3];
		leftEnemy = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(112 + (i * 16), 16, 16, 16);
		}
		for (int i = 0; i < 3; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(112 + (i * 16), 32, 16, 16);
		}

	}

	public void tick() {// OBS: debug funciona apenas em loops
		// if(Game.rand.nextInt(100) < 50) maneira 1 de randomizar inimigos(simples)
		/*
		 * maskX = 10; maskY = 8; maskW = 10; maskH =10;
		 */
		if (isColiddingWithPlayer() == false) {
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
					&& !isColidding((int) (x + speed), this.getY())) {
				x += speed;
				dir = right_dir;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
					&& !isColidding((int) (x - speed), this.getY())) {
				x -= speed;
				dir = left_dir;

			}
			if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
					&& !isColidding(this.getX(), (int) (y + speed))) {
				y += speed;

			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
					&& !isColidding(this.getX(), (int) (y - speed))) {
				y -= speed;
			} else {
				// estamos colidindo

			}
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		} else {
			// estamos perto do player, o que fazer?
			if (Game.rand.nextInt(100) > 10) {
				Game.player.life -= Game.rand.nextInt(3);
				Game.player.isDamaged = true;
				// Game.player.x = Game.player.x - 4;

			}

		}

	}

	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX() + maskX, Game.player.getY() + maskY, 16, 16);
		return enemyCurrent.intersects(player);
	}

	public boolean isColidding(int xnext, int ynext) { // metodo para checar colisao de retangulos
		Rectangle enemyCurrent = new Rectangle(xnext + maskX, ynext + maskY, maskW, maskH);

		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) // enemy = este enemy continua
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;

			}
		}
		return false;
	}

	public void render(Graphics g) {

		if (dir == right_dir) {
			g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
//			g.setColor(Color.BLUE); // Para ver a mask
//			g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, maskW, maskH);

		} else if (dir == left_dir) {
			g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
//			g.setColor(Color.BLUE); // Para ver a mask
//			g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, maskW, maskH);

		}
		// g.setColor(Color.BLUE); Para ver a mask
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskW, maskH);
	}

}
