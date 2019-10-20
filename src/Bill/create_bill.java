package Bill;

import Bill.Data.Bill;
import Bill.Data.Customer;
import Bill.Dialogues.EditInvoice;
import Bill.Dialogues.NewInvoice;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class create_bill implements Initializable {

    @FXML
    private JFXComboBox sh;
    @FXML
    private AnchorPane parent;
    @FXML
    public JFXTextField bill_no;
    @FXML
    private TableView<Bill> tb;
    @FXML
    private TableColumn<Bill,String> date;
    @FXML
    private TableColumn<Bill,String> gr;
    @FXML
    private TableColumn<Bill,Float> weight;
    @FXML
    private TableColumn<Bill, Integer> amt;
    @FXML
    private TableColumn<Bill,String> name;
    @FXML
    private TableColumn<Bill,String> Invoice;
    @FXML
    public DatePicker bill_date;
    @FXML
    private TableColumn<Bill,Integer> nop;
    @FXML
    private JFXTextField Total;
    @FXML
    public JFXTextArea consignor;
    @FXML
    private TableColumn<Bill,String> misc;
    @FXML
    private TableColumn<Bill,String> consignee;
    @FXML
    public JFXTextField vendorNo;
    private boolean prinFlag=true;
    private String bill_number;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Calendar cal = Calendar.getInstance();
        bill_number=new SimpleDateFormat("MMM").format(cal.getTime());
        if(cal.get(Calendar.MONTH)<=2)
            bill_number=bill_number+"/"+(cal.get(Calendar.YEAR)-1)+"-"+(cal.get(Calendar.YEAR)-2000)+"/";
        else
            bill_number=bill_number+"/"+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.YEAR)-1999)+"/";
        bill_no.setText(bill_number);
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        amt.setCellValueFactory(new PropertyValueFactory<>("amount"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Invoice.setCellValueFactory(new PropertyValueFactory<>("invoice"));
        gr.setCellValueFactory(new PropertyValueFactory<>("gr"));
        nop.setCellValueFactory(new PropertyValueFactory<>("no_of_packages"));
        consignee.setCellValueFactory(new PropertyValueFactory<>("shipTo"));
        misc.setCellValueFactory(new PropertyValueFactory<>("misc"));
        misc.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        date.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        weight.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        amt.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        name.prefWidthProperty().bind(tb.widthProperty().multiply(20.0/100.0));
        Invoice.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        gr.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        nop.prefWidthProperty().bind(tb.widthProperty().multiply( 10.0/100.0));
        consignee.prefWidthProperty().bind(tb.widthProperty().multiply( 10.0/100.0));

        DateTimeFormatter dateFormatter =DateTimeFormatter.ofPattern("dd/MM/yy");
        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        bill_date.setConverter(converter);
        LocalDate date=LocalDate.now();
        bill_date.getEditor().setText(dateFormatter.format(date));
        bill_date.setDayCellFactory(picker -> new DateCell() {

            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        ObservableList<Customer> customers=FXCollections.observableArrayList(Customer.getInstance().getCustomers());
        sh.setItems(customers);
        new AutoCompleteComboBoxListener<>(sh);
        
        int great=0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT BillNo FROM billData");
            int prev=Integer.parseInt(resultSet.getString("BillNo").substring(12)),nxt;
            great=prev;
            while (resultSet.next()){
                nxt=Integer.parseInt(resultSet.getString("BillNo").substring(12));
                if(nxt>prev)
                    great=nxt+1;
                prev=Integer.parseInt(resultSet.getString("BillNo").substring(12));
            }
            resultSet.close();
            statement.close();
        }catch (Exception e){e.printStackTrace();}
        if (great<10)
            bill_no.appendText("00");
        else if(great<100)
            bill_no.appendText("0");
        bill_no.appendText(String.valueOf(great));

        if(!Bill.getInstance().getBills().isEmpty())
            ini();

    }



    @FXML
    public void print_bill(){
        if((bill_no.getText().trim().equals(bill_number))){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Error in Print");
            dialog.setContentText("Please fill Bill number");
            dialog.showAndWait();
            return;
        }
        else if(prinFlag){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Error in Print");
            dialog.setContentText("Please Save before printing");
            dialog.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if(Prin.print_pdf(bill_no.getText().trim())) {
            dialog.setTitle("Success");
            dialog.setContentText("Bill printed successfully");
        }
        else{
            dialog.setTitle("Abort");
            dialog.setContentText("Printing aborted");
        }
        dialog.showAndWait();
    }
    @FXML
    public void save_bill() throws Exception{
        if((sh.getEditor().getText().equals(""))||(bill_date.getEditor().getText().equals(""))||(Bill.getInstance().getBills().isEmpty())
            ||(bill_no.getText().trim().equals(bill_number)||bill_no.getText().trim().equals(""))){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Error in Save");
            dialog.setContentText("Please fill all the details");
            dialog.showAndWait();
            return;
        }
        Connection connection= DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
        Statement statement=connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS billData (BillDate TEXT, Consignor TEXT, BillNo TEXT, VendorNo TEXT, Status TEXT)");
        statement.execute("CREATE TABLE IF NOT EXISTS bills (Bill_no TEXT, Name TEXT, Date TEXT, Invoice TEXT,Consignee TEXT,Miscellaneous TEXT" +
                ", GR TEXT, NOP INTEGER, Weight INTEGER, Amount INTEGER)");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM billData ");
        boolean flag=false;
        while (resultSet.next()){
            if(resultSet.getString("BillNo").equals(bill_no.getText().trim()))
                flag=true;
        }
        if(flag){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            dialog.setTitle("Bill already exists");
            dialog.setContentText("Do you want to Overwrite existing bill?");
            Optional<ButtonType> result=dialog.showAndWait();
            if(result.get()==ButtonType.OK) {
                statement.execute("DELETE FROM billData WHERE BillNo='" + bill_no.getText().trim() + "'");
                statement.execute("DELETE FROM bills WHERE Bill_no='"+bill_no.getText().trim()+"'");
            }
            else
                return;
        }
        statement.execute("INSERT INTO billData(BillDate, Consignor, BillNo, VendorNo, Status) VALUES( '"+bill_date.getEditor().getText().trim()+
                            "',  '"+consignor.getText().trim()+"', '"+bill_no.getText().trim()+"', '"+vendorNo.getText().trim()+"', 'No')");
        Iterator<Bill> billIterator=Bill.getInstance().getBills().iterator();
        while (billIterator.hasNext()){
            Bill wr=billIterator.next();
            statement.execute("INSERT INTO bills (Bill_no, Name, Date, Invoice,Consignee, Miscellaneous GR, NOP, Weight, Amount) "+
                                "VALUES( '"+wr.getBill_no()+"', '"+wr.getName()+"', '"+wr.getDate()+"', '"+wr.getInvoice()+"', '"
                    +wr.getShipTo()+"', '"+wr.getMisc()+"', '"+wr.getGr()+"', "+wr.getNo_of_packages()+", "+wr.getWeight()+", "+wr.getAmount()+")");
        }
        statement.close();
        connection.close();
        prinFlag=false;
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setTitle("Success");
        dialog.setContentText("Saved successfully");
        dialog.showAndWait();
    }
    private void update(){
        Iterator<Bill> billIterator=Bill.getInstance().getBills().iterator();
        int total=0;
        while(billIterator.hasNext()){
            total+=billIterator.next().getAmount();
        }
        Total.setText("Total: "+total);
        tb.setItems(FXCollections.observableArrayList(Bill.getInstance().getBills()));

    }
    @FXML
    public void new_invoice(){
        if(bill_no.getText().trim().equals(bill_number)){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Error");
            dialog.setContentText("Please Fill Bill Number");
            dialog.showAndWait();
            return;
        }
        NewInvoice.billNO=bill_no.getText().trim();
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        FXMLLoader fxmlLoader1=new FXMLLoader();
        fxmlLoader1.setLocation(getClass().getResource("Dialogues/new_invoice.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader1.load());
        }catch (IOException e){
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.setTitle("New Invoice");
        dialog.showAndWait();
        NewInvoice a=fxmlLoader1.getController();
        a.add_invoice();
        update();
    }
    @FXML
    public void edit(){
        if(tb.getSelectionModel().isEmpty()){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Error in edit");
            dialog.setContentText("Please select an item to edit");
            dialog.showAndWait();
            return;
        }
        Bill.getInstance().getBills().remove(tb.getSelectionModel().getSelectedItem());
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        fxmlLoader1.setLocation(getClass().getResource("Dialogues/edit_invoice.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader1.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.setTitle("Edit Invoice");
        EditInvoice a = fxmlLoader1.getController();
        a.show_Invoice(tb.getSelectionModel().getSelectedItem());
        dialog.showAndWait();
        a.edit_invoice();
        update();
    }
    @FXML
    public void delete(){
        if(tb.getSelectionModel().isEmpty()){
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.initOwner(parent.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("Error in edit");
            dialog.setContentText("Please select an item to delete");
            dialog.showAndWait();
            return;
        }
        Bill bill=tb.getSelectionModel().getSelectedItem();
        List<Bill> bills=Bill.getInstance().getBills();
        try{
            Connection connection= DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement=connection.createStatement();
            statement.execute("Delete FROM bills where Name='"+bill.getName()+"'");
        }catch (Exception e){
            e.printStackTrace();
        }
        bills.remove(bill);
        Bill.getInstance().setBills(bills);
        update();
    }
    @FXML
    public void show_cus() throws ArrayIndexOutOfBoundsException{
        ObservableList<Customer> customers=FXCollections.observableArrayList(sh.getItems());
        int index =sh.getSelectionModel().getSelectedIndex();
        if(index<0)
            return;
        Customer customer=customers.get(index);
        consignor.setText(sh.getEditor().getText()+"\n"+customer.getAddress()+", "+customer.getCity()+", "+customer.getState()+"\n"+customer.getGST());
    }
    @FXML
    private JFXButton backButton;
    @FXML
    public void back(){
        try{
            Bill.getInstance().getBills().clear();
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Userscreen.fxml"));
            Stage mainStage=new Stage();
            mainStage.setTitle("Bill facility");
            mainStage.getIcons().add(new Image("file:icon\\bill-icon-13.jpg"));
            mainStage.setScene(new Scene(window1, 800, 600));
            mainStage.show();
            mainStage.setResizable(false);
            Stage close=(Stage)backButton.getScene().getWindow();
            close.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ini(){
        Bill bill=Bill.getInstance().getBills().get(0);
        bill_no.setText(bill.getBill_no());
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * FROM billData where BillNo='" + bill_no.getText() + "'");
            String c[]=resultSet.getString("Consignor").split("\n");
            sh.getEditor().setText(c[0]);
            vendorNo.setText(resultSet.getString("VendorNo"));
            bill_date.getEditor().setText(resultSet.getString("BillDate"));
            consignor.setText(resultSet.getString("Consignor"));
            update();
            resultSet.close();
            statement.close();
            connection.close();

        }catch (SQLException s){s.printStackTrace();}
    }

}
