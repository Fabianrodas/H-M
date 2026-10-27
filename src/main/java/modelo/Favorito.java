package modelo;

public class Favorito {
    private int idFavorito;
    private int idProducto;
    private int idCliente;

    public Favorito(int idFavorito, int idProducto, int idCliente) {
        this.idFavorito = idFavorito;
        this.idProducto = idProducto;
        this.idCliente = idCliente;
    }

    public Favorito(int idProducto, int idCliente) {
        this.idProducto = idProducto;
        this.idCliente = idCliente;
    }
    
    public Favorito(){
    }

    public int getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    
}
