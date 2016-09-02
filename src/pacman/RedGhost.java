package pacman;

public class RedGhost extends Ghost {
	
	public RedGhost() {
		super();
		construct(14, 11.5, Directions.LEFT);
		//xStart = 14;
		//yStart = 11.5;
		//directionStart = Directions.LEFT;
		
	}
	
	public boolean leftHouse = true;
	
	// resets position and other variables
	public void reset() {
		leftHouse = false;
		xPos = 14;
		yPos = 14.5;
		xTile = (int)xPos;
		yTile = (int)yPos;
		lastX = xTile;
		lastY = yTile;
		speed = .140625;
		direction = Directions.UP;
	}
	
	// moves if all conditions for movement are met
	public void move() {
		if (!leftHouse) {
			superMove();
			if (yPos < 12) {
				leftHouse = true;
			}
		}
		else {
			super.move();
		}
	}
	
	// chooses target tile
	public void chooseTargetTile() { // returns a 2-long int array [xTarget, yTarget]
		// TODO chase mode and frightened mode
		int xTarget, yTarget;
		if (aiMode == AIMode.SCATTER) {
			xTarget = 27; //27
			yTarget = 0; //0
		}
		else if (aiMode == AIMode.FRIGHTENED) {
			xTarget = 14;
			yTarget = 15;
		}
		else /*if (aiMode == AIMode.CHASE) */ {
			Pacman pacman = GameRunner.game.pacman;
			xTarget = pacman.xTile;
			yTarget = pacman.yTile;
		}
		setTargetTile(new int[] {xTarget, yTarget});
	}
}
