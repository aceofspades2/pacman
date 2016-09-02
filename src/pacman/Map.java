package pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
	
	public void readIn() {
		fileName = "walls.txt";
		readDataFile(walls);
		fileName = "dots.txt";
		readDataFile(dots);
		
		
//		door = new ArrayList<ArrayList<Integer>>();
//		for (int i = 0; i < 12; i++) {
//			door.add(null);
//		}
//		door.add(12, new ArrayList<Integer>());
//		door.get(12).add(13);
//		door.get(12).add(14);
	}
	
	public ArrayList<ArrayList<Integer>> walls = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> dots = new ArrayList<ArrayList<Integer>>();
	private String fileName;
	private Scanner file;
	
//	public static ArrayList<ArrayList<Integer>> door;
	
	//reads in input file for wall locations
	private void readDataFile(ArrayList<ArrayList<Integer>> lst) {
		try {
			file = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Bye.");
			System.exit(0);
		}
		file.useDelimiter(", ");
		
		for (int i = 0; i < 30; i++) {
			//System.out.println(i);
			lst.add(new ArrayList<Integer>());
			while (file.hasNextInt()) {
				//String temp = file.next();
				//System.out.println(temp);
				lst.get(i).add(file.nextInt());//Integer.parseInt(temp));
			}
			file.nextLine();
		}
		lst.add(new ArrayList<Integer>());
		while (file.hasNextInt()) {
			lst.get(30).add(file.nextInt());
		}
	}
}
