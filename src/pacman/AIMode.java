package pacman;

public enum AIMode {
	CHASE (0),
	SCATTER (1),
	FRIGHTENED (2);
	
	final int mode;
	
	AIMode(int mode) {
		this.mode = mode;
	}
}
