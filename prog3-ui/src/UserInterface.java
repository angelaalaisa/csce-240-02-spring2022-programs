/*
 * Written by Angela Alaisa
 */

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//does not extract information directly from the html file yet, data is input manually but I am working on integrating it into my code

public class UserInterface {

	public static void main(String[] args) {
		
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
				answerQuery(query);
			}
		}
		
		
	}
	
	public static void answerQuery(String query) {
		
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
		Pattern personalP = Pattern.compile("(about(\\sthe\\s|[a-z\\\\s])(representative|rep))|(personal)");
		
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
			output = "J. Todd Rutherford";
		
		Matcher b = partyP.matcher(line);
		if(b.find())
			output = "Democrat";
		
		Matcher c = districtNumP.matcher(line);
		if(c.find())
			output = "District 74";
		
		Matcher d = countyP.matcher(line);
		if(d.find())
			output = "Richland County";
		
		Matcher e = homeAddP.matcher(line);
		if(e.find())
			output = "2113 Park Street, Columbia 29201";
		
		Matcher f = workAddP.matcher(line);
		if(f.find())
			output = "335B Blatt Bldg., Columbia 29201";
		
		Matcher g = anyAddP.matcher(line);
		if(g.find())
			output = "Business Address: 335B Blatt Bldg., Columbia 29201\n"
					+ "Home Address: 2113 Park Street, Columbia 29201";
		
		Matcher h = contactP.matcher(line);
		if(h.find())
			output = "Columbia Address:\nBusiness Phone (803) 734-9441\n"
					+ "Home Address:\nHome Phone (803) 799-8633\nBusiness Phone (803) 256-3003";
		
		Matcher i = personalP.matcher(line);
		if(i.find())
			output = "Attorney\n"
					+ "Residing at 2321 Lincoln St., Columbia\n"
					+ "Born October 10, 1970 in Columbia\n"
					+ "Son of Harry and Karen Rutherford\n"
					+ "Howard University, B.A., 1992\t\n"
					+ "University of South Carolina, J.D., 1996\n"
					+ "2 children, J. Todd II \"Ty\" and Tripp Benjamin\n"
					+ "Legislative Assistant, Congressman Robin Tallon, 1992-93\n"
					+ "Owner, 3 Tech Integrated Systems, 1993\n"
					+ "Assistant Solicitor/Special Prosecutor of Narcotic and Drug Cases, Fifth Solicitor's Office, 1996-98\n"
					+ "The Rutherford Law Firm, LLC, 1998-11\n"
					+ "Owner, 803 Trucking, LLC\n"
					+ "WasteSouth, LLC\n"
					+ "T.I.S. Consulting and Auto\n"
					+ "National Board member, NAACP Special Contributions Fund\n"
					+ "Board Member, Brookland Baptist Church Community Credit Union, 1999\n"
					+ "Member, South Carolina Bar Association";
		
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
		if(m.find())
			output = "House of Representatives, 1999 - Present";
		
		Matcher n = everythingP.matcher(line);
		if(n.find())
			output = "Name: J. Todd Rutherford\\n"
					+ "Party: Democrat\n"
					+ "District: District 74\n"
					+ "County: Richland County\n"
					+ "Business Address: 335B Blatt Bldg., Columbia 29201\n"
					+ "Home Address: 2113 Park Street, Columbia 29201\n"
					+ "Personal Info:\n"
					+ "\tAttorney\n"
					+ "\tResiding at 2321 Lincoln St., Columbia\n"
					+ "\tBorn October 10, 1970 in Columbia\n"
					+ "\tSon of Harry and Karen Rutherford\n"
					+ "\tHoward University, B.A., 1992\n"
					+ "\tUniversity of South Carolina, J.D., 1996\n"
					+ "\t2 children, J. Todd II \"Ty\" and Tripp Benjamin\n"
					+ "\tLegislative Assistant, Congressman Robin Tallon, 1992-93\n"
					+ "\tOwner, 3 Tech Integrated Systems, 1993\n"
					+ "\tAssistant Solicitor/Special Prosecutor of Narcotic and Drug Cases, Fifth Solicitor's Office, 1996-98\n"
					+ "\tThe Rutherford Law Firm, LLC, 1998-11\n"
					+ "\tOwner, 803 Trucking, LLC\n"
					+ "\tWasteSouth, LLC\n"
					+ "\tT.I.S. Consulting and Auto\n"
					+ "\tNational Board member, NAACP Special Contributions Fund\n"
					+ "\tBoard Member, Brookland Baptist Church Community Credit Union, 1999\n"
					+ "\tMember, South Carolina Bar Association\n"
					+ "Committee Assignments: no past or current assignments"
					+ "Sponsored bills: in another webpage section, currently unavailable"
					+ "Voting Record: in another webpage section, currently unavailable"
					+ "Service in Public Office: House of Representatives, 1999 - Present";
		
		if(output.equals("none")) {
			System.out.println(query + " - I do not know this information\n"
					+ "Please enter in another query\n");
			output = "";
		}
		else
			System.out.println("\n"+output+"\n");
		
	}

}
