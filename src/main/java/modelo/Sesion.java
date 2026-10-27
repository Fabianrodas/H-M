package modelo;

public class Sesion {
    private int idSesion;
    private int idCliente;

    public Sesion(int idSesion, int idCliente) {
        this.idSesion = idSesion;
        this.idCliente = idCliente;
    }

    public Sesion(int idCliente) {
        this.idCliente = idCliente;
    }

    public Sesion() {
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    
}
