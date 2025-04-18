package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Categoria;
import modelo.DbConnection;
import modelo.Genero;

public class OtroController {
    private DbConnection connection = new DbConnection();
    
    public ArrayList<Categoria> ReadCategorias(){
        ArrayList<Categoria> categorias = new ArrayList<>();
        String req = "SELECT c.idCategoria,c.nombre_categoria,s.idSubcategoria,s.nombre_Subcategoria,ss.idSubsub,ss.nombre_Subsub" +
        " FROM Categoria c JOIN Subcategoria s ON c.idCategoria = s.idCategoria JOIN Subsub ss ON s.idSubcategoria = ss.idSubcategoria;";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int categoriaID = resultSet.getInt("idCategoria");
                String nombre_cat = resultSet.getString("nombre_categoria");
                int subcategoriaID = resultSet.getInt("idSubcategoria");
                String nombre_subcat = resultSet.getString("nombre_Subcategoria");
                int subsubID = resultSet.getInt("idSubsub");
                String nombre_subsub = resultSet.getString("nombre_Subsub");
                categorias.add(new Categoria(categoriaID,nombre_cat,subcategoriaID,nombre_subcat,subsubID,nombre_subsub));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return categorias;
    }
    
    public ArrayList<Genero> ReadGeneros(){
        ArrayList<Genero> generos = new ArrayList<>();
        String req = "SELECT * FROM genero";
        try {
            this.connection.conectar();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int generoID = resultSet.getInt("idGenero");
                String nombre = resultSet.getString("nombre");
                generos.add(new Genero(generoID,nombre));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);        
        } finally{
            this.connection.desconectar();
        }
        return generos;
    }
}
