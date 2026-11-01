package ar.edu.uade.poo.grupo3.tpo.modelo.reglas;

import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * No se permite una selección que deje un asiento libre aislado en la fila
 * (entre dos ocupados, o entre un ocupado y el borde), porque ese asiento ya no se vende.
 */
public class NoDejarAsientoSuelto implements ReglaDeVenta {

    @Override
    public void validar(Compra compra, Funcion funcion, Sector sector, List<Asiento> seleccion,
                        LocalDateTime ahora) throws SeleccionInvalidaException {
        if (!(sector instanceof SectorNumerado numerado) || seleccion.isEmpty()) {
            return;
        }
        Set<Asiento> ocupadosAntes = new HashSet<>(numerado.getAsientos());
        ocupadosAntes.removeAll(funcion.getAsientosLibres(numerado));
        Set<Asiento> ocupadosDespues = new HashSet<>(ocupadosAntes);
        ocupadosDespues.addAll(seleccion);

        for (String fila : filasDe(seleccion)) {
            for (Asiento asiento : numerado.getAsientosDeFila(fila)) {
                boolean quedaSuelto = estaSuelto(asiento, numerado, ocupadosDespues);
                boolean yaEstabaSuelto = estaSuelto(asiento, numerado, ocupadosAntes);
                if (quedaSuelto && !yaEstabaSuelto) {
                    throw new SeleccionInvalidaException("La selección deja el asiento " + asiento
                            + " solo. Elegí otros asientos para dejar al menos dos juntos libres.");
                }
            }
        }
    }

    private boolean estaSuelto(Asiento asiento, SectorNumerado sector, Set<Asiento> ocupados) {
        if (ocupados.contains(asiento)) {
            return false;
        }
        boolean izquierdaBloqueada = bloqueado(sector, asiento.getFila(), asiento.getNumero() - 1, ocupados);
        boolean derechaBloqueada = bloqueado(sector, asiento.getFila(), asiento.getNumero() + 1, ocupados);
        return izquierdaBloqueada && derechaBloqueada;
    }

    /** Un lado está bloqueado si hay un asiento ocupado o si es el borde de la fila. */
    private boolean bloqueado(SectorNumerado sector, String fila, int numero, Set<Asiento> ocupados) {
        return sector.buscarAsiento(fila, numero).map(ocupados::contains).orElse(true);
    }

    private Set<String> filasDe(List<Asiento> seleccion) {
        Set<String> filas = new HashSet<>();
        seleccion.forEach(a -> filas.add(a.getFila()));
        return filas;
    }
}
