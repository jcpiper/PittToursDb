/*
* PittTours Application Driver
*/
import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.*;

public class pittTours{
	static Random rand = new Random();
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
		
		System.out.println("Testing Customer program.");
		System.out.println("Adding 5000 customers");
		int customersAdded = addCustomerTest(conn, fNames, lNames);
		System.out.println(customersAdded + " customers created!");
		System.out.println("\nRetrieving info for all added customers");
		getCustomerInfoTest(conn, fNames, lNames);
		System.out.println("Testing GET PRICE function");
		getAllPrices(conn);
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
}