import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;

// Diapositiva 49 - Convertir un objeto Java a JSON con Gson
public class EscribirJSONGson {
    public static void main(String[] args) {
        Persona persona = new Persona("Maximiliano", 30, "Ingeniero en Sistemas");
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter("persona.json")) {
            gson.toJson(persona, writer);
            System.out.println("Archivo JSON creado correctamente con Gson.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
