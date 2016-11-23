/*
/ Administrator interface for pittTours
*/
import java.io.*;

public class pittToursAdmin {
	
	public static void main (String[] args) throws IOException {
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
		switch (option) {
			case "1": 
				System.out.println("You have chosen to erase the database.");
				break;
			case "2":
				System.out.println("You have chosen to load airline info.");
				break;
			case "3":
				System.out.println("You have chosen to load schedule info.");
				break;
			case "4":
				System.out.println("You have chosen to load pricing info.");
				break;
			case "5":
				System.out.println("You have chosen to load plane info.");
				break;
			case "6":
				System.out.println("You have chosen to generate passenger manifest for specific flight on given day.");
				break;
			default:
				System.out.println("Invalid option, choose again");
				break;
		}
	}
	
	
}
