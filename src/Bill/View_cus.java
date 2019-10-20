package Bill;

import Bill.Data.Customer;
import Bill.Dialogues.EditCus;
import Bill.Dialogues.NewCus;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class View_cus implements Initializable {
    @FXML
    private BorderPane use;
    @FXML
    private TableView<Customer> tb;
    @FXML
    private TableColumn<Customer,String> nam;
    @FXML
    private TableColumn<Customer,String> gst;
    @FXML
    private TableColumn<Customer,String> mob;
    @FXML
    private TableColumn<Customer,String> mail;
    @FXML
    private TableColumn<Customer,String> address;
    @FXML
    private TableColumn<Customer,String> city;
    @FXML
    private TableColumn<Customer,String> State;
    @FXML
    private JFXButton backButton;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        nam.setCellValueFactory(new PropertyValueFactory<>("name"));
        gst.setCellValueFactory(new PropertyValueFactory<>("GST"));
        mob.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mail.setCellValueFactory(new PropertyValueFactory<>("e_mail"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        city.setCellValueFactory(new PropertyValueFactory<>("City"));
        State.setCellValueFactory(new PropertyValueFactory<>("State"));
        nam.prefWidthProperty().bind(tb.widthProperty().multiply(20.0/100.0));
        gst.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        mob.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        mail.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        address.prefWidthProperty().bind(tb.widthProperty().multiply(30.0/100.0));
        city.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        State.prefWidthProperty().bind(tb.widthProperty().multiply(10.0/100.0));
        update();
    }
    private void update(){
        tb.setItems(FXCollections.observableArrayList(Customer.getInstance().getCustomers()));
    }
    @FXML
    public void add_new(){
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(use.getScene().getWindow());
        FXMLLoader fxmlLoader1=new FXMLLoader();
        fxmlLoader1.setLocation(getClass().getResource("Dialogues/new_cus.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader1.load());
        }catch (IOException e){
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.setTitle("Add new Customer");
        int flag=-1;
        while (flag==-1) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                NewCus a = fxmlLoader1.getController();
                flag = a.add_new_cus();
                update();
            }
            else
                return;
        }
    }
    @FXML
    public void delete(){
        Customer customer=tb.getSelectionModel().getSelectedItem();
        List<Customer> customers=Customer.getInstance().getCustomers();
        customers.remove(customer);
        Customer.getInstance().setCustomers(customers);
        try{
            Customer.getInstance().write_customers();
        }catch (Exception e){
            e.printStackTrace();
        }
        update();
    }
    @FXML
    public void edit(){
        Customer c=tb.getSelectionModel().getSelectedItem();
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(use.getScene().getWindow());
        FXMLLoader fxmlLoader1=new FXMLLoader();
        fxmlLoader1.setLocation(getClass().getResource("Dialogues/edit_cus.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader1.load());
        }catch (IOException e){
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.setTitle("Edit customer");
        EditCus a=fxmlLoader1.getController();
        a.show_cus(c);
        int flag=-1;
        while (flag==-1) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                flag = a.edit_cus(c);
            }
            else
                return;
        }
        update();
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
