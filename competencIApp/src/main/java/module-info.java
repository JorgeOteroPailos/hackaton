module com.hackudc.competenciapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hackudc.competenciapp to javafx.fxml;
    exports com.hackudc.competenciapp;
}