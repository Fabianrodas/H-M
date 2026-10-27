package modelo;

public class ProductoSingleton {
    private static ProductoSingleton instancia;
    private Producto producto;

    private ProductoSingleton() {
    }

    public static ProductoSingleton getInstance() {
        if (instancia == null) {
            instancia = new ProductoSingleton();
        }
        return instancia;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }
    
    
}