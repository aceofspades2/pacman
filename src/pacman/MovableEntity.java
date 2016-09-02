package pacman;

public abstract class MovableEntity {
	
	// resets position to starting position
	public void reset() { // resets position to starting position
		//System.out.println("resetting "+this);
		
		xPos = xStart;
		yPos = yStart;
		xTile = (int)xPos;
		yTile = (int)yPos;
		lastX = xTile;
		lastY = yTile;
		speed = 0.15;
		direction = directionStart;
	}
	
	// variables
	public double xStart;
	public double yStart;
	public Directions directionStart;
	
	public double xPos; // exact x location
	public double yPos; // exact y location
	public int xTile, lastX; // x tile location (or what column it's in), x tile location from before last move
	public int yTile, lastY; // y tile location (or what row it's in), y tile location from before last move
	public Directions direction;
	public double speed; // in tiles per frame
	
	// moves entity in direction direction by speed amount
	public void move() {
		//System.out.println("moving " + this.getClass() + direction);
		if (direction == Directions.LEFT) {
			moveLeft(speed);
		}
		else if (direction == Directions.UP) {
			moveUp(speed);
		}
		else if (direction == Directions.RIGHT) {
			moveRight(speed);
		}
		else if (direction == Directions.DOWN) {
			moveDown(speed);
		}
		tunnel();
	}
	
	// the following 4 methods move the entity in the correct direction by the argument amount
	// if the front is clear
	public void moveUp(double speed) {
		double amount;
		if (isFrontClear(direction, speed+0.5)) {
			amount = speed;
		}
		else {
			amount = yPos - ((int)(yPos - speed) + 0.5);
		}
		yPos -= amount;
	}
	
	public void moveDown(double speed) {
		double amount;
		if (isFrontClear(direction, speed+0.5)) {
			amount = speed;
		}
		else {
			amount = (int)(yPos + speed) + 0.5 - yPos;
		}
		yPos += amount;
	}
	
	public void moveLeft(double speed) {
		double amount;
		if (isFrontClear(direction, speed+0.5)) {
			amount = speed;
		}
		else {
			amount = xPos - ((int)(xPos - speed) + 0.5);
		}
		xPos -= amount;
	}
	
	public void moveRight(double speed) {
		double amount;
		if (isFrontClear(direction, speed+0.5)) {
			amount = speed;
		}
		else {
			amount = (int)(xPos + speed) + 0.5 - xPos;
		}
		xPos += amount;
	}
	
	// if entity is at end of tunnel, teleports it to the other side
	private void tunnel() {
		if (xPos < 0 && yTile == 14) {
			xPos = 27.9;
		}
		else if(xPos >= 28) {
			xPos = 0;
		}
	}
	
	// checks if the front is clear (not blocked by a wall
	public boolean isFrontClear(Directions dir, double amount) {
		if (dir == Directions.LEFT) {
			int newX = (int)(xPos - amount);
			if (GameRunner.game.map.walls.get((int)yPos).contains(newX)) {
				return false;
			}
		}
		else if (dir == Directions.UP) {
			int newY = (int)(yPos - amount);
			if (newY == 12 && (xTile == 13 || xTile == 14)) { // exiting door from ghost house
				return true;
			}
			if (GameRunner.game.map.walls.get(newY).contains((int)xPos)) {
				return false;
			}
		}
		else if (dir == Directions.RIGHT) {
			int newX = (int)(xPos + amount);
			if (GameRunner.game.map.walls.get((int) yPos).contains(newX)) {
				return false;
			}
		}
		else if (dir == Directions.DOWN) {
			int newY = (int)(yPos + amount);
			if (GameRunner.game.map.walls.get(newY).contains((int)xPos)) {
				return false;
			}
		}
		return true;
	}

}
