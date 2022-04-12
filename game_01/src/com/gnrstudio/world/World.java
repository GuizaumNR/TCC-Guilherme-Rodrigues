package com.gnrstudio.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


import javax.imageio.ImageIO;

import com.gnrstudio.entities.*;
import com.gnrstudio.graficos.Spritesheet;
import com.gnrstudio.main.Game;

public class World {

	public static Tile[] tiles;

	public static int WIDTH, HEIGHT;

	public final static int TILE_SIZE = 16;

	public static BufferedImage[] AWeapon;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			int index = 0;
			int maxFrames = 5;
			int frames = 0;
			int maxIndex = 3;
			for (int i = 0; i < 3; i++) {
				frames++;
				if (frames == maxFrames) {
					frames = 0;
					index++;
					if (index > maxIndex) {
						index = 0;
					}
				}
			}

			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					if(Game.Cur_Level == 1) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR3);
					}else {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}
					if (pixelAtual == 0xFF000000) {
						// CHÃO
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0XFF00FFFF) {
						// Chao2
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR2);

					} else if (pixelAtual == 0XFF4C8400) {
						// Chao3
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR3);

					}else if (pixelAtual == 0XFF808080) {
						// ESTRADA
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_RUA);

					} else if (pixelAtual == 0XFF4800FF) {
						// PAREDE
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);

					} else if (pixelAtual == 0XFFFFFFFF) {
						// PAREDE2
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL2);

					} else if (pixelAtual == 0XFF0000FF) {
						// PLAYER
						Game.player.setX(xx * 15);
						Game.player.setY(yy * 15);
					} else if (pixelAtual == 0XFFFF0000) {
						// ENEMY
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}else if (pixelAtual == 0XFFA80000) {
						// ENEMY2
						Enemy2 en2 = new Enemy2(xx * 16, yy * 16, 9, 15, Entity.ENEMY2_EN);
						Game.entities.add(en2);
						Game.enemies2.add(en2);
					} else if (pixelAtual == 0XFFFF6A00) {
						// WEAPON
						Weapon weapon = new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN);
						Game.entities.add(weapon);
						
					} else if (pixelAtual == 0XFF00FF00) {
						// LIFEPACK
						LifePack pack = new LifePack(xx * 16, yy * 16, 11, 11, Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					} else if (pixelAtual == 0XFF9E7B5D) {
						// Map
						Map ma = new Map(xx * 16, yy * 16, 11, 11, Entity.MAP_EN);
						Game.entities.add(ma);
					} else if(pixelAtual == 0XFF00870B){
					//Tumulo
					tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_TOMB);
				
				} else if(pixelAtual == 0XFF004904){
					//Tumulo2
					tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_TOMB2);
				
				} else if (pixelAtual == 0XFFFFFF00) {
						// BULLET
						Bullet bullet = new Bullet(xx * 16, yy * 16, 14, 13, Entity.BULLET_EN);
						Game.entities.add(bullet);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static boolean isFree(int xnext, int ynext, int z) {//para checar se a proxima posisao esta livre
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		if( !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) || // verificando se o tile é parede(WallTile)
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) || // retorna true se for por isso "!"
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) || // para "isFree" ser false
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))) {
		return true;
	}
	if(z > 0) { //z representa altura, se for > 0 ele está pulando, então não colide
		return true;
	}
	return false;
	}
	public static void restartgame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.enemies2.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.enemies2 = new ArrayList<Enemy2>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		// fazendo renderizar apenas o que é visto
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) // Pelo erro do indice negativo
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void renderMinimapa(){
		for(int i = 0; i < Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;
		}
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy * WIDTH)] instanceof WallTile) {
					Game.minimapaPixels[xx + (yy * WIDTH)] = 0xFFFFFF;
				}
				
			}
		}
		
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;
		
		Game.minimapaPixels[xPlayer + (yPlayer * WIDTH)] = 0x3CFFF3;
		
	}
}
