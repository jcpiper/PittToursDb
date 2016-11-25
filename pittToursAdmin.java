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
				eraseDb();
				break;
			case "2":
				System.out.println("You have chosen to load airline info.");
				loadAirline();
				break;
			case "3":
				System.out.println("You have chosen to load schedule info.");
				loadSchedule();
				break;
			case "4":
				System.out.println("You have chosen to load pricing info.");
				loadPricing();
				break;
			case "5":
				System.out.println("You have chosen to load plane info.");
				loadPlane();
				break;
			case "6":
				System.out.println("You have chosen to generate passenger manifest for specific flight on given day.");
				generatePassengerList();
				break;
			default:
				System.out.println("Invalid option, choose again");
				String[] params = args;
				main(params);
				break;
		}
	}
	
	public static void eraseDb() throws IOException {
		System.out.println("Are you sure you want to erase the database?\ny/n");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String confirm = input.readLine();
		if (confirm.equals("y")) {
			System.out.println("Erasing the database.");
			// delete all tuples from all tables;
		}
		else {
			System.out.println("Action aborted, returning to main menu\n");
			String[] args = new String[1];
			main(args);
		}
	}
	public static void loadAirline() throws IOException {
		System.out.println("Preparing to load airline information.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the file name where the airline info is stored.");
		String file = input.readLine();
		// load data from airline file into airline table
	}
	
	public static void loadSchedule() throws IOException {
		System.out.println("Preparing to load schedule information");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the file name where the schedule info is stored.");
		String file = input.readLine();
		// load data from schedule file into flight table of db
	}
	
	public static void loadPricing() throws IOException {
		System.out.println("Preparing to load pricing information.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the file name where the pricing info is stored.");
		String file = input.readLine();
		// load data from schedule file into price table of db
	}
	
	public static void loadPlane() throws IOException {
		System.out.println("Preparing to load plane information.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the file name where the plane info is stored.");
		String file = input.readLine();
		// load data from schedule file into plane table of db
	}
	
	public static void generatePassengerList() throws IOException {
		System.out.println("Preparing to generate passenger manifest.");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the flight number of the desired flight.");
		String flightNum = input.readLine();
		System.out.println("Please enter the desired date.");
		String flightDate = input.readLine();
		// call procedure and print results
		/*
			select customer info from join of customer, reservation, res_detail
			select c.salutation, c. firstName, c.lastName
			from Customer c, Reservation r, Reservation_Detail d
			where r. reservation_number = d.reservation_number and c.cid = r.cid and d.flight_number = flightNum and d.date = flightDate;
		*/
	}
}
