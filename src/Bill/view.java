package Bill;

import Bill.Data.Bill;
import Bill.Data.Customer;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class view implements Initializable {
    @FXML
    private JFXTreeTableView<Bill> tb;
    @FXML
    private TreeTableColumn<Bill,String> date;
    @FXML
    private TreeTableColumn<Bill,String> gr;
    @FXML
    private TreeTableColumn<Bill,String> consignee;
    @FXML
    private TreeTableColumn<Bill,Float> weight;
    @FXML
    private TreeTableColumn<Bill, Integer> amt;
    @FXML
    private TreeTableColumn<Bill,String> name;
    @FXML
    private TreeTableColumn<Bill,String> Invoice;
    @FXML
    private TreeTableColumn<Bill,Integer> nop;
    @FXML
    private JFXTextField billNo;
    @FXML
    private JFXTextArea consignor;
    @FXML
    private JFXTextField dat;
    @FXML
    private JFXTextField Total;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXTextField vendorNo;
    @FXML
    private JFXCheckBox pay;

    private ObservableList<Bill> bills;

    public static String x;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int total=0;
        date.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        weight.setCellValueFactory(new TreeItemPropertyValueFactory<>("weight"));
        amt.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        name.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        Invoice.setCellValueFactory(new TreeItemPropertyValueFactory<>("invoice"));
        gr.setCellValueFactory(new TreeItemPropertyValueFactory<>("gr"));
        nop.setCellValueFactory(new TreeItemPropertyValueFactory<>("no_of_packages"));
        consignee.setCellValueFactory(new TreeItemPropertyValueFactory<>("shipTo"));
        date.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/90.0));
        consignee.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/90.0));
        weight.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/90.0));
        amt.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/90.0));
        name.prefWidthProperty().bind(tb.widthProperty().multiply(20.0/90.0));
        Invoice.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/90.0));
        gr.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/90.0));
        nop.prefWidthProperty().bind(tb.widthProperty().multiply( 10.0/90.0));
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("Select * FROM billData where BillNo='"+x+"'");
            if(resultSet.getString("Status").equals("Yes"))
                pay.setSelected(true);
            dat.appendText(resultSet.getString("BillDate"));
            billNo.setText("Bill No.: "+resultSet.getString("BillNo"));
            vendorNo.setText(resultSet.getString("VendorNo"));
            String to=resultSet.getString("Consignor");
            resultSet= statement.executeQuery("SELECT * FROM bills WHERE Bill_no='"+x+"'");
            bills = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Bill bill = new Bill();
                bill.setBill_no(resultSet.getString("Bill_no"));
                bill.setName(resultSet.getString("Name"));
                bill.setShipTo(resultSet.getString("Consignee"));
                bill.setDate(resultSet.getString("Date"));
                bill.setInvoice(resultSet.getString("Invoice"));
                bill.setGr(resultSet.getString("GR"));
                bill.setAmount(resultSet.getInt("Amount"));
                bill.setNo_of_packages(resultSet.getInt("NOP"));
                bill.setWeight(resultSet.getInt("Weight"));
                total+=resultSet.getInt("Amount");
                bills.add(bill);
            }
            Total.appendText(String.valueOf(total));
            TreeItem<Bill> billTreeItem=new RecursiveTreeItem<>(bills, RecursiveTreeObject::getChildren);
            tb.setRoot(billTreeItem);
            tb.setShowRoot(false);
            consignor.setText(to);
        }catch (Exception e){e.printStackTrace();}
    }
    @FXML
    public void print_b(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if(Prin.print_pdf(x)) {
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
    public void stat() {
        if(pay.isArmed()){
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
                Statement statement = connection.createStatement();
                statement.execute("UPDATE billData SET Status='Yes' WHERE BillNo='"+x+"'");
            }catch (SQLException s){s.printStackTrace();}
        }
        else{
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
                Statement statement = connection.createStatement();
                statement.execute("UPDATE billData SET Status='No' WHERE BillNo='"+x+"'");
            }catch (SQLException s){s.printStackTrace();}
        }
    }


    @FXML
    public void edit(){
        Bill.getInstance().setBills(bills);
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Create_bill.fxml"));
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

    @FXML
    public void back(){
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Viewbills.fxml"));
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
}
