package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.DbConnection;
import modelo.Favorito;
import modelo.Producto;

public class FavoritoController {
    private DbConnection connection = new DbConnection();
    private ProductoController productoController = new ProductoController();
    
    public int Create(Favorito favorito) {
        int result = 0;
        String req = "INSERT INTO favorito(idProducto,idCliente)" + "VALUES (?, ?)";
        try {
            connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, favorito.getIdProducto());
            statement.setInt(2, favorito.getIdCliente());
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
    
    public int Delete(int id) {
        int result = 0;
        String req = "DELETE FROM favorito WHERE idFavorito=?";
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
    
    public ArrayList<Favorito> Read(){
        ArrayList<Favorito> favoritos = new ArrayList<>();
        String req = "SELECT * FROM favorito";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int favoritoID = resultSet.getInt("idFavorito");
                int productoID = resultSet.getInt("idProducto");
                int clienteID = resultSet.getInt("idCliente");
                favoritos.add(new Favorito(favoritoID,productoID,clienteID));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        }
        finally{
            this.connection.desconectar();
        }
        return favoritos;
    }
    
    public ArrayList<Producto> favoritosCliente(int id) {
        ArrayList<Producto> favoritos = new ArrayList<>();
        String req = "SELECT idProducto FROM favorito WHERE idCliente=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idProd = resultSet.getInt("idProducto");
                favoritos.add(productoController.GetById(idProd));
            }
        } catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return favoritos;
        
    }
    
    public Favorito GetById(int id){
        Favorito favorito = null;
        String req = "SELECT * FROM favorito where idFavorito=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int favoritoID = resultSet.getInt("idFavorito");
                int productoID = resultSet.getInt("idProducto");
                int clienteID = resultSet.getInt("idCliente");
                favorito = new Favorito(favoritoID,productoID,clienteID);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
    }
       return favorito; 
    }
    
    public Favorito GetByClieProd(int idClie, int idProd){
        Favorito favorito = null;
        String req = "SELECT * FROM favorito where idCliente=? AND idProducto=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, idClie);
            statement.setInt(2, idProd);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int favoritoID = resultSet.getInt("idFavorito");
                int productoID = resultSet.getInt("idProducto");
                int clienteID = resultSet.getInt("idCliente");
                favorito = new Favorito(favoritoID,productoID,clienteID);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
    }
       return favorito; 
    }
    
    public boolean tieneFavorito(int id) {
        int resultado = 0;
        String req = "SELECT COUNT(idCliente) AS cliente FROM favorito WHERE idCliente=?";
        try {
           this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultado = resultSet.getInt("cliente");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
        return resultado > 0;
    } 
}
