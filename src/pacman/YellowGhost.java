package pacman;

public class YellowGhost extends Ghost {
	public YellowGhost() {
		super();
		construct(14.5, 14.5, Directions.UP);
//		xStart = 16;
//		yStart = 14.5;
//		directionStart = Directions.UP;
	}
	
	public boolean leftHouse = false;

	// resets position and other variables
	public void reset() {
		leftHouse = false;
		super.reset();
		speed = .140625;
	}
	
	// if all conditions for movement are met, moves
	public void move() {
		if (GameRunner.game.pacman.dotsEaten >= 90) {
			if (!leftHouse) {
				if (((RedGhost)(GameRunner.game.blinky)).leftHouse && ((PinkGhost)(GameRunner.game.pinky)).leftHouse
					&& ((BlueGhost)(GameRunner.game.inky)).leftHouse) {
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
	
	// chooses the target tile
	public void chooseTargetTile() {
		// TODO chase mode and frightened mode
				int xTarget, yTarget;
				if (aiMode == AIMode.SCATTER) {
					xTarget = 0; //0
					yTarget = 30; //30
				}
				else if (aiMode == AIMode.FRIGHTENED){
					xTarget = 14;
					yTarget = 15;
				}
				else {
					Pacman pacman = GameRunner.game.pacman;
					if (Math.sqrt(Math.pow(pacman.yTile - yTile, 2) + Math.pow(pacman.xTile - xTile, 2)) <= 8) {
						xTarget = 0; //0
						yTarget = 30; //30
					}
					else {
						xTarget = pacman.xTile;
						yTarget = pacman.yTile;
					}
				}
				setTargetTile(new int[] {xTarget, yTarget});
	}
}
