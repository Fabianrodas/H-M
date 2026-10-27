package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoriaProperty {
    private StringProperty idCategoria;
    private StringProperty nombre_Categoria;
    private StringProperty idSubcategoria;
    private StringProperty nombre_Subcategoria;
    private StringProperty idSubsub;
    private StringProperty nombre_Subsub;

    public CategoriaProperty(int idCategoria, String nombre_Categoria, int idSubcategoria, String nombre_Subcategoria, int idSubsub, String nombre_Subsub) {
        this.idCategoria = new SimpleStringProperty(String.valueOf(idCategoria));
        this.nombre_Categoria = new SimpleStringProperty(nombre_Categoria);
        this.idSubcategoria = new SimpleStringProperty(String.valueOf(idSubcategoria));
        this.nombre_Subcategoria = new SimpleStringProperty(nombre_Subcategoria);
        this.idSubsub = new SimpleStringProperty(String.valueOf(idSubsub));
        this.nombre_Subsub = new SimpleStringProperty(nombre_Subsub);
    }

    public StringProperty getIdCategoria() {
        return idCategoria;
    }

    public StringProperty getNombre_Categoria() {
        return nombre_Categoria;
    }

    public StringProperty getIdSubcategoria() {
        return idSubcategoria;
    }

    public StringProperty getNombre_Subcategoria() {
        return nombre_Subcategoria;
    }

    public StringProperty getIdSubsub() {
        return idSubsub;
    }

    public StringProperty getNombre_Subsub() {
        return nombre_Subsub;
    }
    
    
}
