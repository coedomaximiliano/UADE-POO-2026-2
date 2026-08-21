public class Main
{
    public static void main(String[] args) {
        Persona p1 = new Persona(); //Llama al constructor por defecto
        Persona p2 = new Persona("Juan", 30); //Llama al constructor con parámetros
    }
}


public class Persona {
    String nombre;
    int edad;

    // Constructor por defecto
    public Persona() {
        nombre = "Desconocido";
        edad = 0;
    }

    // Constructor con parámetros
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
}