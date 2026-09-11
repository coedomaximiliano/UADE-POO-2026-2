package eventos;

import java.time.LocalDate;

/**
 * Calendario es el "proveedor" en la relacion de DEPENDENCIA con Evento.
 */
public class Calendario {

    public LocalDate restarDias(LocalDate fecha, int dias) {
        return fecha.minusDays(dias);
    }
}
