package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import modelo.Carrito;
import modelo.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "HM_ENV_FILE", matches = ".+")
class PedidoCheckoutTest {

    @Test
    void successStoresEveryProductSizeAndQuantity() throws SQLException {
        int customerId = createCustomer();
        CarritoController cartController = new CarritoController();
        PedidoController orderController = new PedidoController();
        LocalDate orderDate = LocalDate.of(2026, 10, 27);

        try {
            assertEquals(1, cartController.Create(new Carrito(customerId, 1, 2, 12.50, "M")));
            assertEquals(1, cartController.Create(new Carrito(customerId, 1, 1, 21.10, "L")));
            List<Carrito> lines = cartController.ReadByCliente(customerId);
            long ordersBefore = countOrders(customerId);

            long orderId = orderController.crearConDetalle(customerId, orderDate, lines);

            assertTrue(orderId > 0L);
            assertEquals(ordersBefore + 1, countOrders(customerId));
            assertEquals(2, countOrderDetails(orderId));
            assertEquals(0, cartController.ReadByCliente(customerId).size());
            assertOrderHeader(orderId, customerId, orderDate, new BigDecimal("53.02"));
            assertOrderDetails(orderId);
        } finally {
            deleteCustomer(customerId);
        }
    }

    @Test
    void failureRollsBackOrderDetailsAndCart() throws SQLException {
        int customerId = createCustomer();
        CarritoController cartController = new CarritoController();
        PedidoController orderController = new PedidoController();

        try {
            assertEquals(1, cartController.Create(new Carrito(customerId, 1, 1, 10.00, "M")));
            List<Carrito> submittedLines = new ArrayList<>(cartController.ReadByCliente(customerId));
            submittedLines.add(new Carrito(customerId, 1, 1, 20.00, "L"));
            long ordersBefore = countOrders(customerId);

            assertThrows(RuntimeException.class, () -> orderController.crearConDetalle(
                    customerId, LocalDate.of(2026, 10, 27), submittedLines));

            assertEquals(ordersBefore, countOrders(customerId));
            assertEquals(0, countOrderDetailsForCustomer(customerId));
            assertEquals(1, cartController.ReadByCliente(customerId).size());
        } finally {
            deleteCustomer(customerId);
        }
    }

    private static void assertOrderHeader(long orderId, int customerId, LocalDate date, BigDecimal total)
            throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT \"idCliente\", fecha, total FROM pedido WHERE \"idPedido\" = ?")) {
            statement.setLong(1, orderId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                assertEquals(customerId, result.getInt("idCliente"));
                assertEquals(Date.valueOf(date), result.getDate("fecha"));
                assertEquals(total, result.getBigDecimal("total"));
            }
        }
    }

    private static void assertOrderDetails(long orderId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT \"idProducto\", talla, cantidad, \"precioUnitario\", subtotal "
                        + "FROM pedido_detalle WHERE \"idPedido\" = ? ORDER BY talla")) {
            statement.setLong(1, orderId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                assertEquals(1, result.getInt("idProducto"));
                assertEquals("L", result.getString("talla"));
                assertEquals(1, result.getInt("cantidad"));
                assertEquals(new BigDecimal("21.10"), result.getBigDecimal("precioUnitario"));
                assertEquals(new BigDecimal("21.10"), result.getBigDecimal("subtotal"));

                assertTrue(result.next());
                assertEquals(1, result.getInt("idProducto"));
                assertEquals("M", result.getString("talla"));
                assertEquals(2, result.getInt("cantidad"));
                assertEquals(new BigDecimal("12.50"), result.getBigDecimal("precioUnitario"));
                assertEquals(new BigDecimal("25.00"), result.getBigDecimal("subtotal"));
                assertFalse(result.next());
            }
        }
    }

    private static long countOrders(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT COUNT(*) FROM pedido WHERE \"idCliente\" = ?")) {
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
                        "SELECT COUNT(*) FROM pedido_detalle WHERE \"idPedido\" = ?")) {
            statement.setLong(1, orderId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getLong(1);
            }
        }
    }

    private static long countOrderDetailsForCustomer(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT COUNT(*) FROM pedido_detalle detalle "
                        + "JOIN pedido ON pedido.\"idPedido\" = detalle.\"idPedido\" "
                        + "WHERE pedido.\"idCliente\" = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getLong(1);
            }
        }
    }

    private static int createCustomer() throws SQLException {
        String email = "checkout-integration-" + UUID.randomUUID() + "@example.test";
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cliente (nombre, apellido, correo, zipcode, fecha_nacimiento, contrasena, genero) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING \"idCliente\"")) {
            statement.setString(1, "Checkout");
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
        try (Connection connection = DatabaseConfig.loadDefault().openConnection()) {
            try (PreparedStatement deleteDetails = connection.prepareStatement(
                    "DELETE FROM pedido_detalle WHERE \"idPedido\" IN "
                    + "(SELECT \"idPedido\" FROM pedido WHERE \"idCliente\" = ?)")) {
                deleteDetails.setInt(1, customerId);
                deleteDetails.executeUpdate();
            }
            try (PreparedStatement deleteOrders = connection.prepareStatement(
                    "DELETE FROM pedido WHERE \"idCliente\" = ?")) {
                deleteOrders.setInt(1, customerId);
                deleteOrders.executeUpdate();
            }
            try (PreparedStatement deleteCart = connection.prepareStatement(
                    "DELETE FROM carrito WHERE \"idCliente\" = ?")) {
                deleteCart.setInt(1, customerId);
                deleteCart.executeUpdate();
            }
            try (PreparedStatement deleteCustomer = connection.prepareStatement(
                    "DELETE FROM cliente WHERE \"idCliente\" = ?")) {
                deleteCustomer.setInt(1, customerId);
                deleteCustomer.executeUpdate();
            }
        }
    }
}
