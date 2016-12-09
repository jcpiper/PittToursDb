/*
* Administrator interface for pittTours
*/
import java.io.*;
import java.util.ArrayList;
import java.sql.*;

public class pittToursCustomer {

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
		
		System.out.println("Welcome! Please select an option to proceed.");
		System.out.println("1.  Add customer.");
		System.out.println("2.  Show customer information.");
		System.out.println("3.  Find price for flights between two cities.");
		System.out.println("4.  Find all routes between two cities.");
		System.out.println("5.  Find all routes between two cities of a given airline.");
		System.out.println("6:  Find all routes with available seats between two cities on a given day.");
    System.out.println("7.  Find all routes with available seats between two cities on a given day on a specific airline.");
    System.out.println("8.  Add a reservation.");
    System.out.println("9.  Show reservation info by reservation number.");
    System.out.println("10. Buy ticket for existing reservation.");

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\n\nPlease enter the option number of your desired action. i.e. to add customer, enter \"1\"");
		int option = Integer.valueOf(input.readLine());
		switch (option) {
			case 1:
				System.out.println("You have chosen to add a customer.");
				addCustomer(conn);
				break;
			case 2:
				System.out.println("You have chosen to show customer information.");
				showCustInfo(conn);
				break;
			case 3:
				System.out.println("You have chosen to find price for flights between two cities.");
				findPrice(conn);
				break;
			case 4:
				System.out.println("You have chosen to find all routes between two cities.");
				findRoutes(conn);
				break;
			case 5:
				System.out.println("You have chosen to find all routes between two cities of a given airline");
				findRoutesByAirline(conn);
				break;
			case 6:
				System.out.println("You have chosen to find all routes with available seats between two cities on a given day.");
				findRoutesOnDay(conn);
				break;
			case 7:
				System.out.println("You have chosen to find all routes with available seats between two cities on a given day on a specific airline.");
				findRoutesOnDayByAirline(conn);
				break;
			case 8:
				System.out.println("You have chosen to add a reservation.");
				addReservation(conn);
				break;
			case 9:
				System.out.println("You have chosen to show reservation info by reservation number.");
				showResInfo(conn);
				break;
			case 10:
				System.out.println("You have chosen to buy a ticket for an existing reservation.");
				buyTicket(conn);
				break;
			default:
				System.out.println("Invalid option, choose again");
				String[] params = args;
				main(params);
				break;
		}
	}

  /* 1. addCustomer */

	public static void addCustomer(Connection conn) throws IOException {

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please input the following customer fields.");
    System.out.println("Title (Mr, Mrs, Ms):");
    String title = input.readLine();

    System.out.println("First Name:");
    String firstName = input.readLine();

    System.out.println("Last Name:");
    String lastName = input.readLine();
		
		String query = "select * from customer where first_name = \'" + firstName + "\' and last_name = \'" + lastName + "\'";
		int rowCount = 0;
		try {
			PreparedStatement srch = conn.prepareStatement(query);	
			ResultSet rs = srch.executeQuery();
			if(rs.next()){
				rowCount = rs.getRow();
			}
		} catch (SQLException e) {
			System.out.println("ERROR! Query failed, see stack trace");
			e.printStackTrace();
			return;
		}
		if (rowCount > 0) {
			System.out.println("\n\nERROR. User with name " + firstName + " " + lastName + " already exists!\n\n");
			// main(new String[1]);
			return;
		}

    System.out.println("Street:");
    String street = input.readLine();

    System.out.println("City:");
    String city = input.readLine();

    System.out.println("State:");
    String state = input.readLine();

    System.out.println("Phone Number:");
    String phoneNum = input.readLine();

    System.out.println("Email:");
    String email = input.readLine();

    System.out.println("Credit Card Number");
    String ccNum = input.readLine();

    System.out.println("Credit Card Expiration Date (YYYY MM DD)");
    String ccExp = input.readLine();

    // input.close();
		
		//generate cid
		query = "select max(cid) from customer";
		int newCid = 0;
		try {
			PreparedStatement getCid = conn.prepareStatement(query);
			ResultSet lastCid = getCid.executeQuery();
			int maxCid;
			if (lastCid.next()) {
				maxCid = lastCid.getInt(1);
				newCid = maxCid + 1;
			}
			else System.out.println("\n\nERROR! CID was not generated.");
			getCid.close();
		} catch (SQLException e) {
			System.out.println("ERROR. QUERY FAILED");
			e.printStackTrace();
			return;
		}
		
    // Concatenate strings to input, call SQL/PL
		String vals = "\'" + newCid + "\', \'" + title + "\', \'" + firstName + "\', \'" + lastName + "\', \'" + ccNum + "\', \'" + ccExp + "\', \'" + street + "\', \'" + city +
									"\', \'" + state + "\', \'" + phoneNum + "\', \'" + email + "\'";
		
		query = "insert into customer (cid, salutation, first_name, last_name, credit_card_num, credit_card_expire, street, city, state, phone, email) values (" +
							vals + ")";
		
		try {
				PreparedStatement insrt = conn.prepareStatement(query);
				
				int rows = insrt.executeUpdate();
				System.out.println(rows + " rows updated.");
				System.out.println("Your PittRewards number is " + newCid + ". Keep this for your records.");
				insrt.close();
			} catch (SQLException e) {
				System.out.println("Update Failed!");
				e.printStackTrace();
			}
	}

  /* 2. showCustInfo */

	public static void showCustInfo(Connection conn) throws IOException {
  	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("\nPlease input the customer's name.");
    System.out.println("\nFirst Name:");
    String firstName = input.readLine();

    System.out.println("\nLast Name:");
    String lastName = input.readLine();

    // input.close();

    // Call SQL/PL, print customer info for customer with that name
		String query = "select * from customer where first_name = \'" + firstName + "\' and last_name = \'" + lastName + "\'";
		
		try {
			PreparedStatement srch = conn.prepareStatement(query);
			
			ResultSet rs = srch.executeQuery();
			if(rs.next()) {
				System.out.println("Name\tPittRewards Number\tCredit Card Number\tCard Expiration Date\tAddress\tPhone Number\tEmail\tFrequent Flyer Status");
				System.out.println("---------------------------------------------------------------");
				
				System.out.println("Name: " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
				System.out.println("PittRewards Number: " + rs.getInt(1));
				System.out.println("Credit Card: " + rs.getString(5));
				System.out.println("Card Expiration Date: " + rs.getString(6));
				System.out.println("Address: " + rs.getString(7) + " " + rs.getString(8) + ", " + rs.getString(9));
				System.out.println("Phone: " + rs.getString(10));
				System.out.println("Email: " + rs.getString(11));
				System.out.println("Frequent Flier Status: " + rs.getString(12));
			}
			else {
				System.out.println("\nCustomer not found!");
				showCustInfo(conn);
			}
			srch.close();
		} catch (SQLException e) {
			System.out.println("Update Failed!");
			e.printStackTrace();
			return;
		}
	}

  /* 3. findPrice */

	public static void findPrice(Connection conn) throws IOException {
  	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the first city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			System.exit(1);
		}
		System.out.println("First City: " + depCity);

		System.out.println("\nPlease enter the second city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			System.exit(1);
		}
		System.out.println("Second City: " + arrCity);

    // input.close();

		String query = "select high_price, low_price from price " +
									"where departure_city = \'" + depCity + "\' and arrival_city = \'" + arrCity + "\'";
		int hiPrice = 0, loPrice = 0;
		
		try {
			PreparedStatement srch = conn.prepareStatement(query);
			
			ResultSet rs = srch.executeQuery();
			if(rs.next()) {
				System.out.println("\n1-way flight from " + depCity + " to " + arrCity);
				System.out.println("High Price\tLow Price");
				System.out.println("----------------------------------");
				hiPrice = rs.getInt(1);
				loPrice = rs.getInt(2);
				System.out.println("\n" + hiPrice + "\t" + loPrice);
			}
			else {
				System.out.println("\nPrice data not found!");
				showCustInfo(conn);
			}
			srch.close();
		} catch (SQLException e) {
			System.out.println("Query Failed!");
			e.printStackTrace();
			return;
		}
		
		query = "select high_price, low_price from price " +
					"where departure_city = \'" + arrCity + "\' and arrival_city = \'" + depCity + "\'";
					
		try {
			PreparedStatement srch2 = conn.prepareStatement(query);
			
			ResultSet rs = srch2.executeQuery();
			if(rs.next()) {
				System.out.println("\n1-way flight from " + arrCity + " to " + depCity);
				System.out.println("High Price\tLow Price");
				System.out.println("----------------------------------");
				
				System.out.println("\n" + rs.getInt(1) + "\t" + rs.getInt(2));
				
				//round trip pricing
				System.out.println("\nRound trip pricing");
				System.out.println("High Price\tLow Price");
				System.out.println("----------------------------------");
				
				hiPrice += rs.getInt(1);
				loPrice += rs.getInt(2);
				
				System.out.println("\n" + hiPrice + "\t" + loPrice);
			}
			else {
				System.out.println("\nPrice data not found!");
				showCustInfo(conn);
			}
			srch2.close();
		} catch (SQLException e) {
			System.out.println("Query Failed!");
			e.printStackTrace();
			return;
		}
    // Call SQL/PL, print prices for one way each way & round trip
	}

  /* 4. findRoutes */

	public static void findRoutes(Connection conn) throws IOException {
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

    input.close();

    // Call SQL/PL, print all possible one way routes
		// print flight number, departure, city, departure time, and arrival time
		String directRoutesQuery = "select flight_number, departure_city, departure_time, arrival_time " +
										"from flight " +
										"where departure_city = \'" + depCity + "\' and arrival_city = \'" + arrCity + "\';
		
		// connections query
		// needs work!
		String connectionsQuery = "Select f.flight_number, f.departure_city, f.departure_time, f.arrival_time, s.flight_number, s.departure_city, s.departure_time, s.arrival_time " +
															"from flight f join flight s on f.arrival_city = s.departure_city " +
															"where f.departure_city = \'" + depCity + "\' and s.arrival_city = \'" + arrCity + 
															"\' and ( (f.flight_number in ...";
		// execute both queries and print results
		// schedule comparison could use improvement to get more results
	}

  /* 5. findRoutesByAirline */

	public static void findRoutesByAirline(Connection conn) throws IOException {
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

		System.out.println("\nPlease enter the preferred airline.");
		System.out.println("Waiting for input...");
		String airline = input.readLine();

		System.out.println("Airline: " + airline);

		input.close();

    // SQL/PL Find routes by airline
	}

  /* 6. findRoutesOnDay */

	public static void findRoutesOnDay(Connection conn) throws IOException {
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

		System.out.println("\nPlease enter the preferred date in the format (YYYY MM DD).");
		System.out.println("Waiting for input...");
		String date = input.readLine();

		System.out.println("Date: " + date);

		input.close();

    // SQL/PL Find Routes on date
	}

  /* 7. findRoutesOnDayByAirline */

	public static void findRoutesOnDayByAirline(Connection conn) throws IOException {
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

		System.out.println("\nPlease enter the preferred airline.");
		System.out.println("Waiting for input...");
		String airline = input.readLine();

		System.out.println("Airline: " + airline);

		System.out.println("\nPlease enter the preferred date in the format (YYYY MM DD).");
		System.out.println("Waiting for input...");
		String date = input.readLine();

		System.out.println("Date: " + date);

		input.close();

    // SQL/PL Find Routes on date
	}

  /* 8. addReservation() */

	public static void addReservation(Connection conn) throws IOException {

    // Keep track of flightnums and depdates in arraylists
    ArrayList<String> flightNumbers = new ArrayList<String>();
    ArrayList<String> depDates = new ArrayList<String>();

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    String inputString = new String();

    // Maximum of four legs on a trip.
    while(flightNumbers.size() < 4) {
      System.out.println("\nPlease input a flight number.");
      System.out.println("Flight Number:");
      String flightNum = input.readLine();

      if(flightNum.equals("0")) break;

  		System.out.println("\nPlease enter the departure date in the format (YYYY MM DD).");
  		System.out.println("Waiting for input...");
  		String date = input.readLine();

      flightNumbers.add(flightNum);
      depDates.add(date);
    }

    input.close();

    // Verify legs, seats, date, generate res # and confirmation/error message.

	}

  /* 9. showResInfo */

  	public static void showResInfo(Connection conn) throws IOException {
    	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\nPlease input your reservation number.");
      System.out.println("Reservation Number:");
      String resNum = input.readLine();

      input.close();

      // Call SQL/PL, print all flights for this reservation number, print error otherwise
  	}


  /* 10. buyTicket */

  	public static void buyTicket(Connection conn) throws IOException {
    	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\nPlease input your reservation number.");
      System.out.println("Reservation Number:");
      String resNum = input.readLine();

      input.close();

      // Call SQL/PL, see if ticket purchased, if not, purchase ticket
  	}
}
