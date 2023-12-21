module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.model;
    opens com.example.project.model to javafx.fxml;

}