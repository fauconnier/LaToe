package org.melodi.tools.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class IO_Service {

	public IO_Service() {

	}

	public ArrayList<String> getAllFile(String file, String pattern){
		
		ArrayList<String> allFile = new ArrayList<String>();
		File folder = new File(file);
		
		if(folder.isDirectory()){
		   for (final File fileEntry : folder.listFiles()) {
		        if (!fileEntry.isDirectory() & fileEntry.getName().contains(pattern)) {
		        	
		        	if(!fileEntry.getName().contains("autosav")){
		        		allFile.add(fileEntry.getAbsolutePath());
		        	}
		        	
		        } else {
		        }
		    }
		}
		else{
			System.err.println(file + " is not a directory");
		}
		
		Collections.sort(allFile);
		
		
		return allFile;
	}
	
	
	public static ArrayList<String> readFileArray(String filename) throws FileNotFoundException{
	
		String filePath = filename;
		Scanner scanner = new Scanner(new File(filePath));

		ArrayList<String> toReturn = new ArrayList<String>();
		

		// On boucle sur chaque champ detecté
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			toReturn.add(line);
			// faites ici votre traitement
		}

		scanner.close();
		return toReturn;
	}

	public static String readFile(String filename)
			throws UnsupportedEncodingException, FileNotFoundException {

		String filePath = filename;
		Scanner scanner = new Scanner(new File(filePath));

		String toReturn = "";

		while (scanner.hasNextLine()) {
			
			String line = scanner.nextLine();

			toReturn += line +"\n";
		}

		scanner.close();
		return toReturn;

	}

	public static void writeFile(String filename, String encoding, String text) {

		File outFile = new File(filename);
		Writer destFile = null;
		try {
			destFile = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFile), encoding));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			destFile.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			destFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
