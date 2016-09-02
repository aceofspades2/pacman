package pacman;

public class BlueGhost extends Ghost {
	public BlueGhost() {
		super();
		construct(13.5, 14.5, Directions.UP);
//		xStart = 12;
//		yStart = 14.5;
//		directionStart = Directions.UP;
	}
	
	public boolean leftHouse = false;
	
	public void reset() {
		leftHouse = false;
		super.reset();
		speed = .140625;
	}

	public void move() {
		if (GameRunner.game.pacman.dotsEaten >= 30) {
			if (!leftHouse) {
				if (((RedGhost)(GameRunner.game.blinky)).leftHouse && ((PinkGhost)(GameRunner.game.pinky)).leftHouse) {
					//leaveHouse();
					superMove();
					if (yPos < 12) {
						leftHouse = true;
					}
				}
			}
			else {
				super.move();
			}
		}
	}

	public void chooseTargetTile() {
		// TODO chase mode and frightened mode
				int xTarget, yTarget;
				if (aiMode == AIMode.SCATTER) {
					xTarget = 27; //27
					yTarget = 30; //30
				}
				else if (aiMode == AIMode.FRIGHTENED) {
					xTarget = 14;
					yTarget = 15;
				}
				else {
					double x, y; // temporary locations for calculations
					Pacman pacman = GameRunner.game.pacman;
					Ghost blinky = GameRunner.game.blinky;
					
					if (pacman.direction == Directions.LEFT) {
						x = pacman.xPos-2;
						y = pacman.yPos;
					}
					else if (pacman.direction == Directions.UP) {
						x = pacman.xPos;
						y = pacman.yPos-2;
					}
					else if (pacman.direction == Directions.RIGHT) {
						x = pacman.xPos+2;
						y = pacman.yPos;
					}
					else /*if (pacman.direction == Directions.DOWN) */ {
						x = pacman.xPos;
						y = pacman.yPos+2;
					}
					
					double xDiff = x-blinky.xTile;
					double yDiff = y-blinky.yTile;
					
					xTarget = (int)(x+xDiff);
					yTarget = (int)(y+yDiff);
				}
				setTargetTile(new int[] {xTarget, yTarget});
	}

}
