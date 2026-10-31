import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

// Diapositiva 52 - Leer una lista de personas desde JSON con Gson
public class LeerListaJSON {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("personas.json")) {
            Type tipoLista = new TypeToken<List<Persona>>(){}.getType();
            List<Persona> personas = gson.fromJson(reader, tipoLista);

            for (Persona p : personas) {
                System.out.println(p.nombre + " - " + p.profesion);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
