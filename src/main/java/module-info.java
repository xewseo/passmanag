module com.example.passmanag {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.bouncycastle.provider;


    opens com.example.passmanag to javafx.fxml;
    exports com.example.passmanag;
}