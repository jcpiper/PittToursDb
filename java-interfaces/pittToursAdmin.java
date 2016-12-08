/*
* Administrator interface for pittTours
*/
import java.io.*;
import java.sql.*;

public class pittToursAdmin {
	
	public static void main (String[] args) throws IOException {
		
		// set up db connection
		Connection conn = null;
		try {
			// register oracle driver
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
			String uname = "jcp68";
			String passwrd = "3788990";
			conn = DriverManager.getConnection(url, uname, passwrd);
			
		} catch (SQLException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		// Statement stmt = null;
		// String query = "Select * from flight";
		
		// try {
			// stmt = conn.createStatement();
			// ResultSet rs = stmt.executeQuery(query);
			
			// System.out.println("Query made!");
			
			// stmt.close();
		// } catch (SQLException e) {
			// System.out.println("Query failed!");
			// e.printStackTrace();
			// return;
		// } 
		
		System.out.println("Welcome! Please select an option to proceed.");
		System.out.println("1. Erase database.");
		System.out.println("2. Load airline info.");
		System.out.println("3. Load schedule information.");
		System.out.println("4. Load pricing information.");
		System.out.println("5. Load plane information.");
		System.out.println("6: Generate passenger manifest for specific flight on given day.");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\n\nPlease enter the option number of your desired action. i.e. to erase the database, enter \"1\"");
		String option = input.readLine();
	
		if (option.equals("1")) {
			System.out.println("You have chosen to erase the database.");
			eraseDb(conn);
		}
			
		else if (option.equals("2")) {
			System.out.println("You have chosen to load airline info.");
			loadAirline(conn);
		}
		else if (option.equals("3")) {
			System.out.println("You have chosen to load schedule info.");
			loadSchedule(conn);
		}
		else if (option.equals("4")) {
			System.out.println("You have chosen to load pricing info.");
			loadPricing(conn);
		}
		else if (option.equals("5")) {
			System.out.println("You have chosen to load plane info.");
			loadPlane(conn);
		}
		else if (option.equals("6")) {
			System.out.println("You have chosen to generate passenger manifest for specific flight on given day.");
			generatePassengerList(conn);
		}
		else {
			System.out.println("Invalid option, choose again");
			String[] params = args;
			main(params);
		}
	}
	
	public static void eraseDb(Connection conn) throws IOException {
		System.out.println("Are you sure you want to erase the database?\ny/n");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String confirm = input.readLine().toLowerCase();
		
		if (confirm.equals("y")) {
			try {
				System.out.println("Erasing the database.");
				//call eraseDb procedure from pl/sql
				// Statement stmt = conn.createStatement();
				String eraseData = "{call eraseDb}";
				CallableStatement stmt = conn.prepareCall(eraseData);
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Call to procedure failed!");
				e.printStackTrace();
				return;
			}
		}
		else {
			System.out.println("Action aborted, returning to main menu\n");
			String[] args = new String[1];
			main(args);
		}
	}
	public static void loadAirline(Connection conn) throws IOException {
		System.out.println("\n\nPreparing to load airline information...");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the file name where the airline info is stored.");
		File file = new File(input.readLine());
		
		// open file to check for valid format
		BufferedReader csv = new BufferedReader(new FileReader(file));
		int lineNum = 0;
		while (csv.ready()) {
			lineNum++;
			if (csv.readLine().split(",").length != 5) {
				System.out.println("\n\nERROR. File is not properly formatted. Error at line " + lineNum + ". Expecting a csv file with 5 entries per line.\n\n**EXAMPLE**:\t001,United Airlines,UAL,Chicago,1931");
				csv.close();
				System.exit(1); // exit program
			}
		}
		csv.close(); 
		// reopen file for reading
		csv = new BufferedReader(new FileReader(file));
		
		while (csv.ready()){
			String line = csv.readLine();
			String[] vals = line.split(",");
			String com = ", ";
			// expecting array like ["001", "United Airlines", "UAL", "Chicago", "1931"]
			line = vals[0].concat(com).concat(vals[1]).concat(com).concat(vals[2]).concat(com).concat(vals[4]);
			System.out.println("params: " + line);
			// replace previous line with call to loadPlane procedure in pl/sql with line string as params;
		}
		input.close();
		csv.close();
	}
	
