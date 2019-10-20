package Bill;

import Bill.Data.Credentials;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


public class ManageAc implements Initializable {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField pan;
    @FXML
    private JFXTextField mail;
    @FXML
    private JFXTextField gst;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField bank;
    @FXML
    private JFXTextField ifsc;
    @FXML
    private JFXTextField acNo;
    @FXML
    private JFXTextField acName;
    @FXML
    private JFXTextArea add;
    @FXML
    private JFXButton backButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        add.setText(Credentials.getInstance().getAddress());
        username.setText(Credentials.getInstance().getUsername());
        password.setText(Credentials.getInstance().getPassword());
        pan.setText(Credentials.getInstance().getPAN());
        mail.setText(Credentials.getInstance().getE_mail());
        gst.setText(Credentials.getInstance().getGST());
        phone.setText(Credentials.getInstance().getPhone());
        bank.setText(Credentials.getInstance().getBankName());
        ifsc.setText(Credentials.getInstance().getIfscCode());
        acName.setText(Credentials.getInstance().getAcName());
        acNo.setText(Credentials.getInstance().getAcNo());
    }

    @FXML
    public void update(){
        if(username.getText().trim().equals("")||password.getText().trim().equals("")
                ||pan.getText().trim().equals("")||phone.getText().trim().equals("")
                ||gst.getText().trim().equals("")||mail.getText().trim().equals("")){

        }
        else {
            Credentials.getInstance().setPhone(phone.getText());
            Credentials.getInstance().setPAN(pan.getText());
            Credentials.getInstance().setGST(gst.getText());
            Credentials.getInstance().setE_mail(mail.getText());
            Credentials.getInstance().setUsername(username.getText());
            Credentials.getInstance().setPassword(password.getText());
            Credentials.getInstance().setAddress(add.getText());
            Credentials.getInstance().setBankName(bank.getText());
            Credentials.getInstance().setAcName(acName.getText());
            Credentials.getInstance().setAcNo(acNo.getText());
            Credentials.getInstance().setIfscCode(ifsc.getText());
            try{
                Credentials.getInstance().write_credentials();
            }catch (Exception e){e.printStackTrace();}
            Popup popup=new Popup();
            Label label=new Label("Changes made successfully");
            label.setTextFill(Color.RED);
            label.setMinWidth(80);
            label.setMinHeight(50);
            popup.getContent().add(label);
            popup.setAnchorX(890);
            popup.setAnchorY(550);
            popup.setAutoHide(true);
            popup.show(backButton.getScene().getWindow());
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
