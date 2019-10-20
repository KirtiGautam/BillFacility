package Bill.Data;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.List;

public class Bill extends RecursiveTreeObject<Bill> {
    private static Bill instance=new Bill();
    private String date;
    private String name;
    private String invoice;
    private String gr;
    private int no_of_packages;
    private float weight;
    private int amount;
    private String bill_no;
    private String shipTo;
    private String misc;
    private List<Bill> bills;

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public String getMisc() {
        return misc;
    }

    public String getShipTo() {
        return shipTo;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public static Bill getInstance() {
        return instance;
    }

    public Bill(){}

    public Bill( String gr,String name,String date, String invoice, int no_of_packages, float weight, int amount) {
        this.date = date;
        this.invoice = invoice;
        this.no_of_packages = no_of_packages;
        this.weight = weight;
        this.amount = amount;
        this.name=name;
        this.gr=gr;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public void setGr(String gr) {
        this.gr = gr;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getGr() {
        return gr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNo_of_packages(int no_of_packages) {
        this.no_of_packages = no_of_packages;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public int getNo_of_packages() {
        return no_of_packages;
    }

    public float getWeight() {
        return weight;
    }

    public int getAmount() {
        return amount;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Bill> getBills() {
        return bills;
    }

    @Override
    public String toString() {
        return name;
    }
}
