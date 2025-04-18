package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Cliente;
import modelo.DbConnection;

public class ClienteController {
    private DbConnection connection = new DbConnection();
    
    public int Create(Cliente cliente) {
        int result = 0;
        String req = "INSERT INTO cliente(nombre,apellido,correo,zipcode,fecha_nacimiento,contrasena,genero)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getApellido());
            statement.setString(3, cliente.getCorreo());
            statement.setString(4, cliente.getZipcode());
            statement.setString(5, cliente.getFecha_nacimiento());
            statement.setString(6, cliente.getContrasena());
            statement.setString(7, cliente.getGenero());
            result = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(this.connection != null){
                this.connection.desconectar();
            }
        }
        return result;
    }
    
    public ArrayList<Cliente> Read(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        String req = "SELECT * FROM cliente";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int clienteID = resultSet.getInt("idCliente");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String correo = resultSet.getString("correo");
                String zipcode = resultSet.getString("zipcode");
                String fecha_nacimiento = resultSet.getString("fecha_nacimiento");
                String contrasena = resultSet.getString("contrasena");
                String genero = resultSet.getString("genero");
                String imagen = resultSet.getString("imagen_usuario");
                clientes.add(new Cliente(clienteID, nombre, apellido, correo,zipcode, fecha_nacimiento,
                contrasena, genero,imagen));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return clientes;
    }

    public Cliente ReadById(int id){
        Cliente cliente = null;
        String req = "SELECT * FROM cliente where idCliente=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int clienteID = resultSet.getInt("idCliente");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String correo = resultSet.getString("correo");
                String zipcode = resultSet.getString("zipcode");
                String fecha_nacimiento = resultSet.getString("fecha_nacimiento");
                String contrasena = resultSet.getString("contrasena");
                String genero = resultSet.getString("genero");
                cliente = new Cliente(clienteID, nombre, apellido, correo,zipcode, fecha_nacimiento,
                contrasena, genero);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
        return cliente; 
    }
    
    public Cliente ReadByEmail(String email){
        Cliente cliente = null;
        String req = "SELECT * FROM cliente where correo=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int clienteID = resultSet.getInt("idCliente");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String correo = resultSet.getString("correo");
                String zipcode = resultSet.getString("zipcode");
                String fecha_nacimiento = resultSet.getString("fecha_nacimiento");
                String contrasena = resultSet.getString("contrasena");
                String genero = resultSet.getString("genero");
                cliente = new Cliente(clienteID, nombre, apellido, correo,zipcode, fecha_nacimiento,
                contrasena, genero);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
        return cliente; 
    }
    public String getImagenCliente(int idCliente) {
      String imagen = null;
      String req = "SELECT imagen_usuario FROM cliente where idCliente=?";
      try {
          this.connection.conectar();
          PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
          statement.setInt(1, idCliente);
          ResultSet resultSet = statement.executeQuery();
          while (resultSet.next()) {
            String imagen_usuario = resultSet.getString("imagen_usuario");
            imagen = imagen_usuario;
         }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
    return imagen;
    }

    public int Delete(int id) {
        int result = 0;
        String req = "DELETE FROM cliente WHERE idCliente=?";
        try{
            this.connection.conectar();
            PreparedStatement statement =this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            result = statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException (e);
        }
        finally {
            if(this.connection != null){
                this.connection.desconectar();
            }
        }
        return result;
    }
    
    public int UpdateContrasena(int id, String nuevaContra) {
        int result = 0;
        String req = "UPDATE cliente SET contrasena=? WHERE idCliente=?";
        try{
            this.connection.conectar();
            PreparedStatement statement =this.connection.getConnection().prepareStatement(req);
            statement.setString(1, nuevaContra);
            statement.setInt(2, id);
            result = statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException (e);
        }
        finally {
            if(this.connection != null){
                this.connection.desconectar();
            }
        }
        return result;
    }
}
