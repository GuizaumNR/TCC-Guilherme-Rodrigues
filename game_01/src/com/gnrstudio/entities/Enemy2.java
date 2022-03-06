package com.gnrstudio.entities;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gnrstudio.main.Game;
import com.gnrstudio.main.Sound;
import com.gnrstudio.world.AStar;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.Vector2i;


public class Enemy2 extends Entity {

	private double speed = 1.4;

	

	private int frames = 0, maxFrames = 3, index = 0, maxIndex = 3;// maxindex se conta a quantia de frames menos 1

	public int right_dir = 0, left_dir = 1, down_dir = 2, up_dir = 3;
	public int dir = right_dir;

	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] downEnemy;
	private BufferedImage[] upEnemy;
	private BufferedImage[] dRightEnemy;
	private BufferedImage[] dLeftEnemy;
	private int life = 1;

	private boolean isDamaged;
	private int damageFrames = 10, damageCurrent = 0;

	private boolean parado;
	
	int z = 0;

	public Enemy2(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		downEnemy = new BufferedImage[4];
		upEnemy = new BufferedImage[4];
		dRightEnemy = new BufferedImage[4];
		dLeftEnemy = new BufferedImage[4];

		if (!parado) {
			for (int i = 0; i < 4; i++) {
				rightEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 112, 9, 15);
			}
			for (int i = 0; i < 4; i++) {
				leftEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 128, 9, 15);
			}
			for (int i = 0; i < 4; i++) {
				upEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 144, 9, 15);
			}
			for (int i = 0; i < 4; i++) {
				downEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 160, 9, 15);
			}
			for (int i = 0; i < 4; i++) {
				dLeftEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 176, 9, 15);
			}
			for (int i = 0; i < 4; i++) {
				dRightEnemy[i] = Game.spritesheet.getSprite(32 + (i * 16), 192, 9, 15);
			}
		}

	}

	public void tick() {
		depth = 0;
		mwidth = 9;
		mheight = 15;
		//if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 100) {
		if (isColiddingWithPlayer() == false) {

			 if(path == null || path.size() == 0) {
				 Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
				 Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
				 path = AStar.findPath(Game.world, start, end);
			 }
			
			 
			 if ((int) x < Game.player.getX() ) {
					dir = right_dir;
				} else if ((int) x > Game.player.getX()) {
					dir = left_dir;
				}
				if ((int) y < Game.player.getY()) {
					dir = up_dir;
				} else if ((int) y > Game.player.getY()) {
					dir = down_dir;
				} else {
					// estamos colidindo
					parado = true;
					
				}
			 
		} else {
			if (Game.rand.nextInt(100) > 10) {
				Sound.hurtEfecct.play();
				double dano = Game.rand.nextInt(5);
				//Game.player.life -= dano;
				Game.player.isDamaged = true;
				if (Game.player.life <= 0) {
					Game.player.life = 0;
				}
			}
		}
		if(new Random().nextInt(100) > 30) {
			followPath(path);
		}
		if(new Random().nextInt(100) < 5) {
			Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			 Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
			 path = AStar.findPath(Game.world, start, end);
		}
		parado = false;
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
		collidingBullet();
		if (life <= 0) {
			destroySelf();
			return;
		}

		if (isDamaged) {
			this.damageCurrent++;
			if (this.damageFrames == this.damageCurrent) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}

	}

	public void destroySelf() {
		Game.enemies2.remove(this);
		Game.entities.remove(this);

	}

	public void collidingBullet() {

		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (e instanceof BulletShoot) {
				if (Entity.isColidding(this, e)) {
					life--;
					isDamaged = true;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}

	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX() + maskx, Game.player.getY() + masky + Game.player.z, Game.player.width, Game.player.height);
		return enemyCurrent.intersects(player);
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == down_dir) {
				g.drawImage(downEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == up_dir) {
				g.drawImage(upEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
//			g.setColor(Color.BLUE);
//			g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
		} else {
			if (dir == right_dir) {
				g.drawImage(dRightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(dLeftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == down_dir) {
				g.drawImage(dRightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == up_dir) {
				g.drawImage(dLeftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}

}
