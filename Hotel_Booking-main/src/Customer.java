import java.io.Serializable;

public class Customer implements Serializable {

    public String customerFirstName;
    public String customerLastName;
    public int customerID;
    static int customerLastID = 0;

    public Customer(String customerFirstName, String customerLastName) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerID = ++customerLastID;
    }

}
