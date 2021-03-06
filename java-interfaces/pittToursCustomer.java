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
		
		System.out.println("\nTo exit program: E/EXIT");

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\n\nPlease enter the option number of your desired action. i.e. to add customer, enter \"1\"");
		String opt = input.readLine();
		if (opt.toUpperCase().equals("E") || opt.toUpperCase().equals("EXIT")) {
			System.exit(0);
		}
		int option = Integer.valueOf(opt);
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
		int rowsUpdated = createCustomer(title, firstName, lastName, ccNum, ccExp, street, city, state, phoneNum, email, conn);
		if (rowsUpdated != 1)
			System.exit(1);
		main(new String[1]);
	}
		
	public static int createCustomer(String title, String firstName, String lastName, String ccNum, String ccExp, String street, String city, String state, String phoneNum, String email, Connection conn) {
		
		String query = "select * from customer where first_name = \'" + firstName + "\' and last_name = \'" + lastName + "\'";
		int rowCount = 0;
		try {
			PreparedStatement srch = conn.prepareStatement(query);	
			ResultSet rs = srch.executeQuery();
			if(rs.next()){
				rowCount = rs.getRow();
			}
			rs.close();
			srch.close();
		} catch (SQLException e) {
			System.out.println("ERROR! Query failed, see stack trace");
			e.printStackTrace();
			return 0;
		}
		if (rowCount > 0) {
			System.out.println("\n\nERROR. User with name " + firstName + " " + lastName + " already exists!\n\n");
			// main(new String[1]);
			return 0;
		}
		//generate cid
		query = "select max(cast(cid as int)) from customer";
		int newCid = 0;
		try {
			PreparedStatement getCid = conn.prepareStatement(query);
			ResultSet lastCid = getCid.executeQuery();
			int maxCid;
			if (lastCid.next()) {
				maxCid = lastCid.getInt(1);
				newCid = maxCid + 1;
				System.out.println("New cid: " + newCid);
			}
			else System.out.println("\n\nERROR! CID was not generated.");
			lastCid.close();
			getCid.close();
		} catch (SQLException e) {
			System.out.println("ERROR. QUERY FAILED");
			e.printStackTrace();
			return 0;
		}
		
    // Concatenate strings to input, call SQL/PL
		String vals = "\'" + newCid + "\', \'" + title + "\', \'" + firstName + "\', \'" + lastName + "\', \'" + ccNum + "\', \'" + ccExp + "\', \'" + street + "\', \'" + city +
									"\', \'" + state + "\', \'" + phoneNum + "\', \'" + email + "\'";
		
		query = "insert into customer (cid, salutation, first_name, last_name, credit_card_num, credit_card_expire, street, city, state, phone, email) values (" +
							vals + ")";
		
		try {
				PreparedStatement insrt = conn.prepareStatement(query);
				
				int rows = insrt.executeUpdate();
				// System.out.println(rows + " rows updated.");
				// System.out.println("Your PittRewards number is " + newCid + ". Keep this for your records.");
				insrt.close();
				return rows;
			} catch (SQLException e) {
				System.out.println("Update Failed!");
				System.out.println("Issue with pk? cid = " + newCid);
				e.printStackTrace();
				return 0;
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
		
		getCustInfo(conn, firstName, lastName);
		
		main(new String[1]);
    // input.close();
	}
	
	public static void getCustInfo(Connection conn, String firstName, String lastName) {
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
				// showCustInfo(conn);
			}
			srch.close();
		} catch (SQLException e) {
			System.out.println("Update Failed!");
			e.printStackTrace();
			return;
		}
		return;
	}

  /* 3. findPrice */

	public static void findPrice(Connection conn) throws IOException {
  	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the first city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3 || depCity.length() < 2) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findPrice(conn);
		}
		System.out.println("First City: " + depCity);

		System.out.println("\nPlease enter the second city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3 || arrCity.length() < 2) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findPrice(conn);
		}
		System.out.println("Second City: " + arrCity);

    main(new String[1]);
	}
	
	public static void findPriceExe(Connection conn, String depCity, String arrCity) {
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
				return;
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
				return;
			}
			srch2.close();
		} catch (SQLException e) {
			System.out.println("Query Failed!");
			e.printStackTrace();
			return;
		}
		return;
    // Call SQL/PL, print prices for one way each way & round trip
	}

  /* 4. findRoutes */

	public static void findRoutes(Connection conn) throws IOException {
  	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the departure city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3 || depCity.length() < 2) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutes(conn);
		}
		System.out.println("Departure City: " + depCity);

		System.out.println("\nPlease enter the destination city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3 || arrCity.length() < 2) {
			input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutes(conn);
		}
		System.out.println("Destination City: " + arrCity);
		
		findRoutesQuery(conn, depCity, arrCity);
		main(new String[1]);
	}
	
	public static void findRoutesQuery(Connection conn, String depCity, String arrCity) {
    // Call SQL/PL, print all possible one way routes
		// print flight number, departure, city, departure time, and arrival time
		String directRoutesQuery = "select flight_number, departure_city, departure_time, arrival_time " +
										"from flight " +
										"where departure_city = \'" + depCity + "\' and arrival_city = \'" + arrCity + "\'";
		
		// connections query
		// Needs to incorporate f.arrival_time/s.departure_time comparison
		String connectionsQuery = "Select f.flight_number, f.departure_city, f.departure_time, f.weekly_schedule, s.arrival_time, s.weekly_schedule " +
															"from flight f join flight s on f.arrival_city = s.departure_city " +
															"where f.departure_city = \'" + depCity + "\' and s.arrival_city = \'" + arrCity + "\' and s.departure_time > (f.arrival_time + 100)";													
		// execute both queries and print results
		try {
			PreparedStatement query1 = conn.prepareStatement(directRoutesQuery);
			PreparedStatement query2 = conn.prepareStatement(connectionsQuery);
			ResultSet dirRoutes = query1.executeQuery();
			ResultSet conRoutes = query2.executeQuery();
			
			System.out.println("All Routes between " + depCity + " and " + arrCity);
			System.out.println("Flight Number\tDeparture Time\tArrival Time\tDeparture City\tArrival City");
			while(dirRoutes.next()) {
				System.out.println(dirRoutes.getString(1) + "\t" + dirRoutes.getString(3) + "\t" + dirRoutes.getString(4) + "\t" + depCity + "\t" + arrCity);
			}
			
			System.out.println("Printing all routes with 1 connection");
			
			while(conRoutes.next()) {
				// check the flights have at least 1 day in common on schedule
				String sched1 = conRoutes.getString(4);
				String sched2 = conRoutes.getString(6);
				boolean aligned = false;
				
				for (int i=0; i < 7; i++) {
					if (sched1.charAt(i) != '-'){
							aligned = (sched1.charAt(i) == sched2.charAt(i));
						if (aligned)
							break;
					}
				}
				if(aligned) {
					System.out.println(conRoutes.getString(1) + " " + conRoutes.getString(2) + " " + conRoutes.getString(3) + " " + 
						conRoutes.getString(5));
				}
				
			}
			query1.close();
			dirRoutes.close();
			query2.close();
			conRoutes.close();
		} catch(SQLException e) {
			System.out.println("Queries failed");
			e.printStackTrace();
			return;
		}
		return;
		// schedule comparison could use improvement to get more results
	}

  /* 5. findRoutesByAirline */

	public static void findRoutesByAirline(Connection conn) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the departure city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3 || depCity.length() < 2) {
			// input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutesByAirline(conn);
		}
		System.out.println("Departure City: " + depCity);

		System.out.println("\nPlease enter the destination city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3 || arrCity.length() < 2) {
			// input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutesByAirline(conn);
		}
		System.out.println("Destination City: " + arrCity);

		System.out.println("\nPlease enter the preferred airline.");
		System.out.println("Waiting for input...");
		String airline = input.readLine();

		System.out.println("Airline: " + airline);
		findRoutesByAirlineQuery(conn, depCity, arrCity, airline);
		main(new String[1]);
	}
	
	public static void findRoutesByAirlineQuery(Connection conn, String depCity, String arrCity, String airline) {
		// print flight number, departure, city, departure time, and arrival time
		String directRoutesQuery = "select flight_number, departure_city, departure_time, arrival_time " +
										"from flight f join airline a on f.airline_id = a.airline_id " +
										"where f.departure_city = \'" + depCity + "\' and f.arrival_city = \'" + arrCity + "\' and a.airline_name = \'" + airline + "\'";
		
		// connections query
		// needs work!
		String connectionsQuery = "Select f.flight_number, f.departure_city, f.arrival_city, f.departure_time, f.arrival_time, f.weekly_schedule, f.airline_id, s.flight_number, s.departure_city, s.arrival_city, s.departure_time, s.arrival_time, s.weekly_schedule, s.airline_id " +
															"from flight f join flight s on f.arrival_city = s.departure_city " +
															"where f.departure_city = \'" + depCity + "\' and s.arrival_city = \'" + arrCity + "\' and f.airline_id = s.airline_id and " + 
															"exists (select * from airline where airline.airline_name = \'" + airline + "\' and f.airline_id = airline.airline_id)";													
		// execute both queries and print results
		try {
			PreparedStatement query1 = conn.prepareStatement(directRoutesQuery);
			PreparedStatement query2 = conn.prepareStatement(connectionsQuery);
			ResultSet dirRoutes = query1.executeQuery();
			ResultSet conRoutes = query2.executeQuery();
			
			System.out.println("All Routes between " + depCity + " and " + arrCity);
			System.out.println("Flight Number\tDeparture Time\tArrival Time\tDeparture City\tArrival City");
			while(dirRoutes.next()) {
				System.out.println(dirRoutes.getString(1) + "\t" + dirRoutes.getString(3) + "\t" + dirRoutes.getString(4) + "\t" + depCity + "\t" + arrCity);
			}
			
			System.out.println("Printing all routes with 1 connection");
			
			while(conRoutes.next()) {
				// check the flights have at least 1 day in common on schedule
				String sched1 = conRoutes.getString(6);
				String sched2 = conRoutes.getString(13);
				boolean aligned = false;
				
				for (int i=0; i < 7; i++) {
					if (sched1.charAt(i) != '-'){
							aligned = (sched1.charAt(i) == sched2.charAt(i));
						if (aligned)
							break;
					}
				}
				if(aligned) {
					System.out.println(conRoutes.getString(1) + " " + conRoutes.getString(2) + " " + conRoutes.getString(3) + " " + conRoutes.getString(4) + " " +
						conRoutes.getString(5) + " " + conRoutes.getString(6) + " " +
						conRoutes.getString(7) + " " + conRoutes.getString(8) + " " + conRoutes.getString(9) + " " + conRoutes.getString(10) + " " + conRoutes.getString(11) + " " + conRoutes.getString(12));
				}
				
			}
			query1.close();
			query2.close();
		} catch(SQLException e) {
			System.out.println("Queries failed");
			e.printStackTrace();
			return;
		}
		return;
    // SQL/PL Find routes by airline
	}

  /* 6. findRoutesOnDay */

	public static void findRoutesOnDay(Connection conn) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the departure city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3 || depCity.length() < 2) {
			// input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutesOnDay(conn);
		}
		System.out.println("Departure City: " + depCity);

		System.out.println("\nPlease enter the destination city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3 || arrCity.length() < 2) {
			// input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutesOnDay(conn);
		}
		System.out.println("Destination City: " + arrCity);

		System.out.println("\nPlease enter the preferred date in the format (YYYY MM DD).");
		System.out.println("Waiting for input...");
		String date = input.readLine();

		System.out.println("Date: " + date);
		findRoutesOnDayQuery(conn, depCity, arrCity, date);
		main(new String[1]);
	}
	
		// input.close();

    // SQL/PL Find Routes on date
		/*
		*	Query
			Find all routes w/ available seats btn. 2 cities on a given date
			allow one connection
			
			select f.flight_number, f.departure_city, f.departure_time, f.arrival_time
			from flight f, flight s
			where ((select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = [date])
			and f.departure_city = [depCity] and f.arrival_city = [arrCity])
			or
			(f.departure_city = [depCity] and s.arrival_city = [arrCity] and f.arrival_city = s.departure_city and s.departure_time >= (f.arrival_time + 1) and
			(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = [date])
			and (select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = s.flight_number and flight_date = [date])
			)			
		*
		*/
	public static void findRoutesOnDayQuery(Connection conn, String depCity, String arrCity, String date) {
		String directQuery = "select f.flight_number, f.departure_city, f.departure_time, f.arrival_time " +
													"from flight f join reservation_detail d on f.flight_number = d.flight_number " +
													"where f.departure_city = \'" + depCity + "\' and f.arrival_city = \'" + arrCity + "\' and d.flight_date = \'"  + date + "\' and " +
													"(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number " +
													" and flight_date = \'" + date + "\') ";
									
		String connectQuery = "select f.flight_number, f.departure_city, f.departure_time, s.arrival_time " +
			"from flight f, flight s, reservation_detail d " +
			"where " +
			"f.departure_city = \'" + depCity + "\' and s.arrival_city = \'" + arrCity + "\' and f.arrival_city = s.departure_city and s.departure_time >= (f.arrival_time + 100) and " +
			"f.flight_number = d.flight_number and d.flight_date = \'" + date + "\' and " + 
			"(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = \'" + date + "\') " +
			"and (select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = s.flight_number and flight_date = \'" + date + "\')";
		
		try {
			PreparedStatement query1 = conn.prepareStatement(directQuery);
			PreparedStatement query2 = conn.prepareStatement(connectQuery);
			ResultSet dirRoutes = query1.executeQuery();
			ResultSet conRoutes = query2.executeQuery();
			
			System.out.println("Flight Number\tDeparture City\tDeparture Time\tArrival Time");
			System.out.println("--------------------------------------------------------------------");
			while(dirRoutes.next()) {
				// output query results
				System.out.println(dirRoutes.getString(1) + "\t" + dirRoutes.getString(2) + "\t" + dirRoutes.getInt(3) + "\t" + dirRoutes.getInt(4));
			}
			while(conRoutes.next()){
				System.out.println(conRoutes.getString(1) + "\t" + conRoutes.getString(2) + "\t" + conRoutes.getInt(3) + "\t" + conRoutes.getInt(4));
			}
			query1.close();
			query2.close();
		} catch(SQLException e) {
			System.out.println("Queries Failed!");
			e.printStackTrace();
			return;
		}
		return;
	}

  /* 7. findRoutesOnDayByAirline */

	public static void findRoutesOnDayByAirline(Connection conn) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease enter the departure city for the flight. E.G. \"CHI\"");
		System.out.println("Waiting for input...");
		String depCity = input.readLine().toUpperCase();
		if (depCity.length() > 3 || depCity.length() < 2) {
			// input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutesOnDayByAirline(conn);
		}
		System.out.println("Departure City: " + depCity);

		System.out.println("\nPlease enter the destination city for the flight. E.G. \"PIT\"");
		System.out.println("Waiting for input...");
		String arrCity = input.readLine().toUpperCase();
		if (arrCity.length() > 3 || arrCity.length() < 2) {
			// input.close();
			System.out.println("ERROR. City name was too long. Expecting a 2 or 3 character abbreviation like \"NY\" or \"CHI\"");
			findRoutesOnDayByAirline(conn);
		}
		System.out.println("Destination City: " + arrCity);

		System.out.println("\nPlease enter the preferred airline.");
		System.out.println("Waiting for input...");
		String airLine = input.readLine();

		System.out.println("Airline: " + airLine);

		System.out.println("\nPlease enter the preferred date in the format (YYYY MM DD).");
		System.out.println("Waiting for input...");
		String date = input.readLine();

		System.out.println("Date: " + date);
		
		findRoutesOnDayByAirlineQuery(conn, depCity, arrCity, date, airLine);
		main(new String[1]);
	}
	
	public static void findRoutesOnDayByAirlineQuery(Connection conn, String depCity, String arrCity, String date, String airLine){
		
		String directQuery = "select distinct f.airline_id, f.flight_number, f.departure_city, f.departure_time, f.arrival_time " +
													"from flight f join airline a on f.airline_id = a.airline_id " +//join reservation_detail d on f.flight_number = d.flight_number " +
													"where f.departure_city = \'" + depCity + "\' and f.arrival_city = \'" + arrCity + "\' and a.airline_name = \'" + airLine + "\' and " +
													// "d.flight_date = \'" + date + "\' and " +
													"(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number " +
													" and flight_date = \'" + date + "\') and " +
													"exists (select * from reservation_detail d where " +
													"f.flight_number=d.flight_number and d.flight_date = \'" + date + "\' and f.airline_id = a.airline_id)";
													
									
		String connectQuery = "select f.airline_id, f.flight_number, f.departure_city, f.departure_time, s.arrival_time " +
			"from flight f, flight s, airline a " +
			"where " +
			"f.departure_city = \'" + depCity + "\' and s.arrival_city = \'" + arrCity + "\' and f.arrival_city = s.departure_city and a.airline_name = \'" + airLine + 
			"\' and s.departure_time >= (f.arrival_time + 100) and a.airline_id = f.airline_id and a.airline_id = s.airline_id and " +
			"(select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = f.flight_number and flight_date = \'" + date + "\') " +
			"and (select max(plane_capacity) from plane) > (select count(*) from reservation_detail where flight_number = s.flight_number and flight_date = \'" + date + "\') and " +
			"exists (select * from reservation_detail d where " +
			"f.flight_number=d.flight_number and d.flight_date = \'" + date + "\' and f.airline_id = a.airline_id)";
		
		
		try {
			PreparedStatement query1 = conn.prepareStatement(directQuery);
			PreparedStatement query2 = conn.prepareStatement(connectQuery);
			ResultSet dirRoutes = query1.executeQuery();
			ResultSet conRoutes = query2.executeQuery();
			
			System.out.println("Airline ID\tFlight Number\tDeparture City\tDeparture Time\tArrival Time");
			System.out.println("--------------------------------------------------------------------");
			while(dirRoutes.next()) {
				// output query results
				System.out.println(dirRoutes.getString(1) + "\t" + dirRoutes.getString(2) + "\t" + dirRoutes.getString(3) + "\t" + dirRoutes.getInt(4) + "\t" + dirRoutes.getInt(5));
			}
			System.out.println("\nConnections:");
			while(conRoutes.next()){
				System.out.println(conRoutes.getString(1) + "\t" + conRoutes.getString(2) + "\t" + conRoutes.getString(3) + "\t" + conRoutes.getInt(4) + "\t" + conRoutes.getInt(5));
			}
			query1.close();
			query2.close();
		} catch(SQLException e) {
			System.out.println("Queries Failed!");
			e.printStackTrace();
			return;
		}
    // SQL/PL Find Routes on date
		return;
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
		addReservationQuery(conn, flightNumbers, depDates);
		main(new String[1]);
	}
	
	public static void addReservationQuery(Connection conn, ArrayList<String> flightNumbers, ArrayList<String> depDates) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		String maxSeatsQuery = "select max(plane_capacity) from plane";
		int maxSeats = 0;
		try{
			PreparedStatement findMax = conn.prepareStatement(maxSeatsQuery);
			ResultSet rs = findMax.executeQuery();
			
			if(rs.next()){
				maxSeats = rs.getInt(1);
			}
		} catch(SQLException e) {
			System.out.println("Failed to find max seating available");
			e.printStackTrace();
			return;
		}
		
		if(!(maxSeats > 0)) {
			System.out.println("Something went wrong!");
			return;
		}
		
		for (int i = 0; i < flightNumbers.size(); i++) {
			String seatsQuery = "select count(*) from reservation_detail where flight_number = \'" + flightNumbers.get(i) + "\' and flight_date = \'" + depDates.get(i) + "\'";
			
			try{
				PreparedStatement search = conn.prepareStatement(seatsQuery);
				
				ResultSet seats = search.executeQuery();
				if (seats.next()) {
					if (!(seats.getInt(1) < maxSeats)){
						System.out.println("Flight Number " + flightNumbers.get(i) + " is already full! Action aborted.");
						return;
					}
				}
			} catch(SQLException e) {
				System.out.println("ERROR! Query Failed.");
				e.printStackTrace();
				return;
			}
		}
		
		//generate new reservation_number
		String maxResQuery = "select max(reservation_number) from reservation";
		int newRes = 0;
		try{
			PreparedStatement getRes = conn.prepareStatement(maxResQuery);
			ResultSet reservationNum = getRes.executeQuery();
			
			if(reservationNum.next()) {
				newRes = reservationNum.getInt(1) + 1;
			}
		} catch(SQLException e) {
			System.out.println("Failed to generate new Reservation Number");
			e.printStackTrace();
			return;
		}
		
		//get user cid to add reservation
		System.out.println("CONFIRMATION: Please enter your PittRewards number to complete your reservation");
		String cid = input.readLine();
	
	
		String resyGen = "insert into reservation (reservation_number, cid) values (\'" + newRes + "\', \'" + cid + "\')";
		try {
			PreparedStatement createRes = conn.prepareStatement(resyGen);
			int rowsChanged = createRes.executeUpdate();
			if (rowsChanged != 1) {
				System.out.println("Failed to create reservation!");
				return;
			}
		} catch(SQLException e) {
			System.out.println("Insertion failed!");
			e.printStackTrace();
			return;
		}
		for (int n = 0; n < flightNumbers.size(); n++) {
			String insertSql = "insert into reservation_detail values (\'" + newRes + "\', \'" + flightNumbers.get(n) + "\', \'" + depDates.get(n) + "\', " + n+1 + ")";
			
			try {
				PreparedStatement resDetail = conn.prepareStatement(insertSql);
				int rowsInserted = resDetail.executeUpdate();
				if(rowsInserted != 1) {
					System.out.println("Failed to add flight " + flightNumbers.get(n) + " to reservation");
					return;
				}
			} catch(SQLException e) {
				System.out.println("Insertion Failed.");
				e.printStackTrace();
				return;
			}
		}
    // input.close();
		System.out.println("\nAction completed! Your reservation number is " + newRes);
    // Verify legs, seats, date, generate res # and confirmation/error message.
		while(true){
			System.out.println("\nWould you like to complete another action?");
			System.out.println("Y/N");
			String choice = input.readLine().toUpperCase();
			if(choice.equals("Y"))
				return;
			else if(choice.equals("N"))
				System.exit(0);
			else {
				System.out.println("INVALID OPTION\n");
			}
		}
	}

  /* 9. showResInfo */

  	public static void showResInfo(Connection conn) throws IOException {
    	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\nPlease input your reservation number.");
      System.out.println("Reservation Number:");
      String resNum = input.readLine();

			showResInfoQuery(conn, resNum);
      main(new String[1]);
		}
		
		public static void showResInfoQuery(Connection conn, String resNum) {
      // Call SQL/PL, print all flights for this reservation number, print error otherwise
			String query = "select distinct flight_number from reservation_detail where reservation_number = \'" + resNum + "\'";
			PreparedStatement getFlights = null;
			ResultSet flights = null;
			try {
				getFlights = conn.prepareStatement(query);
				flights = getFlights.executeQuery();
				
				//check that query is not empty
				if (!flights.next()) {
					System.out.println("Error. There are no reservations matching the given reservatio number!");
					return;
				}
				
				System.out.println("Flights for this reservation:");
				System.out.println("Flight Number");
				System.out.println("-------------");
				//debugging output
				// System.out.println("Current row: " + flights.getRow());
				// System.out.println("Has another row? " + flights.next());
				
				do {
					// System.out.println("Now at row " + flights.getRow());
					System.out.println("Flight: " + flights.getString(1));
				} while(flights.next());
				
				
			} catch(SQLException e) {
				System.out.println("Query Failed!");
				e.printStackTrace();
				
			} finally {
				try{ flights.close();} catch(Exception e) {};
				try { getFlights.close();} catch(Exception e) {};
				return;
			}
			// return;
  	}
/*
* ^^^ NEEDS TESTING ^^^
*/

  /* 10. buyTicket */

	public static void buyTicket(Connection conn) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nPlease input your reservation number.");
		System.out.println("Reservation Number:");
		String resNum = input.readLine();

		input.close();

		buyTicketQuery(conn, resNum);
		main(new String[1]);
	}
	
	public static void buyTicketQuery(Connection conn, String resNum) {
		String sql = "update reservation set ticketed = \'Y\' where reservation_number = \'" + resNum + "\'";
		
		try {
			PreparedStatement updt = conn.prepareStatement(sql);
			int result = updt.executeUpdate();
			if (result == 1)
				System.out.println("Ticket purchased.");
			else{
				System.out.println("Uh oh! Something went wrong.");
				System.exit(1);
			}
		} catch(SQLException e) {
			System.out.println("Update Failed!");
			e.printStackTrace();
			return;
		}
		return;
	}
}