	public static void loadSchedule(Connection conn) throws IOException {
		System.out.println("\n\nPreparing to load schedule information...");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the file name where the schedule info is stored.\n");
		File file = new File(input.readLine());
		
		// open file to check format of file
		BufferedReader csv = new BufferedReader(new FileReader(file));
		int lineNum = 0;
		while (csv.ready()) {
			lineNum++;
			if (csv.readLine().split(",").length != 8) {
				System.out.println("\n\nERROR! File is not properly formatted. Error at line " + lineNum + ". Expecting a csv file with 8 entries per line.\n\n**EXAMPLE**:\t153,001,A320,PIT,JFK,1000,1120,SMTWTFS");
				csv.close();
				System.exit(1); // exit the program
			}
		}
		csv.close();
		// reopen file for reading
		csv = new BufferedReader(new FileReader(file));
		while (csv.ready()) {
			String line = csv.readLine();
			line = line.replaceAll(",", ", ");
			System.out.println("Params: " + line);
			// delete print statement and call loadSchedule procedure
		}		
	}
	
	public static void loadPricing(Connection conn) throws IOException {
		System.out.println("Preparing to load pricing information.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nWould you like to LOAD PRICING INFORMATION or CHANGE THE PRICE OF AN EXISTING FLIGHT?");
		System.out.println("Enter \"L\" to load pricing info");
		System.out.println("Enter \"C\" to change the price of a flight");
		String choice = input.readLine().toUpperCase();
		if (choice.equals("L")) {
			// input.close();
			loadPriceData(conn);
		}
		else if(choice.equals("C")) {
			// input.close();
			changePriceData(conn);
		}
		else {
			System.out.println("ERROR: Invalid input! Options are \"L\" or \"C\"!");
			loadPricing(conn);
		}			
	}
	public static void changePriceData(Connection conn) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the departure city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			System.exit(1);
		}
		System.out.println("Departure City: " + depCity);
		
		System.out.println("\nPlease enter the destination city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			System.exit(1);
		}
		System.out.println("Destination City: " + arrCity);
		
		System.out.println("\nPlease eneter the new high price for the flight.");
		System.out.println("Waiting for input...");
		int hiPrice = Integer.valueOf(input.readLine());
		
		System.out.println("High Price: $" + hiPrice);
		
		System.out.println("\nPlease eneter the new low price for the flight.");
		System.out.println("Waiting for input...");
		int loPrice = Integer.valueOf(input.readLine());
		
		System.out.println("Low Price: $" + loPrice);
		input.close();
		// call changePrice with params depCity, arrCity, hiPrice, loPrice
	}
	public static void loadPriceData(Connection conn) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the file name where the pricing info is stored.");
		System.out.println("Waiting for input...");
		File file = new File(input.readLine());
		
		// open file to check format of file
		BufferedReader csv = new BufferedReader(new FileReader(file));
		int lineNum = 0;
		while (csv.ready()) {
			lineNum++;
			if (csv.readLine().split(",").length != 5) {
				System.out.println("\n\nERROR! File is not properly formatted. Error at line " + lineNum + ". Expecting a csv file with 5 entries per line.\n\n**EXAMPLE**:\tPIT,JFK,001,250,120");
				csv.close();
				System.exit(1); // exit the program
			}
		}
		csv.close();
		// reopen file for reading
		csv = new BufferedReader(new FileReader(file));
		while (csv.ready()) {
			String line = csv.readLine();
			line = line.replaceAll(",", ", ");
			System.out.println("Params: " + line);
			// delete print statement and call loadPricing procedure
		}
	}
	
	public static void loadPlane(Connection conn) throws IOException {
		System.out.println("Preparing to load plane information.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the file name where the plane info is stored.");
		File file = new File(input.readLine());
		
		// open file to check format of file
		BufferedReader csv = new BufferedReader(new FileReader(file));
		
		int lineNum = 0;
		while (csv.ready()) {
			lineNum++;
			if (csv.readLine().split(",").length != 5) {
				System.out.println("\n\nERROR! File is not properly formatted. Error at line " + lineNum + ". Expecting a csv file with 5 entries per line.\n\n**EXAMPLE**:\tB737,Boeing 125,09/09/2009,1996,001");
				csv.close();
				System.exit(1); // exit the program
			}
		}
		csv.close();
		// reopen file for reading
		csv = new BufferedReader(new FileReader(file));
		while (csv.ready()) {
			String line = csv.readLine();
			line = line.replaceAll(",", ", ");
			System.out.println("Params: " + line);
			// delete print statement and call loadPlane procedure
		}
	}
	
	public static void generatePassengerList(Connection conn) throws IOException {
		System.out.println("Preparing to generate passenger manifest.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the flight number of the desired flight.");
		String flightNum = input.readLine();
		System.out.println("Please enter the desired date.");
		String flightDate = input.readLine();
		// create and execute query on passengers_on_flight view (in admin-procedures.sql), then print results
		
		String query = "select salutation, first_name, last_name from passengers_on_flight where flight_number = " + flightNum + " and flight_date = " + flightDate;
		// execute query
	}
}
