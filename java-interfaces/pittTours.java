/*
* PittTours Application Driver
*/
import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.*;

public class pittTours{
	static Random rand = new Random();
	static String[] cities = {"NY", "PIT", "BOS", "CHI", "LA", "MIL"};
	static String[] airlines = {
		"United Airlines", "Delta Airlines", "American Airlines", "Northwest Airlines",
		"Southwest Airlines", "Fronteir Airlines", "Malaysia Airlines",
		"Emirates Airlines", "Alaska Airlines", "JetBlue"
	};

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome! Would you like to view the administrator menu or the customer menu? Choose an option");
		System.out.println("1. Administrator\n2. Customer");
		System.out.println("3. Stress Test");
		System.out.println("Waiting for input...");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		String option = input.readLine();

		if (option.equals("1")) {
			pittToursAdmin.main(args);
		}
		else if (option.equals("2")) {
			pittToursCustomer.main(args);
		}
		else if (option.equals("3")) {
			breakIt();
		}
		else {
			System.out.println("\nInvalid option!");
			main(args);
		}
	}

	// this method will call all methods multiple times w/ large datasets and output results
	public static void breakIt() {
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
		String[] fNames = new String[5000];
		String[] lNames = new String[5000];

		System.out.println("Testing load airline info");
		try {loadAirlineTest(conn);}
    catch(IOException e) {}
		System.out.println("\n\n---------------------------------------------\n");

		System.out.println("Testing load flight info");
		try {loadFlightTest(conn);}
    catch(IOException e) {}
		System.out.println("\n\n---------------------------------------------\n");

		System.out.println("Testing load pricing info");
		try {loadPricingTest(conn);}
    catch(IOException e) {}
		System.out.println("\n\n---------------------------------------------\n");

		System.out.println("Testing load plane info");
		try {loadPlaneTest(conn);}
    catch(IOException e) {}
		System.out.println("\n\n---------------------------------------------\n");

		//test customer creation
		// System.out.println("Testing Customer program.");
		// System.out.println("Adding 5000 customers");
		// int customersAdded = addCustomerTest(conn, fNames, lNames);
		// System.out.println(customersAdded + " customers created!");
		// test customer info retrieval
		// System.out.println("\nRetrieving info for all added customers");
		// getCustomerInfoTest(conn, fNames, lNames);
		// test price retrieval
		System.out.println("Testing GET PRICE function");
		getAllPrices(conn);
		System.out.println("\n\n---------------------------------------------\n");
		// test routes retrieval
		System.out.println("Testing get routes function");
		getRoutesTest(conn);
		System.out.println("\n\n---------------------------------------------\n");

		// test routes by airline retrieval
		System.out.println("Testing get routes by airline function");
		getRoutesByAirlineTest(conn);
		System.out.println("\n\n---------------------------------------------\n");

		//test routes by day
		System.out.println("Testing get routes on day function");
		getRoutesOnDayTest(conn);
		System.out.println("\n\n---------------------------------------------\n");

		//test routes by day by airline
		System.out.println("Testing get routes on day by airline function");
		getRoutesOnDayByAirlineTest(conn);
		System.out.println("\n\n---------------------------------------------\n");

	}

	public static int addCustomerTest(Connection conn, String[] fNames, String[] lNames) {
		int customers = 0;

		for (int i = 0; i < 5000; i++) {
			String fName = getName(i%25 +5);
			String lName = getName(i%25 +5);
			customers += pittToursCustomer.createCustomer("Ms", fName, lName, getCardNum(), "30-SEP-2017", "DAWSON", "PITTSBURGH", "PA", "4121234567", "fake@gmail.com", conn);
			fNames[i] = fName;
			lNames[i] = lName;
		}
		return customers;
	}

	//generate a random name
	public static String getName(int len) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ )
			sb.append( base.charAt( rand.nextInt(base.length()) ) );
		return sb.toString();
	}

	public static String getCardNum() {
		String base = "0123456789";
		StringBuilder sb = new StringBuilder(16);
		for( int i = 0; i < 16; i++ )
			sb.append( base.charAt( rand.nextInt(base.length()) ) );
		return sb.toString();
	}

	public static void getCustomerInfoTest(Connection conn, String[] fNames, String[] lNames) {
		for (int i = 0; i < fNames.length; i++) {
			pittToursCustomer.getCustInfo(conn, fNames[i], lNames[i]);
		}
	}

	public static void getAllPrices(Connection conn) {
		pittToursCustomer.findPriceExe(conn, "NY", "PIT");
		pittToursCustomer.findPriceExe(conn, "PIT", "BOS");
		pittToursCustomer.findPriceExe(conn, "BOS", "CHI");
		pittToursCustomer.findPriceExe(conn, "CHI", "LA");
		pittToursCustomer.findPriceExe(conn, "LA", "NY");
	}

	public static void getRoutesTest(Connection conn) {
		for (int i = 0; i < cities.length; i++) {
			if (i < (cities.length - 1))
				pittToursCustomer.findRoutesQuery(conn, cities[i], cities[i+1]);
			else
				pittToursCustomer.findRoutesQuery(conn, cities[i], cities[0]);
		}
	}

	public static void getRoutesByAirlineTest(Connection conn) {
		for (int i = 0; i < cities.length; i++) {
			if (i < (cities.length - 1)){
				System.out.println("\n--------------------------------------------\n");
				System.out.println("From " + cities[i] + " TO " + cities[i+1]);
			}
			else{
				System.out.println("\n--------------------------------------------\n");
				System.out.println("FROM " + cities[i] + " TO " + cities[0]);
			}
			for (int n = 0; n < airlines.length; n++){
				System.out.println("\n---------------------------------------------\n");
				System.out.println("Airline: " + airlines[n]);
				if (i < (cities.length - 1))
					pittToursCustomer.findRoutesByAirlineQuery(conn, cities[i], cities[i+1], airlines[n]);
				else
					pittToursCustomer.findRoutesByAirlineQuery(conn, cities[i], cities[0], airlines[n]);
			}
		}
	}

	public static void getRoutesOnDayTest(Connection conn) {
		String[] dates = {"11-SEP-2017", "16-JUN-2017", "12-dec-2017"};
		for (int i = 0; i < cities.length; i++){
			if (i < (cities.length - 1)){
				System.out.println("\n--------------------------------------------\n");
				System.out.println("From " + cities[i] + " TO " + cities[i+1]);
			}
			else{
				System.out.println("\n--------------------------------------------\n");
				System.out.println("FROM " + cities[i] + " TO " + cities[0]);
			}
			for (int n = 0; n < dates.length; n++) {
				System.out.println("--------------------------------");
				System.out.println("DATE: " + dates[n] + "\n");
				if (i < (cities.length - 1))
					pittToursCustomer.findRoutesOnDayQuery(conn, cities[i], cities[i+1], dates[n]);
				else
					pittToursCustomer.findRoutesOnDayQuery(conn, cities[i], cities[0], dates[n]);
			}
		}
	}

	public static void getRoutesOnDayByAirlineTest(Connection conn) {
		String[] dates = {"11-SEP-2017", "16-JUN-2017", "12-dec-2017"};
		// pittToursCustomer.findRoutesOnDayByAirlineQuery(conn, "PIT", "BOS", "11-SEP-2017", "JetBlue");
		// System.out.println("Different airline now");
		// pittToursCustomer.findRoutesOnDayByAirlineQuery(conn, "PIT", "BOS", "11-SEP-2017", "Delta Airlines");

		for (int i = 0; i < cities.length; i++){
			if (i < (cities.length - 1)){
				System.out.println("\n--------------------------------------------\n");
				System.out.println("From " + cities[i] + " TO " + cities[i+1]);
			}
			else{
				System.out.println("\n--------------------------------------------\n");
				System.out.println("FROM " + cities[i] + " TO " + cities[0]);
			}
			for (int n = 0; n < dates.length; n++) {
				System.out.println("--------------------------------");
				System.out.println("DATE: " + dates[n] + "\n");
				for (int x = 0; x < airlines.length; x++){
					System.out.println("--------------------------------");
					System.out.println("AIRLINE: " + airlines[x] + "\n");
					if (i < (cities.length - 1))
						pittToursCustomer.findRoutesOnDayByAirlineQuery(conn, cities[i], cities[i+1], dates[n], airlines[x]);
					else
						pittToursCustomer.findRoutesOnDayByAirlineQuery(conn, cities[i], cities[0], dates[n], airlines[x]);
				}
			}
		}
	}

  public static void loadAirlineTest(Connection conn) throws IOException {
    pittToursAdmin.loadAirlineExe(conn);
  }
  public static void loadFlightTest(Connection conn) throws IOException {
    pittToursAdmin.loadScheduleExe(conn);
  }
  public static void loadPricingTest(Connection conn) throws IOException {
    pittToursAdmin.loadPriceDataExe(conn);
  }
  public static void loadPlaneTest(Connection conn) throws IOException {
    pittToursAdmin.loadPlaneExe(conn);
  }

	public static void addReservationTest(Connection conn) {

	}
}
