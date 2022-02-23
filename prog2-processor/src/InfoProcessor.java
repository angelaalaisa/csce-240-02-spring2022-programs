/*
 * Written by Angela Alaisa
 */

import java.util.Scanner;
import java.io.*;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class InfoProcessor {
	
	//variables
		String name;
		String region;
		String colaAddress;
		String homeAddress;
		String busiPhone;
		String homePhone;
		List<String> personalInfo;		//a list to hold personal info since the number of info is not known	


	public static void main(String[] args) {
		
		InfoProcessor info = new InfoProcessor();
		
		Scanner keyboard = new Scanner(System.in);

		// get input
		System.out.println("Enter District 74 to continue: ");
		String input = keyboard.nextLine();
		
		//verify input
		if(input.equalsIgnoreCase("District 74")) {
			System.out.println("Please enter one of the following identifiers to select an information type:\n"
					+ "Contact Information - I1\n"
					+ "Personal Information - I2\n"
					+ "Committee Assignments - I3\n"
					+ "Sponsored Bills in the House - I4\n"
					+ "Voting Record - I5\n"
					+ "Service in Public Office - I6");
			
			//get user info input
			String type = keyboard.nextLine();
			try {
				info.readFile(type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("invalid district. try again.");
		}
		
	}
	
	
	public void readFile(String type) throws Exception{
		
		int addressCount = 0;
		
		//Regex patterns for types of info
		Pattern nameP = Pattern.compile("(?<=Representative\\s)[A-Za-z.\\s]+\\<");
		Pattern regionP = Pattern.compile("[a-zA-z]+(?=(\\sCounty))");
		Pattern addressP = Pattern.compile("(?<=\\>)\\d+([a-zA-Z\\s.])+");
		Pattern cityP = Pattern.compile("(?<=\\>)[a-zA-Z]+\\s\\d+");
		Pattern phoneP = Pattern.compile("\\(\\d\\d\\d\\)\\s\\d\\d\\d-\\d\\d\\d\\d");
		Pattern personalP = Pattern.compile("\\>[a-zA-z0-9\\s\\.\\,]+");
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader("document.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			
			Matcher a = nameP.matcher(line);
			if(a.find()) {
				name = a.group();
				System.out.println("Name: "+name);
			}
			
			Matcher b = regionP.matcher(line);
			if(b.find()) {
				region = b.group();
				System.out.println("Region: "+region);
			}
		
			Matcher c = addressP.matcher(line);
			if(c.find()) {
				addressCount++;
				if(addressCount == 1) {
					colaAddress = c.group();
				}
				else if(addressCount == 2) {
					homeAddress = c.group();
				}
			}
			
		}
		reader.close();
		getContents(type);	
	}
	
	//retrieves information that user wants
	public static String getContents(String type) {
		String infoType;
		switch(type) {
			
		case "I1":
			infoType = "Contact Information";
			//print info here
			break;
			
		case "I2":
			infoType = "Personal Information";
			//print info here
			break;
			
		case "I3":
			infoType = "Committee Assignments";
			//print info here
			break;
			
		case "I4":
			infoType = "Sponsored Bills";
			//print info here
			break;
			
		case "I5":
			infoType = "Voting Record";
			//print info here
			break;
			
		case "I6":
			infoType = "Service in Public Office";
			//print info here
			break;
		
		default:
			infoType = "invalid";
			break;
			
		}
		return infoType;
	}
		
}

