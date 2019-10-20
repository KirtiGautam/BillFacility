package Bill;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewBills implements Initializable {
    @FXML
    private TextField id;
    @FXML
    private JFXListView<String> BillItems;
    @FXML
    private JFXButton backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> x = FXCollections.observableArrayList();
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT BillNo, Status FROM billData");
            while (resultSet.next()) {
                String app="Not Received";
                if(resultSet.getString("Status").equals("Yes"))
                    app="Recieved";
                x.add(resultSet.getString("BillNo")+"                                                  " +
                        "                   "+app);
            }
            BillItems.setItems(x);
            statement.close();
            connection.close();
        }catch (SQLException s){s.getErrorCode();}
    }

    @FXML
    public void populate() throws Exception{
        ObservableList<String> x= FXCollections.observableArrayList();
        Connection connection= DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
        Statement statement=connection.createStatement();
        ResultSet resultSet=statement.executeQuery("SELECT BillNo, Status FROM billData WHERE (BillNo LIKE'%"+id.getText().trim()+"%') OR (BillDate LIKE '%"+id.getText().trim()+"%') OR " +
                "(Consignor LIKE '%"+id.getText().trim()+"%')");
        while (resultSet.next()){
            String ap="Not Received";
            if(resultSet.getString("Status").equals("Yes"))
                ap="Recieved";
            x.add(resultSet.getString("BillNo")+"                                                  " +
                    "                   "+ap);
        }
        BillItems.setItems(x);
        statement.close();
        connection.close();
    }
    @FXML
    public void load(){
        if(BillItems.getSelectionModel().isEmpty())
            return;
        String x[]=BillItems.getSelectionModel().getSelectedItem().split("                      ");
        view.x=x[0];
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("view.fxml"));
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
}
