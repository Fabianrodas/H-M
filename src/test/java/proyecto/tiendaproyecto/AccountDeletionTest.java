package proyecto.tiendaproyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.CarritoController;
import controller.ClienteController;
import controller.PedidoController;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import modelo.Carrito;
import modelo.Cliente;
import modelo.ClienteSingleton;
import modelo.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "HM_ENV_FILE", matches = ".+")
class AccountDeletionTest {

    @Test
    void deletesRelatedMockRowsAndReportsSuccess() throws SQLException {
        int customerId = createCustomer();
        ClienteSingleton singleton = ClienteSingleton.getInstance();

        try {
            CarritoController cartController = new CarritoController();
            assertEquals(1, cartController.Create(new Carrito(customerId, 1, 2, 12.50, "M")));
            long orderId = new PedidoController().crearConDetalle(
                    customerId, LocalDate.of(2026, 10, 27), cartController.ReadByCliente(customerId));
            assertTrue(orderId > 0);
            assertEquals(1, cartController.Create(new Carrito(customerId, 1, 1, 14.00, "L")));
            insertFavorite(customerId);
            insertSession(customerId);
            singleton.setCliente(testClient(customerId));

            assertTrue(new AccountController().deleteAccountAndSignOut(customerId));

            assertFalse(singleton.isFull());
            assertEquals(0, count("SELECT COUNT(*) FROM cliente WHERE idcliente = ?", customerId));
            assertEquals(0, count("SELECT COUNT(*) FROM sesion WHERE idcliente = ?", customerId));
            assertEquals(0, count("SELECT COUNT(*) FROM favorito WHERE idcliente = ?", customerId));
            assertEquals(0, count("SELECT COUNT(*) FROM carrito WHERE idcliente = ?", customerId));
            assertEquals(0, count("SELECT COUNT(*) FROM pedido WHERE idcliente = ?", customerId));
            assertEquals(0, countOrderDetails(orderId));
        } finally {
            singleton.signOut();
            deleteCustomer(customerId);
        }
    }

    @Test
    void reportsFailureWithoutSigningOut() throws SQLException {
        int customerId = createCustomer();
        ClienteSingleton singleton = ClienteSingleton.getInstance();

        try {
            insertSession(customerId);
            singleton.setCliente(testClient(customerId));

            assertFalse(new AccountController().deleteAccountAndSignOut(Integer.MAX_VALUE));

            assertTrue(singleton.isFull());
            assertEquals(customerId, singleton.getCliente().getIdCliente());
            assertEquals(1, count("SELECT COUNT(*) FROM cliente WHERE idcliente = ?", customerId));
            assertEquals(1, count("SELECT COUNT(*) FROM sesion WHERE idcliente = ?", customerId));
        } finally {
            singleton.signOut();
            deleteCustomer(customerId);
        }
    }

    private static Cliente testClient(int customerId) {
        Cliente customer = new Cliente();
        customer.setIdCliente(customerId);
        customer.setNombre("Deletion");
        return customer;
    }

    private static int createCustomer() throws SQLException {
        String email = "deletion-integration-" + UUID.randomUUID() + "@example.test";
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cliente (nombre, apellido, correo, zipcode, fecha_nacimiento, contrasena, genero) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idcliente")) {
            statement.setString(1, "Deletion");
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

    private static void insertFavorite(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO favorito (idproducto, idcliente) VALUES (1, ?)")) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }
    }

    private static void insertSession(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO sesion (idcliente) VALUES (?)")) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }
    }

    private static long count(String sql, int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getLong(1);
            }
        }
    }

    private static long countOrderDetails(long orderId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT COUNT(*) FROM pedido_detalle WHERE idpedido = ?")) {
            statement.setLong(1, orderId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getLong(1);
            }
        }
    }

    private static void deleteCustomer(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection()) {
            try (PreparedStatement deleteDetails = connection.prepareStatement(
                    "DELETE FROM pedido_detalle WHERE idpedido IN "
                    + "(SELECT idpedido FROM pedido WHERE idcliente = ?)")) {
                deleteDetails.setInt(1, customerId);
                deleteDetails.executeUpdate();
            }
            for (String table : new String[]{"sesion", "favorito", "carrito", "pedido"}) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM " + table + " WHERE idcliente = ?")) {
                    statement.setInt(1, customerId);
                    statement.executeUpdate();
                }
            }
            try (PreparedStatement deleteCustomer = connection.prepareStatement(
                    "DELETE FROM cliente WHERE idcliente = ?")) {
                deleteCustomer.setInt(1, customerId);
                deleteCustomer.executeUpdate();
            }
        }
    }
}
