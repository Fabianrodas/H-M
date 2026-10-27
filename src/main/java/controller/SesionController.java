package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import modelo.DbConnection;
import modelo.Sesion;

public class SesionController {
    private final DbConnection connection = new DbConnection();
    
    public boolean haySesion(){
        int sesiones = 0;
        String req = "SELECT COUNT(idSesion) AS sesiones FROM sesion";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sesiones = resultSet.getInt("sesiones");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
    }
       return sesiones>0;
    }
    
    public Sesion getSesion(){
        Sesion sesion = null;
        String req = "SELECT * FROM sesion";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int sesionID = resultSet.getInt("idsesion");
                int clienteID = resultSet.getInt("idcliente");
                sesion = new Sesion(sesionID,clienteID);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(this.connection != null){    
            this.connection.desconectar();
            }
    }
       return sesion; 
    }
    
    public int setSesion(int idCliente){
        int borrar = Delete();
        if(borrar>0){
            int result = 0;
            String req = "INSERT INTO sesion(idCliente)" + "VALUES (?)";
            try {
                connection.conectar();
                PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
                statement.setInt(1, idCliente);
                result = statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(this.connection != null){
                    this.connection.desconectar();
                }
            }
            return result;
        } else {
            System.out.println("Ocurrio un error borrando la anterior sesión.");
            return 0;
        }
    }
    
    public int Delete() {
        int result;
        try {
            this.connection.conectar();
            try (PreparedStatement statement = this.connection.getConnection()
                    .prepareStatement("DELETE FROM sesion")) {
                result = statement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            this.connection.desconectar();
        }
        return result > 0 ? result : 1;
    }

    public int DeleteByClient(int clienteId) {
        try {
            Connection database = this.connection.conectar();
            return DeleteByClient(database, clienteId);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            this.connection.desconectar();
        }
    }

    public int DeleteByClient(Connection database, int clienteId) throws SQLException {
        try (PreparedStatement statement = database.prepareStatement(
                "DELETE FROM sesion WHERE idcliente = ?")) {
            statement.setInt(1, clienteId);
            return statement.executeUpdate();
        }
    }
    
    public ArrayList<Sesion> Read(){
        ArrayList<Sesion> sesiones = new ArrayList<>();
        String req = "SELECT * FROM sesion";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int sesionID = resultSet.getInt("idsesion");
                int clienteID = resultSet.getInt("idcliente");
                sesiones.add(new Sesion(sesionID,clienteID));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return sesiones;
    }
}
