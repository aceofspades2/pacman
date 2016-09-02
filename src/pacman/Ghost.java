package pacman;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public abstract class Ghost extends MovableEntity {
	
	public Ghost () {
		try {
			robot = new Robot();
		}
		catch (Exception e) {
		}
	}
	// not sure if this is still used, but it was used as a constructor in subclasses because i needed code
	// before i called the super constructor, and that wasn't allowed
	public void construct(double x, double y, Directions d) { // acts as a constructor because an actualy constructor didn't work
		xStart = x;
		yStart = y;
		directionStart = d;
		reset();
		speed = .140625;
		newDirection = directionStart;
		//aiMode = AIMode.SCATTER;
	}
	
	// see other ghost classes reset comment
	public void reset() {
		super.reset();
		newDirection = direction;
		aiMode = AIMode.CHASE;
	}
	
//	public void leaveHouse() {
//		xStart = 13.5;
//		yStart = 11.5;
//		directionStart = Directions.LEFT;
//		reset();
//		speed = .140625;
//		newDirection = direction;
//	}
	//chooses target tile
	abstract void chooseTargetTile(); // target tile calculations will rely on tile (int) locations, not exact (double)
	public void setTargetTile(int[] target) {
		//System.out.println("\n\n\nsetting xTarget to "+target[0]+"\nsetting yTarget to "+target[1]+"\n\n");
		xTarget = target[0];
		yTarget = target[1];
	}
	public int[] getTargetTile() {
		return new int[] {xTarget, yTarget};
	}
	
	public int xTarget;
	public int yTarget;
	
	public Directions newDirection;
	
	public AIMode aiMode;
	
	Robot robot;
	
	public void nextMove() {
		//System.out.println("x target: "+xTarget+"\ny target: "+yTarget);
		
		
		
		// calculate distances from tiles to up, left, down, and right of current tile to target tile
		double upDist = Math.sqrt(Math.pow(yTarget - (yTile - 1), 2) + Math.pow(xTarget - xTile, 2));
		double leftDist = Math.sqrt(Math.pow(yTarget - yTile, 2) + Math.pow(xTarget - (xTile - 1), 2));
		double downDist = Math.sqrt(Math.pow(yTarget - (yTile + 1), 2) + Math.pow(xTarget - xTile, 2));
		double rightDist = Math.sqrt(Math.pow(yTarget - yTile, 2) + Math.pow(xTarget - (xTile + 1), 2));
		
		//System.out.println("up: "+upDist+"\nleft: "+leftDist+"\ndown: "+downDist+"\nright: "+rightDist);
		
		
		double smallDist = 500; // starts smallest distance to target tile as 500, which is bigger than anything
								// possible
		
		// sets newDirection to up if the tile above is not a wall or the last tile
		if (!GameRunner.game.map.walls.get(yTile-1).contains(xTile) && (lastY != yTile-1) ) {
			//System.out.println("switching from "+direction+" to up");
			newDirection = Directions.UP;
			smallDist = upDist;
		}
		// sets newDirection to left under same conditions as above and if leftDist < smallDist
		if (!GameRunner.game.map.walls.get(yTile).contains(xTile-1) && (lastX != xTile-1) && (leftDist < smallDist) ) {
			//System.out.println("switching from "+direction+" to left");
			newDirection = Directions.LEFT;
			smallDist = leftDist;
		}
		// sets newDirection to down under same conditions as above
		if (!GameRunner.game.map.walls.get(yTile+1).contains(xTile) && (lastY != yTile+1) && (downDist < smallDist) ) {
			//System.out.println("switching from "+direction+" to down");
			newDirection = Directions.DOWN;
			smallDist = downDist;
		}
		// sets newDirection to right under same conditions as above
		if (!GameRunner.game.map.walls.get(yTile).contains(xTile+1) && (lastX != xTile+1) && (rightDist < smallDist) ) {
			//System.out.println("switching from "+direction+" to right");
			newDirection = Directions.RIGHT;
			smallDist = rightDist;
		}
		//System.out.println("Smallest distance: "+smallDist);
	}
	
	public void move() {
		if (yTile == 14 && (xTile < 6 || xTile > 21)) {
			speed = .075;
		}
		else {
			speed = .14625;
		}
		super.move();
		
		//System.out.println("x: "+xPos+"y: "+yPos+"direction: "+direction);
		
		lastX = xTile; // stores the last x tile position in lastX
		lastY = yTile; // stores the last y tile position in lastY
		
		xTile = (int)xPos; // recalculates xTile position
		yTile = (int)yPos; // recalculates yTile position
		
		if (xTile != lastX || yTile != lastY) { // if the ghost moved into a new tile
			if (aiMode != AIMode.FRIGHTENED) {
				chooseTargetTile(); // recalculate target tile
				nextMove(); // choose what next move will be
			}
			else {
				boolean leftClear = isFrontClear(Directions.LEFT, 1) && direction != Directions.RIGHT;
				boolean rightClear = isFrontClear(Directions.RIGHT, 1) && direction != Directions.LEFT;
				boolean upClear = isFrontClear(Directions.UP, 1) && direction != Directions.DOWN;
				boolean downClear = isFrontClear(Directions.DOWN, 1) && direction != Directions.UP;

				int temp = 0;
				if (leftClear) temp++;
				if (rightClear) temp++;
				if (upClear) temp++;
				if (downClear) temp++;

				if (temp > 2) {

					int randInt = PseudoRandom.randomHexDigit();
					switch (randInt) {
					default:
					case 0:
					case 1:
					case 2:
					case 3:
						newDirection = Directions.UP;
						break;
					case 4:
					case 5:
					case 6:
					case 7:
						newDirection = Directions.LEFT;
						break;
					case 8:
					case 9:
					case 10:
					case 11:
						newDirection = Directions.DOWN;
						break;
					case 12:
					case 13:
					case 14:
					case 15:
						newDirection = Directions.RIGHT;
						break;
					}
				}

				if (!isFrontClear(newDirection, 1)) {
					if (upClear && direction != Directions.DOWN) {
						newDirection = Directions.UP;
					}
					else if (leftClear && direction != Directions.RIGHT) {
						newDirection = Directions.LEFT;
					}
					else if (downClear && direction != Directions.UP) {
						newDirection = Directions.DOWN;
					}
					else if (rightClear && direction != Directions.LEFT) {
						newDirection = Directions.RIGHT;
					}
				}
			}
		}
		else if (direction == Directions.LEFT && (xPos+speed) >= (xTile+0.5) /*&& (direction != newDirection)*/) {
			direction = newDirection;
			if (direction == Directions.UP || direction == Directions.DOWN) {
				xPos = xTile + 0.5;
			}
		}
		else if (direction == Directions.RIGHT && (xPos-speed) <= (xTile+0.5) /*&& (direction != newDirection)*/) {
			direction = newDirection;
			if (direction == Directions.UP || direction == Directions.DOWN) {
				xPos = xTile + 0.5;
			}
		}
		else if (direction == Directions.UP && (yPos+speed) >= (yTile+0.5) /*&& (direction != newDirection)*/) {
			direction = newDirection;
			if (direction == Directions.LEFT || direction == Directions.RIGHT) {
				yPos = yTile + 0.5;
			}
		}
		else if (direction == Directions.DOWN && (yPos-speed) <= (yTile+0.5) /*&& (direction != newDirection)*/) {
			direction = newDirection;
			if (direction == Directions.LEFT || direction == Directions.RIGHT) {
				yPos = yTile + 0.5;
			}
		}
		
		if (xTile == GameRunner.game.pacman.xTile && yTile == GameRunner.game.pacman.yTile) {
			if (aiMode != AIMode.FRIGHTENED) {
				//System.out.println("death, now key press");
				robot.keyPress(KeyEvent.VK_HIRAGANA);
				robot.keyRelease(KeyEvent.VK_HIRAGANA);
			}
			else {
				// ghost eaten
				reset();
			}
		}
	}
	
	public void superMove() {
		super.move();
	}
	
	public void setAIMode(AIMode mode) {
		aiMode = mode;
	}
	public AIMode getAIMode() {
		return aiMode;
	}
}
