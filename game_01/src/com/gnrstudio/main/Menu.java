package com.gnrstudio.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.gnrstudio.world.World;

public class Menu {

	public String[] options = { "Novo Jogo", "Carregar Jogo", "Sair" };

	public int currentOption = 0;
	public int maxOption = options.length - 1;

	public boolean up, down, enter;

	public static boolean pause = false;
	
	Color tituloColor = new Color(255, 255, 255);
	Color tituloFundoColor = new Color(225, 255, 218, 150);
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
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
				file = new File("save.txt");
				file.delete();
			}else if(options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if(file.exists()) { //só por garantia
					String saver = loadGame(10); // 10 é o encode (criptografia que estamos usando)
					applySave(saver);
				}
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
			}
		
		}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			
			case "level":
				World.restartgame("level"+spl2[1]+".png");	
				Game.gameState = "NORMAL";
				pause = false;
				break;
				
			case "vida":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
			}
		}
	}
	
	public static String loadGame(int encode){
		String line = "";
		File file = new File("save.txt");
		if(file.exists()){
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null ){
						String[] trans = singleLine.split(":"); 
						char[] val = trans[1].toCharArray(); //decodificando
						trans[1] = "";
						for(int i = 0; i < val.length; i++){
							val[i] -= encode;
							trans[1] += val[i];
							}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
						}
				}catch(IOException e){
					
				}
			}catch(FileNotFoundException e){
				
			}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n] += encode; //criptografando o save para o usuário não modificar
				current += value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) {
					write.newLine();
				}
			}catch(IOException e) {
				
			}
		}
		
		try {
			write.flush();
			write.close();
		}catch(IOException e) {
			
		}
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//blur
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		
		//titulo
		g.setColor(tituloColor);
		g.setFont(Game.graspingFont);
		g.drawString("Sipri Game", (Game.WIDTH * Game.SCALE)/2 - 94, (Game.HEIGHT * Game.SCALE)/2 - 160);
		//fundo do titulo
		g.setColor(tituloFundoColor);
		g.setFont(Game.graspingFontFundo);
		g.drawString("Sipri Game", (Game.WIDTH * Game.SCALE)/2 - 93, (Game.HEIGHT * Game.SCALE)/2 - 161);
		
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
