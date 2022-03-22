package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.gnrstudio.main.Game;
import com.gnrstudio.main.Sound;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.World;

public class Player extends Entity {

	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public double speed = 1.4;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	public boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;

	private BufferedImage[] RDamagePlayer;
	private BufferedImage[] LDamagePlayer;
	private int damageFrame = 0;

	public boolean shoot = false, mouseShoot = false;

	private boolean hasGun = false;

	public double ammo = 0, maxAmmo = 5;
	public boolean isDamaged = false;
	public double life = 100, maxlife = 100;
	public int mx, my;

	public boolean jump = false;

	public boolean isJumping = false;

	public int z = 0;

	public int jumpFrames = 40, jumpCur = 0;

	public boolean jumpUp = false, jumpDown = false;

	public double jumpSpd = 1;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		RDamagePlayer = new BufferedImage[4];
		LDamagePlayer = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			RDamagePlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 64, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			LDamagePlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, 16, 16);
		}
	}

	public void tick() {
		depth = 1;
		//System.out.println(""+ (getX()/Game.SCALE - Camera.x/Game.SCALE) + " " + mx);
//		if(mx/Game.SCALE > (getX()/Game.SCALE)) {
//			dir = right_dir;
//		}else if (mx/Game.SCALE < (getX()/Game.SCALE - Camera.x/Game.SCALE)) {
//			dir = left_dir;
//		}
		
		if (jump) {
			if (isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		if (isJumping == true) {

			
				if(jumpUp) {
				jumpCur +=2;
				}else if(jumpDown) {
					jumpCur -=2;
					if(jumpCur <= 0) {
						isJumping = false;
						jumpDown = false;
						jumpUp = false;
					}
				}
				z = jumpCur;
			if (jumpCur >= jumpFrames) {
				jumpUp = false;
				jumpDown = true;
					}
				
			}

		moved = false;
		if (right && World.isFree((int) (x + speed), this.getY(), z)) {
			moved = true;
			dir = right_dir;
			x += speed;

		} else if (left && World.isFree((int) (x - speed), this.getY(), z)) {
			moved = true;
			dir = left_dir;
			x -= speed;

		}
		if (up && World.isFree(this.getX(), (int) (y - speed), z)) {
			moved = true;
			dir = up_dir;
			y -= speed;

		} else if (down && World.isFree(this.getX(), (int) (y + speed), z)) {
			moved = true;
			dir = down_dir;
			y += speed;

		}
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}

		checkColissionLifePack();
		checkCollissionAmmo();
		checkCollissionGun();

		if (isDamaged) {
			this.damageFrame++;
			if (this.damageFrame == 8) {
				this.damageFrame = 0;
				isDamaged = false;

			}
		}

		if (shoot) {
			// Criar bala e atirar TECLADO
			shoot = false;
			if (hasGun && ammo > 0) {
				Sound.shootEfecct.play();
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 5;

				if (dir == right_dir) {
					dx = 1;
					px = 18;
				} else {
					px = -8;
					dx = -1;
				}

				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 1, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}

		if (mouseShoot) {
			// Criar bala e atirar MOUSE
			// converter
			mouseShoot = false;
			if (hasGun && ammo > 0) {
				//Sound.shootEfecct.play();
				ammo--;

				int px = 0;
				int py = 7;
				double angle = 0;
				if (dir == right_dir) {
					px = 16;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)); // pegando os valores para converter

				} else if (dir == left_dir) {
					px = -6;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));

				} else if (dir == up_dir) {
					py = 5;
					px = 13;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)); 

				} else if (dir == down_dir) {
					py = 10;
					px = -5;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)); 

				}
				double dx = Math.cos(angle); // pegando o angulo x
				double dy = Math.sin(angle); // pegando o angulo y

				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 1, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}

		if (life <= 0) {
			// Game over
			this.life = 0;
			Sound.deadEfecct.play();
			Game.gameState = "GAME_OVER";
		}

		if (life >= maxlife) {
			life = maxlife;

		}
		if (ammo >= maxAmmo) {
			ammo = maxAmmo;

		}
		updateCamera();

	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkCollissionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet) {
				if (Entity.isColidding(this, atual)) {
					if (ammo < maxAmmo) {
						ammo += 5;
						if (ammo > maxAmmo) {
							ammo = maxAmmo;
						}
					Game.entities.remove(atual);
				}
			}
		}
	}
}

	public void checkColissionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack) {
				if (Entity.isColidding(this, atual)) {
					if (life < maxlife) {
						life += 10;
						if (life > maxlife) {
							life = maxlife;
						}
						Game.entities.remove(atual);
					}
				}
			}
		}
	}

	public void checkCollissionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isColidding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para direita
					g.drawImage(Entity.RIGHTGUN_EN, this.getX() - Camera.x + 13, this.getY() - Camera.y + 6 - z, null);

				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para esquerda
					g.drawImage(Entity.LEFTGUN_EN, this.getX() - Camera.x - 6, this.getY() - Camera.y + 6 - z, null);

				}
			} else if (dir == up_dir) {
				if (hasGun) {
					// Desenhando a arma para cima
					g.drawImage(Entity.UPGUN_EN, this.getX() - Camera.x + 13, this.getY() - Camera.y + 3 - z, null);
				}
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);

			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para baixo
					g.drawImage(Entity.DOWNGUN_EN, this.getX() - Camera.x - 4, this.getY() - Camera.y + 8 - z, null);
				}
			}
//			g.setColor(Color.red);
//			g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
		} else {

			if (dir == right_dir) {
				g.drawImage(RDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para direita(com dano)
					g.drawImage(Entity.DRIGHTGUN_EN, this.getX() - Camera.x + 13, this.getY() - Camera.y + 6 - z, null);

				}
			}
			if (dir == left_dir) {
				g.drawImage(LDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para esquerda(com dano)
					g.drawImage(Entity.DLEFTGUN_EN, this.getX() - Camera.x - 6, this.getY() - Camera.y + 6 - z, null);

				}

			}
			if (dir == up_dir) {
				g.drawImage(RDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para esquerda(com dano)
					g.drawImage(Entity.DUPGUN_EN, this.getX() - Camera.x + 13, this.getY() - Camera.y + 3 - z, null);

				}
			}
			if (dir == down_dir) {
				g.drawImage(LDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					// Desenhando a arma para esquerda(com dano)
					g.drawImage(Entity.DDOWNGUN_EN, this.getX() - Camera.x - 4, this.getY() - Camera.y + 8 - z, null);

				}
			}
		}

		if (isJumping) {
			g.setColor(Color.BLACK);
			g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y + 8, 9, 9);
			if(hasGun) {
				if(dir == right_dir) {
					g.fillRect((this.getX() - Camera.x) + 5, this.getY() - Camera.y + 12, 8, 3);
				}else if(dir == left_dir) {
					g.fillRect((this.getX() - Camera.x) - 4, this.getY() - Camera.y + 12, 8, 3);
				}else if(dir == up_dir) {
					g.fillRect((this.getX() - Camera.x)  + 7, (this.getY() - Camera.y + 12) - 6, 3, 8);
				}else if(dir == down_dir){			
					g.fillRect(this.getX() - Camera.x, (this.getY() - Camera.y + 12) + 2, 3, 8);
					}
				}
			}
	}
}
