package modelo;

public class Producto {
    private int idProd;
    private String nombre; 
    private String descripcion;
    private double precio;
    private int idSubsub;
    private int idGenero;
    private String imagen;

    public Producto(int idProd, String nombre, String descripcion, double precio, int idSubsub, int idGenero, String imagen) {
        this.idProd = idProd;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.idSubsub = idSubsub;
        this.idGenero = idGenero;
        this.imagen = imagen;
    }
    
    public Producto(){
    }
    
    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public int getIdSubsub() {
        return idSubsub;
    }

    public void setIdSubsub(int idSubsub) {
        this.idSubsub = idSubsub;
    }

    public int getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
