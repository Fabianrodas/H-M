package modelo;

public class Carrito {
    private int idCarrito;
    private int idCliente;
    private int idProducto;
    private int cantidad;
    private double precio;
    private String talla;

    public Carrito(int idCarrito, int idCliente, int idProducto, int cantidad, double precio, String talla) {
        this.idCarrito = idCarrito;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.talla = talla;
    }

    public Carrito(int idCliente, int idProducto, int cantidad, double precio, String talla) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.talla = talla;
    }

    public Carrito() {
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }
    
    
}
