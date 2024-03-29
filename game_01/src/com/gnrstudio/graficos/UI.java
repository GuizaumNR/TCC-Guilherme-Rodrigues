package com.gnrstudio.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.gnrstudio.entities.Enemy;
import com.gnrstudio.entities.Enemy2;
import com.gnrstudio.entities.Player;
import com.gnrstudio.main.Game;

public class UI {

	public static BufferedImage s2;
	public static BufferedImage bullets;
	public static BufferedImage gun;

	public static int hora;
	public static DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("HH");;
	public static LocalDateTime timePoint2 = LocalDateTime.now();

	public static BufferedImage minimapa;

	public void tick() {

		String horaAtual = LocalDateTime.now().format(fmt2);
		hora = Integer.parseInt(horaAtual);

	}

	public void render(Graphics g) {

		// Vida
		s2 = Game.spritesheet.getSprite(96, 48, 13, 10);
		bullets = Game.spritesheet.getSprite(96, 18, 13, 8);
		gun = Game.spritesheet.getSprite(112, 0, 13, 8);
		g.drawImage(s2, 4, 8, 13, 10, null);
		g.setColor(Color.black);
		g.fillRect(18, 6, 53, 12);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(20, 8, 49, 8);
		g.setColor(Color.red);
		g.fillRect(20, 8, (int) ((Game.player.life / Game.player.maxlife) * 49), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, 8));
		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxlife, 32, 15);

		// inventário
		g.setColor(Color.BLACK);
		g.fillRect((Game.WIDTH / 2) - 22, 6, 39, 14);
		g.setColor(new Color(246, 202, 156));
		g.fillRect((Game.WIDTH / 2) - 20, 8, 35, 10);
		g.setColor(Color.BLACK);
		g.fillRect((Game.WIDTH / 2) - 9, 6, 2, 14);
		g.setColor(Color.BLACK);
		g.fillRect((Game.WIDTH / 2) + 3, 6, 2, 14);

		// municao

		if (Player.hasGun) {
			g.drawImage(bullets, 4, 22, 13, 9, null);
			g.setColor(Color.black);
			g.fillRect(18, 20, 53, 12);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(21, 22, 47, 8);
			g.setColor(new Color(236, 197, 0));
			g.fillRect(21, 22, (int) ((Game.player.ammo / Game.player.maxAmmo) * 47), 8);
			g.setColor(Color.black);
			g.fillRect(28, 20, 3, 12);
			g.setColor(Color.black);
			g.fillRect(38, 20, 3, 12);
			g.setColor(Color.black);
			g.fillRect(48, 20, 3, 12);
			g.setColor(Color.black);
			g.fillRect(58, 20, 3, 12);

			g.drawImage(gun, (Game.WIDTH / 2) - 19, 10, 13, 9, null);
		}

		// hora na tela
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH: mm");
		LocalDateTime timePoint = LocalDateTime.now();

		g.setColor(Color.BLACK);
		g.fillRect(195, 5, 30, 14);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(196, 6, 28, 12);
		g.setColor(Color.BLACK);
		g.fillRect(198, 8, 24, 8);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.PLAIN, 8));
		g.drawString(timePoint.format(fmt), 200, 15);

	}

}
