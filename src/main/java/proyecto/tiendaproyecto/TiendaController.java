package proyecto.tiendaproyecto;

import controller.CarritoController;
import controller.ClienteController;
import controller.FavoritoController;
import controller.OtroController;
import controller.PedidoController;
import controller.ProductoController;
import controller.SesionController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;

public class TiendaController implements Initializable {
        
    @FXML private Rectangle rectanguloPie;
    @FXML private MenuItem bd, about;
    @FXML private ImageView logoHM, imagen_producto, imagenPie;
    @FXML private Button btnNext, btnPrevious, btnsearch, btnsignin, btnFavorito, btnxs, btns, btnm, btnl, btnxl, btnxxl, btnPedido;
    @FXML private Label cliente_nombre, labelLlena, preciototal, labelCantidad, nombre_producto, descripcion_producto,
    precio_producto, labelPie, labelGenero, labelSeccion, labelNom_producto, labelid_producto;

    @FXML private Text baby_baby, baby_newborn, babyboy_clothing, babyboy_outerwear,babyboy_shoes, babygirl_clothing, babygirl_outerwear, babygirl_shoes,boy1_clothing, boy1_outerwear, 
    boy1_shoes, boy2_clothing, boy2_outerwear, boy2_shoes,girl1_clothing, girl1_outerwear, girl1_shoes, girl2_clothing, girl2_outerwear,girl2_shoes,kids1, kids2, men_clothes, men_dress_shoes, men_gym, men_hoodies,men_jeans, men_pants, men_running, men_shirts, men_shoes, 
    men_sneakers, men_sport_shorts,men_sports, newborn_clothing, newborn_outerwear, newborn_shoes, women_blouses, women_boots, women_clothes, women_dresses, women_gym, women_jeans, women_leggings, women_running, women_shoes, women_sneakers, women_sportbra, women_sports, women_tops, women2, men2,baby2,kid;
    @FXML private Label baby_baby2, baby_newborn2, babyboy_clothing2, babyboy_outerwear2, babyboy_shoes2,babygirl_clothing2, babygirl_outerwear2, babygirl_shoes2, boy1_clothing2, boy1_outerwear2, 
    boy1_shoes2, boy2_clothing2, boy2_outerwear2, boy2_shoes2, girl1_clothing2, girl1_outerwear2, girl1_shoes2, girl2_clothing2, girl2_outerwear2, girl2_shoes2, kids12, kids22, men_clothes2, men_dress_shoes2, men_gym2, men_hoodies2, men_jeans2, men_pants2, men_running2, men_shirts2, 
    men_shoes2, men_sneakers2, men_sport_shorts2, men_sports2, newborn_clothing2, newborn_outerwear2, newborn_shoes2, women_blouses2, women_boots2, women_clothes2, women_dresses2, women_gym2,women_jeans2, women_leggings2, women_running2, women_shoes2, women_sneakers2, women_sportbra2, 
    women_sports2, women_tops2, baby, women, kids, men, bd2, about2, member, shopping_bag, signin, favorites;
    @FXML private Label nombre1, nombre2, nombre3, nombre4, nombre5, nombre6, nombre7, nombre8, nombre9, nombre10,nombre11, nombre12, nombre13, nombre14, nombre15, nombre16, nombre17, nombre18, nombre19, nombre20,nombre21, nombre22, nombre23, nombre24, nombre25, nombre26, nombre27, nombre28, nombre29, nombre30,
    nombre31, nombre32, nombre33, nombre34, nombre35, nombre36, nombre37, nombre38, nombre39, nombre40,nombre41, nombre42, nombre43, nombre44, nombre45, nombre46, nombre47, nombre48, nombre49, nombre50,nombre51, nombre52, nombre53, nombre54, nombre55, nombre56, nombre57, nombre58, nombre59, nombre60,
    nombre61, nombre62, nombre63, nombre64, nombre65, nombre66, nombre67, nombre68, nombre69, nombre70,nombre71, nombre72, nombre73, nombre74, nombre75, nombre76, nombre77, nombre78, nombre79, nombre80,nombre81, nombre82, nombre83, nombre84, nombre85, nombre86, nombre87, nombre88, nombre89, nombre90,
    nombre91, nombre92, nombre93, nombre94, nombre95, nombre96, nombre97, nombre98, nombre99, nombre100,nombre101, nombre102, nombre103, nombre104, nombre105, nombre106, nombre107, nombre108, nombre109, nombre110,nombre111, nombre112, nombre113, nombre114, nombre115, nombre116, nombre117, nombre118, nombre119, nombre120;
    @FXML private Label precio1, precio2, precio3, precio4, precio5, precio6, precio7, precio8, precio9, precio10,precio11, precio12, precio13, precio14, precio15, precio16, precio17, precio18, precio19, precio20,precio21, precio22, precio23, precio24, precio25, precio26, precio27, precio28, precio29, precio30,
    precio31, precio32, precio33, precio34, precio35, precio36, precio37, precio38, precio39, precio40,precio41, precio42, precio43, precio44, precio45, precio46, precio47, precio48, precio49, precio50,precio51, precio52, precio53, precio54, precio55, precio56, precio57, precio58, precio59, precio60,
    precio61, precio62, precio63, precio64, precio65, precio66, precio67, precio68, precio69, precio70,precio71, precio72, precio73, precio74, precio75, precio76, precio77, precio78, precio79, precio80,precio81, precio82, precio83, precio84, precio85, precio86, precio87, precio88, precio89, precio90,
    precio91, precio92, precio93, precio94, precio95, precio96, precio97, precio98, precio99, precio100,precio101, precio102, precio103, precio104, precio105, precio106, precio107, precio108, precio109, precio110,precio111, precio112, precio113, precio114, precio115, precio116, precio117, precio118, precio119, precio120;
    @FXML private ImageView imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen10,imagen11, imagen12, imagen13, imagen14, imagen15, imagen16, imagen17, imagen18, imagen19, imagen20,imagen21, imagen22, imagen23, imagen24, imagen25, imagen26, imagen27, imagen28, imagen29, imagen30,
    imagen31, imagen32, imagen33, imagen34, imagen35, imagen36, imagen37, imagen38, imagen39, imagen40,imagen41, imagen42, imagen43, imagen44, imagen45, imagen46, imagen47, imagen48, imagen49, imagen50,imagen51, imagen52, imagen53, imagen54, imagen55, imagen56, imagen57, imagen58, imagen59, imagen60,
    imagen61, imagen62, imagen63, imagen64, imagen65, imagen66, imagen67, imagen68, imagen69, imagen70,imagen71, imagen72, imagen73, imagen74, imagen75, imagen76, imagen77, imagen78, imagen79, imagen80,imagen81, imagen82, imagen83, imagen84, imagen85, imagen86, imagen87, imagen88, imagen89, imagen90,
    imagen91, imagen92, imagen93, imagen94, imagen95, imagen96, imagen97, imagen98, imagen99, imagen100,imagen101, imagen102, imagen103, imagen104, imagen105, imagen106, imagen107, imagen108, imagen109, imagen110,imagen111, imagen112, imagen113, imagen114, imagen115, imagen116, imagen117, imagen118, imagen119, imagen120;
   
