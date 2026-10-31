import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

// Diapositiva 51 - Leer un JSON directo a un objeto Persona con fromJson()
public class LeerJSONGson {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("persona.json")) {
            Persona persona = gson.fromJson(reader, Persona.class);
            System.out.println("Nombre: " + persona.nombre);
            System.out.println("Edad: " + persona.edad);
            System.out.println("Profesión: " + persona.profesion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
