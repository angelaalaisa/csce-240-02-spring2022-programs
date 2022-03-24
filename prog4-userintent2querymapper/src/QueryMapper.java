/*
 * Written by Angela Alaisa
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryMapper {
	
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
			
	// new variables
			String party;
			String districtNum;
			

	public static void main(String[] args) {
		
		QueryMapper info = new QueryMapper();

		Scanner keyboard = new Scanner(System.in);
		String exitProg = "no";
				
		System.out.println("Welcome to the District 74 chatbot!\n(still under development)\n");
		
		while(exitProg != "yes") {
			
			System.out.println("Enter query below\n(type \"Quit\", \"quit\", or \"q\" to exit the program.)");
			
			String query = keyboard.nextLine();
			
			if(query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("q")) {
				System.out.println("Program will be terminated. Goodbye");
				exitProg = "yes";
			}
			else if(query != "Quit"){
				
				try {
					info.readFileContents();
				} catch (Exception e) {
					e.printStackTrace();
				}
				info.answerQuery(query);
			}
			
		}
		
		
	}
	
	public void readFileContents() throws Exception{
		
		int addressCount = 0;
		int phoneCount = 0;
		
		//Regex patterns for types of info
		Pattern nameP = Pattern.compile("(?<=Representative\\s)([A-Za-z.]+\\s[A-Za-z.]+\\s[A-Za-z.]+\\s)");
		Pattern countyP = Pattern.compile("([A-Za-z]+)(?=(\\sCounty))");
		Pattern addressP = Pattern.compile("(?<=\\>)(\\d+[a-zA-Z\\s.]+)(\\<br\\>)([A-Za-z\\s]+\\d+)");
		Pattern phoneP = Pattern.compile("\\(\\d\\d\\d\\)\\s\\d\\d\\d-\\d\\d\\d\\d");
		Pattern personalP = Pattern.compile("(<li style=\"margin: 5px 0 0 0; list-style-type:square;\" >)([a-zA-z0-9\\s\\\\.\\,\\-\\\"\\/\\']+)");
		Pattern serviceP = Pattern.compile("^(<li style=\\\"margin: 5px 0 0 0; list-style-type:square;\\\" >)([A-Za-z\\d\\s]+\\,\\s)(\\d\\d\\d\\d\\s\\-\\s[A-Za-z\\d]+)+(<\\/li><\\/ul>)");
		
		//new patterns
		Pattern partyP = Pattern.compile("<p style=\"font-size: 17px; margin: 0 0 0 0; padding: 0;\">([A-Za-z]+)\\s-\\s[A-Za-z]+<\\/p>");
		Pattern districtNumP = Pattern.compile("<p style=\"font-size: 17px; margin: 0 0 0 0; padding: 0;\">District\\s(\\d\\d)");
		
		BufferedReader reader = new BufferedReader(new FileReader("data/document.txt"));

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
			
			Matcher g = partyP.matcher(line);
			if(g.find()) {
				party = g.group(1);								//edit these two later to only include matching group
			}
			
			Matcher h = districtNumP.matcher(line);
			if(h.find()) {
				districtNum = h.group(1);
			}
		}
	}
	
	
	public void answerQuery(String query) {
		
		String line = query.toLowerCase();
		String output = "none";
		
		
		
								//regex for information
		//Contact Info (I1)
		Pattern nameP = Pattern.compile("(who|name)");
		Pattern partyP = Pattern.compile("party");
		Pattern districtNumP = Pattern.compile("district");
		Pattern countyP = Pattern.compile("(county|region)");
		Pattern homeAddP = Pattern.compile("(live|home|house)");
		Pattern workAddP = Pattern.compile("(office|work|business[a-z\\s]address)");
		Pattern anyAddP = Pattern.compile("address");
		Pattern contactP = Pattern.compile("(number|contact)");
		
		//Personal Info (I2)
		Pattern personalP = Pattern.compile("about(.)+(rep|representative)|(personal)");
		
		//Committee Assignments (I3)
		Pattern committeeP = Pattern.compile("(committee|assign)");
		
		//Sponsored Bills (I4)
		Pattern billsP = Pattern.compile("(bills|sponsor)");
		
		//Voting Record (I5)
		Pattern votesP = Pattern.compile("vot");
		
		//Service in Public Office (I6)
		Pattern serviceP = Pattern.compile("(public office|serv)");
		
		//Everything
		Pattern everythingP = Pattern.compile("(everything|(all[a-z\\s]info))");
		
		//begin searching query for keywords		
		Matcher a = nameP.matcher(line);
		if(a.find())
			output = name;			//change all output to get data from file
		
		Matcher b = partyP.matcher(line);
		if(b.find())
			output = party;
		
		Matcher c = districtNumP.matcher(line);
		if(c.find())
			output = districtNum;
		
		Matcher d = countyP.matcher(line);
		if(d.find())
			output = county;
		
		Matcher e = homeAddP.matcher(line);
		if(e.find())
			output = homeAddress;
		
		Matcher f = workAddP.matcher(line);
		if(f.find())
			output = colaAddress;
		
		Matcher g = anyAddP.matcher(line);
		if(g.find())
			output = "Business Address: " + colaAddress
					+ "Home Address: " + homeAddress;
		
		Matcher h = contactP.matcher(line);
		if(h.find())
			output = "Home Address: \n" + homeAddress
					+ "\n\tHome Phone: " + homePhone
					+ "\n\tBusiness Phone: " + busiHomePhone
					+ "\nColumbia Address:\n" + colaAddress
					+ "\n\tBusiness Phone: " + busiColaPhone;
		
		Matcher i = personalP.matcher(line);
		if(i.find()) {
			for(int z = 0; z<personalInfo.size(); z++) {
				System.out.println("\t" + personalInfo.get(z) + "\n");
			}
			output = "contents have been printed";
			//use a loop to print contents of the array list
		}
		
		Matcher j = committeeP.matcher(line);
		if(j.find())
			output = "has not been assigned to any committees yet";
		
		Matcher k = billsP.matcher(line);
		if(k.find())
			output = "Sponsored bills are found in a separate link. Data retrieval is still in progress";
		
		Matcher l = votesP.matcher(line);
		if(l.find())
			output = "Voting records are found in a separate link. Data retrieval is still in progress";
		
		Matcher m = serviceP.matcher(line);
		if(m.find()) {
			for(int z = 0; z<serviceInfo.size(); z++) {
				System.out.println(serviceInfo.get(z) + "\n");
			}
			output = "contents have been printed";
			//use a loop to print array list contents
		}
		
		Matcher n = everythingP.matcher(line);
		if(n.find())
			output = "Name: " + name
					+ "\nParty: " + party
					+ "\nDistrict: " + districtNum
					+ "\nCounty: " + county
					+ "\nBusiness Address: " + colaAddress
					+ "\nHome Address: " + homeAddress
					+ "\nPersonal Info:\n" //print personal info list
					+ "\nCommittee Assignments: no past or current assignments"
					+ "\nSponsored bills: in another webpage section, currently unavailable"
					+ "\nVoting Record: in another webpage section, currently unavailable"
					+ "\nService in Public Office: House of Representatives, 1999 - Present";
		
		if(output.equals("none")) {
			System.out.println(query + " - I do not know this information\n"
					+ "Please rephrase query\n");
		}
		else
			System.out.println("\n"+output+"\n");
		
	}

}
