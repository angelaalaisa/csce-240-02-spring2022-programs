/*
 * Written by Angela Alaisa
 */

import java.util.Scanner;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.FileWriter;

public class InfoProcessor {
	
	//variables
		String name;
		String region;
		String colaAddress;
		String homeAddress;
		String busiColaPhone;
		String busiHomePhone;
		String homePhone;
		List<String> personalInfo = new ArrayList<String>();		//a list to hold personal info since the number of info is not known	


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
				info.readWriteFile(type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("invalid district. try again.");
		}
		
	}
	
	public void readWriteFile(String type) throws Exception{
		
		int addressCount = 0;
		int phoneCount = 0;
		
		//Regex patterns for types of info
		Pattern nameP = Pattern.compile("(?<=Representative\\s)([A-Za-z.]+\\s[A-Za-z.]+\\s[A-Za-z.]+\\s)");
		Pattern regionP = Pattern.compile("([A-Za-z]+)(?=(\\sCounty))");
		Pattern addressP = Pattern.compile("(?<=\\>)(\\d+[a-zA-Z\\s.]+)(\\<br\\>)([A-Za-z\\s]+\\d+)");
		Pattern phoneP = Pattern.compile("\\(\\d\\d\\d\\)\\s\\d\\d\\d-\\d\\d\\d\\d");
		Pattern personalP = Pattern.compile("(<li style=\"margin: 5px 0 0 0; list-style-type:square;\" >)([a-zA-z0-9\\s\\\\.\\,\\-\\\"\\/\\']+)");
		
		
		BufferedReader reader = new BufferedReader(new FileReader("document.txt"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("test_output.txt"));

		String line;
		while ((line = reader.readLine()) != null) {
			
			Matcher a = nameP.matcher(line);
			if(a.find()) {
				name = a.group();
			}
			
			Matcher b = regionP.matcher(line);
			if(b.find()) {
				region = b.group();
			}
		
			Matcher c = addressP.matcher(line);
			if(c.find()) {
				addressCount++;
				if(addressCount == 1) {
					colaAddress = c.group(1) + " " + c.group(3);
				}
				else if(addressCount == 2) {
					homeAddress = c.group(1) + " " + c.group(3);
				}
			}
			
			Matcher d = phoneP.matcher(line);
			if(d.find()) {
				String phone = d.group();
				if(line.contains("Home Phone"))
					homePhone = phone;
				else if(line.contains("Business Phone") && phoneCount==0) {
					phoneCount++;
					busiColaPhone = phone;
				}
				else if(line.contains("Business Phone") && phoneCount==1)
					busiHomePhone = phone;
			}
			
			Matcher e = personalP.matcher(line);
			if(e.find()) {
				personalInfo.add(e.group(2));			}
		}
		
		System.out.println("Data extracted to output file");
		
		//writing contents to file
		writer.write("Name: " + name + "\n");
		writer.write("Region: " + region + "\n");
		writer.write("Columbia Address: " + colaAddress + "\n");
		writer.write("\tBusiness Phone: " + busiColaPhone + "\n");
		writer.write("Home Address: " + homeAddress + "\n");
		writer.write("\tBusiness Phone: " + busiHomePhone + "\n");
		writer.write("\tHome Phone: " + homePhone + "\n");
		//writer.write(personalInfo.toString());
		
		reader.close();
		writer.close();
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

