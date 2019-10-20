package Bill.Data;
import java.sql.*;

public class Credentials {
    private static Credentials instance=new Credentials();
    private String username;
    private String Password;
    private String GST;
    private String PAN;
    private String Phone;
    private String E_mail;
    private String address;
    private String acNo;
    private String acName;
    private String bankName;
    private String ifscCode;

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAcNo() {
        return acNo;
    }

    public String getAcName() {
        return acName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public static Credentials getInstance() {
        return instance;
    }

    private Credentials(){
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setE_mail(String e_mail) {
        E_mail = e_mail;
    }

    public String getGST() {
        return GST;
    }

    public String getPAN() {
        return PAN;
    }

    public String getPhone() {
        return Phone;
    }

    public String getE_mail() {
        return E_mail;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return Password;
    }

    public void write_credentials() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS Credentials");
            statement.execute("CREATE TABLE IF NOT EXISTS Credentials (Username TEXT, Password TEXT, eMail TEXT, Phone TEXT" +
                    ", PAN TEXT, GST TEXT, Address TEXT, bankName TEXT, acName TEXT, acNumber TEXT, IFSC TEXT)");
            statement.execute("INSERT INTO Credentials(Username, Password, eMail, Phone, PAN, GST, Address, bankName, " +
                    " acName, acNumber, IFSC) VALUES( '"+Credentials.getInstance().getUsername().trim()+"',  '"+Credentials.getInstance().getPassword().trim()+"', '"
                    +Credentials.getInstance().getE_mail().trim()+"', '"+Credentials.getInstance().getPhone().trim()+"', '"+Credentials.getInstance().getPAN().trim()
                    +"', '"+Credentials.getInstance().getGST().trim()+"', '"+Credentials.getInstance().getAddress().trim()+"', '"+Credentials.getInstance().getBankName().trim()+
                    "', '"+Credentials.getInstance().getAcName().trim()+"', '"+Credentials.getInstance().getAcNo().trim()+"', '"+Credentials.getInstance().getIfscCode().trim()+"')");
        }catch (SQLException e){e.printStackTrace();}
    }
    public void read_credentials(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM Credentials");
            Credentials.getInstance().setUsername(resultSet.getString("Username"));
            Credentials.getInstance().setPassword(resultSet.getString("Password"));
            Credentials.getInstance().setE_mail(resultSet.getString("eMail"));
            Credentials.getInstance().setPhone(resultSet.getString("Phone"));
            Credentials.getInstance().setPAN(resultSet.getString("PAN"));
            Credentials.getInstance().setGST(resultSet.getString("GST"));
            Credentials.getInstance().setAddress(resultSet.getString("Address"));
            Credentials.getInstance().setBankName(resultSet.getString("bankName"));
            Credentials.getInstance().setAcName(resultSet.getString("acName"));
            Credentials.getInstance().setAcNo(resultSet.getString("acNumber"));
            Credentials.getInstance().setIfscCode(resultSet.getString("IFSC"));
        }catch (SQLException e){e.printStackTrace();}
    }

}
