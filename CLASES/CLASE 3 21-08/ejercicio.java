import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        int edad = 30;
        Integer edadWrapper = edad; // Autoboxing: int a Integer

        ArrayList<Integer> edades = new ArrayList<>();
        edades.add(edadWrapper); // Se puede agregar a una colección

        int otraEdad = edades.get(0); // Unboxing: Integer a int

        System.out.println("otraEdad " + otraEdad);
    }
}