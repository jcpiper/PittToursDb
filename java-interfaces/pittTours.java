/*
* PittTours Application Driver
*/
import java.io.*;

public class pittTours{
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome! Would you like to view the administrator menu or the customer menu? Choose an option");
		System.out.println("1. Administrator\n2. Customer");
		System.out.println("Waiting for input...");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		String option = input.readLine();
		
		if (option.equals("1")) {
			pittToursAdmin.main(args);
		}
		else if (option.equals("2")) {
			pittToursCustomer.main(args);
		}
		else {
			System.out.println("\nInvalid option!");
			main(args);
		}
	}
}