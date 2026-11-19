package ar.edu.uade.poo.grupo3.tpo;

import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.TipoSector;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Evento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;
import ar.edu.uade.poo.grupo3.tpo.modelo.promociones.Promocion;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Configura la boletería: carga los .txt de la carpeta datos/ y, si están vacíos, crea datos de ejemplo.
 * Los eventos y funciones no se guardan: se crean en memoria cada vez que arranca, con fechas a partir de hoy.
 */
public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static void inicializar(Boleteria boleteria) throws IOException {
        boleteria.agregarPromocion(new Promocion("BANCO20", 20, "Promo bancaria"));
        boleteria.agregarPromocion(new Promocion("FAN10", 10, "Club de fans"));

        boleteria.cargarDatos();
        if (boleteria.getSedes().isEmpty()) {
            crearSedesDeEjemplo(boleteria);
            boleteria.guardarSedes();
        }
        if (boleteria.getClientes().isEmpty()) {
            boleteria.registrarCliente("30111222", "Ana García", "ana@mail.com");
            boleteria.registrarCliente("28999111", "Bruno Díaz", "bruno@mail.com");
            boleteria.registrarCliente("35444777", "Carla Méndez", "carla@mail.com");
            boleteria.guardarClientes();
        }
        crearEventosDeEjemplo(boleteria);
    }

    private static void crearSedesDeEjemplo(Boleteria boleteria) {
        Sede esfera = new Sede("Esfera Las Vegas", "255 Sands Ave, Las Vegas, NV");
        esfera.agregarSector(Sector.crearNumerado("FLR", "Floor", 0, 5, 8, 16));
        esfera.agregarSector(Sector.crearNumerado("100", "Nivel 100", 1, 15, 6, 20));
        esfera.agregarSector(Sector.crearNumerado("200", "Nivel 200", 2, 30, 6, 24));
        esfera.agregarSector(Sector.crearNumerado("300", "Nivel 300", 3, 45, 5, 28));
        esfera.agregarSector(Sector.crearNumerado("400", "Nivel 400", 4, 60, 5, 30));
        boleteria.agregarSede(esfera);

        Sede estadio = new Sede("Estadio Monumental", "Av. Figueroa Alcorta 7597, CABA");
        estadio.agregarSector(Sector.crear(TipoSector.GENERAL, "CAMPO", "Campo", 0, 10, 0, 0, 5000));
        estadio.agregarSector(Sector.crearNumerado("PSM", "Platea San Martín", 1, 40, 10, 30));
        estadio.agregarSector(Sector.crearNumerado("PBE", "Platea Belgrano", 1, 40, 10, 30));
        boleteria.agregarSede(estadio);
    }

    public static void crearEventosDeEjemplo(Boleteria boleteria) {
        Sede esfera = boleteria.buscarSede("Esfera Las Vegas");
        if (esfera == null) {
            return;
        }
        Evento recital = new Evento(1, "Los Satélites — Órbita Tour", "Show en 360°", 120, "Rock");
        recital.agregarArtista("Los Satélites");
        LocalDate hoy = LocalDate.now();
        Funcion f1 = new Funcion(1, recital, LocalDateTime.of(hoy.plusDays(15), LocalTime.of(21, 0)),
                50000, esfera);
        Funcion f2 = new Funcion(2, recital, LocalDateTime.of(hoy.plusDays(30), LocalTime.of(21, 0)),
                50000, esfera);
        Funcion f3 = new Funcion(3, recital, LocalDateTime.of(hoy.plusDays(1), LocalTime.of(21, 0)),
                50000, esfera);
        recital.agregarFuncion(f1);
        recital.agregarFuncion(f2);
        recital.agregarFuncion(f3);
        boleteria.agregarEvento(recital);
    }
}
