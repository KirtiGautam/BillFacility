package Bill.Dialogues;

import Bill.AutoCompleteComboBoxListener;
import Bill.Data.Customer;
import Bill.Main;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class NewCus implements Initializable {
    @FXML
    private JFXTextField nam;
    @FXML
    private JFXTextField gs;
    @FXML
    private JFXTextField mo;
    @FXML
    private JFXTextField ad;
    @FXML
    private JFXTextField mai;
    @FXML
    private JFXComboBox cit;
    @FXML
    private JFXComboBox stat;
    @FXML
    private Label error;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> cities=FXCollections.observableArrayList();
        ObservableList<String> states=FXCollections.observableArrayList();
        try{
            Connection connection= DriverManager.getConnection("jdbc:sqlite:Billsdata\\\\Bills.db");
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM cities");
            while (resultSet.next())
                cities.add(resultSet.getString("city_name"));
            resultSet=statement.executeQuery("SELECT * FROM States");
            while (resultSet.next())
                states.add(resultSet.getString("state"));
            stat.setItems(states);
            cit.setItems(cities);
        }catch (SQLException e){e.printStackTrace();}
        new AutoCompleteComboBoxListener<>(cit);
        new AutoCompleteComboBoxListener<>(stat);
    }

    @FXML
    public int add_new_cus(){
        if(nam.getText().trim().equals("")||gs.getText().trim().equals("")
                ||mo.getText().trim().equals("")||ad.getText().trim().equals("")
                ||cit.getEditor().getText().trim().equals("")||mai.getText().trim().equals("")
                ||stat.getEditor().getText().trim().equals("")){
            error.setText("Please fill all the details");
            return -1;
        }
        String d1=nam.getText().trim();
        String d2=gs.getText().trim();
        String d3=mo.getText().trim();
        String d4=mai.getText().trim();
        String d5=ad.getText().trim();
        String d6=cit.getEditor().getText().trim();
        String d7=stat.getEditor().getText().trim();
        Customer cus=new Customer();
        cus.setName(d1);
        cus.setGST(d2);
        cus.setMobile(d3);
        cus.setE_mail(d4);
        cus.setAddress(d5);
        cus.setCity(d6);
        cus.setState(d7);
        List<Customer> temp=Customer.getInstance().getCustomers();
        temp.add(cus);
        Customer.getInstance().setCustomers(temp);
        try {
            Customer.getInstance().write_customers();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
