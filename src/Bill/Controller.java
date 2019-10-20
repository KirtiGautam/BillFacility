package Bill;

import Bill.Data.Credentials;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label address;
    @FXML
    private Label GST;
    @FXML
    private Label PAN;
    @FXML
    private Label Phone;
    @FXML
    private Label E_mail;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GST.setText("GST    : "+Credentials.getInstance().getGST());
        PAN.setText("PAN    : "+Credentials.getInstance().getPAN());
        Phone.setText("Phone : "+Credentials.getInstance().getPhone());
        E_mail.setText(" E-mail : "+Credentials.getInstance().getE_mail());
        address.setText(Credentials.getInstance().getAddress());
    }
    private void showPop(String text){
        Popup popup=new Popup();
        Label label=new Label(text);
        label.setTextFill(Color.RED);
        label.setMinWidth(80);
        label.setMinHeight(50);
        popup.getContent().add(label);
        popup.setAnchorX(870);
        popup.setAnchorY(550);
        popup.setAutoHide(true);
        popup.show(Main.parentWindow);
    }
    @FXML
    public void login(){
        if((username.getText().trim().equals(""))||(password.getText().trim().equals(""))){
            showPop("Username or Password wrong");
            return;
        }
        String use=username.getText().trim(),pas=password.getText().trim();
        if((Credentials.getInstance().getUsername().equals(use))&&(Credentials.getInstance().getPassword().equals(pas))){
            try{
                Parent window1;
                window1 = FXMLLoader.load(getClass().getResource("Userscreen.fxml"));
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int width = gd.getDisplayMode().getWidth();
                int height = gd.getDisplayMode().getHeight();
                window1.prefHeight(height);
                window1.prefWidth(width);
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
            showPop("Username or Password wrong");
    }

}
