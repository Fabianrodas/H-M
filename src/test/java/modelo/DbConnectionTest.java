package modelo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DbConnectionTest {

    @TempDir
    Path tempDirectory;

    @Test
    void unavailableServerReportsConnectionError() throws IOException {
        Path envFile = tempDirectory.resolve(".env");
        Files.writeString(envFile,
                "DB_URL=jdbc:mysql://127.0.0.1:1/unavailable?connectTimeout=1000\n"
                + "DB_USER=test-user\n"
                + "DB_PASSWORD=test-only\n"
                + "ADMIN_EMAIL=admin@example.test\n");
        DatabaseConfig config = DatabaseConfig.load(envFile);
        DbConnection database = new DbConnection(config);

        SQLException error = assertThrows(SQLException.class, database::conectar);

        assertNotNull(error.getMessage());
        assertThrows(IllegalStateException.class, database::getConnection);
    }
}
