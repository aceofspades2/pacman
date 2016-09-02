package pacman;

public class PinkGhost extends Ghost {
	public PinkGhost() {
		super();
		construct(14.5, 14.5, Directions.UP);
//		xStart = 14;
//		yStart = 14.5;
//		directionStart = Directions.UP;
	}
	
	public boolean leftHouse = false;

	// resets position and other variables (eg. when pacman dies or start of game/level)
	public void reset() {
		leftHouse = false;
		super.reset();
		speed = .140625;
	}
	
	// moves if all conditions for movement are met
	public void move() {
		if (GameRunner.game.pacman.dotsEaten >= 4) {
			if (!leftHouse) {
				if (((RedGhost)(GameRunner.game.blinky)).leftHouse) {
					//leaveHouse();
					superMove();
					if (yPos < 12) {
						leftHouse = true;
					}
				}
			}
			else {
				//System.out.println("moving pink");
				super.move();
			}
		}
	}
	
	// chooses a target tile
	public void chooseTargetTile() {
		// TODO chase mode and frightened mode
				int xTarget, yTarget;
				if (aiMode == AIMode.SCATTER) {
					xTarget = 0; //0
					yTarget = 0; //0
				}
				else if (aiMode == AIMode.FRIGHTENED) {
					xTarget = 14;
					yTarget = 15;
				}
				else /*if (aiMode == AIMode.CHASE) */ {
					Pacman pacman = GameRunner.game.pacman;
					if (pacman.direction == Directions.LEFT) {
						xTarget = pacman.xTile-4;
						yTarget = pacman.yTile;
					}
					else if (pacman.direction == Directions.UP) {
						xTarget = pacman.xTile;
						yTarget = pacman.yTile-4;
					}
					else if (pacman.direction == Directions.RIGHT) {
						xTarget = pacman.xTile+4;
						yTarget = pacman.yTile;
					}
					else /*if (pacman.direction == Directions.DOWN) */ {
						xTarget = pacman.xTile;
						yTarget = pacman.yTile+4;
					}
				}
				setTargetTile(new int[] {xTarget, yTarget});
	}
}
