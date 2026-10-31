package datos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Turno;

// Capa de datos: solo sabe leer y escribir turnos en un archivo de texto
public class TurnoRepositorio {
    private final String ruta;

    public TurnoRepositorio(String ruta) {
        this.ruta = ruta;
    }

    public List<Turno> cargar() throws IOException {
        List<Turno> lista = new ArrayList<>();
        if (!new File(ruta).exists()) return lista;
        try (BufferedReader in = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = in.readLine()) != null) {
                if (!linea.isBlank()) lista.add(Turno.desdeLinea(linea));
            }
        }
        return lista;
    }

    public void guardar(List<Turno> turnos) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(ruta))) {
            for (Turno t : turnos) {
                out.write(t.aLinea());
                out.newLine();
            }
        }
    }
}
