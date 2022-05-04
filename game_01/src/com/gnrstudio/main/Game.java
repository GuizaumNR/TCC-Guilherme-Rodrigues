package com.gnrstudio.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.gnrstudio.entities.BulletShoot;
import com.gnrstudio.entities.Enemy;
import com.gnrstudio.entities.Enemy2;
import com.gnrstudio.entities.Entity;
import com.gnrstudio.entities.Player;
import com.gnrstudio.graficos.Spritesheet;
import com.gnrstudio.graficos.UI;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.World;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;

	public static int Cur_Level = 1, Max_Level = 4;

	private BufferedImage image;
	public static Spritesheet spritesheet;

	public static ArrayList<Entity> entities;

	public static ArrayList<Enemy> enemies;
	public static ArrayList<Enemy2> enemies2;

	public static ArrayList<BulletShoot> bullets;

	public static World world;

	public static Player player;

	public static Random rand;

	public UI ui;

	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;

	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("grasping.ttf");
	public InputStream streamFundo = ClassLoader.getSystemClassLoader().getResourceAsStream("grasping.ttf");
	public static Font graspingFont;
	public static Font graspingFontFundo;
	
	public Menu menu;

	public boolean saveGame = false;
	
	public int[] pixels;
	public BufferedImage lightmap;
	public int[] lightMapPixels;
	public static int[] minimapaPixels;
	
	public static BufferedImage minimapa;
	
	public int mx, my;
	
	int ahora = 0;
	public Game() {
		Sound.musicBackground.play();
		rand = new Random();
		addKeyListener(this); // adicionando o "ouvidor" de teclas aqui(this)
		addMouseListener(this); // adicionando o "ouvidor" de mouse aqui(this)
		addMouseMotionListener(this); //adiconando o "ouvidor" de movimentos do mouse aqui(this)
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		// Inicializando objetos;
		ui = new UI();
	
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		enemies2 = new ArrayList<Enemy2>();
		bullets = new ArrayList<BulletShoot>();

		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 11, 15, spritesheet.getSprite(32, 0, 11, 15));
		entities.add(player);
		world = new World("/level1.png");
		
		minimapa = new BufferedImage(world.WIDTH, world.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt) minimapa.getRaster().getDataBuffer()).getData();
		menu = new Menu();
		
		try {
			graspingFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(46f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			graspingFontFundo = Font.createFont(Font.TRUETYPE_FONT, streamFundo).deriveFont(45f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initFrame() {
		frame = new JFrame("O Ronda");
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setUndecorated(true);
		frame.setVisible(true);

	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}

	public void tick() {
		ui.tick();
		if (gameState == "NORMAL") { 
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level","vida"}; //nome no arquivo
				int[] opt2 = {this.Cur_Level,(int) player.life}; //variaveis no projeto, tipo insert do SQL
				Menu.saveGame(opt1,opt2, 10);
				System.out.println("Jogo salvo com sucesso.");
				}
			this.restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				if (e instanceof Player) {
					// estou dando tick no player, instanceof = "é um ...(classe)
				}
				e.tick();
			}

			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}

			if (enemies.size() == 0 && enemies2.size() == 0) {
				// avançar de nivel
				Cur_Level++;
				if (Cur_Level > Max_Level) {
					Cur_Level = 1;
				}
				String newWorld = "level" + Cur_Level + ".png";
				Game.world.nextLevel(newWorld);
			}
		} else if (gameState == "GAME_OVER") {
			this.framesGameOver++;
			if (framesGameOver == 35) {
				framesGameOver = 0;
				if (this.showMessageGameOver) {
					this.showMessageGameOver = false;
				} else {
					this.showMessageGameOver = true;
				}
			}

			if (restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				Cur_Level = 1;
				String newWorld = "level" + Cur_Level + ".png";
				Game.world.restartgame(newWorld);
			}
		} else if (gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
	}
	
	//rotação de objetos
//public void drawRectangleExample(int xoff, int yoff) {
//	for(int xx = 0; xx < 32; xx++) {
//		for(int yy = 0; yy < 32; yy ++) {
//			xoff = xx + xoff;
//			yoff = yy +  yoff;
//			if(xoff < 0 || yoff < 0 || xoff >= WIDTH || yoff >= HEIGHT) {
//				continue;
//			}
//			pixels[xoff + ((yoff*WIDTH))] = 0x00ff00;
//		}
//	}
//}

// visão de olho
	public void applyLight() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(lightMapPixels[xx+(yy * WIDTH)] == 0xffffffff) {// pegando na imagem lightmap
					pixels[xx +(yy*WIDTH)] = 0;
				}
			}
		}
	}
	
	
	
/* RENDERIZAÇÃO DO JOGO */
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		if(player.life <= 40){		
			applyLight();
		}
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		if(Player.hasMap) {
		World.renderMinimapa();
		g.drawImage(minimapa, 615, 375, world.WIDTH * 2, world.HEIGHT * 2, null);
		}
		
		Graphics2D g2 = (Graphics2D) g; // criando os "filtros" de escurecer
	
		if(gameState == "NORMAL"){
		if(ahora <= 5) {
		g2.setColor(new Color(8, 20, 80, (ahora + 24) * 3));
		g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
		System.out.println("hora " + ahora + " opacidade " +(ahora + 24) * 3);
		}else if(ahora < 12) {
			g2.setColor(new Color(8, 20, 80,((int)(100.1/ahora - 5)  * 3)));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			System.out.println("hora " + ahora + " opacidade " +((int)(100.1/ahora - 5) * 3));
		}else if(ahora > 12) {
			g2.setColor(new Color(8, 20, 80,(int)(1  * 3)));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			System.out.println("hora " + ahora + " opacidade " +
			(int)((Math.log(2)/Math.log(ahora - 12))/(Math.log(2)/Math.log(18))  * 3));
    	}else if(ahora == 12) {
		g2.setColor(new Color(8, 20, 80, 0));
		g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
		System.out.println("hora " + ahora + " opacidade " + 0);
		}
		}
		
		if (gameState == "GAME_OVER") {
			g2 = (Graphics2D) g; // criando opacidade
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 38));
			g.setColor(Color.RED);
			g.drawString("SE FODEU", (WIDTH * SCALE) / 2 - 80, (HEIGHT * SCALE) / 2 - 60);
			g.setFont(new Font("arial", Font.BOLD, 20));
			if (showMessageGameOver) {
				g.drawString(">Pressione enter para reinicar<", (WIDTH * SCALE) / 2 - 136, (HEIGHT * SCALE) / 2 - 10);
			}

		} else if (gameState == "MENU") {
			menu.render(g);
		}
		
		
//		Graphics2D g2 = (Graphics2D) g; //rotação de objetos
//		double angleMouse = Math.atan2(my - 200 + 45, mx - 200 + 25);
//		g2.rotate(angleMouse, 200+25, 200+45);
//		g.setColor(Color.RED);
//		g.fillRect(200, 200, 50, 70);
//		
		bs.show();
	}

	public void run() {
		// FPS
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
//				System.out.println("FPS: " + frames);
//				frames = 0;
				timer += 1000;
				ahora++;
				if(ahora >= 24) {
					ahora = 0;
				}
			}
		}
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// executar movimentos, eventos.
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if (gameState == "MENU") {
				menu.enter = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F) {
			if(gameState == "NORMAL") {
			this.saveGame = true;
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			if (gameState == "MENU") {
				menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;

			if (gameState == "MENU") {
				menu.down = true;
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3); // dividir por 3 pelo o SCALE da tela
		player.my = (e.getY() / 3);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}

}
