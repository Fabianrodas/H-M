package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import modelo.Carrito;
import modelo.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "HM_ENV_FILE", matches = ".+")
class CarritoControllerTest {

    @Test
    void allowsSameProductInDifferentSizes() throws SQLException {
        int customerId = createCustomer();
        CarritoController controller = new CarritoController();

        try {
            assertEquals(1, controller.Create(new Carrito(customerId, 1, 1, 15.00, "M")));
            assertEquals(1, controller.Create(new Carrito(customerId, 1, 2, 15.00, "L")));

            var lines = controller.ReadByCliente(customerId);
            assertEquals(2, lines.size());
            assertEquals(Set.of("M", "L"), lines.stream()
                    .map(Carrito::getTalla)
                    .collect(Collectors.toSet()));
            assertNotNull(controller.ReadByCliePro(customerId, 1, "M"));
            assertNotNull(controller.ReadByCliePro(customerId, 1, "L"));
            assertFalse(lines.get(0).getIdCarrito() == lines.get(1).getIdCarrito());

            Carrito medium = controller.ReadByCliePro(customerId, 1, "M");
            assertEquals(1, controller.Delete(medium.getIdCarrito()));
            var remainingLines = controller.ReadByCliente(customerId);
            assertEquals(1, remainingLines.size());
            assertEquals("L", remainingLines.get(0).getTalla());
        } finally {
            deleteCustomer(customerId);
        }
    }

    @Test
    void rejectsDuplicateProductAndSameSize() throws SQLException {
        int customerId = createCustomer();
        CarritoController controller = new CarritoController();

        try {
            Carrito medium = new Carrito(customerId, 1, 1, 15.00, "M");
            assertEquals(1, controller.Create(medium));
            assertNotNull(controller.ReadByCliePro(customerId, 1, "M"));
            assertNull(controller.ReadByCliePro(customerId, 1, "L"),
                    "A different size must not be treated as a duplicate.");

            assertEquals(0, controller.Create(medium),
                    "The same customer, product, and size must not be inserted twice.");
            assertEquals(1, controller.ReadByCliente(customerId).size());
        } finally {
            deleteCustomer(customerId);
        }
    }

    private static int createCustomer() throws SQLException {
        String email = "cart-integration-" + UUID.randomUUID() + "@example.test";
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cliente (nombre, apellido, correo, zipcode, fecha_nacimiento, contrasena, genero) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING \"idCliente\"")) {
            statement.setString(1, "Cart");
            statement.setString(2, "Integration");
            statement.setString(3, email);
            statement.setString(4, "00000");
            statement.setDate(5, Date.valueOf("2000-01-01"));
            statement.setString(6, "test-only-password");
            statement.setString(7, "Otro");
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getInt(1);
            }
        }
    }

    private static void deleteCustomer(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement deleteCart = connection.prepareStatement(
                        "DELETE FROM carrito WHERE \"idCliente\" = ?");
                PreparedStatement deleteCustomer = connection.prepareStatement(
                        "DELETE FROM cliente WHERE \"idCliente\" = ?")) {
            deleteCart.setInt(1, customerId);
            deleteCart.executeUpdate();
            deleteCustomer.setInt(1, customerId);
            deleteCustomer.executeUpdate();
        }
    }
}
