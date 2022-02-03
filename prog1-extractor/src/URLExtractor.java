/*
 * Written by Angela Alaisa
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.io.FileWriter;

public class URLExtractor {

	public static void main(String[] args) throws Exception {
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Please enter \"District 74\" to confirm");	//Take in District as input
		String input = keyboard.nextLine();
		if(input.equalsIgnoreCase("District 74")) {
			readWriteURL();
		}
		else {
			System.out.println("invalid entry, please try again");
		}
		keyboard.close();
	}
	
	public static void readWriteURL() throws Exception{
		
		//variables
		int lineCount = 0;
		int wordCount = 0;
		int charCount = 0;
		
		URL website = new URL("https://www.scstatehouse.gov/member.php?code=1614772534");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(website.openStream()));
		BufferedWriter writer = new BufferedWriter(new FileWriter("Assignment1Output.txt"));
		
		//Getting URL Statistics
		String inputLine;
		while ((inputLine = reader.readLine()) != null) {	//reading URL
			lineCount++;
			String[] words = inputLine.split(" ");
			wordCount += words.length;
			for(int i = 0; i<words.length; i++) {
				charCount += words[i].length();
			}
			//System.out.println(inputLine); for testing purposes
			writer.write(inputLine+"\n");					//outputting each line to file
		}
		reader.close();
		writer.close();
		
		System.out.println("Output file created in data folder."+"\n");
		System.out.println("Line Count: "+lineCount+"\nWord Count: "+wordCount+"\nCharacter Count: "+charCount);
	}
}
