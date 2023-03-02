module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens org.domain to javafx.fxml;
    exports org.domain;
}
