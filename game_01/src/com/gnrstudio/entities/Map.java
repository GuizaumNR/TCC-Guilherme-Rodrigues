package com.gnrstudio.entities;

import java.awt.image.BufferedImage;

public class Map extends Entity {

	public static int x;
	public static int y;
	
	public Map(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 0;
		
		this.x = x;
		this.y = y;
		
	}

}