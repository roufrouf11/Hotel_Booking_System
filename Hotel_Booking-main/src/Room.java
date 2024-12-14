import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Room implements Serializable {
    public Hotel parent;
    public int roomNumber;
    public int roomPrice;
    public String roomPackage;
    ArrayList<Date[]> isReserved = new ArrayList<Date[]>(0);
    public String priceRange;

    public Room(Hotel parent, int roomNumber, int roomPrice, String roomPackage) {
        this.parent = parent;
        parent.roomList.add(this);
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomPackage = roomPackage;
    }

    public boolean checkReserved(String checkInStr, String checkOutStr) {
        Date checkIn, checkOut;
        try {
            checkIn = new SimpleDateFormat("dd-MM-yyyy").parse(checkInStr);
            checkOut = new SimpleDateFormat("dd-MM-yyyy").parse(checkOutStr);
        } catch (ParseException e) {return true;}

        //CHECKS THE IS RESERVED LIST
        for (int i=0; i<isReserved.size(); i++) {
            if (!checkIn.before(isReserved.get(i)[1]) || !checkOut.after(isReserved.get(i)[0])) continue;
            else return true;
        }
        return false;
    }

    //TO STRING - METHOD
    public String toString(String date) {
        return parent.name + " -> " + roomNumber + ": " + getPrice(date) + "$";
    }

    //GET PRICE - METHOD | GETS THE DATE
    public int getPrice(String date) {
        //WE CHECK THE SEASON
        String season = DateHandler.getSeason(date);

        switch (season) {
            case "winter":
            return roomPrice - 20;
            
            case "spring":
            return roomPrice;

            case "summer":
            return roomPrice + 20;

            case "autumn":
            return roomPrice;

            default:
            return 0;
        }
    }

    public String getPriceRange(String date) {
        int price = getPrice(date);

        if (price <= 50) return "0-50";
        else if (price <= 100) return "50-100";
        else return "100+";
    }
}
