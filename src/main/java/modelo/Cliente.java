package modelo;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String correo;
    private String zipcode;
    private String fecha_nacimiento;
    private String contrasena;
    private String genero;
    private String imagen;

    public Cliente(int idCliente, String nombre, String apellido, String correo, String zipcode, String fecha_nacimiento, String contrasena, String genero, String imagen) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.zipcode = zipcode;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.genero = genero;
        this.imagen = imagen;
    }
    
    public Cliente(int idCliente, String nombre, String apellido, String correo, String zipcode, String fecha_nacimiento, String contrasena, String genero) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.zipcode = zipcode;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.genero = genero;
    }

    public Cliente(String nombre, String apellido, String correo, String zipcode, String fecha_nacimiento, String contrasena, String genero) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.zipcode = zipcode;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.genero = genero;
    }

    public Cliente() {
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    
}