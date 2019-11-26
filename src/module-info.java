module Bill.facility {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires commons.logging;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires com.jfoenix;
    requires itextpdf;

    opens Bill;
    opens Bill.Data;
    opens Bill.Dialogues;
    opens Bill.Images;
}