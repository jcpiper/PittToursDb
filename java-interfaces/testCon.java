// Oracle connection test
import java.sql.*;
// import ojdbc14.oracle.jdbc.driver.OracleDriver;


public class testCon{
	public static void main(String[] args) {
		// initialize conneciton
		Connection conn = null;
		try {
			
					DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
           String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
					 String uname = "jcp68";
					 String passwrd = "3788990";
           conn = DriverManager.getConnection(url, uname, passwrd);

			// Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (SQLException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		Statement stmt = null;
		String query = "Select * from flight";
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			System.out.println("Query made!");
			
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Query failed!");
			e.printStackTrace();
			return;
		} 
	
	}
}