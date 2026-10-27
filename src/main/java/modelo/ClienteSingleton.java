package modelo;

public class ClienteSingleton {
    private static ClienteSingleton instancia;
    private Cliente cliente;
    private boolean passwordChangeRequired;

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

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }
    
    public boolean isFull() {
        return this.cliente != null;
    }
    
    public void signOut() {
        this.cliente = null;
        this.passwordChangeRequired = false;
    }
}
