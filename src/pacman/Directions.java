package pacman;

public enum Directions { 
	// this was reused and modified from code that Gavi (and Harry Wu, Jeff Page, and I) wrote for a hackathon
	UP (0),
	LEFT (1),
	DOWN (2),
	RIGHT (3);
	
	final int direction;
	
	Directions(int direction) {
		this.direction = direction;
	}
}
