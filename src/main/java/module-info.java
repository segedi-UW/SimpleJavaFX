module com.sdc.javafx_demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sdc.javafx_demo to javafx.fxml;
    exports com.sdc.javafx_demo;
}