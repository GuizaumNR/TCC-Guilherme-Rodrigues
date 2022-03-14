package com.gnrstudio.entities;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.Node;
import com.gnrstudio.world.Vector2i;

public class Entity {

	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(98, 5, 11, 11);
	
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(0 , 96, 10, 16);
	
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96, 18, 13, 13);
	
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 32, 14, 16);
	public static BufferedImage ENEMY2_EN = Game.spritesheet.getSprite(32, 112, 9, 15);
	
	public static BufferedImage RIGHTGUN_EN = Game.spritesheet.getSprite(112, 0, 8, 6);
	public static BufferedImage LEFTGUN_EN = Game.spritesheet.getSprite(135, 0, 8, 6);
	public static BufferedImage DOWNGUN_EN = Game.spritesheet.getSprite(144, 0, 6, 8);
	public static BufferedImage UPGUN_EN = Game.spritesheet.getSprite(160, 0, 6, 8);
	
	public static BufferedImage DRIGHTGUN_EN = Game.spritesheet.getSprite(160, 32, 8, 6);
	public static BufferedImage DLEFTGUN_EN = Game.spritesheet.getSprite(183, 32, 8, 6);
	public static BufferedImage DDOWNGUN_EN = Game.spritesheet.getSprite(160, 16, 6, 8);
	public static BufferedImage DUPGUN_EN = Game.spritesheet.getSprite(176, 16, 6, 8);
	
	protected double x;
	protected double y;
	protected int z;
	protected int width;
	protected int height;
  
	public int depth;
	protected List<Node> path;
	
	private BufferedImage sprite;

	public int maskx, masky,mwidth, mheight;

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
public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity n0,Entity n1) {
			if(n1.depth < n0.depth)
				return +1;
			if(n1.depth > n0.depth)
				return -1;
			return 0;
		}
		
	};
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
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 -  y2) * (y1 - y2)); //calculando a distancia com seno e cosseno, por base no angulo, pela menor distancia
	}
	
	public boolean isColidding(int xnext,int ynext){
		Rectangle enemyCurrent = new Rectangle(xnext + maskx,ynext + masky,mwidth,mheight);
		for(int i = 0; i < Game.enemies2.size(); i++){
			Enemy2 e = Game.enemies2.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX()+ maskx,e.getY()+ masky,mwidth,mheight);
			if(enemyCurrent.intersects(targetEnemy)){
				return true;
			}
		}
		
		return false;
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x * 16 && !isColidding(this.getX() + 1, this.getY())) {
					x++;
				}else if(x > target.x * 16 && !isColidding(this.getX() - 1, this.getY())) {
					x--;
				}
				
				if(y < target.y * 16 && !isColidding(this.getX(), this.getY() + 1)) {
					y++;
				}else if(y > target.y * 16 && !isColidding(this.getX(), this.getY() - 1)) {
					y--;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
				}
				
			}
		}
	}
	
	public static boolean isColidding(Entity e1,Entity e2){
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx,e1.getY()+e1.masky,e1.mwidth,e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx,e2.getY()+e2.masky,e2.mwidth,e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	    //g.setColor(Color.red);
	    //g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
	}

}
