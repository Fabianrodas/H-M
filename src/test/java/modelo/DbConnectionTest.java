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
                "HM_DB_HOST=127.0.0.1\n"
                + "HM_DB_PORT=1\n"
                + "HM_DB_NAME=unavailable\n"
                + "HM_DB_USER=test-user\n"
                + "HM_DB_PASSWORD=test-only\n");
        DatabaseConfig config = DatabaseConfig.load(envFile);
        DbConnection database = new DbConnection(config);

        SQLException error = assertThrows(SQLException.class, database::conectar);

        assertNotNull(error.getMessage());
        assertThrows(IllegalStateException.class, database::getConnection);
    }
}
