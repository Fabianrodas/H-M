package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.ClienteController;
import controller.ProductoController;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "HM_ENV_FILE", matches = ".+")
class PostgreSqlIntegrationTest {

    @Test
    void connectsUsingEnvironmentOverride() throws SQLException {
        DatabaseConfig config = DatabaseConfig.loadDefault();
        assertTrue(config.getUrl().startsWith("jdbc:postgresql:"));
        try (Connection connection = config.openConnection()) {
            assertEquals("PostgreSQL", connection.getMetaData().getDatabaseProductName());
            assertEquals("hm-db", connection.getCatalog());
        }
    }

    @Test
    void seedSequencesAdvanceAfterImport() throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT (SELECT COALESCE(MAX(idcategoria), 0) + 1 FROM categoria), "
                        + "nextval(pg_get_serial_sequence('categoria', 'idcategoria'))");
                ResultSet result = statement.executeQuery()) {
            assertTrue(result.next());
            assertEquals(result.getLong(1), result.getLong(2));
        }
    }

    @Test
    void findsCustomerByEmailIgnoringCase() throws SQLException {
        String email;
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT correo FROM cliente WHERE correo IS NOT NULL "
                        + "AND correo ~ '[a-z]' ORDER BY idcliente LIMIT 1");
                ResultSet result = statement.executeQuery()) {
            assertTrue(result.next(), "Seed must contain a mock email with lowercase letters");
            email = result.getString(1);
        }

        assertNotNull(new ClienteController().ReadByEmail(email.toUpperCase(Locale.ROOT)));
    }

    @Test
    void findsProductByNameIgnoringCase() throws SQLException {
        String name;
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT nombre_producto FROM producto WHERE nombre_producto ~ '[a-z]' "
                        + "ORDER BY idproducto LIMIT 1");
                ResultSet result = statement.executeQuery()) {
            assertTrue(result.next(), "Seed must contain a product name with lowercase letters");
            name = result.getString(1);
        }

        assertTrue(new ProductoController().ReadByName(name.toUpperCase(Locale.ROOT)).size() > 0);
    }

    @Test
    void controllersReadPostgreSqlColumnLabels() {
        assertTrue(new ClienteController().Read().size() > 0);
        assertTrue(new ProductoController().Read().size() > 0);
    }

    @Test
    void upgradesLegacyCustomerPasswordInPostgreSql() throws SQLException {
        String email = "integration-" + UUID.randomUUID() + "@example.test";
        int customerId = createLegacyCustomer(email);
        char[] password = "legacy-customer-test-password".toCharArray();

        try {
            Cliente customer = new ClienteController().ReadByEmail(email);
            assertNotNull(customer);
            assertTrue(PasswordService.verify(password, customer.getContrasena()).needsUpgrade());

            String upgraded = PasswordService.upgradeLegacyPassword(customer.getContrasena(), false);
            assertTrue(new ClienteController().UpdateContrasena(customerId, upgraded) > 0);

            String stored = readCustomerPassword(customerId);
            PasswordService.VerificationResult result = PasswordService.verify(password, stored);
            assertTrue(result.isValid());
            assertTrue(stored.startsWith("pbkdf2-sha256$"));
        } finally {
            deleteCustomer(customerId);
        }
    }

    @Test
    void legacyAdministratorMustChangePasswordAfterUpgrade() throws SQLException {
        int customerId = createLegacyCustomer("admin@example.test");
        char[] password = "legacy-customer-test-password".toCharArray();

        try {
            Cliente customer = new ClienteController().ReadById(customerId);
            assertNotNull(customer);
            String upgraded = PasswordService.upgradeLegacyPassword(customer.getContrasena(), true);
            assertTrue(new ClienteController().UpdateContrasena(customerId, upgraded) > 0);

            PasswordService.VerificationResult result = PasswordService.verify(password, readCustomerPassword(customerId));
            assertTrue(result.isValid());
            assertTrue(result.mustChangePassword());
        } finally {
            deleteCustomer(customerId);
        }
    }

    private static int createLegacyCustomer(String email) throws SQLException {
        String legacyPassword = toLegacyHex("legacy-customer-test-password");
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cliente (nombre, apellido, correo, zipcode, fecha_nacimiento, contrasena, genero) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idcliente")) {
            statement.setString(1, "Integration");
            statement.setString(2, "Customer");
            statement.setString(3, email);
            statement.setString(4, "00000");
            statement.setDate(5, Date.valueOf("2000-01-01"));
            statement.setString(6, legacyPassword);
            statement.setString(7, "Otro");
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getInt(1);
            }
        }
    }

    private static String readCustomerPassword(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT contrasena FROM cliente WHERE idcliente = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getString(1);
            }
        }
    }

    private static void deleteCustomer(int customerId) throws SQLException {
        try (Connection connection = DatabaseConfig.loadDefault().openConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM cliente WHERE idcliente = ?")) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }
    }

    private static String toLegacyHex(String password) {
        StringBuilder encoded = new StringBuilder();
        for (char character : password.toCharArray()) {
            encoded.append(String.format("%02x", (int) character));
        }
        return encoded.toString();
    }
}
