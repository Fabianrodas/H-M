package proyecto.tiendaproyecto;

import controller.CarritoController;
import controller.ClienteController;
import controller.PedidoController;
import controller.SesionController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Carrito;
import modelo.Cliente;
import modelo.ClienteSingleton;
import modelo.Pedido;
import modelo.Producto;

public class CompraController implements Initializable {
    
    @FXML private Label nombre_pedido, fecha_pedido, subtotal_pedido, total_pedido;
    private PedidoController pedidoController = new PedidoController();
    private CarritoController carritoController = new CarritoController();
    private ClienteController clienteController = new ClienteController();
    private SesionController sesionController = new SesionController();
    @FXML private VBox vboxPedido;
    @FXML private Button btnComprar, cerrar;
    @FXML private AnchorPane anchorPedido, contenedor;
    private Cliente cliente;
    private double total;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(sesionController.haySesion()){
            cliente = clienteController.ReadById(sesionController.getSesion().getIdCliente());
            ClienteSingleton.getInstance().setCliente(cliente);
        } else {
            cliente = ClienteSingleton.getInstance().getCliente();
        }
        
        ArrayList<Producto> carrito = carritoController.productosCliente(cliente.getIdCliente());
        insertarPedido(carrito);
        
        if(vboxPedido != null && nombre_pedido != null && fecha_pedido != null 
        && total_pedido != null && subtotal_pedido != null) {
                confirmarPedido();
        }
    }
    
    private void confirmarPedido() {
        nombre_pedido.setText(cliente.getNombre()+" "+cliente.getApellido());
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String fechaPedido = fechaHoraActual.format(formatter);
        fecha_pedido.setText(fechaPedido);
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String fecha = fechaActual.format(formatter2);
        double total_iva = this.total;
        btnComprar.setOnMouseClicked(e -> {
            Pedido pedido = new Pedido(cliente.getIdCliente(),fecha,total_iva);
            int result = pedidoController.Create(pedido);
            if(result>0){
                int result2 = carritoController.borrarCarritos(cliente.getIdCliente());
                if(result2>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pedido Exitoso");
                    alert.setHeaderText("N° FACTURA: "+pedidoController.ultimaFactura());
                    alert.setContentText("Disfrute de sus productos H&M"+"\n"+"Vuelva a llenar su carrito para realizar otro pedido.");
                    alert.showAndWait();
                    cerrarStage();
                    try {
                        App.setRoot("shopping");
                    } catch (IOException ex) {
                        Logger.getLogger(CompraController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("ERROR BORRANDO EL CARRITO DEL CLIENTE");
                }
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR EN EL PEDIDO");
                alert.setHeaderText("Error al crear el pedido");
                alert.setContentText("No se pudo completar la acción.");
                alert.showAndWait();
            }
        });
    }
    
    private void insertarPedido(List<Producto> productos) {
        this.total = 0;
        vboxPedido.setSpacing(5);
        int cantidadFilas = 0;
        double alturaImagen = 75;
        double alturaLabel = 10;
        double espaciadoVertical = 5;
        double alturaFila = alturaImagen + 1 * alturaLabel + 1 * espaciadoVertical;
        contenedor.setStyle("-fx-border-color: black; "+"-fx-border-width: 1px;");
        
        for (Producto producto : productos) {
            HBox hbox = new HBox();
            hbox.setStyle("-fx-border-color: black; "+"-fx-border-width: 0.5px;");
            hbox.setSpacing(20); 
            Carrito carrito = carritoController.ReadByCliePro(cliente.getIdCliente(), producto.getIdProd());
            VBox inVbox = new VBox();
            HBox inHbox = new HBox();
            inVbox.setSpacing(5); 
            inHbox.setSpacing(2);
            inVbox.setAlignment(Pos.CENTER_LEFT);
            ImageView imagen = new ImageView(new Image("/imagenes/" + producto.getImagen()));
            Label nombre = new Label(producto.getNombre());
            nombre.setMaxWidth(205);
            Label precio = new Label("$" + String.valueOf(producto.getPrecio()));
            Label cantidad = new Label("Cantidad: "+carrito.getCantidad());
            Label talla = new Label("Talla: "+carrito.getTalla());
            Label descripcion = new Label(producto.getDescripcion());
            descripcion.setPrefWidth(200);
            this.total+= Math.round((producto.getPrecio() * carrito.getCantidad()) * 100.0) /100.0;
            imagen.setFitWidth(75);
            imagen.setFitHeight(75);
            imagen.setPreserveRatio(true);
            nombre.setPrefWidth(100);
            nombre.setStyle("-fx-font-weight: bold;");
            hbox.getChildren().add(imagen);
            hbox.setMaxWidth(285);
            HBox inin = new HBox();
            inin.setSpacing(15);
            inin.getChildren().addAll(precio,cantidad,talla);
            inVbox.getChildren().addAll(nombre,inin,descripcion);
            hbox.getChildren().addAll(inVbox);
            vboxPedido.getChildren().add(hbox);
            cantidadFilas++;
        }
        double espaciadoVerticalEntreFilas = 2;
        double nuevaAltura = cantidadFilas * alturaFila + (cantidadFilas - 1) * espaciadoVerticalEntreFilas; 
        anchorPedido.setPrefHeight(nuevaAltura);
        subtotal_pedido.setText("$"+this.total);
        double total_iva = Math.round((this.total+(this.total*0.15)) * 100.0) / 100.0;
        total_pedido.setText("$"+total_iva);
        this.total = total_iva;
    }
    
    @FXML
    private void cerrarStage() {
        Stage stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
    }

}

