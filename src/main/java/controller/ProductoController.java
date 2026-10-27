package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.DbConnection;
import modelo.Producto;

public class ProductoController {
    private DbConnection connection = new DbConnection();
    
    public ArrayList<Producto> Read(){
        ArrayList<Producto> productos = new ArrayList<>();
        String req = "SELECT * FROM producto";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productoID = resultSet.getInt("idProducto");
                String nombreProducto = resultSet.getString("nombre_Producto");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");
                int subsubID = resultSet.getInt("idSubsub");
                int generoID = resultSet.getInt("idGenero");
                String imagen = resultSet.getString("imagen_nombre");
                productos.add(new Producto(productoID, nombreProducto, descripcion, precio,
                subsubID,generoID,imagen));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        }
        finally{
            this.connection.desconectar();
        }
        return productos;
    }
    
    public Producto GetById(int id){
        Producto producto = null;
        String req = "SELECT * FROM producto where idProducto=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productoID = resultSet.getInt("idProducto");
                String nombreProducto = resultSet.getString("nombre_Producto");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");
                int subsubID = resultSet.getInt("idSubsub");
                int generoID = resultSet.getInt("idGenero");
                String imagen = resultSet.getString("imagen_nombre");
                producto = new Producto(productoID, nombreProducto, descripcion, precio,
                subsubID,generoID,imagen);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
    }
       return producto; 
    }
    
    public Producto GetByName(String name){
        Producto producto = null;
        String req = "SELECT * FROM producto where nombre_Producto=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productoID = resultSet.getInt("idProducto");
                String nombreProducto = resultSet.getString("nombre_Producto");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");
                int subsubID = resultSet.getInt("idSubsub");
                int generoID = resultSet.getInt("idGenero");
                String imagen = resultSet.getString("imagen_nombre");
                producto = new Producto(productoID, nombreProducto, descripcion, precio,
                subsubID,generoID,imagen);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
                this.connection.desconectar();
            }
        }
       return producto; 
    }
    
    public ArrayList<Producto> ReadByName(String name){
        ArrayList<Producto> productos = new ArrayList<>();
        String req = "SELECT * FROM producto WHERE nombre_Producto like ?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setString(1, '%'+name+'%');
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productoID = resultSet.getInt("idProducto");
                String nombreProducto = resultSet.getString("nombre_Producto");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");
                int subsubID = resultSet.getInt("idSubsub");
                int generoID = resultSet.getInt("idGenero");
                String imagen = resultSet.getString("imagen_nombre");
                productos.add(new Producto(productoID, nombreProducto, descripcion, precio,
                subsubID,generoID,imagen));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
                this.connection.desconectar();
            }
        }
       return productos; 
    }
    
    public String getGenero(int id){
        String genero = "";
        String req = "SELECT g.nombre FROM genero g JOIN producto p ON g.idGenero = p.idGenero WHERE p.idProducto = ?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                genero = resultSet.getString("nombre");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
                this.connection.desconectar();
            }
        }
        return genero;
    }
    
    public String getCategoria(int id){
        String categoria = "";
        String req = "SELECT c.nombre_categoria FROM categoria c JOIN subcategoria s ON c.idCategoria = s.idCategoria JOIN subsub ss ON s.idSubcategoria = ss.idSubcategoria JOIN producto p ON ss.idSubsub = p.idSubsub WHERE p.idProducto = ?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categoria = resultSet.getString("nombre_categoria");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
                this.connection.desconectar();
            }
        }
        return categoria;
    }
    
    public String getSubsub(int id){
        String subsub = "";
        String req = "SELECT nombre_subsub FROM subsub ss JOIN producto p ON ss.idSubsub = p.idSubsub WHERE p.idProducto = ?;";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                subsub = resultSet.getString("nombre_subsub");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
                this.connection.desconectar();
            }
        }
        return subsub;
    }
    
}
