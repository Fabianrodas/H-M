package modelo;

public class ClienteSingleton {
    private static ClienteSingleton instancia;
    private Cliente cliente;

    private ClienteSingleton() {
    }

    public static ClienteSingleton getInstance() {
        if (instancia == null) {
            instancia = new ClienteSingleton();
        }
        return instancia;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }
    
    public boolean isFull() {
        return this.cliente != null;
    }
    
    public void signOut() {
        this.cliente = null;
    }
}
