import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Diapositiva 48 - Escribir un JSON "a mano" con BufferedWriter
public class EscribirJSONManual {
    public static void main(String[] args) {
        String ruta = "datos.json";

        // Creamos un texto con formato JSON
        String json = "{\n" +
                      "  \"nombre\": \"Maximiliano\",\n" +
                      "  \"edad\": 30,\n" +
                      "  \"profesion\": \"Ingeniero en Sistemas\"\n" +
                      "}";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(json);
            System.out.println("Archivo JSON creado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
