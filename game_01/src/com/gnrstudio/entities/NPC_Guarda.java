package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.gnrstudio.main.Game;

public class NPC_Guarda extends Entity {

	public String[] frases = new String[7];
	public boolean showMessage = false;
	public boolean show = false;
	public int currentMessage = 0;
	public int curIndex = 0;

	public int time = 0;
	public int maxTime = 5;

	public NPC_Guarda(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		frases[0] = "-O que fazes aqui?!";
		frases[1] = "-As rondas foram canceladas.";
		frases[2] = "-Não devias estar aqui.";
		frases[3] = "-Este é um lugar perigoso agora.";
		frases[4] = "-Agora é tarde.";
		frases[5] = "-Pegue esta arma.";
		frases[6] = "-Você vai precisar...";
	}

	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();

		int xGuarda = (int) x;
		int yGuarda = (int) y;

		if (Math.abs(xPlayer - xGuarda) < 35 && Math.abs(yGuarda - yPlayer) < 35) {

			if (show == false) {
				showMessage = true;
				show = true;
			}
		} else {
			showMessage = false;
			show = false;
		}
		if (showMessage) {
			this.time++;

			if (this.time >= this.maxTime) {
				this.time = 0;
				
				if(showMessage) {
				if (curIndex < frases[currentMessage].length()) {
					curIndex++;
				}else {
//					if (currentMessage < frases.length - 1) {
//						currentMessage++;
//						curIndex = 0;
//					}else if (currentMessage >= frases.length - 1) {
//						currentMessage = 0;
//						showMessage = false;
//
//					}
					
				}
			}
			}

					 
//				}

		}

	}

	public void render(Graphics g) {
		super.render(g);
		if (showMessage) {

			g.setColor(Color.BLACK);
			g.fillRect(Game.WIDTH / Game.SCALE - 70, Game.HEIGHT / Game.SCALE + 42, 220, 60);

			g.setColor(Color.WHITE);
			g.fillRect(Game.WIDTH / Game.SCALE - 65, Game.HEIGHT / Game.SCALE + 47, 210, 50);

			g.setFont(new Font("Arial", Font.BOLD, 11));
			g.setColor(Color.BLACK);
			g.drawString(frases[currentMessage].substring(0, curIndex), Game.WIDTH / Game.SCALE - 60,
					Game.HEIGHT / Game.SCALE + 60);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.drawString("Enter pra continuar", Game.WIDTH / Game.SCALE + 50, Game.HEIGHT / Game.SCALE + 95);
		}
	}

}
