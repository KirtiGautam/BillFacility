package Bill.Dialogues;


import Bill.Data.Bill;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import Bill.Main;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NewInvoice implements Initializable {
    @FXML
    private JFXTextArea name;
    @FXML
    private JFXTextField invoice;
    @FXML
    private JFXTextField gr;
    @FXML
    private JFXTextField weigh;
    @FXML
    private JFXTextField nop;
    @FXML
    private DatePicker date;
    @FXML
    private JFXTextField amt;
    @FXML
    private JFXTextField consignee;
    @FXML
    private JFXTextField misc;

    public static String billNO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yy");

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
        amt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amt.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        nop.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    nop.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        weigh.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    weigh.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        date.setConverter(converter);

    }

    public int add_invoice(){
        Bill bill=new Bill();
        if(weigh.getText().isEmpty())
            bill.setWeight(0);
        else
            bill.setWeight(Float.valueOf(weigh.getText().trim()));
        if(amt.getText().isEmpty())
            bill.setAmount(0);
        else
            bill.setAmount(Integer.parseInt(amt.getText().trim()));
        if(nop.getText().isEmpty())
            bill.setNo_of_packages(0);
        else
            bill.setNo_of_packages(Integer.parseInt(nop.getText().trim()));
        bill.setMisc(misc.getText().trim());
        bill.setShipTo(consignee.getText().trim());
        bill.setBill_no(billNO);
        bill.setDate(date.getEditor().getText().trim());
        bill.setInvoice(invoice.getText().trim());
        bill.setGr(gr.getText().trim());
        bill.setName(name.getText().trim());
        ObservableList<Bill> bills=FXCollections.observableArrayList(Bill.getInstance().getBills());
        bills.add(bill);
        Bill.getInstance().setBills(bills);
        return 0;
    }
}
