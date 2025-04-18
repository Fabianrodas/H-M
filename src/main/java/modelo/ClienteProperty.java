package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClienteProperty {
    private StringProperty idCliente;
    private StringProperty nombre;
    private StringProperty apellido;
    private StringProperty correo;
    private StringProperty zipcode;
    private StringProperty fecha_nacimiento;
    private StringProperty contrasena;
    private StringProperty genero;
    private StringProperty imagen;

    public ClienteProperty(int idCliente, String nombre, String apellido, String correo, String zipcode, String fecha_nacimiento, String contrasena, String genero, String imagen) {
        this.idCliente = new SimpleStringProperty(String.valueOf(idCliente));
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.correo = new SimpleStringProperty(correo);
        this.zipcode = new SimpleStringProperty(zipcode);
        this.fecha_nacimiento = new SimpleStringProperty(fecha_nacimiento);
        this.contrasena = new SimpleStringProperty(contrasena);
        this.genero = new SimpleStringProperty(genero);
        this.imagen = new SimpleStringProperty(imagen);
    }

    public StringProperty getIdCliente() {
        return idCliente;
    }

    public StringProperty getNombre() {
        return nombre;
    }

    public StringProperty getApellido() {
        return apellido;
    }

    public StringProperty getCorreo() {
        return correo;
    }

    public StringProperty getZipcode() {
        return zipcode;
    }

    public StringProperty getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public StringProperty getContrasena() {
        return contrasena;
    }

    public StringProperty getGenero() {
        return genero;
    }

    public StringProperty getImagen() {
        return imagen;
    }
    
    
}
