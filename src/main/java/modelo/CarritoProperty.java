package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarritoProperty {
    private StringProperty idCarrito;
    private StringProperty idCliente;
    private StringProperty idProducto;
    private StringProperty cantidad;
    private StringProperty precio;
    private StringProperty talla;

    public CarritoProperty(int idCarrito, int idCliente, int idProducto, int cantidad, double precio, String talla) {
        this.idCarrito = new SimpleStringProperty(String.valueOf(idCarrito));
        this.idCliente = new SimpleStringProperty(String.valueOf(idCliente));
        this.idProducto = new SimpleStringProperty(String.valueOf(idProducto));
        this.cantidad = new SimpleStringProperty(String.valueOf(cantidad));
        this.precio = new SimpleStringProperty(String.valueOf(precio));
        this.talla = new SimpleStringProperty(talla);
    }

    public StringProperty getIdCarrito() {
        return idCarrito;
    }

    public StringProperty getIdCliente() {
        return idCliente;
    }

    public StringProperty getIdProducto() {
        return idProducto;
    }

    public StringProperty getCantidad() {
        return cantidad;
    }

    public StringProperty getPrecio() {
        return precio;
    }

    public StringProperty getTalla() {
        return talla;
    }
    
    
}
