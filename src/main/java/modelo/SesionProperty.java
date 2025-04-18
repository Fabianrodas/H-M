package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SesionProperty {
    private StringProperty idSesion;
    private StringProperty idCliente;

    public SesionProperty(int idSesion, int idCliente) {
        this.idSesion = new SimpleStringProperty(String.valueOf(idSesion));
        this.idCliente = new SimpleStringProperty(String.valueOf(idCliente));
    }

    public StringProperty getIdSesion() {
        return idSesion;
    }

    public StringProperty getIdCliente() {
        return idCliente;
    }
    
    
}
