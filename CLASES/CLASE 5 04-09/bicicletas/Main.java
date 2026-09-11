package bicicletas;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Cliente ocasional = new ClienteOcasional("30111222", "Nico", "Alonso", "**** 4521");
        Cliente suscripto = new ClienteSuscripto("28999888", "Marina", "Ruiz", "SUS-77", 3500.0);

        Bicicleta bici1 = new Bicicleta("BICI-01");
        Bicicleta bici2 = new Bicicleta("BICI-02");

        Estacion estacionA = new Estacion("EST-A", "Plaza Central");
        Estacion estacionB = new Estacion("EST-B", "Terminal");
        estacionA.agregarBicicleta(bici1);
        estacionA.agregarBicicleta(bici2);
        // bici2 se reasigna a otra estacion sin dejar de existir:
        estacionA.quitarBicicleta(bici2);
        estacionB.agregarBicicleta(bici2);

        LocalDateTime retiro = LocalDateTime.of(2026, 9, 10, 9, 0);
        LocalDateTime devolucion = LocalDateTime.of(2026, 9, 10, 9, 42);

        Alquiler alquilerOcasional = new Alquiler(ocasional, bici1, retiro);
        alquilerOcasional.registrarDevolucion(devolucion);

        Alquiler alquilerSuscripto = new Alquiler(suscripto, bici2, retiro);
        alquilerSuscripto.registrarDevolucion(devolucion);

        System.out.println("Estacion A: " + estacionA.getBicicletas().size() + " bici(s)");
        System.out.println("Estacion B: " + estacionB.getBicicletas().size() + " bici(s)");
        System.out.println("Costo ocasional (" + ocasional.getNombreCompleto() + "): $" + alquilerOcasional.calcularCosto());
        System.out.println("Costo suscripto (" + suscripto.getNombreCompleto() + "): $" + alquilerSuscripto.calcularCosto());
        System.out.println("Bici1 disponible tras devolucion: " + bici1.estaDisponible());
    }
}
