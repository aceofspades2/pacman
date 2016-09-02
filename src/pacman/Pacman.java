package pacman;

public class Pacman extends MovableEntity {
	
	public int dotsEaten;
	
	public Pacman() {
		xStart = 14.0;
		yStart = 23.5;
		directionStart = Directions.LEFT;
		speed = 0.5;
		reset();
		newDirection = direction;
		dotsEaten = 0;
		dotSlownessTimer = 0;
	}
	private Directions newDirection;
	
	public int getDirectionNumber() {
		if (direction == Directions.UP) {
			return 0;
		}
		else if (direction == Directions.LEFT) {
			return 1;
		}
		else if (direction == Directions.DOWN) {
			return 2;
		}
		else {// if (direction == Directions.RIGHT) {
			return 3;
		}
	}
	
	private int dotSlownessTimer;
	
	public void move() {
		//System.out.println(dotSlownessTimer);
		if (dotSlownessTimer == 0) {
			//System.out.println("moving");
			super.move();
			
			//cornering
			if ((direction == Directions.UP || direction == Directions.DOWN) && xPos != xTile+0.5) {
				if ((xPos > xTile+0.5 && xPos < xTile+0.625) || (xPos < xTile+0.5 && xPos > xTile+0.375)) {
					xPos = xTile+0.5;
				}
				else if (xPos > xTile+0.5) {
					super.moveLeft(0.125);
				}
				else if (xPos < xTile+0.5) {
					super.moveRight(0.125);
				}
			}
			else if ((direction == Directions.LEFT || direction == Directions.RIGHT) && yPos != yTile+0.5) {
				if ((yPos > yTile+0.5 && yPos < yTile+0.625) || (yPos < yTile+0.5 && yPos > yTile+0.375)) {
					yPos = yTile+0.5;
				}
				else if (yPos > yTile+0.5) {
					super.moveUp(0.125);
				}
				else if (yPos < yTile+0.5) {
					super.moveDown(0.125);
				}
			}

			//System.out.println("x: "+xPos+"y: "+yPos+"direction: "+direction);

			lastX = xTile; // stores the last x tile position in lastX
			lastY = yTile; // stores the last y tile position in lastY

			xTile = (int)xPos; // recalculates xTile position
			yTile = (int)yPos; // recalculates yTile position

			if (xTile != lastX || yTile != lastY) { // if pacman moved into a new tile
				if (GameRunner.game.map.dots.get(yTile).remove(new Integer(xTile))) {
					dotsEaten++;
					dotSlownessTimer = 1;
					//System.out.println("eating dot");
				}
				GameRunner.game.graphics2.drawImage(GameRunner.IMAGES[6], xTile*16, yTile*16, 16, 16, null);
			}

			if (direction == Directions.LEFT && (xPos+speed) >= (xTile+0.5) && (direction != newDirection)) {
				direction = newDirection;
//				if (direction == Directions.UP || direction == Directions.DOWN) {
//					xPos = xTile + 0.5;
//				}
			}
			else if (direction == Directions.RIGHT && (xPos-speed) <= (xTile+0.5) && (direction != newDirection)) {
				direction = newDirection;
//				if (direction == Directions.UP || direction == Directions.DOWN) {
//					xPos = xTile + 0.5;
//				}
			}
			else if (direction == Directions.UP && (yPos+speed) >= (yTile+0.5) && (direction != newDirection)) {
				direction = newDirection;
//				if (direction == Directions.LEFT || direction == Directions.RIGHT) {
//					yPos = yTile + 0.5;
//				}
			}
			else if (direction == Directions.DOWN && (yPos-speed) <= (yTile+0.5) && (direction != newDirection)) {
				direction = newDirection;
//				if (direction == Directions.LEFT || direction == Directions.RIGHT) {
//					yPos = yTile + 0.5;
//				}
			}
		}
		else {
			dotSlownessTimer--;
			//System.out.println("changing dotSlownessTimer to "+dotSlownessTimer);
		}
	}

	public void turnLeft() {
		newDirection = Directions.LEFT;
	}

	public void turnDown() {
		newDirection = Directions.DOWN;
	}

	public void turnUp() {
		newDirection = Directions.UP;
	}

	public void turnRight() {
		newDirection = Directions.RIGHT;
	}
	

}
