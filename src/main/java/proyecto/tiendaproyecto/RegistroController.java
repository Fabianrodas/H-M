package proyecto.tiendaproyecto;

import controller.ClienteController;
import controller.SesionController;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.Cliente;
import modelo.ClienteSingleton;

public class RegistroController {
    
    @FXML private Button become_member, btnSignin, cerrar, toggleButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField textField, txtemail, txtnombre, txtapellido, txtzipcode, emailSignin;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox cmbGenero;
    @FXML private ClienteController clienteController = new ClienteController();
    @FXML private SesionController sesionController = new SesionController();
    @FXML private CheckBox checkbox;

    @FXML
    private void initialize() {
        if(passwordField != null && textField != null && toggleButton != null) {
            passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!textField.getText().equals(newValue)) {
                    textField.setText(newValue);
                }
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!passwordField.getText().equals(newValue)) {
                    passwordField.setText(newValue);
                }
            });
            toggleButton.setOnAction(event -> {
                if (passwordField.isVisible()) {
                    passwordField.setVisible(false);
                    textField.setVisible(true);
                    toggleButton.setText("HIDE");
                    toggleButton.setStyle("-fx-font-size: 16px;");
                    toggleButton.setStyle("-fx-background-color: transparent;");
                    
                } else {
                    passwordField.setVisible(true);
                    textField.setVisible(false);
                    toggleButton.setText("SHOW");
                    toggleButton.setStyle("-fx-background-color: transparent;");
                }
            });
        }
        
        if(cmbGenero != null){
           cmbGenero.getItems().removeAll(cmbGenero.getItems());
            cmbGenero.getItems().addAll("Hombre", "Mujer"); 
        }
        
        if(datePicker != null){
            datePicker.setDayCellFactory(getDayCellFactory());
            datePicker.setOnAction(event -> {
            LocalDate diaSeleccionado = datePicker.getValue();
            if (diaSeleccionado.isAfter(LocalDate.now())) {
                // Mostrar alerta si se selecciona una fecha futura
                showAlert("Fecha no válida", "No puedes seleccionar una fecha posterior a hoy.");
                // Restablecer el valor del DatePicker a null o a una fecha válida
                datePicker.setValue(null);
            }
        });
        }

    }
    
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #b5b3b3");
                }
            }
        };
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void switchToMember() throws IOException {
        become_member.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("become_member.fxml"));
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
    }
    
    @FXML
    private void switchToSignin() throws IOException {
        btnSignin.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("signin.fxml"));
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
    }
    
    @FXML
    private void switchToTermsConditions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("termsconditions.fxml"));
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
    }
    
    @FXML
    private void cerrarStage() {
        Stage stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
    }
    
    public String encriptarContrasena(String texto){
         StringBuilder stringBuilder = new StringBuilder();
        for (char c : texto.toCharArray()) {
            stringBuilder.append(String.format("%02x", (int) c));
        }
        return stringBuilder.toString();
    }
    
    private String desencriptarContrasena(String hexadecimal){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hexadecimal.length(); i += 2) {
            String parteHexadecimal = hexadecimal.substring(i, i + 2);
            char caracter = (char) Integer.parseInt(parteHexadecimal, 16);
            stringBuilder.append(caracter);
        }
        return stringBuilder.toString();
    }
    
    @FXML
    private void registrar() {
        String email = txtemail.getText();
        String nombre = txtnombre.getText();
        String apellido = txtapellido.getText();
        String genero;
        if(cmbGenero.getValue() == null){
            genero = null;
        } else {
            genero = cmbGenero.getValue().toString();
        }
        String zipcode = txtzipcode.getText();
        String fecha;
        if(datePicker.getValue() != null){
            fecha = datePicker.getValue().toString();
        } else {
            fecha = null;
        }
        String contrasena = passwordField.getText();
        if (nombre == null || apellido == null || genero == null || email == null || fecha == null || contrasena == null
        || nombre.isBlank() || apellido.isBlank() || genero.isBlank() || email.isBlank() || fecha.isBlank() || contrasena.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error al registrar");
            alert.setContentText("Los campos obligatorios tienen que estar llenos");
            alert.showAndWait();
        } else if (!email.matches("\\S+@\\S+\\.\\S+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Correo inválido");
            alert.setContentText("Por favor ingresa un correo electrónico válido.");
            alert.showAndWait();
        } else {
            String contrasenaEncriptada = encriptarContrasena(passwordField.getText());
            Cliente cliente = new Cliente(nombre,apellido,email,zipcode,fecha,contrasenaEncriptada,genero);
            int result = clienteController.Create(cliente);
            if (result>0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro de Usuarios");
                alert.setHeaderText("Usuario agregado");
                alert.setContentText("Te registraste exitosamente, bienvenido a H&M.");
                alert.showAndWait();
                try {
                    switchToSignin();
                } catch (IOException ex) {
                    Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro de Usuarios");
                alert.setHeaderText("Error al registrar");
                alert.setContentText("El registro no se ha podido agregar");
                alert.showAndWait(); 
            }
        }
    }
    
    @FXML
    private void signin(){
        Cliente cliente;
        String email = emailSignin.getText();
        String contrasena = passwordField.getText();
        if(email.isBlank() || contrasena.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Iniciar Sesión");
            alert.setHeaderText("Error al iniciar sesión");
            alert.setContentText("El email y/o contraseña estan vacíos");
            alert.showAndWait();
            return;
        }
        if(clienteController.ReadByEmail(email) == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Iniciar Sesión");
            alert.setHeaderText("Error al iniciar sesión (1)");
            alert.setContentText("El email y/o contraseña son incorrectos");
            alert.showAndWait();
            return;
        }
        ArrayList<Cliente> clientes = clienteController.Read();
        for(Cliente c : clientes) { 
            if(clienteController.ReadByEmail(email).getIdCliente() == c.getIdCliente()) {
                cliente = clienteController.ReadByEmail(email);
                String contrasenaCliente = desencriptarContrasena(cliente.getContrasena());
                if (contrasenaCliente.equals(contrasena)) {
                    try {
                        ClienteSingleton.getInstance().setCliente(cliente);
                        if(checkbox.isSelected()){
                            int ses = sesionController.setSesion(cliente.getIdCliente());
                            if(ses>0){
                                System.out.println("Sesion almacenada exitosamente");
                            } else {
                                System.out.println("Ocurrio un error y no se pudo guardar la sesion");
                            }
                        }
                        cerrarStage();
                        App.setRoot("account");
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Iniciar Sesión");
                    alert.setHeaderText("Error al iniciar sesión (3)");
                    alert.setContentText("El email y/o contraseña son incorrectos");
                    alert.showAndWait();
                    return;
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Iniciar Sesión");
        alert.setHeaderText("Error al iniciar sesión (2)");
        alert.setContentText("El email y/o contraseña son incorrectos");
        alert.showAndWait();
    }
}
