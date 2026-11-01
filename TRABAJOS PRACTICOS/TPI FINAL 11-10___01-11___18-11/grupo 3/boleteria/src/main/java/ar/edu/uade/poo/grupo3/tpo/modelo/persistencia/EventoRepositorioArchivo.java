package ar.edu.uade.poo.grupo3.tpo.modelo.persistencia;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoFuncion;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Evento;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Preventa;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda eventos en datos/eventos.txt y sus funciones en datos/funciones.txt.
 * Formato eventos: id|nombre|descripcion|duracionMin|genero|artistas(separados por coma)
 * Formato funciones: idEvento|idFuncion|sede|fechaHora(ISO)|precioBase|estado|prevCodigo|prevInicio|prevFin
 */
public class EventoRepositorioArchivo implements Repositorio<Evento> {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path archivoEventos;
    private final Path archivoFunciones;
    private List<Sede> sedesParaCarga = List.of();

    public EventoRepositorioArchivo(Path directorioDatos) {
        this.archivoEventos = directorioDatos.resolve("eventos.txt");
        this.archivoFunciones = directorioDatos.resolve("funciones.txt");
    }

    /** Las funciones referencian sedes ya cargadas; hay que indicarlas antes de {@link #cargarTodos()}. */
    public void prepararCarga(List<Sede> sedes) {
        this.sedesParaCarga = sedes == null ? List.of() : List.copyOf(sedes);
    }

    @Override
    public List<Evento> cargarTodos() throws IOException {
        Map<Integer, Evento> porId = new LinkedHashMap<>();
        for (String linea : SedeRepositorioArchivo.leerLineas(archivoEventos)) {
            String[] p = linea.split("\\|", -1);
            if (p.length < 5) {
                continue;
            }
            int id = Integer.parseInt(p[0].trim());
            int duracion = Integer.parseInt(p[3].trim());
            String genero = p.length > 4 ? p[4].trim() : "";
            Evento evento = new Evento(id, p[1], p[2], duracion, genero);
            if (p.length > 5 && !p[5].isBlank()) {
                for (String artista : p[5].split(",")) {
                    if (!artista.isBlank()) {
                        evento.agregarArtista(artista.trim());
                    }
                }
            }
            porId.put(id, evento);
        }
        int nroLinea = 0;
        for (String linea : SedeRepositorioArchivo.leerLineas(archivoFunciones)) {
            nroLinea++;
            try {
                cargarFuncion(linea, porId);
            } catch (RuntimeException e) {
                throw new IOException("funciones.txt, registro " + nroLinea + " inválido: " + e.getMessage(), e);
            }
        }
        return new ArrayList<>(porId.values());
    }

    private void cargarFuncion(String linea, Map<Integer, Evento> eventos) {
        String[] p = linea.split("\\|", -1);
        if (p.length < 6) {
            throw new IllegalArgumentException("faltan campos");
        }
        int idEvento = Integer.parseInt(p[0].trim());
        Evento evento = eventos.get(idEvento);
        if (evento == null) {
            throw new IllegalArgumentException("no existe el evento " + idEvento);
        }
        String nombreSede = p[2].trim();
        Sede sede = sedesParaCarga.stream().filter(s -> s.getNombre().equalsIgnoreCase(nombreSede)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("la sede " + nombreSede + " no existe"));
        int idFuncion = Integer.parseInt(p[1].trim());
        LocalDateTime fechaHora = LocalDateTime.parse(p[3].trim(), ISO);
        double precio = Double.parseDouble(p[4].trim());
        Funcion funcion = new Funcion(idFuncion, evento, fechaHora, precio, sede);
        if (p.length > 5 && !p[5].isBlank()) {
            funcion.setEstado(EstadoFuncion.valueOf(p[5].trim()));
        }
        if (p.length > 8 && !p[6].isBlank()) {
            LocalDateTime inicio = LocalDateTime.parse(p[7].trim(), ISO);
            LocalDateTime fin = LocalDateTime.parse(p[8].trim(), ISO);
            funcion.setPreventa(new Preventa(p[6].trim(), inicio, fin));
        }
        evento.agregarFuncion(funcion);
    }

    @Override
    public void guardarTodos(List<Evento> eventos) throws IOException {
        Files.createDirectories(archivoEventos.getParent());
        try (BufferedWriter wEventos = Files.newBufferedWriter(archivoEventos, StandardCharsets.UTF_8);
             BufferedWriter wFunciones = Files.newBufferedWriter(archivoFunciones, StandardCharsets.UTF_8)) {
            wEventos.write("# id|nombre|descripcion|duracionMin|genero|artistas(coma)");
            wEventos.newLine();
            wFunciones.write("# idEvento|idFuncion|sede|fechaHora|precioBase|estado|prevCodigo|prevInicio|prevFin");
            wFunciones.newLine();
            for (Evento evento : eventos) {
                wEventos.write(String.join("|",
                        String.valueOf(evento.getId()),
                        evento.getNombre(),
                        evento.getDescripcion(),
                        String.valueOf(evento.getDuracionMinutos()),
                        evento.getGenero(),
                        String.join(",", evento.getArtistas())));
                wEventos.newLine();
                for (Funcion funcion : evento.getFunciones()) {
                    Preventa prev = funcion.getPreventa();
                    wFunciones.write(String.join("|",
                            String.valueOf(evento.getId()),
                            String.valueOf(funcion.getId()),
                            funcion.getSede().getNombre(),
                            funcion.getFechaHora().format(ISO),
                            String.valueOf((long) funcion.getPrecioBase()),
                            funcion.getEstado().name(),
                            prev == null ? "" : prev.getCodigoAcceso(),
                            prev == null ? "" : prev.getFechaInicio().format(ISO),
                            prev == null ? "" : prev.getFechaFin().format(ISO)));
                    wFunciones.newLine();
                }
            }
        }
    }
}
