package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Statics {

	public static final int nbOfIterations = 50;
	public static final int maxAge = 18;
	//Name of file to be processed
	public static final String url = "alt_view.txt";
	//If copied 'other view', set this to true. 
	//If all player information is on one line in a self created file, set this to false
	public static final boolean entered = true;
	public static boolean threeLines = false;
	public static final String FILENAME = "log.txt";
	
	private static FileWriter fw;
	private static BufferedWriter bw;
	
	public static void writeToFile(String text) {
		try {
			if(fw == null && bw == null){
				fw = new FileWriter(Statics.FILENAME);
				bw = new BufferedWriter(fw);
			}
			bw.write(text + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
}
