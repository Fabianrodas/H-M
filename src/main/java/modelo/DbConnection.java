package modelo;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    private Connection connection;
    private FileOutputStream fout = null;
    private ObjectInputStream in = null;
    
    public boolean conectar(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectoprueba",leerInfo()[0],leerInfo()[1]);
            return true;
        }catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }
    public void desconectar(){
        try{
            connection.close();
        }catch (SQLException ex){
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE,null,ex);
            }
    }
    
    public Connection getConnection(){
        return connection;
    }
    
    private String[] leerInfo() {
        String rutaArchivo = "mysql_login.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = br.readLine();
            String[] partes = linea.split(",");
            return partes;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return null;
    }
}