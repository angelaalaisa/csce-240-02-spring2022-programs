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
		String county;
		String colaAddress;
		String homeAddress;
		String busiColaPhone;
		String busiHomePhone;
		String homePhone;
		List<String> serviceInfo = new ArrayList<String>();
		List<String> personalInfo = new ArrayList<String>();		//a list to hold personal info since the number of info is not known	
		String committeeAssign = "Has not been assigned to any committees yet";
		String sponsoredBills = "Sponsored bills are found in a separate link. Data retrieval is still in progress";
		String votingRecord = "Voting records are found in a separate link. Data retrieval is still in progress";


	public static void main(String[] args) {			//removed static
		
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
		Pattern countyP = Pattern.compile("([A-Za-z]+)(?=(\\sCounty))");
		Pattern addressP = Pattern.compile("(?<=\\>)(\\d+[a-zA-Z\\s.]+)(\\<br\\>)([A-Za-z\\s]+\\d+)");
		Pattern phoneP = Pattern.compile("\\(\\d\\d\\d\\)\\s\\d\\d\\d-\\d\\d\\d\\d");
		Pattern personalP = Pattern.compile("(<li style=\"margin: 5px 0 0 0; list-style-type:square;\" >)([a-zA-z0-9\\s\\\\.\\,\\-\\\"\\/\\']+)");
		Pattern serviceP = Pattern.compile("^(<li style=\\\"margin: 5px 0 0 0; list-style-type:square;\\\" >)([A-Za-z\\d\\s]+\\,\\s)(\\d\\d\\d\\d\\s\\-\\s[A-Za-z\\d]+)+(<\\/li><\\/ul>)");
		
		BufferedReader reader = new BufferedReader(new FileReader("document.txt"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("test_output.txt"));

		String line;
		while ((line = reader.readLine()) != null) {
			
			Matcher a = nameP.matcher(line);
			if(a.find()) {
				name = a.group();
			}
			
			Matcher b = countyP.matcher(line);
			if(b.find()) {
				county = b.group();
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
			
			//splitting line for personal info
			if(line.contains("<li style=\"margin: 5px 0 0 0; list-style-type:square;\" >")) {
				String[] infoSplit = line.split("</li>");
				for(int i = 0; i<infoSplit.length; i++) {
					Matcher e = personalP.matcher(infoSplit[i]);
					if(e.find()) {
						personalInfo.add(e.group(2));
					}
				}
			}
			
			Matcher f = serviceP.matcher(line);
			if(f.find()) {
				serviceInfo.add(f.group(2)+f.group(3));
			}
		}
		
		
		switch(type) {
		
		case "I1": //Contact Information
			//output info to console
			System.out.println("Name: " + name + "\n");
			System.out.println("County: " + county + "\n");
			System.out.println("Columbia Address: " + colaAddress + "\n");
			System.out.println("\tBusiness Phone: " + busiColaPhone + "\n");
			System.out.println("Home Address: " + homeAddress + "\n");
			System.out.println("\tBusiness Phone: " + busiHomePhone + "\n");
			System.out.println("\tHome Phone: " + homePhone + "\n");
			//write info to file
			writer.write("Contact Information (I1)\n");
			writer.write("Name: " + name + "\n");
			writer.write("County: " + county + "\n");
			writer.write("Columbia Address: " + colaAddress + "\n");
			writer.write("\tBusiness Phone: " + busiColaPhone + "\n");
			writer.write("Home Address: " + homeAddress + "\n");
			writer.write("\tBusiness Phone: " + busiHomePhone + "\n");
			writer.write("\tHome Phone: " + homePhone + "\n");	
			System.out.println("Data extracted to output file\n");
			
			break;
			
		case "I2": //Personal Information

			System.out.println("Personal Information: " + personalInfo + "\n");
			writer.write("Personal Info (I2): \n" + personalInfo + "\n");
			System.out.println("Data extracted to output file\n");
			
			break;
			
		case "I3": //Committee Assignments
			
			System.out.println("Committee Assignments: " + committeeAssign + "\n");
			writer.write("Committee Assignments (I3): \n" + committeeAssign + "\n");
			System.out.println("Data extracted to output file\n");
			
			break;
			
		case "I4": //Sponsored Bills
			
			System.out.println("Sponsored Bills: " + sponsoredBills + "\n");
			writer.write("Sponsored Bills (I4): \n" + sponsoredBills + "\n");
			System.out.println("Data extracted to output file\n");
			
			break;
			
		case "I5": //Voting Record

			System.out.println("Voting Records: " + votingRecord + "\n");
			writer.write("Voting Records (I5): \n" + votingRecord + "\n");
			System.out.println("Data extracted to output file\n");
			
			break;
			
		case "I6": //Service in Public Office"
			
			System.out.println("Serivce in Public Office: " + serviceInfo + "\n");
			writer.write("Service in Public Office (I6): \n" + serviceInfo + "\n");
			System.out.println("Data extracted to output file\n");
			
			break;
		
		default:
			
			System.out.println("Invalid information type\n");
			
			break;
			
		}
		
		reader.close();
		writer.close();
		
	}
	
	
		
}

