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
        String configuredFile = System.getenv("HM_ENV_FILE");
        Path envFile = configuredFile == null || configuredFile.trim().isEmpty()
                ? Paths.get(".env")
                : Paths.get(configuredFile.trim());
        return load(envFile);
    }

    public static DatabaseConfig load(Path envFile) {
        if (envFile == null || !Files.isRegularFile(envFile)) {
            throw new IllegalStateException(
                    "Configuration file not found. Copy .env.example to .env and enter your local PostgreSQL connection settings.");
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(envFile, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException | IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Could not read the configuration file. Check its format and local connection settings.", exception);
        }

        String host = requireNonBlank(properties, "HM_DB_HOST", envFile);
        String portValue = requireNonBlank(properties, "HM_DB_PORT", envFile);
        int port;
        try {
            port = Integer.parseInt(portValue);
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "HM_DB_PORT must be a number between 1 and 65535.", exception);
        }
        if (port < 1 || port > 65535) {
            throw new IllegalStateException("HM_DB_PORT must be a number between 1 and 65535.");
        }
        String databaseName = requireNonBlank(properties, "HM_DB_NAME", envFile);
        String user = requireNonBlank(properties, "HM_DB_USER", envFile);
        String password = requirePresent(properties, "HM_DB_PASSWORD", envFile);
        String jdbcHost = host.indexOf(':') >= 0 && !host.startsWith("[") ? "[" + host + "]" : host;
        String url = "jdbc:postgresql://" + jdbcHost + ":" + port + "/" + databaseName;
        String adminEmail = properties.getProperty("ADMIN_EMAIL", "").trim();
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
                "Missing " + key + " in " + envFile + ". Add the local setting shown in .env.example.");
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
