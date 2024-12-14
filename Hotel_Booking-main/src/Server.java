import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

//SERVER - CLASS
class Server {
    //Arraylist with all the hotels
    public static ArrayList<Hotel> hotelList = new ArrayList<Hotel>(0);

    //MAIN - METHOD
    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            //Accepting client requests (always working)
            while (true) {
                Socket client = server.accept();

                System.out.println("New client connected " + client.getInetAddress().getHostAddress());

                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();
            }
        }catch (IOException e) { e.printStackTrace();}

        finally{
            if(server != null){
                try {
                    server.close();
                }catch (IOException e) { e.printStackTrace();}
            }
        }
    }


    //CLIENT HANDLER - CLASS
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        //Constructor of client handler
        public ClientHandler(Socket socket) {this.clientSocket = socket;}

        //OVERRIDE RUN
        public void run(){
            OutputStream outStream;
            PrintWriter out = null;
            BufferedReader in = null;
            FileInputStream fileIn;
            ObjectInputStream objectIn = null;

            try {
                outStream = clientSocket.getOutputStream();
                //Output stream for sending to client
                out = new PrintWriter(outStream, true);
                //Input stream for getting from client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String input;
                String[] inputs;

                while ((input = in.readLine()) != null) {
                    inputs = input.split(";");

                    if (inputs[0].equals("reservation")) {
                        String location = inputs[1];
                        String price = inputs[2];
                        String nOfBeds = inputs[3];
                        String checkInStr = inputs[4];
                        String checkOutStr = inputs[5];
                        ArrayList<Room> toShow = new ArrayList<>();

                        try {
                            fileIn = new FileInputStream("db.txt");
                            objectIn = new ObjectInputStream(fileIn);
                        } catch (FileNotFoundException e) {
                            System.exit(2);
                        } catch (IOException e) {
                            System.exit(3);
                        }

                        try {
                            hotelList = (ArrayList<Hotel>)objectIn.readObject();
                        } catch (ClassNotFoundException e) {System.exit(4);}

                        Room toBeReserved = null;
                        for (int i=0; i<hotelList.size(); i++) {
                            Hotel h = hotelList.get(i);

                            //IF LOCATIONS DONT MATCH THEN GO TO NEXT HOTEL
                            if (!h.region.equals(location)) continue;

                            //CHECK THE ROOMS
                            for (int j=0; j<h.roomList.size(); j++) {
                                Room r = h.roomList.get(j);
                                if (r.getPriceRange(checkInStr).equals(price) &&
                                    r.roomPackage.equals(nOfBeds) &&
                                    !r.checkReserved(checkInStr, checkOutStr))
                                    toShow.add(r);
                            }
                        }
                        String toSend = "";
                        for (int i=0; i<toShow.size(); i++) {
                            toSend = new String(toSend + (i + 1) + ") " + toShow.get(i).toString(checkInStr));
                            if (i != toShow.size() - 1) toSend = new String(toSend + ";");
                        }
                        out.println(toSend);
                        if (toShow.size() == 0) {
                            out.println("Rooms not found.");
                            continue;
                        }
                        int choice = Integer.parseInt(in.readLine());
                        toBeReserved = toShow.get(choice - 1);

                        // check that the room is not reserved
                        FileInputStream fileInCheck;
                        ObjectInputStream objectInCheck = null;
                        ArrayList<Hotel> hotelListCheck = null;
                        System.out.println(toShow.get(choice - 1).toString(checkInStr));
                        try {
                            fileInCheck = new FileInputStream("db.txt");
                            objectInCheck = new ObjectInputStream(fileInCheck);
                            hotelListCheck = (ArrayList<Hotel>)objectInCheck.readObject();
                        } catch (ClassNotFoundException e) {
                            System.exit(4);
                        } catch (IOException e) {
                            System.exit(4);
                        }

                        for (int i=0; i<hotelListCheck.size(); i++) {
                            if (!hotelListCheck.get(i).name.equals(toBeReserved.parent.name)) continue;
                            for (int j=0; j<hotelListCheck.get(i).roomList.size(); j++) {
                                Room r = hotelListCheck.get(i).roomList.get(j);
                                if (r.roomNumber == toBeReserved.roomNumber) {
                                    if (!r.checkReserved(checkInStr, checkOutStr)) {
                                        r.isReserved.add(new Date[]{DateHandler.fromString(checkInStr), DateHandler.fromString(checkOutStr)});

                                        // write object
                                        FileOutputStream fos = new FileOutputStream("db.txt");
                                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                                        oos.writeObject(hotelListCheck);
                                        out.println("Room reserved succesfully!");
                                        break;
                                    } else {
                                        out.println("The room was reserved a moment ago!");
                                    }
                                }
                            }
                            break;
                        }
                    }

                }
            }catch (IOException e) { e.printStackTrace();}

            finally {
                try {
                    if (out != null) {
                        out.close();
                    }

                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }catch (IOException e) {e.printStackTrace();}
            }
        }
    }

    public static void resetDB() {
        hotelList = new ArrayList<>();
        populate();
        try {
            FileOutputStream fos = new FileOutputStream("db.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hotelList);
            oos.close();
        } catch (FileNotFoundException e) {
            System.exit(2);
        } catch (IOException e) {
            System.exit(2);
        }
    }

    //INITIALIZE THE HOTELS
    public static void populate() {
        Hotel h1 = new Hotel("Crystal", "Katerini");
        Hotel h2 = new Hotel("Zeus", "Katerini");
        Hotel h3 = new Hotel("Orfeas", "Athens");
        Hotel h4 = new Hotel("Venus", "Athens");
        hotelList.add(h1);
        hotelList.add(h2);
        hotelList.add(h3);
        hotelList.add(h4);

        Room r1 = new Room(h1, 0, 40, "double");
        Room r2 = new Room(h1, 1, 40, "double");
        Room r3 = new Room(h1, 2, 40, "double");
        Room r4 = new Room(h1, 3, 40, "double");
        Room r5 = new Room(h1, 4, 60, "triple");
        Room r6 = new Room(h1, 5, 60, "triple");
        Room r7 = new Room(h1, 6, 60, "triple");
        Room r8 = new Room(h1, 7, 60, "triple");
        Room r9 = new Room(h1, 8, 80, "deluxe");
        Room r10 = new Room(h1, 9, 80, "deluxe");

        Room r11 = new Room(h2, 0, 60, "double");
        Room r12 = new Room(h2, 1, 60, "double");
        Room r13 = new Room(h2, 2, 60, "double");
        Room r14 = new Room(h2, 3, 60, "double");
        Room r15 = new Room(h2, 4, 80, "triple");
        Room r16 = new Room(h2, 5, 80, "triple");
        Room r17 = new Room(h2, 6, 80, "triple");
        Room r18 = new Room(h2, 7, 80, "triple");
        Room r19 = new Room(h2, 8, 110, "deluxe");
        Room r20 = new Room(h2, 9, 110, "deluxe");

        Room r21 = new Room(h3, 0, 30, "double");
        Room r22 = new Room(h3, 1, 30, "double");
        Room r23 = new Room(h3, 2, 30, "double");
        Room r24 = new Room(h3, 3, 30, "double");
        Room r25 = new Room(h3, 4, 50, "triple");
        Room r26 = new Room(h3, 5, 50, "triple");
        Room r27 = new Room(h3, 6, 50, "triple");
        Room r28 = new Room(h3, 7, 50, "triple");
        Room r29 = new Room(h3, 8, 70, "deluxe");
        Room r30 = new Room(h3, 9, 70, "deluxe");

        Room r31 = new Room(h4, 0, 80, "double");
        Room r32 = new Room(h4, 1, 80, "double");
        Room r33 = new Room(h4, 2, 80, "double");
        Room r34 = new Room(h4, 3, 80, "double");
        Room r35 = new Room(h4, 4, 100, "triple");
        Room r36 = new Room(h4, 5, 100, "triple");
        Room r37 = new Room(h4, 6, 100, "triple");
        Room r38 = new Room(h4, 7, 100, "triple");
        Room r39 = new Room(h4, 8, 150, "deluxe");
        Room r40 = new Room(h4, 9, 150, "deluxe");

    }
}
