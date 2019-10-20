package Bill.Data;

import javafx.collections.FXCollections;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class Customer {
    private static Customer instance=new Customer();
    private String name;
    private String GST;
    private String mobile;
    private String e_mail;
    private String address;
    private String City;
    private String State;
    private List<Customer> customers;
    public Customer(){}

    public static Customer getInstance() {
        return instance;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }


    public Customer(String name, String GST, String mobile, String e_mail, String address, String city, String state) {
        this.name = name;
        this.GST = GST;
        this.mobile = mobile;
        this.e_mail = e_mail;
        this.address = address;
        this.City = city;
        this.State = state;
    }

    public String getName() {
        return name;
    }

    public String getGST() {
        return GST;
    }

    public String getMobile() {
        return mobile;
    }

    public String getE_mail() {
        return e_mail;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }
    public void write_customers() {
        try{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS Customers");
            statement.execute("CREATE TABLE IF NOT EXISTS Customers (Nam TEXT, GST TEXT, Mobile TEXT, eMail TEXT" +
                    ", Address TEXT, City TEXT, State TEXT)");
            Iterator<Customer> iter=customers.iterator();
            while (iter.hasNext()){
                Customer wr=iter.next();
                statement.execute("INSERT INTO Customers(Nam, GST, Mobile, eMail, Address, City, State) VALUES( '"+wr.getName().trim()+"',  '"
                        +wr.getGST().trim()+"', '"+wr.getMobile().trim()+"', '"+wr.getE_mail().trim()+"', '"
                        +wr.getAddress().trim()+"', '"+wr.getCity().trim()+"', '"+wr.getState().trim()+"')");
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public void read_customers(){
        customers= FXCollections.observableArrayList();
        try{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM Customers");
            while(resultSet.next()){
                Customer c=new Customer(resultSet.getString("Nam")
                                        , resultSet.getString("GST"), resultSet.getString("Mobile"), resultSet.getString("eMail"),
                                        resultSet.getString("Address"), resultSet.getString("City"), resultSet.getString("State"));
                customers.add(c);
            }
        }catch (Exception e){e.printStackTrace();}
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    @Override
    public String toString() {
        return name;
    }
}
