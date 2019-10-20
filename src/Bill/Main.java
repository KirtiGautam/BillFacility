package Bill;

import Bill.Data.Bill;
import Bill.Data.Credentials;
import Bill.Data.Customer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public  static Stage parentWindow;

    @Override
    public void init() {
        Bill.getInstance().setBills(FXCollections.observableArrayList());
        Credentials.getInstance().read_credentials();
        Customer.getInstance().read_customers();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        parentWindow=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Login_screen.fxml"));
        primaryStage.setTitle("Bill facility");
        parentWindow.getIcons().add(new Image("file:icon\\bill-icon-13.jpg"));
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    @Override
    public void stop(){
        Credentials.getInstance().write_credentials();
        Customer.getInstance().write_customers();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