    private ProductoController productoController;
    private FavoritoController favoritoController = new FavoritoController();
    private CarritoController carritoController = new CarritoController();
    private SesionController sesionController = new SesionController();
    private ClienteController clienteController = new ClienteController();
    private ArrayList<Producto> productos;
    private Producto producto;
    private Cliente cliente;
    @FXML private VBox vboxResultados, vboxFavoritos, vboxSave, vboxScroll; 
    @FXML private AnchorPane anchorBuscar, anchorFavorito, anchorShopping;
    @FXML private HBox hboxPie;
    @FXML private StackPane stackPane;
    @FXML private TextField txtbuscar;
    private String talla = "";
    private double total;
    private int prod;
    @FXML private Label nombreTablaLabel;
    private int currentTableIndex = 0; 
    private ArrayList<String> tableNames;
    
    @FXML private TableView<ProductoProperty> tablaProducto;
    @FXML private TableColumn<ProductoProperty, String> cidproducto;
    @FXML private TableColumn<ProductoProperty, String> cnombre_producto;
    @FXML private TableColumn<ProductoProperty, String> cdescripcion;
    @FXML private TableColumn<ProductoProperty, String> cprecio;
    @FXML private TableColumn<ProductoProperty, String> cidsubsub;
    @FXML private TableColumn<ProductoProperty, String> cidgeneroproducto;
    @FXML private TableColumn<ProductoProperty, String> cimagen_nombre;
    private ObservableList<ProductoProperty> olProducto = FXCollections.observableArrayList();
    @FXML private TableView<ClienteProperty> tablaCliente;
    @FXML private TableColumn<ClienteProperty, String> cidcliente;
    @FXML private TableColumn<ClienteProperty, String> cnombrecliente;
    @FXML private TableColumn<ClienteProperty, String> capellido;
    @FXML private TableColumn<ClienteProperty, String> ccorreo;
    @FXML private TableColumn<ClienteProperty, String> czipcode;
    @FXML private TableColumn<ClienteProperty, String> cfecha_nacimiento;
    @FXML private TableColumn<ClienteProperty, String> ccontrasena;
    @FXML private TableColumn<ClienteProperty, String> cgenero;
    @FXML private TableColumn<ClienteProperty, String> cimagen_usuario;
    private ObservableList<ClienteProperty> olCliente = FXCollections.observableArrayList();
    @FXML private TableView<CarritoProperty> tablaCarrito;
    @FXML private TableColumn<CarritoProperty, String> cidcarrito;
    @FXML private TableColumn<CarritoProperty, String> cidclientecarrito;
    @FXML private TableColumn<CarritoProperty, String> cidproductocarrito;
    @FXML private TableColumn<CarritoProperty, String> ccantidad;
    @FXML private TableColumn<CarritoProperty, String> cpreciounitario;
    @FXML private TableColumn<CarritoProperty, String> ctalla;
    private ObservableList<CarritoProperty> olCarrito = FXCollections.observableArrayList();
    @FXML private TableView<FavoritoProperty> tablaFavorito;
    @FXML private TableColumn<FavoritoProperty, String> cidfavorito;
    @FXML private TableColumn<FavoritoProperty, String> cidproductofavorito;
    @FXML private TableColumn<FavoritoProperty, String> cidclientefavorito;
    private ObservableList<FavoritoProperty> olFavorito = FXCollections.observableArrayList();
    @FXML private TableView<CategoriaProperty> tablaCategoria;
    @FXML private TableColumn<CategoriaProperty, String> cidcategoria;
    @FXML private TableColumn<CategoriaProperty, String> cnombre_categoria;
    @FXML private TableColumn<CategoriaProperty, String> cidsubcategoria;
    @FXML private TableColumn<CategoriaProperty, String> cnombre_subcategoria;
    @FXML private TableColumn<CategoriaProperty, String> cidsubsubcategoria;
    @FXML private TableColumn<CategoriaProperty, String> cnombre_subsub;
    private ObservableList<CategoriaProperty> olCategoria = FXCollections.observableArrayList();
    @FXML private TableView<SesionProperty> tablaSesion;
    @FXML private TableColumn<SesionProperty, String> cidsesion;
    @FXML private TableColumn<SesionProperty, String> cidclientesesion;
    private ObservableList<SesionProperty> olSesion = FXCollections.observableArrayList();
    @FXML private TableView<GeneroProperty> tablaGenero;
    @FXML private TableColumn<GeneroProperty, String> cidgenero;
    @FXML private TableColumn<GeneroProperty, String> cnombregenero;
    private ObservableList<GeneroProperty> olGenero = FXCollections.observableArrayList();
    @FXML TableView<PedidoProperty> tablaPedido;
    @FXML private TableColumn<PedidoProperty, String> cidpedido;
    @FXML private TableColumn<PedidoProperty, String> cidclientepedido;
    @FXML private TableColumn<PedidoProperty, String> cfechapedido;
    @FXML private TableColumn<PedidoProperty, String> ctotalpedido;
    private ObservableList<PedidoProperty> olPedido = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        
        prod = 0;
        
