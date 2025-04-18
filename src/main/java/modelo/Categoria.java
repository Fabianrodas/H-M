package modelo;

public class Categoria {
    private int idCategoria;
    private String nombre_Categoria;
    private int idSubcategoria;
    private String nombre_Subcategoria;
    private int idSubsub;
    private String nombre_Subsub;

    public Categoria(int idCategoria, String nombre_Categoria, int idSubcategoria, String nombre_Subcategoria, int idSubsub, String nombre_Subsub) {
        this.idCategoria = idCategoria;
        this.nombre_Categoria = nombre_Categoria;
        this.idSubcategoria = idSubcategoria;
        this.nombre_Subcategoria = nombre_Subcategoria;
        this.idSubsub = idSubsub;
        this.nombre_Subsub = nombre_Subsub;
    }

    public String getNombre_Subsub() {
        return nombre_Subsub;
    }

    public void setNombre_Subsub(String nombre_Subsub) {
        this.nombre_Subsub = nombre_Subsub;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre_Categoria() {
        return nombre_Categoria;
    }

    public void setNombre_Categoria(String nombre_Categoria) {
        this.nombre_Categoria = nombre_Categoria;
    }

    public int getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(int idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public String getNombre_Subcategoria() {
        return nombre_Subcategoria;
    }

    public void setNombre_Subcategoria(String nombre_Subcategoria) {
        this.nombre_Subcategoria = nombre_Subcategoria;
    }

    public int getIdSubsub() {
        return idSubsub;
    }

    public void setIdSubsub(int idSubsub) {
        this.idSubsub = idSubsub;
    }
    
    
}
