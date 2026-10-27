package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductoProperty {
    private StringProperty idProd;
    private StringProperty nombre; 
    private StringProperty descripcion;
    private StringProperty precio;
    private StringProperty idSubsub;
    private StringProperty idGenero;
    private StringProperty imagen;

    public ProductoProperty(int idProd, String nombre, String descripcion, double precio, int idSubsub, int idGenero, String imagen) {
        this.idProd = new SimpleStringProperty(String.valueOf(idProd));
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleStringProperty(String.valueOf(precio));
        this.idSubsub = new SimpleStringProperty(String.valueOf(idSubsub));
        this.idGenero = new SimpleStringProperty(String.valueOf(idGenero));
        this.imagen = new SimpleStringProperty(imagen);
    }

    public StringProperty getIdProd() {
        return idProd;
    }

    public StringProperty getNombre() {
        return nombre;
    }

    public StringProperty getDescripcion() {
        return descripcion;
    }

    public StringProperty getPrecio() {
        return precio;
    }

    public StringProperty getIdSubsub() {
        return idSubsub;
    }

    public StringProperty getIdGenero() {
        return idGenero;
    }

    public StringProperty getImagen() {
        return imagen;
    }
    
    
    
    
}
