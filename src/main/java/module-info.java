module com.example.cbclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.cbclient to javafx.fxml;
    exports com.example.cbclient;
    exports model;
    exports service;
}