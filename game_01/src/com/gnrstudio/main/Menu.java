package com.gnrstudio.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = { "Novo Jogo", "Carregar Jogo", "Sair" };

	public int currentOption = 0;
	public int maxOption = options.length - 1;

	public boolean up, down, enter;

	public boolean pause = false;
	
	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			Sound.selectEfecct.play();
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}

		if (down) {
			down = false;
			currentOption++;
			Sound.selectEfecct.play();
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter){
			enter = false;
			if(options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
			}
		}

	public void render(Graphics g) {
		//titulo
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 38));
		g.drawString("Sipri Game", (Game.WIDTH * Game.SCALE)/2 - 94, (Game.HEIGHT * Game.SCALE)/2 - 160);
		
		//opcoes
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if(pause == false) {
		g.drawString(options[0], (Game.WIDTH * Game.SCALE)/2 - 60, (Game.HEIGHT * Game.SCALE)/2 - 90);
		}else {
			g.drawString("Continuar", (Game.WIDTH * Game.SCALE)/2 - 60, (Game.HEIGHT * Game.SCALE)/2 - 90);
		}
		g.drawString(options[1], (Game.WIDTH * Game.SCALE)/2 - 80, (Game.HEIGHT * Game.SCALE)/2 - 55);
		g.drawString(options[2], (Game.WIDTH * Game.SCALE)/2 - 25, (Game.HEIGHT * Game.SCALE)/2 - 20);
		
		if(options[currentOption] == "Novo Jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE)/2 - 80, (Game.HEIGHT * Game.SCALE)/2 - 90);
			g.drawString("<", (Game.WIDTH * Game.SCALE)/2 + 70, (Game.HEIGHT * Game.SCALE)/2 - 90);
		}
		if(options[currentOption] == "Carregar Jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE)/2 - 100, (Game.HEIGHT * Game.SCALE)/2 - 55);
			g.drawString("<", (Game.WIDTH * Game.SCALE)/2 + 90, (Game.HEIGHT * Game.SCALE)/2 - 55);
		}
		if(options[currentOption] == "Sair") {
			g.drawString(">", (Game.WIDTH * Game.SCALE)/2 - 45, (Game.HEIGHT * Game.SCALE)/2 - 20);
			g.drawString("<", (Game.WIDTH * Game.SCALE)/2 + 30, (Game.HEIGHT * Game.SCALE)/2 - 20);
		}
	}

}
