package ar.edu.uade.poo.grupo3.tpo.modelo.promociones;

import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;

/**
 * Promoción por código: descuento porcentual sobre las entradas al ingresar un código
 * (por ejemplo, de un banco o un sponsor). Una compra admite una sola promoción.
 */
public class Promocion {

    private final String codigo;
    private final double porcentaje;
    private final String descripcion;

    public Promocion(String codigo, double porcentaje, String descripcion) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la promoción es obligatorio");
        }
        if (porcentaje <= 0 || porcentaje >= 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.porcentaje = porcentaje;
        this.descripcion = descripcion;
    }

    public boolean coincideCon(String codigoIngresado) {
        return codigoIngresado != null && codigo.equalsIgnoreCase(codigoIngresado.trim());
    }

    public String getDescripcion() {
        return descripcion + " (" + codigo + ", " + (int) porcentaje + "% off)";
    }

    public boolean aplicaA(Compra compra) {
        return !compra.getEntradasActivas().isEmpty();
    }

    public double calcularDescuento(Compra compra) {
        return Math.round(compra.calcularSubtotalEntradas() * porcentaje / 100.0);
    }

    public String getCodigo() {
        return codigo;
    }
}
