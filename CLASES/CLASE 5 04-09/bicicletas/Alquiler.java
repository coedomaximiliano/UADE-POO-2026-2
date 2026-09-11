package bicicletas;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * ASOCIACION: Alquiler vincula un Cliente y una Bicicleta (multiplicidad
 * 1 a 0..* de cada lado: un cliente/bicicleta puede tener muchos alquileres
 * en el tiempo, pero cada alquiler referencia exactamente a uno de cada).
 *
 * DEPENDENCIA: calcularCosto() usa Tarifario como variable local, nunca
 * como atributo de la clase.
 */
public class Alquiler {

    private Cliente cliente;
    private Bicicleta bicicleta;
    private LocalDateTime retiro;
    private LocalDateTime devolucion;

    public Alquiler(Cliente cliente, Bicicleta bicicleta, LocalDateTime retiro) {
        this.cliente = cliente;
        this.bicicleta = bicicleta;
        this.retiro = retiro;
        bicicleta.setEstado("EN_USO");
    }

    public void registrarDevolucion(LocalDateTime devolucion) {
        this.devolucion = devolucion;
        bicicleta.setEstado("DISPONIBLE");
    }

    public double calcularCosto() {
        Tarifario tarifario = new Tarifario(); // dependencia: variable local, no atributo
        long minutos = Duration.between(retiro, devolucion).toMinutes();
        return tarifario.costoPorMinuto((int) minutos, cliente.esSuscripto());
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }
}
