package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PedidoProperty {
    private StringProperty idPedido;
    private StringProperty idCliente;
    private StringProperty fecha;
    private StringProperty total;

    public PedidoProperty(int idPedido, int idCliente, String fecha, double total) {
        this.idPedido = new SimpleStringProperty(String.valueOf(idPedido));
        this.idCliente = new SimpleStringProperty(String.valueOf(idCliente));
        this.fecha = new SimpleStringProperty(fecha);
        this.total = new SimpleStringProperty(String.valueOf(total));
    }

    public StringProperty getIdPedido() {
        return idPedido;
    }

    public StringProperty getIdCliente() {
        return idCliente;
    }

    public StringProperty getFecha() {
        return fecha;
    }

    public StringProperty getTotal() {
        return total;
    }
    
    
}
