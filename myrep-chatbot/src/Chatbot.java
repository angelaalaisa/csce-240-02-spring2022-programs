/*
 * Written by Angela Alaisa
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Chatbot {

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
		String party;
		String districtNum;
		String systemOutput;
		
		//statistics variables
		int userInputs;					//number of user utterances
		int systemResponses;				//number of system utterances
		long sessionTime;				//session duration
		int chatCount;
		List<String> queries = new ArrayList<String>();
		List<String> responses = new ArrayList<String>();
		
		
		public static void main(String[] args) throws Exception{
			
			Chatbot info = new Chatbot();
			
			//create log file
			Logger logger = Logger.getLogger("MyLog");  					
			FileHandler fh;  

			fh = new FileHandler("test/MyLogFile.log");  			
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
			
			
			//create file for chat contents
			Date date = new Date();											//get current date and convert to string
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = simpleDate.format(date);
			
			LocalTime time = LocalTime.now();								//get current time and convert to string
			DateTimeFormatter simpleTime = DateTimeFormatter.ofPattern("HH-mm-ss");
			String timeString = time.format(simpleTime);	
			
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("data/chat_statistics.csv"));
			writer2.write("chat_file,#user_utterance,#system_utterance,time taken\n");		//writer .csv file header

			
			Scanner keyboard = new Scanner(System.in);
			String exitProg = "no";
					
			System.out.println("Welcome to the District 74 chatbot!"
					+ "\nChat session numbers start at zero");
			
			
			long startTime = System.nanoTime();								//start timer			
			
			
			while(exitProg != "yes") {
				
				info.readFileContents();									//read file contents
				
				System.out.println("Enter query below\n(type \"Quit\", \"quit\", or \"q\" to exit the program.)");
				
				String query = keyboard.nextLine();
				
				//log input
				logger.info(query); 
				
				
				if(query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("q")) {
					
					BufferedWriter writer = new BufferedWriter(new FileWriter("data/chat_sessions/"+dateString+"_"+timeString+".txt"));
					
					System.out.println("Files created:\n"
							+ "\tchat_statistics.csv\n"
							+ "\t"+dateString+"_"+timeString+".txt\n"
							+ "\tMyLogFile.log");
					
					long endTime = System.nanoTime();												//end timer and close file writer
					info.sessionTime = TimeUnit.NANOSECONDS.toSeconds(endTime-startTime);			//calculate total session duration
					
					info.writeChatSessionFile(writer, info.queries, info.responses);
					writer.write("\nTotal session duration: "+info.sessionTime+" seconds");
					
					info.writeChatStatsFile(writer2, info.sessionTime, dateString, timeString, info.userInputs, info.systemResponses);
					
					writer.close();
					writer2.close();
					
					System.out.println("Program will be terminated. Goodbye");
					//exit program
					exitProg = "yes";
					
				}	
				else if(query.contains("showchat") || query.contains("summary")) {						//print all user queries and system responses
					
					File folder = new File("data/chat_sessions");								//get total chat count
					File[] chatFiles = folder.listFiles(); 										
					for(int i = 0; i<chatFiles.length; i++) {
						info.chatCount++;
					}
					
					if(query.equalsIgnoreCase("summary")) {
						info.getAllChatStats(chatFiles);
					}
					else {
						
						Pattern showchatSumNum = Pattern.compile("showchat.summary\\s(\\d+)");			//handles query for "showchat-summary <number>"
						Matcher sumNumMatcher = showchatSumNum.matcher(query);
						if(sumNumMatcher.find()) {
							info.getChatSummary(sumNumMatcher.group(1), chatFiles);
						}
						
						Pattern showchatNum = Pattern.compile("showchat.(\\d+)");
						Matcher numMatcher = showchatNum.matcher(query);
						if(numMatcher.find()) {
							info.getShowchatNum(numMatcher.group(1), chatFiles);
						}
						
						
					}
					
					
				}
				else {
					
					info.userInputs++;								//increase user utterance count by one
					
					info.answerQuery(query, info.queries, info.responses, logger);
					
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
					party = g.group(1);								
				}
				
				Matcher h = districtNumP.matcher(line);
				if(h.find()) {
					districtNum = h.group(1);
				}
			}
		}
		
		public void answerQuery (String query, List<String> aInput, List<String> aReponse, Logger aLog) throws Exception{
			
			String line = query.toLowerCase();
			String output = "none";
			
									//regex for information
			//Contact Info (I1)
			Pattern nameP = Pattern.compile("(who|name|rep|representative)");
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
				output = "District " + districtNum + ", " + county;
			
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
				
				StringBuilder sb = new StringBuilder();
				for(int y = 0; y<personalInfo.size(); y++) {
					sb.append("\t"+personalInfo.get(y)+"\n");
				}
				output = sb.toString();
			}
			
			Matcher j = committeeP.matcher(line);
			if(j.find())
				output = "has not been assigned to any committees yet";
			
			Matcher k = billsP.matcher(line);
			if(k.find())
				output = "Sponsored bills are found in a separate link.";
			
			Matcher l = votesP.matcher(line);
			if(l.find())
				output = "Voting records are found in a separate link.";
			
			Matcher m = serviceP.matcher(line);
			if(m.find()) {
				
				StringBuilder sb = new StringBuilder();
				for(int y = 0; y<serviceInfo.size(); y++) {
					sb.append(serviceInfo.get(y)+"\n");
				}
				output = sb.toString();
			}
			
			Matcher n = everythingP.matcher(line);
			if(n.find())
				output = "Name: " + name
						+ "\nParty: " + party
						+ "\nDistrict: " + districtNum
						+ "\nCounty: " + county
						+ "\nBusiness Address: " + colaAddress
						+ "\nHome Address: " + homeAddress
						+ "\nPersonal Info:\n" + personalInfo.toString()
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
			
			systemOutput = output;
			systemResponses++;
			
			//add output to log
			aLog.info(output);
			 
			//add queries and responses to list
			aInput.add(query);
			aReponse.add(output);
			
		}
		
		public void writeChatSessionFile(BufferedWriter aWriter, List<String> aInput, List<String> aResponse) throws Exception{
			
			for(int i = 0; i<aInput.size(); i++) {
					aWriter.write("User Query: \n\t"+aInput.get(i)+"\n");
					aWriter.write("System Response: \n\t"+aResponse.get(i)+"\n");
			}
			
		}
		
		public void writeChatStatsFile(BufferedWriter bWriter, long time, String aDate, String aTime, int userUt, int sysUt) throws Exception{ 
			
			String chatFileName = aDate+"_"+aTime+".txt";
			String userU = Integer.toString(userUt);
			String sysU = Integer.toString(sysUt);
			String timeS = Long.toString(time);
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(chatFileName);
			sb.append(',');
			sb.append(userU);
			sb.append(',');
			sb.append(sysU);
			sb.append(',');
			sb.append(timeS);
			sb.append('\n');
			
			bWriter.write(sb.toString());
			
		}
		
		public void getAllChatStats(File[] files) throws Exception{
			
			int qNum = 0, rNum = 0;
			double duration = 0.0; 
			
			for(int i = 0; i<files.length; i++) {
				
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				String line;
				
				while((line = br.readLine()) != null) {
					
					if(line.contains("User Query: ")) {
						qNum++;
					}
					if(line.contains("System Response:")) {
						rNum++;
					}
					if(line.contains("Total session duration:")) {
						Pattern dur = Pattern.compile("(\\d+)\\sseconds");
						Matcher aDur = dur.matcher(line);
						if(aDur.find()) {
							duration += Double.parseDouble(aDur.group(1));
						}
					}
					
				}
				
				br.close();
				
			}
			
			
			System.out.println("There are "+chatCount+" chats to date with user asking "+qNum+" times and "
					+ "system responding "+rNum+" times. The total duration is "+duration+" seconds");
			
		}
		
		public void getChatSummary(String aChatNum, File[] files) throws Exception{						
			
			int aChatNumber = Integer.parseInt(aChatNum);
			if(aChatNumber > chatCount) {
				System.out.println("There are only "+chatCount+" chat sessions. Please choose a valid number");
			}
			else {
				
				int qNum = 0;
				int rNum = 0;
				String duration = "none";
				BufferedReader br = new BufferedReader(new FileReader(files[aChatNumber]));
				String line;
				while((line = br.readLine()) != null) {
					
					if(line.contains("User Query: ")) {
						qNum++;
					}
					if(line.contains("System Response:")) {
						rNum++;
					}
					if(line.contains("Total session duration: ")) {
						duration = line;
					}
				}
				
				br.close();
				
				System.out.println("Chat "+aChatNumber+" has user asking "
						+ qNum +" times and system respond " + rNum + " times.\n"
								+ duration);
				
			}
			
		}
		
		public void getShowchatNum(String aChatNum, File[] files) throws Exception{
			
			int aChatNumber = Integer.parseInt(aChatNum);
			
			if(aChatNumber > chatCount) {
				System.out.println("There are only "+chatCount+" chat sessions. Please choose a valid number");
			}
			else {
				
				System.out.println("Chat "+aChatNum+" chat is: \n");
				
				BufferedReader br = new BufferedReader(new FileReader(files[aChatNumber]));
				String line;
				while((line = br.readLine()) != null) {
					System.out.println(line);
				}
				
				br.close();
			}
			
			
		}

}
