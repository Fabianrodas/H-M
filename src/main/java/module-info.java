module proyecto.hm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens proyecto.hm to javafx.fxml;
    exports proyecto.hm;
}
