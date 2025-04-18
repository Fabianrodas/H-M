package proyecto.tiendaproyecto;

import controller.CarritoController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.ClienteSingleton;
import modelo.Cliente;
import controller.ClienteController;
import controller.PedidoController;
import controller.SesionController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Producto;


public class AccountController implements Initializable {
    @FXML private Button changePicture, btnSavePassword, btnAccountSettings, btnsearch, btnsignin;       
    @FXML private ImageView fotoUsuario, logoHM;    
    @FXML private TextField accName, accApellido, accEmail, accGenero, accFecha, newPassword;
    @FXML private PasswordField accPassword;
    @FXML private Label btnDeleteAccount, pedidosCliente;
    @FXML private MenuItem bd, about;

    
    private Cliente cliente;
    private int clienteID;
    
    private final ClienteController clienteController = new ClienteController();
    private final RegistroController registroController = new RegistroController();
    private final SesionController sesionController = new SesionController();
    private final PedidoController pedidoController = new PedidoController();
    private final CarritoController carritoController = new CarritoController();
        
    @FXML private Text baby_baby, baby_newborn, babyboy_clothing, babyboy_outerwear,babyboy_shoes, babygirl_clothing, babygirl_outerwear, babygirl_shoes,boy1_clothing, boy1_outerwear, 
    boy1_shoes, boy2_clothing, boy2_outerwear, boy2_shoes,girl1_clothing, girl1_outerwear, girl1_shoes, girl2_clothing, girl2_outerwear,girl2_shoes,kids1, kids2, men_clothes, men_dress_shoes, men_gym, men_hoodies,men_jeans, men_pants, men_running, men_shirts, men_shoes, 
    men_sneakers, men_sport_shorts,men_sports, newborn_clothing, newborn_outerwear, newborn_shoes, women_blouses, women_boots, women_clothes, women_dresses, women_gym, women_jeans, women_leggings, women_running, women_shoes, women_sneakers, women_sportbra, women_sports, women_tops, women2, men2,baby2,kid;
    @FXML private Label baby, women, kids, men, bd2, about2, member, shopping_bag, signin, favorites;
   
    private int prod;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        if(sesionController.haySesion()){
            cliente = clienteController.ReadById(sesionController.getSesion().getIdCliente());
            ClienteSingleton.getInstance().setCliente(cliente);
        } else {
            cliente = ClienteSingleton.getInstance().getCliente();
        }
        
        if(pedidosCliente != null){
            pedidosCliente.setText(String.valueOf(pedidoController.totalPedidos(cliente.getIdCliente())));
        }
    
        clienteID = ClienteSingleton.getInstance().getCliente().getIdCliente();
        
        if (clienteController.getImagenCliente(clienteID) != null) {
            if (fotoUsuario != null) {
                String destinationPath = "user_images" + File.separator + clienteController.getImagenCliente(clienteID);
                File imagenFile = new File(destinationPath);
                if (imagenFile.exists()) {
                    Image image = new Image(imagenFile.toURI().toString());
                    fotoUsuario.setImage(image);
                }
            }
        }
        if(accName != null && accApellido != null && accEmail != null && accGenero != null && accFecha != null) {
                accName.setText(cliente.getNombre());
                accApellido.setText(cliente.getApellido());
                accEmail.setText(cliente.getCorreo());
                accGenero.setText(cliente.getGenero());
                accFecha.setText(cliente.getFecha_nacimiento());
            }
            
