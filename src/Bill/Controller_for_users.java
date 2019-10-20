package Bill;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_for_users {
    @FXML
    private ImageView viewCustomer;
    @FXML
    private ImageView manageAcc;
    @FXML
    private ImageView createBill;
    @FXML
    private ImageView viewBill;

    @FXML
    public void View(){
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Add_customer.fxml"));
            Stage mainStage=new Stage();
            mainStage.setTitle("Bill facility");
            mainStage.getIcons().add(new Image("file:icon\\bill-icon-13.jpg"));
            mainStage.setScene(new Scene(window1, 800, 600));
            mainStage.show();
            mainStage.setResizable(false);
            Stage stage=(Stage) viewCustomer.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void Manage(){
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Manage_ac.fxml"));
            Stage mainStage=new Stage();
            mainStage.setTitle("Bill facility");
            mainStage.getIcons().add(new Image("file:icon\\bill-icon-13.jpg"));
            mainStage.setScene(new Scene(window1, 800, 600));
            mainStage.show();
            mainStage.setResizable(false);
            Stage stage=(Stage) manageAcc.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void new_bill(){
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Create_bill.fxml"));
            Stage mainStage=new Stage();
            mainStage.setTitle("Bill facility");
            mainStage.getIcons().add(new Image("file:icon\\bill-icon-13.jpg"));
            mainStage.setScene(new Scene(window1, 800, 600));
            mainStage.show();
            mainStage.setResizable(false);
            Stage stage=(Stage) createBill.getScene().getWindow();
            stage.close();
//            mainStage = Main.parentWindow;
//            mainStage.getScene().setRoot(window1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void viewBills(){
        try{
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("Viewbills.fxml"));
            Stage mainStage=new Stage();
            mainStage.setTitle("Bill facility");
            mainStage.getIcons().add(new Image("file:icon\\bill-icon-13.jpg"));
            mainStage.setScene(new Scene(window1, 800, 600));
            mainStage.show();
            mainStage.setResizable(false);
            Stage stage=(Stage) viewBill.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
