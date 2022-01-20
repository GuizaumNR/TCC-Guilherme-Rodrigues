package com.gnrstudio.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.SwingUtilities;

import com.gnrstudio.main.Game;


public class UI {
	
	public static BufferedImage s2;
	
	
	public void render(Graphics g) {
		
		//Vida
		s2 = Game.spritesheet.getSprite(96, 48, 13, 10);
		g.drawImage(s2, 4, 8, 13, 10, null);
		g.setColor(Color.black);
		g.fillRect(18, 6, 54, 12);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(20, 8, 50, 8);
		g.setColor(Color.red);
		g.fillRect(20, 8,(int) ((Game.player.life/Game.player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, 8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife, 32, 15);
		
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime timePoint = LocalDateTime.now();
        g.setColor(null);
        g.drawString(timePoint.format(fmt), 10, 40);
          
    }

    
	
	
}
