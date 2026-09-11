package bicicletas;

/**
 * Tarifario es el "proveedor" en la relacion de DEPENDENCIA con Alquiler:
 * se usa dentro de un metodo (variable local), nunca como atributo.
 */
public class Tarifario {

    private static final double COSTO_BASE_MINUTO = 15.0;
    private static final double DESCUENTO_SUSCRIPTO = 0.5;

    public double costoPorMinuto(int minutos, boolean esSuscripto) {
        double costo = minutos * COSTO_BASE_MINUTO;
        if (esSuscripto) {
            costo *= DESCUENTO_SUSCRIPTO;
        }
        return costo;
    }
}
