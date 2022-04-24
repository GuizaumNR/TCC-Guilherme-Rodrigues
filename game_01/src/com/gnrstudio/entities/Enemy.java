package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.AStar;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.Vector2i;
import com.gnrstudio.world.World;

public class Enemy extends Entity {
	private double speed = 1.15;

	private int maskX = 8, maskY = 8, maskW = 10, maskH = 16;

	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;// maxindex se conta a quantia de frames menos 1

	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;

	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] dRightEnemy;
	private BufferedImage[] dLeftEnemy;
	private int life = 1;
	
	private boolean isDamaged;
	private int damageFrames = 10, damageCurrent = 0;
	
	private boolean parado;
	
	int z = 1;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		depth = 0;
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		dRightEnemy = new BufferedImage[4];
		dLeftEnemy = new BufferedImage[4];
		
		if(!parado) {
		for (int i = 0; i < 4; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(96 + (i * 16), 112, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(96 + (i * 16), 128, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			dLeftEnemy[i] = Game.spritesheet.getSprite(96 + (i * 16), 144, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			dRightEnemy[i] = Game.spritesheet.getSprite(96 + (i * 16), 160, 16, 16);
		}

	}}

	public void tick() {// OBS: debug funciona apenas em loops
		// if(Game.rand.nextInt(100) < 50) maneira 1 de randomizar inimigos(simples)		
		 if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 150) { //distancia do player
		if (isColiddingWithPlayer() == false) {
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY(), z)
					&& !isColidding((int) (x + speed), this.getY())) {
				x += speed;
				dir = right_dir;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY(), z)
					&& !isColidding((int) (x - speed), this.getY())) {
				x -= speed;
				dir = left_dir;

			}
			if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed), z)
					&& !isColidding(this.getX(), (int) (y + speed))) {
				y += speed;

			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed), z)
					&& !isColidding(this.getX(), (int) (y - speed))) {
				y -= speed;
			} else {
				// estamos colidindo
				parado = true;
				
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
			
		} else {
			// estamos perto do player, o que fazer?
			if (Game.rand.nextInt(100) > 10) {
				double danoRandom = Game.rand.nextDouble();
				double inicio = 0.1;
				double fim = 0.5;
				Game.player.life -= inicio + (danoRandom * (fim - inicio)); //Limitando os valores 
				Game.player.isDamaged = true;		
				if(Game.player.life <= 0) {
					Game.player.life = 0;
				}
//				if(this.getX() > Game.player.getX()) {
//				 Game.player.x = Game.player.x -= 20; 
//					// tentando fazer o player ser empurrado pelo inimigo
//				}else if(this.getX() < Game.player.getX()) {
//					Game.player.x = Game.player.x += 20; 
//				}
			}
			
		}
				 
	 }
		collidingBullet();	
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageFrames == this.damageCurrent) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}

	}

	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);

//		for (int i = 0; i < Game.enemies.size(); i++) { //tentando criar o drop de item
//			Enemy e = Game.enemies.get(i);
//			if (e.life <= 0) {
//				LifePack pack = new LifePack(e.getX() * 16, e.getY() * 16, 11, 11, Entity.LIFEPACK_EN);
//				Game.entities.add(pack);
//		}}
	}
	
	public void collidingBullet() {
	
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColidding(this, e)) {
					life--;
					isDamaged = true;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX() + maskX, Game.player.getY() + maskY + Game.player.z,  Game.player.width, Game.player.height);
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
		if(!isDamaged) {
		if (dir == right_dir) {
			g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		// g.setColor(Color.BLUE); Para ver a mask
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		// maskW, maskH);
	}else{
		if (dir == right_dir) {
			g.drawImage(dRightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(dLeftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}		
	}
		}

}
