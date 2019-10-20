package Bill.Dialogues;


import Bill.Data.Bill;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import Bill.Main;

public class EditInvoice {
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
    public void show_Invoice(Bill bil){
        name.setText(bil.getName());
        invoice.setText(bil.getInvoice());
        gr.setText(bil.getGr());
        weigh.setText(String.valueOf(bil.getWeight()));
        nop.setText(String.valueOf(bil.getNo_of_packages()));
        date.getEditor().setText(bil.getDate());
        amt.setText(String.valueOf(bil.getAmount()));
        misc.setText(bil.getMisc());
        billNO=bil.getBill_no();
        consignee.setText(bil.getShipTo());
    }

    public int edit_invoice(){
        Bill bill=new Bill();
        bill.setMisc(misc.getText().trim());
        bill.setWeight(Float.valueOf(weigh.getText().trim()));
        bill.setAmount(Integer.parseInt(amt.getText().trim()));
        bill.setNo_of_packages(Integer.parseInt(nop.getText().trim()));
        bill.setBill_no(billNO);
        bill.setDate(date.getEditor().getText().trim());
        bill.setInvoice(invoice.getText().trim());
        bill.setGr(gr.getText().trim());
        bill.setName(name.getText().trim());
        bill.setShipTo(consignee.getText().trim());
        ObservableList<Bill> bills=FXCollections.observableArrayList(Bill.getInstance().getBills());
        bills.add(bill);
        Bill.getInstance().setBills(bills);
        return 0;
    }
}
