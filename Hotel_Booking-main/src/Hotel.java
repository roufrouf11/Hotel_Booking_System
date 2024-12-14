import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {

    public String name;
    public String region;
    public ArrayList<Room> roomList = new ArrayList<>();

    public Hotel(String name, String region) {
        this.name = name;
        this.region = region;
    }
}
