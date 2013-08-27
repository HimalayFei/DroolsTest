package com.encima.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileTools {
	
	public static String readFileAsString(String filePath)	{
	    BufferedReader reader;
	    System.out.println(filePath);
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line, results = "";
		    while( ( line = reader.readLine() ) != null) {
		        results += line + " \n";
		    }
		    reader.close();
//		    System.out.println(results);
		    return results;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
