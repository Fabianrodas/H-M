package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.DbConnection;
import modelo.Sesion;

public class SesionController {
    private DbConnection connection = new DbConnection();
    
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
                int sesionID = resultSet.getInt("idSesion");
                int clienteID = resultSet.getInt("idCliente");
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
        if(haySesion()){
            int result = 0;
            String req = "DELETE FROM sesion WHERE idSesion BETWEEN 1 AND 500;";
            try{
                this.connection.conectar();
                PreparedStatement statement =this.connection.getConnection().prepareStatement(req);
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
        } else {
            return 1;
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
                int sesionID = resultSet.getInt("idSesion");
                int clienteID = resultSet.getInt("idCliente");
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
