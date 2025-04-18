package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GeneroProperty {
    private StringProperty idGenero;
    private StringProperty nombre;

    public GeneroProperty(int idGenero, String nombre) {
        this.idGenero = new SimpleStringProperty(String.valueOf(idGenero));
        this.nombre = new SimpleStringProperty(nombre);
    }

    public StringProperty getIdGenero() {
        return idGenero;
    }

    public StringProperty getNombre() {
        return nombre;
    }
    
    
}
