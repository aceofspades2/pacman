package pacman;

import java.awt.BasicStroke;
//import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
//import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
//import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")

/**
 * This is the main runner class for Pacman
 * 
 * it deals with all coordination between entities, graphics, game-level tasks, etc
 * 
 * There are a lot of things that are reused and modified from CardSlap
 * CardSlap is the game that Gavi Zahavi, Jeff Page, Yuxuan/Harry Wu, and I, Philippe Lessard,
 * created at the Blueprint Hackathon in Spring 2016
 * most of the graphics and complicated stuff including listeners and thread was written originally by Gavi
 * and almost everything that was reused from it was modified a lot to fit this program
 * Some of the things that were reused, I just reused the structure, but I had to rewrite almost all of the 
 * actual functional code
 */

public class GameRunner extends JFrame {
	Graphics2D graphics;
	Graphics2D graphics2;
	BufferedImage bg;
	static GameRunner game;
	static final int WIDTH = 448;
	static final int HEIGHT = 496;
	
	Map map = new Map();
	
	// key codes for the keys that control pacman. change these to change the controls
	final int LEFT = KeyEvent.VK_LEFT;
	final int UP = KeyEvent.VK_UP;
	final int RIGHT = KeyEvent.VK_RIGHT;
	final int DOWN = KeyEvent.VK_DOWN;
	
	// reused/modified from CardSlap
	static final BufferedImage[] IMAGES = {
			getResource("pacmanClosed.png"),	//0
			getResource("redGhost.png"),		//1
			getResource("blueGhost.png"),		//2
			getResource("pinkGhost.png"),		//3
			getResource("yellowGhost.png"),		//4
			getResource("wall.png"),			//5
			getResource("emptyTile.png"),		//6
			getResource("door.png"),			//7
			getResource("dot.png"),				//8
			getResource("energizer.png"),		//9
			getResource("pacmanUp.png"),		//10
			getResource("pacmanLeft.png"),		//11
			getResource("pacmanDown.png"),		//12
			getResource("pacmanRight.png")		//13
	};
	
