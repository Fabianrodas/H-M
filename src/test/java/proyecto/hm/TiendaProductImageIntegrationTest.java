package proyecto.hm;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.ProductoController;
import controller.SesionController;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import modelo.Cliente;
import modelo.ClienteSingleton;
import modelo.Producto;
import modelo.ProductoSingleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "HM_ENV_FILE", matches = ".+")
class TiendaProductImageIntegrationTest {

    @BeforeAll
    static void startJavaFxToolkit() throws InterruptedException {
        CountDownLatch toolkitReady = new CountDownLatch(1);
        try {
            Platform.startup(toolkitReady::countDown);
        } catch (IllegalStateException alreadyStarted) {
            Platform.runLater(toolkitReady::countDown);
        }
        assertTrue(toolkitReady.await(10, TimeUnit.SECONDS), "JavaFX toolkit must start");
    }

    @AfterAll
    static void stopJavaFxToolkit() {
        Platform.exit();
    }

    @AfterEach
    void clearCurrentUserAndProduct() {
        ClienteSingleton.getInstance().signOut();
        ProductoSingleton.getInstance().setProducto(null);
    }

    @Test
    void authenticatedCustomerCanOpenProductDetails() throws InterruptedException {
        ClienteSingleton.getInstance().setCliente(new Cliente(
                1, "Mock", "Customer", "mock-customer@example.test", "00000",
                "2000-01-01", "pbkdf2-sha256$1$mock$mock", "Otro"));
        new SesionController().Delete();
        Producto product = new ProductoController().Read().get(0);
        assertNotNull(product, "The PostgreSQL seed must contain a product");
        ProductoSingleton.getInstance().setProducto(product);

        Throwable loadFailure = loadProductDetails();

        assertNull(loadFailure, () -> describeFailure(loadFailure));
    }

    @Test
    void guestCanOpenProductDetails() throws InterruptedException {
        ClienteSingleton.getInstance().signOut();
        new SesionController().Delete();
        Producto product = new ProductoController().Read().get(0);
        assertNotNull(product, "The PostgreSQL seed must contain a product");
        ProductoSingleton.getInstance().setProducto(product);

        Throwable loadFailure = loadProductDetails();

        assertNull(loadFailure, () -> describeFailure(loadFailure));
    }

    private Throwable loadProductDetails() throws InterruptedException {
        AtomicReference<Throwable> loadFailure = new AtomicReference<>();
        CountDownLatch loadFinished = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("comprar_producto.fxml"));
                Parent productDetails = loader.load();
                if (productDetails == null) {
                    throw new AssertionError("The product details view was not loaded");
                }
            } catch (Throwable throwable) {
                loadFailure.set(throwable);
            } finally {
                loadFinished.countDown();
            }
        });

        assertTrue(loadFinished.await(15, TimeUnit.SECONDS), "Product details loading must finish");
        return loadFailure.get();
    }

    private static String describeFailure(Throwable failure) {
        StringBuilder description = new StringBuilder("Product details failed to load:");
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            description.append(System.lineSeparator())
                    .append(cause.getClass().getName())
                    .append(": ")
                    .append(cause.getMessage());
            for (int frame = 0; frame < Math.min(3, cause.getStackTrace().length); frame++) {
                description.append(System.lineSeparator()).append("  at ").append(cause.getStackTrace()[frame]);
            }
        }
        return description.toString();
    }
}
