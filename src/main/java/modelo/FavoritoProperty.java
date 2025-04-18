package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FavoritoProperty {
    private StringProperty idFavorito;
    private StringProperty idProducto;
    private StringProperty idCliente;

    public FavoritoProperty(int idFavorito, int idProducto, int idCliente) {
        this.idFavorito = new SimpleStringProperty(String.valueOf(idFavorito));
        this.idProducto = new SimpleStringProperty(String.valueOf(idProducto));
        this.idCliente = new SimpleStringProperty(String.valueOf(idCliente));
    }

    public StringProperty getIdFavorito() {
        return idFavorito;
    }

    public StringProperty getIdProducto() {
        return idProducto;
    }

    public StringProperty getIdCliente() {
        return idCliente;
    }
    
}