	// constructor
	// all of the graphics setup is reused from CardSlap, and maybe modified a bit
	public GameRunner() {
		super("Pacman");
		
		map.readIn();
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		BufferedImage image = new BufferedImage(448,496, BufferedImage.TYPE_INT_RGB);
		bg = new BufferedImage(448,496, BufferedImage.TYPE_INT_RGB);

		graphics = image.createGraphics();
		graphics2 = bg.createGraphics();

		graphics.setStroke(new BasicStroke(1));
		graphics.setFont(new Font("Calibri", Font.PLAIN, 75));
		
		graphics2.setStroke(new BasicStroke(1));
		
		JLabel display = new JLabel(new ImageIcon(image));
		JLabel entity = new JLabel(new ImageIcon(bg));
		///////////////////////////////////////////////////////////////////

		// key listener that listens for valid keys pressed
		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == LEFT) { // turn left
					pacman.turnLeft();
				}
				else if (e.getKeyCode() == UP) { // turn up
					pacman.turnUp();
				}
				else if (e.getKeyCode() == RIGHT) { // turn right
					pacman.turnRight();
				}
				else if (e.getKeyCode() == DOWN) { // turn down
					pacman.turnDown();
				}
				else if (e.getKeyCode() == KeyEvent.VK_K) { // manual frightened mode switch
					activateFrightenedMode();
					frightenedTimer = 300;
				}
				else if (e.getKeyCode() == KeyEvent.VK_HIRAGANA) { // pacman dies
//					for (MovableEntity entity : entities) {
//						entity.reset();
//					}
					//System.out.println("death");
					for (int i = 0; i < entities.length; i++) {
						entities[i].reset();
					}
					//System.out.println("just reset");
				}
			}
		});
		
		///////////////////////////////////////////////////////////////////
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getLayeredPane().add(entity);

		contentPane.add(display);

		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

		//setResizable(false);

		pack();
		setLocationRelativeTo(null);//puts the window in the center of the screen. Must be called after pack
	}
	
	// starts the game
	// reused/modified from CardSlap
	public static void main(String[] args) {
		game = new GameRunner();
		game.draw();
		game.start();
	}
	
	public void start() {
		
		// main loop that runs the game until 5 deaths reached
		// moves all entities, does necessary tasks for changing AI and images, and then redraws screen
		// reused and modified from CardSlap
		Thread thread = new Thread(() -> {
			while(deathCounter < 5) {
				try {
					if (frightenedMode) {
						if (frightenedTimer == 0) {
							//TODO setAIMode() back to whatever was on rotation
						}
						else {
							frightenedTimer--;
						}
					}
					else if (AIWaveCounter < 8) {
						if (AIWaveTimer == 0) {
							AIWaveCounter++;
							changeAIWave(AIWaveCounter);
						}
						else {
							AIWaveTimer--;
						}
					}
					
					//entities[1].move();
					moveAll();
					togglePacmanImage();
					redraw();
					Thread.sleep(20);
				} catch (Exception e) {
				}
			}
			
		});
		thread.start();
	}
	
	
	// creates moveable entities
	Ghost blinky = new RedGhost();
	Ghost inky = new BlueGhost();
	Ghost pinky = new PinkGhost();
	Ghost clyde = new YellowGhost();
	
	Pacman pacman = new Pacman();
	
	// all moveable entities
	MovableEntity[] entities = {pacman, blinky, inky, pinky, clyde};
	
	// moves all moveable entitities
	private void moveAll() {
		for (int i = 0; i < entities.length; i++) {
			entities[i].move();
		}
	}
	
	private int deathCounter = 0;
	
	// utility variables for the AI mode waves and frightened mode
	private int frightenedTimer = 0;
	private boolean frightenedMode = false;
	private int AIWaveCounter = 0;
	private int AIWaveTimer = 0;
	private AIMode aiMode;
	// 7 seconds -> 350
	// 20 seconds -> 1000
	// 5 seconds -> 250
	
	// turns on frightened mode
	// at this point in time, energizers are not included in the game, so frightened mode
	// cannot be activated except manually
	// by pressing k
	// also the ghosts don't turn blue
	// actually frightened mode hasn't been implemented yet. currently, it just sets all target tiles
	// to middle of map
	public void activateFrightenedMode() {
		frightenedMode = true;
		changeAIMode(AIMode.FRIGHTENED);
	}
	
	// changes the wave of ghost AI mode
	private void changeAIWave(int wave) {
		switch (wave) {
			case 1:
				AIWaveTimer = 350;
				aiMode = AIMode.SCATTER;
				break;
			case 2:
				AIWaveTimer = 1000;
				aiMode = AIMode.CHASE;
				break;
			case 3:
				AIWaveTimer = 350;
				aiMode = AIMode.SCATTER;
				break;
			case 4:
				AIWaveTimer = 1000;
				aiMode = AIMode.CHASE;
				break;
			case 5:
				AIWaveTimer = 250;
				aiMode = AIMode.SCATTER;
				break;
			case 6:
				AIWaveTimer = 1000;
				aiMode = AIMode.CHASE;
				break;
			case 7:
				AIWaveTimer = 250;
				aiMode = AIMode.SCATTER;
				break;
			default:
			case 8:
				AIWaveTimer = -1;
				aiMode = AIMode.CHASE;
				break;
		}
		changeAIMode(aiMode);
	}
	
	// changes the AI mode for all ghosts
	private void changeAIMode(AIMode mode) {
		for (int i = 1; i < entities.length; i++) {
			((Ghost)(entities[i])).setAIMode(mode);
		}
	}
	
	// utility variables for pacman's mouth animation
	private int pacmanImage = 0;
	private int pacmanImageCounter = 0;
	
	// changes pacman's image so that he opens and closes mouth
	private void togglePacmanImage() {
		if (pacmanImageCounter >= 7) {
			pacmanImage = pacman.getDirectionNumber()+10;
		}
		else {
			pacmanImage = 0;
		}
		pacmanImageCounter++;
		if (pacmanImageCounter >= 15) {
			pacmanImageCounter = 0;
		}
	}
	
	// imports BufferedImages from a file
	// reused from CardSlap
	public static BufferedImage getResource(String filename) {
		try {
			return ImageIO.read(GameRunner.class.getClassLoader().getResource("resource/"+filename));
		} catch (Exception e) {
			System.out.println(filename);
			e.printStackTrace();
			return null;
		}
	}
	
	// draws the walls and door to ghost house and background tiles
	public void draw() {
		for (int row = 0; row < map.walls.size(); row++) {
			for (int col = 0; col < map.walls.get(0).size(); col++) {
				if (map.walls.get(row).contains(col)) {
					if ( (row == 12) && (col == 13 || col == 14) ) {
						graphics2.drawImage(IMAGES[7], col*16, row*16, 16, 16, null);
					}
					else {
						graphics2.drawImage(IMAGES[5], col*16, row*16, 16, 16, null);
					}
				}
				else if (map.dots.get(row).contains(col)) {
					graphics2.drawImage(IMAGES[8], col*16, row*16, 16, 16, null);
				}
				else {
					graphics2.drawImage(IMAGES[6], col*16, row*16, 16, 16, null);
				}
			}
		}
		
		setVisible(true);
		repaint();
	}
	
	// redraws the screen
	public void redraw() {
		graphics.clearRect(0, 0, getWidth(), getHeight());
		graphics.drawImage(bg, 0, 0, null);
//		for (int row = 0; row < Map.walls.size(); row++) {
//			for (int col = 0; col < Map.walls.get(0).size(); col++) {
//				if (Map.dots.get(row).contains(col)) {
//					graphics.drawImage(IMAGES[8], col*16, row*16, 16, 16, null);
//				}
//				else {
//					graphics.drawImage(IMAGES[6], col*16, row*16, 16, 16, null);
//				}
//			}
//		}
		for (int i = 0; i < entities.length; i++) {
			int imageNum = i;
			if (imageNum == 0) {
				imageNum = pacmanImage;
			}
			graphics.drawImage(IMAGES[imageNum], (int)((entities[i].xPos-0.5)*16), (int)((entities[i].yPos-0.5)*16), 16, 16, null);
		}
		repaint();
	}
}
