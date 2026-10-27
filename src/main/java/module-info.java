module proyecto.tiendaproyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens proyecto.tiendaproyecto to javafx.fxml;
    exports proyecto.tiendaproyecto;
}
