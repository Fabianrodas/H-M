package modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PedidoDetalle {
    private long idPedido;
    private final int idProducto;
    private final String talla;
    private final int cantidad;
    private final BigDecimal precioUnitario;
    private final BigDecimal subtotal;

    public PedidoDetalle(Carrito carrito) {
        if (carrito == null) {
            throw new IllegalArgumentException("The shopping bag item is required.");
        }
        if (carrito.getCantidad() <= 0 || carrito.getPrecio() < 0) {
            throw new IllegalArgumentException("The product quantity and price are invalid.");
        }
        this.idProducto = carrito.getIdProducto();
        this.talla = carrito.getTalla();
        this.cantidad = carrito.getCantidad();
        this.precioUnitario = BigDecimal.valueOf(carrito.getPrecio()).setScale(2, RoundingMode.HALF_UP);
        this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getTalla() {
        return talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
