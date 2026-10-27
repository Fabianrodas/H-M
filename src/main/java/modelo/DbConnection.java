package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    private static final Logger LOGGER = Logger.getLogger(DbConnection.class.getName());
    private Connection connection;

    public boolean conectar() {
        try {
            connection = DatabaseConfig.loadDefault().openConnection();
            return true;
        } catch (IllegalStateException | SQLException exception) {
            LOGGER.log(Level.SEVERE, "No se pudo conectar con la base de datos local.", exception);
            return false;
        }
    }

    public void desconectar() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
            connection = null;
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "No se pudo cerrar la conexión con la base de datos.", exception);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}