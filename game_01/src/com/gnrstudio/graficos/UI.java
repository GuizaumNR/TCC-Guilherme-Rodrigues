package com.gnrstudio.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gnrstudio.entities.Player;
import com.gnrstudio.main.Game;

public class UI {
	
	public static BufferedImage s2;
	
	
	public void render(Graphics g) {
		
		try {
			s2 = ImageIO.read(getClass().getResourceAsStream("/s2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.drawImage(s2, 4, 8, 13, 10, null);
		g.setColor(Color.black);
		g.fillRect(18, 6, 54, 12);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(20, 8, 50, 8);
		g.setColor(Color.red);
		g.fillRect(20, 8,(int) ((Player.life/Player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, 8));
		g.drawString((int)Player.life+"/"+(int)Player.maxlife, 32, 15);
	}
	
}
