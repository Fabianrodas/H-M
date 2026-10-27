package modelo;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Loads the local database settings from an ignored .env file. */
public final class DatabaseConfig {

    private final String url;
    private final String user;
    private final String password;
    private final String adminEmail;

    private DatabaseConfig(String url, String user, String password, String adminEmail) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.adminEmail = adminEmail;
    }

    public static DatabaseConfig loadDefault() {
        return load(Paths.get(".env"));
    }

    public static DatabaseConfig load(Path envFile) {
        if (envFile == null || !Files.isRegularFile(envFile)) {
            throw new IllegalStateException(
                    "No se encontró .env. Copia .env.example a .env y completa la conexión local a MySQL.");
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(envFile, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException | IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "No se pudo leer .env. Revisa su formato y los valores de conexión local.", exception);
        }

        String url = requireNonBlank(properties, "DB_URL", envFile);
        String user = requireNonBlank(properties, "DB_USER", envFile);
        String password = requirePresent(properties, "DB_PASSWORD", envFile);
        String adminEmail = requireNonBlank(properties, "ADMIN_EMAIL", envFile);
        return new DatabaseConfig(url, user, password, adminEmail);
    }

    private static String requireNonBlank(Properties properties, String key, Path envFile) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw missingSetting(key, envFile);
        }
        return value.trim();
    }

    private static String requirePresent(Properties properties, String key, Path envFile) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw missingSetting(key, envFile);
        }
        return value;
    }

    private static IllegalStateException missingSetting(String key, Path envFile) {
        return new IllegalStateException(
                "Falta " + key + " en " + envFile + ". Completa la configuración local indicada en .env.example.");
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}
