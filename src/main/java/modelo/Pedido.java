package modelo;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private String fecha;
    private double total;

    public Pedido(int idPedido, int idCliente, String fecha, double total) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.total = total;
    }

    public Pedido(int idCliente, String fecha, double total) {
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.total = total;
    }

    public Pedido() {
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    
}
