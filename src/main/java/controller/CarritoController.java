package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Carrito;
import modelo.DbConnection;
import modelo.Producto;

public class CarritoController {
    private DbConnection connection = new DbConnection();
    private ProductoController productoController = new ProductoController();
    
    public int Create(Carrito carrito) {
        int result = 0;
        String req = "INSERT INTO carrito(idCliente,idProducto,cantidad,precioUnitario,talla)" + "VALUES (?, ?, ?, ?, ?)";
        try {
            connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, carrito.getIdCliente());
            statement.setInt(2, carrito.getIdProducto());
            statement.setInt(3, carrito.getCantidad());
            statement.setDouble(4, carrito.getPrecio());
            statement.setString(5, carrito.getTalla());
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
        String req = "DELETE FROM carrito WHERE idCarrito=?";
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
    
    public int borrarCarritos(int id) {
        int result = 0;
        String req = "DELETE FROM carrito WHERE idCliente=?";
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
    
    public ArrayList<Carrito> Read(){
        ArrayList<Carrito> carritos = new ArrayList<>();
        String req = "SELECT * FROM carrito";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int carritoID = resultSet.getInt("idCarrito");
                int clienteID = resultSet.getInt("idCliente");
                int productoID = resultSet.getInt("idProducto");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precioUnitario");
                String talla = resultSet.getString("talla");
                carritos.add(new Carrito(carritoID,clienteID,productoID,cantidad,precio,talla));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return carritos;
    }

    public Carrito ReadByCliePro(int idClie, int idProd){
        Carrito carrito = null;
        String req = "SELECT * FROM carrito where idCliente=? AND idProducto=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, idClie);
            statement.setInt(2, idProd);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int carritoID = resultSet.getInt("idCarrito");
                int clienteID = resultSet.getInt("idCliente");
                int productoID = resultSet.getInt("idProducto");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precioUnitario");
                String talla = resultSet.getString("talla");
                carrito = new Carrito(carritoID,clienteID,productoID,cantidad,precio,talla);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
        return carrito; 
    }
    
    public Carrito ReadById(int id){
        Carrito carrito = null;
        String req = "SELECT * FROM carrito where idCarrito=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int carritoID = resultSet.getInt("idCarrito");
                int clienteID = resultSet.getInt("idCliente");
                int productoID = resultSet.getInt("idProducto");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precioUnitario");
                String talla = resultSet.getString("talla");
                carrito = new Carrito(carritoID,clienteID,productoID,cantidad,precio,talla);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
        return carrito; 
    }
    
    public ArrayList<Carrito> ReadByCliente(int id){
        ArrayList<Carrito> carrito = new ArrayList<>();
        String req = "SELECT * FROM carrito where idCliente=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int carritoID = resultSet.getInt("idCarrito");
                int clienteID = resultSet.getInt("idCliente");
                int productoID = resultSet.getInt("idProducto");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precioUnitario");
                String talla = resultSet.getString("talla");
                carrito.add(new Carrito(carritoID,clienteID,productoID,cantidad,precio,talla));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
        }
        return carrito; 
    }
    
    
    public boolean tieneCarrito(int id) {
        int resultado = 0;
        String req = "SELECT COUNT(idCliente) AS cliente FROM carrito WHERE idCliente=?";
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
    
    public ArrayList<Producto> productosCliente(int id) {
        ArrayList<Producto> carrito = new ArrayList<>();
        String req = "SELECT idProducto FROM carrito WHERE idCliente=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idProd = resultSet.getInt("idProducto");
                carrito.add(productoController.GetById(idProd));
            }
        } catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return carrito;
        
    }

}
