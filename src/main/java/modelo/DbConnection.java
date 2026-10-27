package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    private static final Logger LOGGER = Logger.getLogger(DbConnection.class.getName());
    private final DatabaseConfig config;
    private Connection connection;

    public DbConnection() {
        this.config = null;
    }

    public DbConnection(DatabaseConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("La configuración de base de datos es obligatoria.");
        }
        this.config = config;
    }

    public Connection conectar() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            DatabaseConfig activeConfig = config == null ? DatabaseConfig.loadDefault() : config;
            connection = activeConfig.openConnection();
            return connection;
        } catch (IllegalStateException exception) {
            throw new SQLException(exception.getMessage(), exception);
        }
    }

    public void desconectar() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "No se pudo cerrar la conexión con la base de datos.", exception);
        } finally {
            connection = null;
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("No hay una conexión activa. Conecta con la base de datos primero.");
        }
        return connection;
    }
}
