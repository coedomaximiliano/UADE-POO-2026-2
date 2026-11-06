package ar.edu.uade.poo.grupo3.tpo.modelo.persistencia;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.TipoSector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorGeneral;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda las sedes en datos/sedes.txt y sus sectores en datos/sectores.txt.
 * Formato de sectores: sede|NUM|codigo|nombre|piso|distancia|filasXasientos
 *                      sede|GEN|codigo|nombre|piso|distancia|capacidad
 */
public class SedeRepositorioArchivo implements Repositorio<Sede> {

    private final Path archivoSedes;
    private final Path archivoSectores;

    public SedeRepositorioArchivo(Path directorioDatos) {
        this.archivoSedes = directorioDatos.resolve("sedes.txt");
        this.archivoSectores = directorioDatos.resolve("sectores.txt");
    }

    @Override
    public List<Sede> cargarTodos() throws IOException {
        List<Sede> sedes = new ArrayList<>();
        for (String linea : leerLineas(archivoSedes)) {
            String[] p = linea.split("\\|", -1);
            if (p.length >= 2) {
                sedes.add(new Sede(p[0], p[1]));
            }
        }
        int nroLinea = 0;
        for (String linea : leerLineas(archivoSectores)) {
            nroLinea++;
            try {
                cargarSector(linea, sedes);
            } catch (RuntimeException e) {
                throw new IOException("sectores.txt, registro " + nroLinea + " inválido: " + e.getMessage(), e);
            }
        }
        return sedes;
    }

    private void cargarSector(String linea, List<Sede> sedes) {
        String[] p = linea.split("\\|", -1);
        if (p.length == 6) {
            // formato viejo (sin nombre de sector): sede|tipo|codigo|piso|distancia|detalle
            p = new String[]{p[0], p[1], p[2], p[2], p[3], p[4], p[5]};
        }
        if (p.length < 7) {
            throw new IllegalArgumentException("faltan campos");
        }
        String nombreSede = p[0].trim();
        Sede sede = sedes.stream().filter(s -> s.getNombre().equalsIgnoreCase(nombreSede)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("la sede " + nombreSede + " no existe"));
        String codigo = p[2];
        String nombre = p[3];
        int piso = Integer.parseInt(p[4].trim());
        double distancia = Double.parseDouble(p[5].trim());
        String detalle = p[6].trim();
        if ("NUM".equalsIgnoreCase(p[1].trim())) {
            if (detalle.matches("\\d+x\\d+")) {
                String[] fx = detalle.split("x");
                sede.agregarSector(Sector.crearNumerado(codigo, nombre, piso, distancia,
                        Integer.parseInt(fx[0]), Integer.parseInt(fx[1])));
            } else {
                SectorNumerado sector = new SectorNumerado(codigo, nombre, piso, distancia);
                for (String token : detalle.split(",")) {
                    String[] fa = token.trim().split("-");
                    sector.agregarAsiento(new Asiento(fa[0], Integer.parseInt(fa[1])));
                }
                sede.agregarSector(sector);
            }
        } else {
            sede.agregarSector(Sector.crear(TipoSector.GENERAL, codigo, nombre, piso, distancia,
                    0, 0, Integer.parseInt(detalle)));
        }
    }

    @Override
    public void guardarTodos(List<Sede> sedes) throws IOException {
        Files.createDirectories(archivoSedes.getParent());
        try (BufferedWriter wSedes = Files.newBufferedWriter(archivoSedes, StandardCharsets.UTF_8);
             BufferedWriter wSectores = Files.newBufferedWriter(archivoSectores, StandardCharsets.UTF_8)) {
            wSedes.write("# nombre|direccion");
            wSedes.newLine();
            wSectores.write("# sede|tipo(NUM/GEN)|codigo|nombre|piso|distancia|filasXasientos o capacidad");
            wSectores.newLine();
            for (Sede sede : sedes) {
                wSedes.write(sede.getNombre() + "|" + sede.getDireccion());
                wSedes.newLine();
                for (Sector sector : sede.getSectores()) {
                    wSectores.write(String.join("|", sede.getNombre(),
                            sector instanceof SectorNumerado ? "NUM" : "GEN",
                            sector.getCodigo(), sector.getNombre(),
                            String.valueOf(sector.getPiso()),
                            String.valueOf(sector.getDistanciaEscenario()),
                            detalle(sector)));
                    wSectores.newLine();
                }
            }
        }
    }

    private String detalle(Sector sector) {
        if (sector instanceof SectorGeneral general) {
            return String.valueOf(general.getCapacidad());
        }
        SectorNumerado numerado = (SectorNumerado) sector;
        List<String> filas = numerado.getFilas();
        int porFila = filas.isEmpty() ? 0 : numerado.getAsientosDeFila(filas.get(0)).size();
        boolean rectangular = filas.size() * porFila == numerado.getCapacidad();
        if (rectangular) {
            return filas.size() + "x" + porFila;
        }
        return String.join(",", numerado.getAsientos().stream().map(Asiento::toString).toList());
    }

    static List<String> leerLineas(Path archivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        if (!Files.exists(archivo)) {
            return lineas;
        }
        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.isBlank() && !linea.startsWith("#")) {
                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }
}
