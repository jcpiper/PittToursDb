/*
* Administrator interface for pittTours
*/
import java.io.*;
import java.util.ArrayList;

public class pittToursCustomer {

	public static void main (String[] args) throws IOException {
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
		String option = input.readLine();
		switch (option) {
			case "1":
				System.out.println("You have chosen to add a customer.");
				addCustomer();
				break;
			case "2":
				System.out.println("You have chosen to show customer information.");
				showCustInfo();
				break;
			case "3":
				System.out.println("You have chosen to find price for flights between two cities.");
				findPrice();
				break;
			case "4":
				System.out.println("You have chosen to find all routes between two cities.");
				findRoutes();
				break;
			case "5":
				System.out.println("You have chosen to find all routes between two cities of a given airline");
				findRoutesByAirline();
				break;
			case "6":
				System.out.println("You have chosen to find all routes with available seats between two cities on a given day.");
				findRoutesOnDay();
				break;
			case "7":
				System.out.println("You have chosen to find all routes with available seats between two cities on a given day on a specific airline.");
				findRoutesOnDayByAirline();
				break;
			case "8":
				System.out.println("You have chosen to add a reservation.");
				addReservation();
				break;
			case "9":
				System.out.println("You have chosen to show reservation info by reservation number.");
				showResInfo();
				break;
			case "10":
				System.out.println("You have chosen to buy a ticket for an existing reservation.");
				buyTicket();
				break;
			default:
				System.out.println("Invalid option, choose again");
				String[] params = args;
				main(params);
				break;
		}
	}

  /* 1. addCustomer */

	public static void addCustomer() throws IOException {

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

    input.close();

    // Concatenate strings to input, call SQL/PL
	}

  /* 2. showCustInfo */

	public static void showCustInfo() throws IOException {
  	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("\nPlease input the customer's name.");
    System.out.println("\nFirst Name:");
    String firstName = input.readLine();

    System.out.println("\nLast Name:");
    String lastName = input.readLine();

    input.close();

    // Call SQL/PL, print customer info for customer with that name
	}

  /* 3. findPrice */

	public static void findPrice() throws IOException {
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

    input.close();

    // Call SQL/PL, print prices for one way each way & round trip
	}

  /* 4. findRoutes */

	public static void findRoutes() throws IOException {
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
										"where departure_city = " + depCity + " and arrival_city = " + arrCity;
		
		String connectionsQuery = "Select f.flight_number, f.departure_city, f.departure_time, f.arrival_time, s.flight_number, s.departure_city, s.departure_time, s.arrival_time " +
															"from flight f join flight s on f.arrival_city = s.departure_city " +
															"where f.departure_city = " + depCity + " and s.arrival_city = " + arrCity;
		// execute both queries and print results
	}

  /* 5. findRoutesByAirline */

	public static void findRoutesByAirline() throws IOException {
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

	public static void findRoutesOnDay() throws IOException {
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

	public static void findRoutesOnDayByAirline() throws IOException {
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

	public static void addReservation() throws IOException {

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

  	public static void showResInfo() throws IOException {
    	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\nPlease input your reservation number.");
      System.out.println("Reservation Number:");
      String resNum = input.readLine();

      input.close();

      // Call SQL/PL, print all flights for this reservation number, print error otherwise
  	}


  /* 10. buyTicket */

  	public static void buyTicket() throws IOException {
    	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\nPlease input your reservation number.");
      System.out.println("Reservation Number:");
      String resNum = input.readLine();

      input.close();

      // Call SQL/PL, see if ticket purchased, if not, purchase ticket
  	}
}
