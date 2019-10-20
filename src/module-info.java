module Bill.facility {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires org.apache.commons.logging;
    requires pdfbox;
    requires java.desktop;
    requires com.jfoenix;
    requires itextpdf;

    opens Bill;
    opens Bill.Data;
    opens Bill.Dialogues;
    opens Bill.Images;
}