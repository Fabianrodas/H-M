package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DatabaseConfigTest {

    @TempDir
    Path tempDirectory;

    @Test
    void missingFileHasActionableError() {
        Path missingFile = tempDirectory.resolve(".env");

        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> DatabaseConfig.load(missingFile));

        assertTrue(error.getMessage().contains(".env"));
    }

    @Test
    void rejectsMissingRequiredFields() throws IOException {
        Path envFile = tempDirectory.resolve(".env");
        Files.writeString(envFile, "DB_URL=jdbc:mysql://localhost/test\nDB_USER=test\nDB_PASSWORD=\n");

        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> DatabaseConfig.load(envFile));

        assertTrue(error.getMessage().contains("ADMIN_EMAIL"));
    }

    @Test
    void loadsValuesAndPreservesEqualsInPassword() throws IOException {
        Path envFile = tempDirectory.resolve(".env");
        Files.writeString(envFile,
                "DB_URL=jdbc:mysql://localhost/test\n"
                + "DB_USER=test-user\n"
                + "DB_PASSWORD=sample=key\n"
                + "ADMIN_EMAIL=admin@example.test\n");

        DatabaseConfig config = DatabaseConfig.load(envFile);

        assertEquals("jdbc:mysql://localhost/test", config.getUrl());
        assertEquals("test-user", config.getUser());
        assertEquals("sample=key", config.getPassword());
        assertEquals("admin@example.test", config.getAdminEmail());
    }
}