        List<Text> textos = Arrays.asList(baby_baby, baby_newborn, babyboy_clothing, babyboy_outerwear,babyboy_shoes, babygirl_clothing, babygirl_outerwear, babygirl_shoes,boy1_clothing, boy1_outerwear, 
        boy1_shoes, boy2_clothing, boy2_outerwear, boy2_shoes,girl1_clothing, girl1_outerwear, girl1_shoes, girl2_clothing, girl2_outerwear,girl2_shoes,kids1, kids2, men_clothes, men_dress_shoes, men_gym, men_hoodies,men_jeans, men_pants, men_running, men_shirts, men_shoes, 
        men_sneakers, men_sport_shorts,men_sports, newborn_clothing, newborn_outerwear, newborn_shoes, women_blouses, women_boots, women_clothes, women_dresses, women_gym, women_jeans, women_leggings, women_running, women_shoes, women_sneakers, women_sportbra, women_sports, women_tops, women2, men2,baby2,kid);
        List<String> fxmlTextos = Arrays.asList("baby_baby", "baby_newborn", "babyboy_clothing", "babyboy_outerwear", "babyboy_shoes", "babygirl_clothing", "babygirl_outerwear", "babygirl_shoes", "boy1_clothing", "boy1_outerwear", "boy1_shoes", "boy2_clothing", "boy2_outerwear", 
        "boy2_shoes", "girl1_clothing", "girl1_outerwear", "girl1_shoes", "girl2_clothing", "girl2_outerwear", "girl2_shoes", "kids1", "kids2", "men_clothes", "men_dress_shoes", "men_gym", "men_hoodies", "men_jeans", "men_pants", "men_running", "men_shirts", "men_shoes", "men_sneakers", "men_sport_shorts", "men_sports", 
        "newborn_clothing", "newborn_outerwear", "newborn_shoes", "women_blouses", "women_boots", "women_clothes", "women_dresses", "women_gym", "women_jeans", "women_leggings", "women_running", "women_shoes", "women_sneakers", "women_sportbra", "women_sports", "women_tops","women", "men", "baby", "kids");
        
        for(int i=0;i<textos.size();i++){
            initializeText(textos.get(i),fxmlTextos.get(i));
        }
        
        List<Label> labels = Arrays.asList(baby_baby2, baby_newborn2, babyboy_clothing2, babyboy_outerwear2, babyboy_shoes2,babygirl_clothing2, babygirl_outerwear2, babygirl_shoes2, boy1_clothing2, boy1_outerwear2, boy1_shoes2, boy2_clothing2, boy2_outerwear2, boy2_shoes2, girl1_clothing2, girl1_outerwear2, girl1_shoes2, 
        girl2_clothing2, girl2_outerwear2, girl2_shoes2, kids12, kids22, men_clothes2, men_dress_shoes2, men_gym2, men_hoodies2, men_jeans2, men_pants2, men_running2, men_shirts2, men_shoes2, men_sneakers2, men_sport_shorts2, men_sports2, newborn_clothing2, newborn_outerwear2, newborn_shoes2, women_blouses2, women_boots2, 
        women_clothes2, women_dresses2, women_gym2,women_jeans2, women_leggings2, women_running2, women_shoes2, women_sneakers2, women_sportbra2, women_sports2, women_tops2, baby, women, kids, men, bd2, about2, member, shopping_bag, signin, favorites);
        List<String> fxmlLabels = Arrays.asList("baby_baby", "baby_newborn", "babyboy_clothing", "babyboy_outerwear", "babyboy_shoes", "babygirl_clothing", "babygirl_outerwear", "babygirl_shoes", "boy1_clothing", "boy1_outerwear", "boy1_shoes", "boy2_clothing", "boy2_outerwear", "boy2_shoes", "girl1_clothing", 
        "girl1_outerwear", "girl1_shoes", "girl2_clothing", "girl2_outerwear", "girl2_shoes", "kids1", "kids2", "men_clothes", "men_dress_shoes", "men_gym", "men_hoodies", "men_jeans", "men_pants", "men_running", "men_shirts", "men_shoes", "men_sneakers", "men_sport_shorts", "men_sports", "newborn_clothing", 
        "newborn_outerwear", "newborn_shoes", "women_blouses", "women_boots", "women_clothes", "women_dresses", "women_gym", "women_jeans", "women_leggings", "women_running", "women_shoes", "women_sneakers", "women_sportbra", "women_sports", "women_tops", "baby", "women", "kids", "men", "bd", "about", "become_member", "shopping_bag", "signin", "favorites");
        
        for(int i=0;i<labels.size();i++){
            initializeLabel(labels.get(i),fxmlLabels.get(i));
        }
        
        initializeMenuItem(bd, "bd");
        initializeMenuItem(about, "about");
        ventanaNueva(member, "become_member");
        
        productoController = new ProductoController();
        productos = productoController.Read();
        
        List<Label> nombres = Arrays.asList(nombre1, nombre2, nombre3, nombre4, nombre5, nombre6, nombre7, nombre8, nombre9, nombre10,nombre11, nombre12, nombre13, nombre14, nombre15, nombre16, nombre17, nombre18, nombre19, nombre20,nombre21, nombre22, nombre23, nombre24, nombre25, nombre26, nombre27, nombre28, nombre29, nombre30,
        nombre31, nombre32, nombre33, nombre34, nombre35, nombre36, nombre37, nombre38, nombre39, nombre40,nombre41, nombre42, nombre43, nombre44, nombre45, nombre46, nombre47, nombre48, nombre49, nombre50,nombre51, nombre52, nombre53, nombre54, nombre55, nombre56, nombre57, nombre58, nombre59, nombre60,
        nombre61, nombre62, nombre63, nombre64, nombre65, nombre66, nombre67, nombre68, nombre69, nombre70,nombre71, nombre72, nombre73, nombre74, nombre75, nombre76, nombre77, nombre78, nombre79, nombre80,nombre81, nombre82, nombre83, nombre84, nombre85, nombre86, nombre87, nombre88, nombre89, nombre90,
        nombre91, nombre92, nombre93, nombre94, nombre95, nombre96, nombre97, nombre98, nombre99, nombre100,nombre101, nombre102, nombre103, nombre104, nombre105, nombre106, nombre107, nombre108, nombre109, nombre110,nombre111, nombre112, nombre113, nombre114, nombre115, nombre116, nombre117, nombre118, nombre119, nombre120);
        List<Label> precios = Arrays.asList(precio1, precio2, precio3, precio4, precio5, precio6, precio7, precio8, precio9, precio10,precio11, precio12, precio13, precio14, precio15, precio16, precio17, precio18, precio19, precio20,precio21, precio22, precio23, precio24, precio25, precio26, precio27, precio28, precio29, precio30,
        precio31, precio32, precio33, precio34, precio35, precio36, precio37, precio38, precio39, precio40,precio41, precio42, precio43, precio44, precio45, precio46, precio47, precio48, precio49, precio50,precio51, precio52, precio53, precio54, precio55, precio56, precio57, precio58, precio59, precio60,
        precio61, precio62, precio63, precio64, precio65, precio66, precio67, precio68, precio69, precio70,precio71, precio72, precio73, precio74, precio75, precio76, precio77, precio78, precio79, precio80,precio81, precio82, precio83, precio84, precio85, precio86, precio87, precio88, precio89, precio90,
        precio91, precio92, precio93, precio94, precio95, precio96, precio97, precio98, precio99, precio100,precio101, precio102, precio103, precio104, precio105, precio106, precio107, precio108, precio109, precio110,precio111, precio112, precio113, precio114, precio115, precio116, precio117, precio118, precio119, precio120);
        List<ImageView> imagenes = Arrays.asList(imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen10,imagen11, imagen12, imagen13, imagen14, imagen15, imagen16, imagen17, imagen18, imagen19, imagen20,imagen21, imagen22, imagen23, imagen24, imagen25, imagen26, imagen27, imagen28, imagen29, imagen30,
        imagen31, imagen32, imagen33, imagen34, imagen35, imagen36, imagen37, imagen38, imagen39, imagen40,imagen41, imagen42, imagen43, imagen44, imagen45, imagen46, imagen47, imagen48, imagen49, imagen50,imagen51, imagen52, imagen53, imagen54, imagen55, imagen56, imagen57, imagen58, imagen59, imagen60,
        imagen61, imagen62, imagen63, imagen64, imagen65, imagen66, imagen67, imagen68, imagen69, imagen70,imagen71, imagen72, imagen73, imagen74, imagen75, imagen76, imagen77, imagen78, imagen79, imagen80,imagen81, imagen82, imagen83, imagen84, imagen85, imagen86, imagen87, imagen88, imagen89, imagen90,
        imagen91, imagen92, imagen93, imagen94, imagen95, imagen96, imagen97, imagen98, imagen99, imagen100,imagen101, imagen102, imagen103, imagen104, imagen105, imagen106, imagen107, imagen108, imagen109, imagen110,imagen111, imagen112, imagen113, imagen114, imagen115, imagen116, imagen117, imagen118, imagen119, imagen120);

