import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHandler {

    public static Date fromString(String dateStr) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
        } catch (ParseException e) {return null;}
    }

    public static String fromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public static String getSeason(String date) {
        String seasons[] = {
            "winter", "winter", "spring", "spring", "spring", "summer", 
            "summer", "summer", "autumn", "autumn", "autumn", "winter"
          };
        
        int month = Integer.parseInt(date.split("-")[1]);
        
        return seasons[month - 1];
    }

}
