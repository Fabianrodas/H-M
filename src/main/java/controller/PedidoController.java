package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Pedido;
import modelo.DbConnection;

public class PedidoController {
    private DbConnection connection = new DbConnection();
    
    public int Create(Pedido pedido) {
        int result = 0;
        String req = "INSERT INTO pedido(idCliente,fecha,total)" + "VALUES (?, ?, ?)";
        try {
            connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, pedido.getIdCliente());
            statement.setString(2, pedido.getFecha());
            statement.setDouble(3, pedido.getTotal());
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
        String req = "DELETE FROM pedido WHERE idPedido=?";
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
    
    public ArrayList<Pedido> Read(){
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String req = "SELECT * FROM pedido";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int pedidoID = resultSet.getInt("idPedido");
                int clienteID = resultSet.getInt("idCliente");
                String fecha = resultSet.getString("fecha");
                double total = resultSet.getDouble("total");
                pedidos.add(new Pedido(pedidoID,clienteID,fecha,total));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return pedidos;
    }
    
    public int ultimaFactura(){
        int factura = 0;
        String req = "SELECT MAX(idPedido) AS factura FROM pedido";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                factura = resultSet.getInt("factura");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return factura;
    }
    
    public int totalPedidos(int id) {
        int pedidos = 0;
        String req = "SELECT COUNT(idPedido) AS pedidosCliente FROM pedido WHERE idCliente=?";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                pedidos = resultSet.getInt("pedidosCliente");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return pedidos;
    
    }
}
