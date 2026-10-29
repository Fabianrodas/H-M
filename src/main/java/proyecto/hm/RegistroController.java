package proyecto.hm;

import controller.ClienteController;
import controller.SesionController;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import modelo.AdminAccess;
import modelo.DatabaseConfig;
import modelo.PasswordService;

public class RegistroController {
    
    @FXML private Button become_member, btnSignin, cerrar, toggleButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField textField, txtemail, txtnombre, txtapellido, txtzipcode, emailSignin;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cmbGenero;
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
        HmAlert alert = new HmAlert(HmAlert.Kind.WARNING);
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
            HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
            alert.setHeaderText("Error al registrar");
            alert.setContentText("Los campos obligatorios tienen que estar llenos");
            alert.showAndWait();
        } else if (!email.matches("\\S+@\\S+\\.\\S+")) {
            HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
            alert.setHeaderText("Correo inválido");
            alert.setContentText("Por favor ingresa un correo electrónico válido.");
            alert.showAndWait();
        } else {
            char[] password = passwordField.getText().toCharArray();
            passwordField.clear();
            if (textField != null) {
                textField.clear();
            }
            try {
                String hashedPassword = PasswordService.hash(password);
                Cliente cliente = new Cliente(nombre, apellido, email, zipcode, fecha, hashedPassword, genero);
                int result = clienteController.Create(cliente);
                if (result > 0) {
                    HmAlert alert = new HmAlert(HmAlert.Kind.SUCCESS);
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
                    HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
                    alert.setTitle("Registro de Usuarios");
                    alert.setHeaderText("Error al registrar");
                    alert.setContentText("El registro no se ha podido agregar");
                    alert.showAndWait();
                }
            } catch (RuntimeException exception) {
                Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, "No se pudo proteger la contraseña del registro.", exception);
                HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
                alert.setHeaderText("Error al registrar");
                alert.setContentText("No se pudo completar el registro. Vuelve a intentarlo.");
                alert.showAndWait();
            } finally {
                Arrays.fill(password, '\0');
            }
        }
    }
    
    @FXML
    private void signin(){
        String email = emailSignin.getText();
        char[] contrasena = passwordField.getText().toCharArray();
        if(email.isBlank() || contrasena.length == 0) {
            HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
            alert.setTitle("Iniciar Sesión");
            alert.setHeaderText("Error al iniciar sesión");
            alert.setContentText("El email y/o contraseña estan vacíos");
            alert.showAndWait();
            Arrays.fill(contrasena, '\0');
            return;
        }
        Cliente cliente = clienteController.ReadByEmail(email);
        if(cliente == null){
            HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
            alert.setTitle("Iniciar Sesión");
            alert.setHeaderText("Error al iniciar sesión");
            alert.setContentText("El email y/o contraseña son incorrectos");
            alert.showAndWait();
            Arrays.fill(contrasena, '\0');
            return;
        }
        try {
            PasswordService.VerificationResult verification = PasswordService.verify(contrasena, cliente.getContrasena());
            if (!verification.isValid()) {
                showInvalidCredentials();
                return;
            }

            boolean admin = AdminAccess.isConfiguredAdmin(
                    cliente.getCorreo(), DatabaseConfig.loadDefault().getAdminEmail());
            if (verification.needsUpgrade()) {
                String upgradedPassword = admin
                        ? PasswordService.hashForPasswordChangeRequired(contrasena)
                        : PasswordService.hash(contrasena);
                if (clienteController.UpdateContrasena(cliente.getIdCliente(), upgradedPassword) <= 0) {
                    showPasswordUpgradeError();
                    return;
                }
                cliente.setContrasena(upgradedPassword);
                verification = PasswordService.verify(contrasena, upgradedPassword);
            }

            ClienteSingleton currentUser = ClienteSingleton.getInstance();
            currentUser.setCliente(cliente);
            currentUser.setPasswordChangeRequired(verification.mustChangePassword());
            if (!verification.mustChangePassword() && checkbox != null && checkbox.isSelected()) {
                sesionController.setSesion(cliente.getIdCliente());
            }

            cerrarStage();
            App.setRoot(verification.mustChangePassword() ? "accountsettings" : "account");
        } catch (IOException | IllegalStateException exception) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, "No se pudo completar el inicio de sesión.", exception);
            showPasswordUpgradeError();
        } finally {
            Arrays.fill(contrasena, '\0');
        }
    }

    private void showInvalidCredentials() {
        HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
        alert.setTitle("Iniciar Sesión");
        alert.setHeaderText("Error al iniciar sesión");
        alert.setContentText("El email y/o contraseña son incorrectos");
        alert.showAndWait();
    }

    private void showPasswordUpgradeError() {
        HmAlert alert = new HmAlert(HmAlert.Kind.ERROR);
        alert.setTitle("Actualizar contraseña");
        alert.setHeaderText("No se pudo completar el inicio de sesión");
        alert.setContentText("Vuelve a intentarlo. Si el problema continúa, revisa la conexión con la base de datos.");
        alert.showAndWait();
    }
}
