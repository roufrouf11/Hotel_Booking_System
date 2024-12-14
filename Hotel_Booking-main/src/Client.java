import java.io.*;
import java.net.*;
import java.util.*;

//CLIENT - CLASS
class Client {
    static String menuCounter;

    //MAIN
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String menuStatus;

        //Establish a connection by providing host and port number
        try (Socket socket = new Socket("localhost", 1234)) {
            //Writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //Reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            while (true) {
                menuStatus = mainMenu(sc);
				if (menuStatus.equals("exit")) break;
                else {
                    // get items for specific reservation
                    out.println(menuStatus);
                    String rooms = in.readLine();
                    rooms = rooms.replace(";", "\n");
                    System.out.println();
                    System.out.println(rooms);

                    if (!rooms.isEmpty()) {
                        System.out.print("Select room: ");
                        out.println(sc.nextLine());
                    }
                    
                    System.out.println(in.readLine());
                }
            }
            // closing the scanner object
            sc.close();
        } catch (IOException e) {e.printStackTrace();}

    }


    //Make an account
    public static String accountDetails(Scanner sc){
        String firstName, lastName;
        System.out.println("Enter First Name: ");
        firstName = sc.nextLine();

        System.out.println("Enter Last Name: ");
        lastName = sc.nextLine();

        System.out.println();
        System.out.println("Account Details: ");
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        return firstName + ";" + lastName;
    }

    public static String mainMenu(Scanner sc){
		int input;
        while (true) {
			System.out.println("[Menu]");
			System.out.println("1) Make a reservation");
			System.out.println("2) Exit");

			input = Integer.parseInt(sc.nextLine());
			switch (input) {
				case 1:
                System.out.println("Making a reservation");
                String options = "reservation;";
                options = new String(options + locationMenu(sc) + ";");
                options = new String(options + priceMenu(sc) + ";");
                options = new String(options + packageMenu(sc) + ";");
                options = new String(options + dateMenu(sc));
                return options;

				case 2:
				return "exit";

				default:
				continue;
			}
		}
    }

    public static String locationMenu(Scanner sc){
        int input;
        String[] locations = new String[]{"Athens", "Katerini"};
        while (true) {
            System.out.println("[Location]");
            for (int i=0; i<locations.length; i++) {
                System.out.println((i + 1) + ") " + locations[i]);
            }
            input = Integer.parseInt(sc.nextLine());
            if (input > locations.length || input < 1) continue;
            return locations[input - 1];
        }
    }

    public static String priceMenu(Scanner sc){
        int input;
        while (true) {
            System.out.println("[Price]");
            System.out.println("1) 0-50");
            System.out.println("2) 50-100");
            System.out.println("3) 100+");

            input = Integer.parseInt(sc.nextLine());
            if (input < 1 || input > 3) continue;
            switch(input) {
                case 1: return "0-50";
                case 2: return "50-100";
                case 3: return "100+";
                default:
            }
        }
    }

    public static String packageMenu(Scanner sc){
        int input;
        while (true) {
            System.out.println("[Package]");
            System.out.println("1) Double");
            System.out.println("2) Triple");
            System.out.println("3) Deluxe (4 people)");

            input = Integer.parseInt(sc.nextLine());
            if (input < 1 || input > 3) continue;

            switch (input) {
                case 1: return "double";
                case 2: return "triple";
                case 3: return "deluxe";
            }
        }
    }

    public static String dateMenu(Scanner sc) {
        String checkIn, checkOut;

        System.out.println("[Date]");
        System.out.println("Enter check in date; format dd-mm-yyyy");
        checkIn = sc.nextLine();
        System.out.println("Enter check out date; format dd-mm-yyyy");
        checkOut = sc.nextLine();
        
        return checkIn + ";" + checkOut;
    }
}
