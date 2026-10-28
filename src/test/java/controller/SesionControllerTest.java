package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import modelo.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "HM_ENV_FILE", matches = ".+")
class SesionControllerTest {

    @Test
    void deletesSessionAboveId500() throws SQLException {
        int customerId = createCustomer();

        try {
            insertSession(501, customerId);

            assertEquals(1, new SesionController().Delete());
            assertEquals(0, countSessions(customerId));
        } finally {
            deleteCustomer(customerId);
        }
    }

    private static int createCustomer() throws SQLException {
        String email = "session-integration-" + UUID.randomUUID() + "@example.test";
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cliente (nombre, apellido, correo, zipcode, fecha_nacimiento, contrasena, genero) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING \"idCliente\"")) {
            statement.setString(1, "Session");
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

    private static void insertSession(int sessionId, int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO sesion (\"idSesion\", \"idCliente\") VALUES (?, ?)")) {
            statement.setInt(1, sessionId);
            statement.setInt(2, customerId);
            statement.executeUpdate();
        }
    }

    private static long countSessions(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT COUNT(*) FROM sesion WHERE \"idCliente\" = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getLong(1);
            }
        }
    }

    private static void deleteCustomer(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection()) {
            try (PreparedStatement deleteSession = connection.prepareStatement(
                    "DELETE FROM sesion WHERE \"idCliente\" = ?")) {
                deleteSession.setInt(1, customerId);
                deleteSession.executeUpdate();
            }
            try (PreparedStatement deleteCustomer = connection.prepareStatement(
                    "DELETE FROM cliente WHERE \"idCliente\" = ?")) {
                deleteCustomer.setInt(1, customerId);
                deleteCustomer.executeUpdate();
            }
        }
    }
}
