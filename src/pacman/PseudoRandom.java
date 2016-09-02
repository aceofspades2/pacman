package pacman;

public class PseudoRandom {
	private static Object temp;
	
	// returns a pseudo random number
	public static int randomNumber() {
		temp = new Object();
		int number = temp.hashCode(); 
		// ^^^ reused from http://stackoverflow.com/questions/1961146/memory-address-of-variables-in-java
		
		temp = null;
		return number;
	}
	
	// returns a pseudorandom number from 0-15
	public static int randomHexDigit() {
		int number = randomNumber();
		String string = Integer.toHexString(number);
		char character = string.charAt(string.length()-1);
		int digit;
		if (character < 72) {
			digit = character - 60;
		}
		else {
			digit = character - 87;
		}
		return digit;
	}
}
