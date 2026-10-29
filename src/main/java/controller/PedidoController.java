package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Carrito;
import modelo.DbConnection;
import modelo.Pedido;
import modelo.PedidoDetalle;

public class PedidoController {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.15");
    private static final BigDecimal ZERO_MONEY = new BigDecimal("0.00");
    private final DbConnection connection = new DbConnection();

    public long crearConDetalle(int clienteId, LocalDate fecha, List<Carrito> lineas) {
        if (clienteId <= 0) {
            throw new IllegalArgumentException("The order customer is invalid.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("The order date is required.");
        }
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("The shopping bag is empty.");
        }

        List<PedidoDetalle> detalles = new ArrayList<>();
        BigDecimal subtotal = ZERO_MONEY;
        for (Carrito linea : lineas) {
            PedidoDetalle detalle = new PedidoDetalle(linea);
            detalles.add(detalle);
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        BigDecimal impuesto = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(impuesto).setScale(2, RoundingMode.HALF_UP);

        Connection database = null;
        try {
            database = connection.conectar();
            database.setAutoCommit(false);

            long orderId;
            String insertOrder = "INSERT INTO pedido (\"idCliente\", fecha, total) VALUES (?, ?, ?) RETURNING \"idPedido\"";
            try (PreparedStatement statement = database.prepareStatement(insertOrder)) {
                statement.setInt(1, clienteId);
                statement.setDate(2, Date.valueOf(fecha));
                statement.setBigDecimal(3, total);
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        throw new SQLException("PostgreSQL did not return the new order number.");
                    }
                    orderId = result.getLong("idPedido");
                }
            }

            String insertDetail = "INSERT INTO pedido_detalle "
                    + "(\"idPedido\", \"idProducto\", talla, cantidad, \"precioUnitario\", subtotal) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = database.prepareStatement(insertDetail)) {
                for (PedidoDetalle detalle : detalles) {
                    detalle.setIdPedido(orderId);
                    statement.setLong(1, detalle.getIdPedido());
                    statement.setInt(2, detalle.getIdProducto());
                    statement.setString(3, detalle.getTalla());
                    statement.setInt(4, detalle.getCantidad());
                    statement.setBigDecimal(5, detalle.getPrecioUnitario());
                    statement.setBigDecimal(6, detalle.getSubtotal());
                    statement.addBatch();
                }
                statement.executeBatch();
            }

            int removedCartLines;
            try (PreparedStatement statement = database.prepareStatement(
                    "DELETE FROM carrito WHERE \"idCliente\" = ? AND \"idCarrito\" = ? AND \"idProducto\" = ? "
                    + "AND cantidad = ? AND \"precioUnitario\" = ? AND talla = ?")) {
                removedCartLines = 0;
                for (int index = 0; index < lineas.size(); index++) {
                    Carrito linea = lineas.get(index);
                    PedidoDetalle detalle = detalles.get(index);
                    statement.setInt(1, clienteId);
                    statement.setInt(2, linea.getIdCarrito());
                    statement.setInt(3, linea.getIdProducto());
                    statement.setInt(4, linea.getCantidad());
                    statement.setBigDecimal(5, detalle.getPrecioUnitario());
                    statement.setString(6, linea.getTalla());
                    removedCartLines += statement.executeUpdate();
                }
            }
            if (removedCartLines != detalles.size()) {
                throw new SQLException("The shopping bag changed during checkout. The order was rolled back.");
            }

            database.commit();
            return orderId;
        } catch (SQLException exception) {
            rollback(database, exception);
            throw new RuntimeException("Could not complete the order.", exception);
        } catch (RuntimeException exception) {
            rollback(database, exception);
            throw exception;
        } finally {
            connection.desconectar();
        }
    }

    private static void rollback(Connection database, Exception cause) {
        if (database == null) {
            return;
        }
        try {
            database.rollback();
        } catch (SQLException rollbackError) {
            cause.addSuppressed(rollbackError);
        }
    }

    public int Delete(long id) {
        int result = 0;
        String deleteDetails = "DELETE FROM pedido_detalle WHERE \"idPedido\"=?";
        String deleteOrder = "DELETE FROM pedido WHERE \"idPedido\"=?";
        Connection database = null;
        try {
            database = this.connection.conectar();
            database.setAutoCommit(false);
            try (PreparedStatement detailStatement = database.prepareStatement(deleteDetails);
                    PreparedStatement orderStatement = database.prepareStatement(deleteOrder)) {
                detailStatement.setLong(1, id);
                detailStatement.executeUpdate();
                orderStatement.setLong(1, id);
                result = orderStatement.executeUpdate();
            }
            database.commit();
        } catch (SQLException exception) {
            rollback(database, exception);
            throw new RuntimeException(exception);
        } finally {
            this.connection.desconectar();
        }
        return result;
    }

    public ArrayList<Pedido> Read() {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String request = "SELECT * FROM pedido";
        try {
            Connection database = this.connection.conectar();
            try (PreparedStatement statement = database.prepareStatement(request);
                    ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long pedidoId = resultSet.getLong("idPedido");
                    int clienteId = resultSet.getInt("idCliente");
                    String fecha = resultSet.getString("fecha");
                    double total = resultSet.getDouble("total");
                    pedidos.add(new Pedido(pedidoId, clienteId, fecha, total));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            this.connection.desconectar();
        }
        return pedidos;
    }

    public int totalPedidos(int id) {
        int pedidos = 0;
        String request = "SELECT COUNT(*) AS pedidosCliente FROM pedido WHERE \"idCliente\"=?";
        try {
            this.connection.conectar();
            try (PreparedStatement statement = this.connection.getConnection().prepareStatement(request)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        pedidos = resultSet.getInt("pedidoscliente");
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            this.connection.desconectar();
        }
        return pedidos;
    }
}
