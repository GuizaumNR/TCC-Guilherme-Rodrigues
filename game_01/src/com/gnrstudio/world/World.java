package com.gnrstudio.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

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
			// boolean moved = false;
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
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == 0xFF000000) {
						// CHÃO-FLOOR
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0XFF00FFFF) {
						// Chao2
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR2);

					} else if (pixelAtual == 0XFF4800FF) {
						// PAREDE
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);

					} else if (pixelAtual == 0XFFFFFFFF) {
						// PAREDE2
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL2);

					} else if (pixelAtual == 0XFF0000FF) {
						// PLAYER
						Game.player.setX(xx * 11);
						Game.player.setY(yy * 15);
					} else if (pixelAtual == 0XFFFF0000) {
						// ENEMY
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (pixelAtual == 0XFFFF6A00) {
						// WEAPON
						Weapon weapon = new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN);
						Game.entities.add(weapon);
						
					} else if (pixelAtual == 0XFF00FF00) {
						// LIFEPACK
						LifePack pack = new LifePack(xx * 16, yy * 16, 11, 11, Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					}
				else if(pixelAtual == 0XFF00870B){
					//Tumulo
					tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_TOMB);
				
				}
					else if (pixelAtual == 0XFFFFFF00) {
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

	public static boolean isFree(int xnext, int ynext) {// para checar se a proxima posisao esta livre
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) || // verificando se o tile é parede(WallTile)
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) || // retorna true se for por isso "!"
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) || // para "isFree" ser false
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}

	public static void restartgame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 11, 15, Game.spritesheet.getSprite(32, 0, 11, 15));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
//	public void hora(){
//		GregorianCalendar calendar = new GregorianCalendar();
//		int hora = calendar.get(Calendar.HOUR_OF_DAY);
//	}
	
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
}