        nombrarProductos(nombres, productos);
        precioProductos(precios, productos);
        
        signin.setOnMouseClicked(e -> {
        if(ClienteSingleton.getInstance().isFull()) {
            try {
                App.setRoot("account");
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("signin" + ".fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
                    double x = 550;
                    double y = 200;
                    stage.setX(x);
                    stage.setY(y);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }  
        }
        });
        
        for(int i = 0; i<imagenes.size(); i++){
            comprarProductos(imagenes.get(i),nombres.get(i));
        }
        
        if(sesionController.haySesion()){
            cliente = clienteController.ReadById(sesionController.getSesion().getIdCliente());
            ClienteSingleton.getInstance().setCliente(cliente);
        } else {
            cliente = ClienteSingleton.getInstance().getCliente();
        }
        
        producto = ProductoSingleton.getInstance().getProducto();
        if (producto != null) {
            if((nombre_producto != null) && (descripcion_producto != null) && (precio_producto != null
            && labelGenero != null && labelSeccion != null && labelNom_producto != null
            && labelid_producto != null)) {
                nombre_producto.setText(producto.getNombre());
                descripcion_producto.setText(producto.getDescripcion());
                precio_producto.setText("$" + producto.getPrecio());
                imagen_producto.setImage(new Image("/imagenes/"+producto.getImagen()));
                labelGenero.setText(productoController.getGenero(producto.getIdProd()));
                labelSeccion.setText(productoController.getCategoria(producto.getIdProd()));
                labelNom_producto.setText(producto.getNombre());
                labelid_producto.setText(String.valueOf(producto.getIdProd()));
            }
        }
        
        logoHM.setOnMouseClicked(e -> {
            try {
                App.setRoot("tienda");
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        if (btnsearch != null) {
            btnsearch.setOnAction(e -> {
                try {
                    App.setRoot("buscar_productos");
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        
         if (btnsignin != null) {
            btnsignin.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("signin" + ".fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
                    double x = 550;
                    double y = 200;
                    stage.setX(x);
                    stage.setY(y);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        
        for (int i = 0; i < nombres.size(); i++) {
            if (nombres.get(i) != null) {
                nombres.get(i).setMaxWidth(175);
            }
        }
        
        if(ClienteSingleton.getInstance().isFull()) {
            if (signin != null){
                 signin.setText("👤 Account");
                 signin.setPrefWidth(100);
            }
        }
        
        if(cliente != null){
            ArrayList<Producto> pr = carritoController.productosCliente(cliente.getIdCliente());
            for(Producto p : pr){
                prod++;
            }
        }
        shopping_bag.setText("👜 Shopping Bag ("+prod+")");
        
        if(cliente != null){
            if(carritoController.tieneCarrito(cliente.getIdCliente())) {
                if(vboxScroll != null && preciototal != null && cliente_nombre != null) {
                    ArrayList<Producto> carrito = carritoController.productosCliente(cliente.getIdCliente());
                    cliente_nombre.setText(cliente.getNombre());
                    insertarCarrito(carrito);
                }
            }
            if(favoritoController.tieneFavorito(cliente.getIdCliente())) {
                if(vboxSave != null && vboxFavoritos != null) {
                    ArrayList<Producto> productosFavoritos = favoritoController.favoritosCliente(cliente.getIdCliente());
                    insertarFavorito(productosFavoritos);
                }
            }
            shopping_bag.setOnMouseClicked(e -> {
                try {
                    App.setRoot("shopping");
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            
            if(btnPedido != null){
                if(this.prod != 0) {
                    btnPedido.setOnMouseClicked(e -> {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("pedido" + ".fxml"));
                            Scene scene = new Scene(fxmlLoader.load());
                            Stage stage = new Stage();
                            scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
                            stage.setScene(scene);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
                            double x = 550;
                            double y = 200;
                            stage.setX(x);
                            stage.setY(y);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else {
                    btnPedido.setOnMouseClicked(e -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Pedido");
                        alert.setHeaderText("Comprar productos del carrito");
                        alert.setContentText("Llena de productos H&M tu carrito para poder comprar.");
                        alert.showAndWait();
                    });
                }    
            } 
        }
        
        if(btnxs != null && btns != null && btnm != null && btnl != null && btnxl != null && btnxxl != null) {
            btnxs.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
            btnxs.setOnMouseClicked(e -> botonTalla(btnxs,"XS",Arrays.asList(btns,btnm,btnl,btnxl,btnxxl)));
            btns.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
            btns.setOnMouseClicked(e -> botonTalla(btns,"S",Arrays.asList(btnxs,btnm,btnl,btnxl,btnxxl)));
            btnm.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
            btnm.setOnMouseClicked(e -> botonTalla(btnm,"M",Arrays.asList(btnxs,btns,btnl,btnxl,btnxxl)));
            btnl.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
            btnl.setOnMouseClicked(e -> botonTalla(btnl,"L",Arrays.asList(btnxs,btns,btnm,btnxl,btnxxl)));
            btnxl.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
            btnxl.setOnMouseClicked(e -> botonTalla(btnxl,"XL",Arrays.asList(btnxs,btns,btnm,btnl,btnxxl)));
            btnxxl.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
            btnxxl.setOnMouseClicked(e -> botonTalla(btnxxl,"XXL",Arrays.asList(btnxs,btns,btnm,btnl,btnxl)));
        }
        
        
        if(btnFavorito != null){
            ArrayList<Producto> productosFavoritos = favoritoController.favoritosCliente(cliente.getIdCliente());
            for(Producto p : productosFavoritos){
                if(p.getIdProd() == producto.getIdProd()) {
                    btnFavorito.setText("Eliminar de Favoritos");
                    btnFavorito.setOnMouseClicked(e -> {
                        eliminarFavorito();
                        btnFavorito.setText("Agregar a Favoritos");
                    });
                }
            }
        }

        tableNames = new ArrayList(Arrays.asList("Producto","Cliente","Carrito","Favorito","Categoría (JOIN)","Sesión","Género","Pedido"));
        
        if(stackPane != null) {
            cidproducto.setCellValueFactory(cellData -> cellData.getValue().getIdProd());
            cnombre_producto.setCellValueFactory(cellData -> cellData.getValue().getNombre());
            cdescripcion.setCellValueFactory(cellData -> cellData.getValue().getDescripcion());
            cprecio.setCellValueFactory(cellData -> cellData.getValue().getPrecio());
            cidsubsub.setCellValueFactory(cellData -> cellData.getValue().getIdSubsub());
            cidgeneroproducto.setCellValueFactory(cellData -> cellData.getValue().getIdGenero());
            cimagen_nombre.setCellValueFactory(cellData -> cellData.getValue().getImagen());

            ProductoController producto = new ProductoController();
            ArrayList<Producto> productos = producto.Read();

            if (productos != null) {
                for (Producto p : productos) {
                    olProducto.add(new ProductoProperty(
                        p.getIdProd(), 
                        p.getNombre(), 
                        p.getDescripcion(), 
                        p.getPrecio(), 
                        p.getIdSubsub(), 
                        p.getIdGenero(), 
                        p.getImagen()));
                }
                tablaProducto.setItems(olProducto);
            }

            cidcliente.setCellValueFactory(cellData -> cellData.getValue().getIdCliente());
            cnombrecliente.setCellValueFactory(cellData -> cellData.getValue().getNombre());
            capellido.setCellValueFactory(cellData -> cellData.getValue().getApellido());
            ccorreo.setCellValueFactory(cellData -> cellData.getValue().getCorreo());
            czipcode.setCellValueFactory(cellData -> cellData.getValue().getZipcode());
            cfecha_nacimiento.setCellValueFactory(cellData -> cellData.getValue().getFecha_nacimiento());
            ccontrasena.setCellValueFactory(cellData -> cellData.getValue().getContrasena());
            cgenero.setCellValueFactory(cellData -> cellData.getValue().getGenero());
            cimagen_usuario.setCellValueFactory(cellData -> cellData.getValue().getImagen());

            ClienteController clienteT = new ClienteController();
            ArrayList<Cliente> clientes = clienteT.Read();

            if (clientes != null) {
                for (Cliente c : clientes) {
                    olCliente.add(new ClienteProperty(
                        c.getIdCliente(), 
                        c.getNombre(), 
                        c.getApellido(), 
                        c.getCorreo(), 
                        c.getZipcode(), 
                        c.getFecha_nacimiento(), 
                        c.getContrasena(), 
                        c.getGenero(), 
                        c.getImagen()));
                }
                tablaCliente.setItems(olCliente);
            }

            cidcarrito.setCellValueFactory(cellData -> cellData.getValue().getIdCarrito());
            cidclientecarrito.setCellValueFactory(cellData -> cellData.getValue().getIdCliente());
            cidproductocarrito.setCellValueFactory(cellData -> cellData.getValue().getIdProducto());
            ccantidad.setCellValueFactory(cellData -> cellData.getValue().getCantidad());
            cpreciounitario.setCellValueFactory(cellData -> cellData.getValue().getPrecio());
            ctalla.setCellValueFactory(cellData -> cellData.getValue().getTalla());

            CarritoController carrito = new CarritoController();
            ArrayList<Carrito> carritos = carrito.Read();

            if (carritos != null) {
                for (Carrito c : carritos) {
                    olCarrito.add(new CarritoProperty(
                        c.getIdCarrito(), 
                        c.getIdCliente(), 
                        c.getIdProducto(), 
                        c.getCantidad(), 
                        c.getPrecio(), 
                        c.getTalla()));
                }
                tablaCarrito.setItems(olCarrito);
            }

            cidfavorito.setCellValueFactory(cellData -> cellData.getValue().getIdFavorito());
            cidproductofavorito.setCellValueFactory(cellData -> cellData.getValue().getIdProducto());
            cidclientefavorito.setCellValueFactory(cellData -> cellData.getValue().getIdCliente());

            FavoritoController favorito = new FavoritoController();
            ArrayList<Favorito> favoritos = favorito.Read();

            if (favoritos != null) {
                for (Favorito f : favoritos) {
                    olFavorito.add(new FavoritoProperty(
                        f.getIdFavorito(), 
                        f.getIdProducto(), 
                        f.getIdCliente()));
                }
                tablaFavorito.setItems(olFavorito);
            }

            cidcategoria.setCellValueFactory(cellData -> cellData.getValue().getIdCategoria());
            cnombre_categoria.setCellValueFactory(cellData -> cellData.getValue().getNombre_Categoria());
            cidsubcategoria.setCellValueFactory(cellData -> cellData.getValue().getIdSubcategoria());
            cnombre_subcategoria.setCellValueFactory(cellData -> cellData.getValue().getNombre_Subcategoria());
            cidsubsubcategoria.setCellValueFactory(cellData -> cellData.getValue().getIdSubcategoria());
            cnombre_subsub.setCellValueFactory(cellData -> cellData.getValue().getNombre_Subsub());

            OtroController categoria = new OtroController();
            ArrayList<Categoria> categorias = categoria.ReadCategorias();

            if (categorias != null) {
                for (Categoria c : categorias) {
                    olCategoria.add(new CategoriaProperty(
                        c.getIdCategoria(), 
                        c.getNombre_Categoria(), 
                        c.getIdSubcategoria(), 
                        c.getNombre_Subcategoria(), 
                        c.getIdSubsub(), 
                        c.getNombre_Subsub()));
                }
                tablaCategoria.setItems(olCategoria);
            }

            cidsesion.setCellValueFactory(cellData -> cellData.getValue().getIdSesion());
            cidclientesesion.setCellValueFactory(cellData -> cellData.getValue().getIdCliente());

            SesionController sesion = new SesionController();
            ArrayList<Sesion> sesiones = sesion.Read();

            if (sesiones != null) {
                for (Sesion s : sesiones) {
                    olSesion.add(new SesionProperty(
                        s.getIdSesion(), 
                        s.getIdCliente()));
                }
                tablaSesion.setItems(olSesion);
            }

            cidgenero.setCellValueFactory(cellData -> cellData.getValue().getIdGenero());
            cnombregenero.setCellValueFactory(cellData -> cellData.getValue().getNombre());

            OtroController genero = new OtroController();
            ArrayList<Genero> generos = genero.ReadGeneros();

            if (generos != null) {
                for (Genero g : generos) {
                    olGenero.add(new GeneroProperty(
                        g.getIdGenero(), 
                        g.getNombre()));
                }
                tablaGenero.setItems(olGenero);
            }

            cidpedido.setCellValueFactory(cellData -> cellData.getValue().getIdPedido());
            cidclientepedido.setCellValueFactory(cellData -> cellData.getValue().getIdCliente());
            cfechapedido.setCellValueFactory(cellData -> cellData.getValue().getFecha());
            ctotalpedido.setCellValueFactory(cellData -> cellData.getValue().getTotal());

            PedidoController pedido = new PedidoController();
            ArrayList<Pedido> pedidos = pedido.Read();

            if (pedidos != null) {
                for (Pedido p : pedidos) {
                    olPedido.add(new PedidoProperty(
                        p.getIdPedido(), 
                        p.getIdCliente(), 
                        p.getFecha(), 
                        p.getTotal()));
                }
                tablaPedido.setItems(olPedido);
            }
            showTable(currentTableIndex);
            btnNext.setOnAction(event -> showNextTable());
            btnPrevious.setOnAction(event -> showPreviousTable());  
        }
        
    }
    
    private void showTable(int index) {
        for (Node node : stackPane.getChildren()) {
            node.setVisible(false);
        }
        stackPane.getChildren().get(index).setVisible(true);
        nombreTablaLabel.setText(tableNames.get(index));
    }

    private void showNextTable() {
        currentTableIndex++;
        if (currentTableIndex >= stackPane.getChildren().size()) {
            currentTableIndex = 0;
        }
        showTable(currentTableIndex);
    }

    private void showPreviousTable() {
        currentTableIndex--;
        if (currentTableIndex < 0) {
            currentTableIndex = stackPane.getChildren().size() - 1;
        }
        showTable(currentTableIndex);
    }
    
    private void botonTalla(Button button, String str, List<Button> botones) {
        button.setStyle("-fx-background-color: black;"+"-fx-text-fill: white;");
        this.talla = str;
        for (Button b : botones) {
            b.setStyle("-fx-background-color: white;"+"-fx-text-fill: black;"+"-fx-border-color: black;"+"-fx-border-width: 0.5px;");
        }
    }
    
    @FXML
    private void agregarUno(){
        int numero = Integer.parseInt(labelCantidad.getText());
        numero++;
        labelCantidad.setText(String.valueOf(numero));
    }
    
    @FXML 
    private void quitarUno(){
        int numero = Integer.parseInt(labelCantidad.getText());
        if (numero == 1) {
            return;
        } else {
            numero--;
        }
        labelCantidad.setText(String.valueOf(numero));
    }
    
    
    @FXML
    private void agregarAlCarrito() {
        if(ClienteSingleton.getInstance().isFull()) {
            int idClie = cliente.getIdCliente();
            int idProd = producto.getIdProd();
            int cantidad = Integer.parseInt(labelCantidad.getText());
            String precio_con_signo = precio_producto.getText();
            precio_con_signo = precio_con_signo.replace("$", "").trim(); 
            double precio = Double.parseDouble(precio_con_signo);
            if(talla == null || talla.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MyCarrito");
                alert.setHeaderText("Datos Incompletos");
                alert.setContentText("Tiene que seleccionar una talla para el producto.");
                alert.showAndWait();
                return; 
            }
            Carrito car = new Carrito(idClie,idProd,cantidad,precio,talla);
            if(carritoController.ReadByCliePro(idClie, idProd) != null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MyCarrito");
                alert.setHeaderText("Agregar producto al carrito");
                alert.setContentText("Este producto ya está en el carrito");
                alert.showAndWait();
                return;
            }
            int result = carritoController.Create(car);
            if (result > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MyCarrito");
                alert.setHeaderText("Agregar producto al carrito");
                alert.setContentText("Producto agregado al carrito exitosamente");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MyCarrito");
                alert.setHeaderText("Agregar producto al carrito");
                alert.setContentText("No se pudo guardar el producto al carrito");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MyCarrito");
            alert.setHeaderText("Error al procesar el carrito");
            alert.setContentText("Inicia sesión para usar el carrito");
            alert.showAndWait();
        }
        
    }
    
    private void comprarProductos(ImageView imagen,Label nombre){
        if (imagen != null && nombre != null) {
            imagen.setOnMouseClicked(e -> {
                producto = productoController.GetByName(nombre.getText());
                ProductoSingleton.getInstance().setProducto(producto);
                try {
                    App.setRoot("comprar_producto");
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    private void nombrarProductos(List<Label> labels, ArrayList<Producto> productos) {
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i) != null) {
               if (i < productos.size()) {
                    labels.get(i).setText(productos.get(i).getNombre());
                } 
            }   
        }
    }
    
    private void precioProductos(List<Label> labels, ArrayList<Producto> productos) {
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i) != null) {
               if (i < productos.size()) {
                    labels.get(i).setText("$"+String.valueOf(productos.get(i).getPrecio()));
                } 
            }   
        }
    }
    
    private void initializeText(Text textElement, String fxmlFileName) {
        if (textElement != null) {
            textElement.setOnMouseClicked(e -> {
                try {
                    App.setRoot(fxmlFileName);
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    private void initializeLabel(Label label, String fxmlFileName) {
        if (label != null) {
            label.setOnMouseClicked(e -> {
                try {
                    App.setRoot(fxmlFileName);
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    private void initializeMenuItem(MenuItem item, String fxmlFileName) {
        if (item != null) {
            item.setOnAction(e -> {
                try {
                    App.setRoot(fxmlFileName);
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    private void ventanaNueva(Label label, String fxmlFileName) {
        label.setOnMouseClicked(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(fxmlFileName + ".fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
                double x = 550;
                double y = 200;
                stage.setX(x);
                stage.setY(y);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }
    
    @FXML
    private void switchToKids() throws IOException {
        App.setRoot("kids");
    }
    
    @FXML
    private void switchToMen() throws IOException {
        App.setRoot("men");
    }
    
    @FXML
    private void switchToKids1() throws IOException {
        App.setRoot("kids1");
    }
    
    @FXML
    private void switchToKids2() throws IOException {
        App.setRoot("kids2");
    }
    
    @FXML
    private void switchToWomen_clothes() throws IOException {
        App.setRoot("women_clothes");
    }
    
    @FXML
    private void switchToWomen_gym() throws IOException {
        App.setRoot("women_gym");
    }
    
    @FXML
    private void switchToMen_shirts() throws IOException {
        App.setRoot("men_shirts");
    }
    
    @FXML
    private void switchToMen_clothes() throws IOException {
        App.setRoot("men_clothes");
    }
    
    @FXML
    private void switchToBaby_newborn() throws IOException {
        App.setRoot("baby_newborn");
    }
    
    @FXML
    private void switchToBabygirl_outerwear() throws IOException {
        App.setRoot("babygirl_outerwear");
    }
    
    @FXML
    private void switchToBuscar() throws IOException {
        App.setRoot("buscar_productos");
    }
       
    @FXML
    private void buscarProducto(){
        String searchText = txtbuscar.getText();
        vboxResultados.getChildren().clear();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectoprueba", "root", "pizza1239");
            String sql = "SELECT idProducto, nombre_Producto, descripcion, precio, imagen_nombre FROM producto WHERE nombre_Producto LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            VBox mainVBox = new VBox();
            mainVBox.setSpacing(30); 
            mainVBox.setAlignment(Pos.CENTER);
            HBox rowHBox = new HBox();
            rowHBox.setSpacing(20); 
            rowHBox.setAlignment(Pos.CENTER); 

            int contadorProducto = 0;
            int cantidadFilas = 0;
            double alturaImagen = 150;
            double alturaLabel = 20;
            double espaciadoVertical = 10;
            double alturaFila = alturaImagen + 3 * alturaLabel + 2 * espaciadoVertical;
            
            while (rs.next()) {
                int productoID = rs.getInt("idProducto");
                String nombre = rs.getString("nombre_Producto");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                String imagenNombre = rs.getString("imagen_nombre");
                VBox productVBox = new VBox();
                productVBox.setSpacing(5);
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/" + imagenNombre)));
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                imageView.setId("imagen"+String.valueOf(productoID));
                Label nameLabel = new Label(nombre);
                Label descriptionLabel = new Label(descripcion);
                Label priceLabel = new Label("Precio: $" + precio);
                nameLabel.setId("nombre"+String.valueOf(productoID));
                nameLabel.setMaxWidth(150);
                nameLabel.setStyle("-fx-font-weight: bold;");
                descriptionLabel.setMaxWidth(150);
                comprarProductos(imageView,nameLabel);
                productVBox.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel);
                rowHBox.getChildren().add(productVBox);
                contadorProducto++;
                
                if (contadorProducto % 4 == 0) {
                    mainVBox.getChildren().add(rowHBox);
                    rowHBox = new HBox();
                    rowHBox.setSpacing(10);
                    rowHBox.setAlignment(Pos.CENTER);
                    cantidadFilas++;
                }
            }
            if (!rowHBox.getChildren().isEmpty()) {
                mainVBox.getChildren().add(rowHBox);
                cantidadFilas++;
            }
            vboxResultados.getChildren().add(mainVBox);
            double espaciadoVerticalEntreFilas = 30;
            cantidadFilas++;
            double rectangulo = rectanguloPie.getHeight();
            double nuevaAltura = cantidadFilas * alturaFila + (cantidadFilas - 1) * espaciadoVerticalEntreFilas; 
            nuevaAltura = nuevaAltura + rectangulo;
            rectanguloPie.setTranslateY(nuevaAltura - rectangulo - 310);
            hboxPie.setTranslateY(nuevaAltura - rectangulo - 310);
            labelPie.setTranslateY(nuevaAltura - rectangulo - 310);
            imagenPie.setTranslateY(nuevaAltura - rectangulo - 310);
            anchorBuscar.setPrefHeight(nuevaAltura);
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @FXML
    private void agregarFavorito() {
        if(ClienteSingleton.getInstance().isFull()){
            int idProd = producto.getIdProd();
            int idClie = cliente.getIdCliente();
            Favorito fav = new Favorito(idProd,idClie);
            ArrayList<Favorito> favoritos;
            if (favoritoController.Read() != null){
                favoritos = favoritoController.Read();
            } else {
                favoritos = new ArrayList<>();
            }
            Favorito comprobacion = favoritoController.GetByClieProd(idClie,idProd);
            if (comprobacion != null) {
                for(int i = 0; i < favoritos.size(); i++){
                    if (favoritos.get(i).getIdCliente() == comprobacion.getIdCliente() && 
                            favoritos.get(i).getIdProducto() == comprobacion.getIdProducto()){
                        return;
                    }
                }
            }
            int result = favoritoController.Create(fav);
            if (result>0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Guardar favorito");
                alert.setHeaderText("Producto guardado en Favorites");
                alert.setContentText("Tu producto favorito puede ser consultado en Favorites");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Guardar favorito");
                alert.setHeaderText("Error al guardar favorito");
                alert.setContentText("No se pudo guardar el producto como favorito");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Guardar favorito");
            alert.setHeaderText("Error al guardar favorito");
            alert.setContentText("Inicia sesión para guardar favoritos");
            alert.showAndWait();
        }
    }
    
    private void eliminarFavorito() {
        int idFav = favoritoController.GetByClieProd(cliente.getIdCliente(), producto.getIdProd()).getIdFavorito();
        int result = favoritoController.Delete(idFav);
        if(result>0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eliminar favorito");
            alert.setHeaderText("Producto eliminado de Favorites");
            alert.setContentText("El producto se eliminó de Favoritos.");
            alert.showAndWait();   
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eliminar favorito");
            alert.setHeaderText("Error al eliminar favorito");
            alert.setContentText("No se pudo eliminar el producto de Favoritos.");
            alert.showAndWait();
        }
    }
    
    private void eliminarCarrito(int idProducto) {
        int idCar = carritoController.ReadByCliePro(cliente.getIdCliente(), idProducto).getIdCarrito();
        int result = carritoController.Delete(idCar);
        if(result>0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eliminar del carrito");
            alert.setHeaderText("Producto eliminado del carrito");
            alert.setContentText("El producto se eliminó del carrito.");
            alert.showAndWait();    
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eliminar del carrito");
            alert.setHeaderText("Error al eliminar del carrito");
            alert.setContentText("No se pudo eliminar el producto del carrito.");
            alert.showAndWait();
        }
    }
    
    private void insertarFavorito(List<Producto> productos) {
        int contadorProducto = 0;
        int cantidadFilas = 0;
        double alturaImagen = 50;
        double alturaLabel = 20;
        double espaciadoVertical = 10;
        double alturaFila = alturaImagen + 3 * alturaLabel + 2 * espaciadoVertical;

        vboxSave.setVisible(false);
        VBox mainVbox = new VBox();
        mainVbox.setSpacing(40);
        mainVbox.setAlignment(Pos.CENTER);
        HBox rowHBox = new HBox();
        rowHBox.setSpacing(20); 
        rowHBox.setAlignment(Pos.CENTER);

        for (Producto producto : productos) {
            VBox inVbox = new VBox();
            HBox inHbox = new HBox();
            inVbox.setSpacing(10); 
            inHbox.setSpacing(5);
            inVbox.setAlignment(Pos.CENTER);
            ImageView imagen = new ImageView(new Image("/imagenes/" + producto.getImagen()));
            imagen.setId("imagen"+producto.getIdGenero());
            Label nombre = new Label(producto.getNombre());
            Label precio = new Label("$" + String.valueOf(producto.getPrecio()));
            Label descripcion = new Label(producto.getDescripcion());
            comprarProductos(imagen,nombre);
            imagen.setFitWidth(50);
            imagen.setFitHeight(50);
            imagen.setPreserveRatio(true);
            nombre.setMaxWidth(150);
            nombre.setStyle("-fx-font-weight: bold;");
            descripcion.setMaxWidth(150);
            inHbox.getChildren().addAll(nombre, precio);
            inVbox.getChildren().addAll(inHbox, descripcion);
            rowHBox.getChildren().add(imagen);
            rowHBox.getChildren().add(inVbox);
            contadorProducto++;

            if (contadorProducto % 3 == 0) {
                mainVbox.getChildren().add(rowHBox);
                rowHBox = new HBox();
                rowHBox.setSpacing(20);
                rowHBox.setAlignment(Pos.CENTER);
                cantidadFilas++;
            }
            imagen.setId("imagen"+producto.getIdProd());
        }

        if (!rowHBox.getChildren().isEmpty()) {
            mainVbox.getChildren().add(rowHBox);
            cantidadFilas++;
        }

        vboxFavoritos.getChildren().add(mainVbox);
        double espaciadoVerticalEntreFilas = 30;
        cantidadFilas++;
        double rectangulo = rectanguloPie.getHeight();
        double nuevaAltura = cantidadFilas * alturaFila + (cantidadFilas - 1) * espaciadoVerticalEntreFilas; 
        nuevaAltura = nuevaAltura + rectangulo;
        rectanguloPie.setTranslateY(nuevaAltura - rectangulo - 380 + 30);
        hboxPie.setTranslateY(nuevaAltura - rectangulo - 375 + 30);
        labelPie.setTranslateY(nuevaAltura - rectangulo - 275 + 30);
        imagenPie.setTranslateY(nuevaAltura - rectangulo - 320 + 30);
        anchorFavorito.setPrefHeight(nuevaAltura+35+30);
    }
    
    private void insertarCarrito(List<Producto> productos) {
        if(this.prod==0){
            labelLlena.setVisible(true);
        } else {
            labelLlena.setVisible(false);
        }
        this.total = 0;
        vboxScroll.setSpacing(5);
        int cantidadFilas = 0;
        double alturaImagen = 75;
        double alturaLabel = 10;
        double espaciadoVertical = 50;
        double alturaFila = alturaImagen + 1 * alturaLabel + 1 * espaciadoVertical;
        
        for (Producto producto : productos) {
            HBox hbox = new HBox();
            hbox.setStyle("-fx-border-color: black; "+"-fx-border-width: 0.5px;");
            hbox.setSpacing(20); 
            Carrito carrito = carritoController.ReadByCliePro(cliente.getIdCliente(), producto.getIdProd());
            VBox inVbox = new VBox();
            HBox inHbox = new HBox();
            inHbox.setMaxWidth(250);
            inVbox.setSpacing(5); 
            inHbox.setSpacing(2);
            inVbox.setAlignment(Pos.CENTER_LEFT);
            ImageView imagen = new ImageView(new Image("/imagenes/" + producto.getImagen()));
            imagen.setId("imagen"+producto.getIdGenero());
            Label nombre = new Label(producto.getNombre());
            nombre.setMaxWidth(205);
            Label precio = new Label("$" + String.valueOf(producto.getPrecio()));
            Label cantidad = new Label("Cantidad: "+carrito.getCantidad());
            Label talla = new Label("Talla: "+carrito.getTalla());
            Label favorito = new Label("Favorito: No");
            for(Producto fav : favoritoController.favoritosCliente(cliente.getIdCliente())) {
                if(fav.getIdProd() == producto.getIdProd()){
                    favorito.setText("Favorito: Si");
                }
            }
            this.total+= Math.round((producto.getPrecio() * carrito.getCantidad()) * 100.0) /100.0;
            preciototal.setText("$"+String.valueOf(this.total));
            comprarProductos(imagen,nombre);
            imagen.setFitWidth(100);
            imagen.setFitHeight(100);
            imagen.setPreserveRatio(true);
            nombre.setMaxWidth(100);
            nombre.setStyle("-fx-font-weight: bold;");
            hbox.getChildren().add(imagen);
            
            HBox inin = new HBox();
            Button eliminarCarrito = new Button("Borrar");
            eliminarCarrito.setTranslateX(25);
            eliminarCarrito.setTranslateY(30);
            eliminarCarrito.setOnMouseClicked(e -> {
                eliminarCarrito(producto.getIdProd());
                try {
                    App.setRoot("shopping");
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            inin.setSpacing(15);
            inin.getChildren().addAll(precio,cantidad);
            
            inVbox.getChildren().addAll(nombre,inin,talla,favorito);
            hbox.getChildren().addAll(inVbox,eliminarCarrito);
            vboxScroll.getChildren().add(hbox);
            cantidadFilas++;
        }
        
        double espaciadoVerticalEntreFilas = 2;
        double nuevaAltura = cantidadFilas * alturaFila + (cantidadFilas - 1) * espaciadoVerticalEntreFilas; 
        anchorShopping.setPrefHeight(nuevaAltura);
    }
    
    @FXML
    private void switchToCuenta () {
        ClienteSingleton clienteSingleton = ClienteSingleton.getInstance();
        if (clienteSingleton.isFull()) {
            try {
                App.setRoot("account");
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("signin" + ".fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
                double x = 550;
                double y = 200;
                stage.setX(x);
                stage.setY(y);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}