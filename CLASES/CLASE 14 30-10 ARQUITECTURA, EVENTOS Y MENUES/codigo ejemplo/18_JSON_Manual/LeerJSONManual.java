import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Diapositiva 50 - Leer un archivo JSON línea por línea
public class LeerJSONManual {
    public static void main(String[] args) {
        String ruta = "persona.json";
        StringBuilder contenido = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            System.out.println("Contenido del archivo JSON:\n" + contenido);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