            if(accPassword != null){
                accPassword.setText(cliente.getContrasena());
            }
        
        
        if(btnAccountSettings != null){
            btnAccountSettings.setOnMouseClicked(e -> {
               try {
                    App.setRoot("accountsettings");
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            });
        }
        
        if(btnDeleteAccount != null){
            btnDeleteAccount.setOnMouseClicked(e -> {
                   Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmación");
                    alert.setHeaderText("Confirmación de eliminación de usuario");
                    alert.setContentText("¿Está seguro de que desea eliminar el usuario?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        clienteController.Delete(clienteID);
                        Alert cuadroDialogo = new Alert(AlertType.INFORMATION);
                        cuadroDialogo.setTitle("Eliminar cuenta");
                        cuadroDialogo.setHeaderText("Eliminación de cuenta exitosa");
                        cuadroDialogo.setContentText("La cuenta ha sido eliminada de nuestra base de datos.");
                        cuadroDialogo.showAndWait();
                        try {
                            ClienteSingleton.getInstance().signOut();
                            App.setRoot("tienda");
                        } catch (IOException ex) {
                            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
                        }                          
                    } else {
                        Alert cuadroDialogo2 = new Alert(AlertType.ERROR);
                        cuadroDialogo2.setTitle("Eliminar cuenta");
                        cuadroDialogo2.setHeaderText("Eliminación de cuenta fallida");
                        cuadroDialogo2.setContentText("El proceso ha fallado y su cuenta no ha sido eliminada.");
                        cuadroDialogo2.showAndWait();
                    }

            });       
    }
        if(btnSavePassword != null){
         btnSavePassword.setOnMouseClicked(e -> {
            if(newPassword.getText() != null && newPassword.getText().isBlank() == false){
            String nuevaContra = newPassword.getText();
            String contraEncriptada = registroController.encriptarContrasena(nuevaContra);
            clienteController.UpdateContrasena(clienteID, contraEncriptada);
            Alert cuadroDialogo3 = new Alert(AlertType.INFORMATION);
            cuadroDialogo3.setTitle("Actualizar contraseña");
            cuadroDialogo3.setHeaderText("Actualización de contraseña exitosa");
            cuadroDialogo3.setContentText("¡La contraseña ha sido actualizada exitosamente!");
            cuadroDialogo3.showAndWait();
            }
            else{
              Alert errorContrasena = new Alert(AlertType.ERROR);
                    errorContrasena.setTitle("Error de actualización");
                    errorContrasena.setHeaderText("Error al actualizar la contraseña");
                    errorContrasena.setContentText("El proceso ha fallado y tu contraseña no ha sido actualizada."+"\n"+"Completa los espacios en blanco y evita los campos en blanco");
                    errorContrasena.showAndWait();  
            }
         });
        }
        
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
        
        List<Label> labels = Arrays.asList(baby, women, kids, men, bd2, about2, member, shopping_bag, signin, favorites);
        List<String> fxmlLabels = Arrays.asList("baby", "women", "kids", "men", "bd", "about", "become_member", "shopping_bag", "signin", "favorites");
        
        for(int i=0;i<labels.size();i++){
            initializeLabel(labels.get(i),fxmlLabels.get(i));
        }
        
        initializeMenuItem(bd, "bd");
        initializeMenuItem(about, "about");
        ventanaNueva(member, "become_member");
                  
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
            shopping_bag.setOnMouseClicked(e -> {
                try {
                    App.setRoot("shopping");
                } catch (IOException ex) {
                    Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
        shopping_bag.setText("👜 Shopping Bag ("+prod+")");
    }
        
       
}    
   
    @FXML
    private void subirImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            File carpetaDestino = new File("user_images");
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs(); // Crea la carpeta si no existe
            }
            String destinationPath = carpetaDestino.getPath() + File.separator + selectedFile.getName();
            File destino = new File(destinationPath);
            try {
                Files.copy(selectedFile.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            guardarImagenBD(selectedFile.getName());
            Image image = new Image(destino.toURI().toString());
            fotoUsuario.setImage(image);
        }
    }

    private void guardarImagenBD(String imageName) {
        // Código para actualizar la base de datos con la ruta de la imagen del usuario
        String sql = "UPDATE cliente SET imagen_usuario = ? WHERE idCliente = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectoprueba", "root", "pizza1239");
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imageName);
            pstmt.setInt(2, clienteID);  

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    @FXML
    private void signOut(){
        ClienteSingleton.getInstance().signOut();
        int sesion = sesionController.Delete();
        if(sesion>0){
            System.out.println("Se cerró la sesión exitosamente.");
        } else {
            System.out.println("Ocurrio un error cerrando la sesión.");
        }
       try {
                App.setRoot("tienda");
            } catch (IOException ex) {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
        
    @FXML
    private void sacarImagen() {
        String defaultImagePath = "user_images" + File.separator + "default_user.jpg";
        Image defaultImage = new Image(new File(defaultImagePath).toURI().toString());
        fotoUsuario.setImage(defaultImage);

        // Actualiza la base de datos para establecer la imagen por defecto
        actualizarImagenBD("default_user.jpg");
    }   
    
    private void actualizarImagenBD(String imageName) {
        String sql = "UPDATE cliente SET imagen_usuario = ? WHERE idCliente = ?";
    
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectoprueba", "root", "pizza1239");
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imageName);
            pstmt.setInt(2, clienteID); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchPublicProfile(){
    try {
            App.setRoot("account");
        } catch (IOException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
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
}
