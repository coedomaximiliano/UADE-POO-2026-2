import java.io.*;
import java.util.Scanner;

// Diapositiva 47 - Entrada por consola, lectura y escritura de archivos
public class EntradaSalidaDemo {
    public static void main(String[] args) {

        // 1) Entrada por consola usando Scanner
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese su nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Ingrese su edad: ");
        int edad = sc.nextInt();
        sc.nextLine(); // consumir salto de línea pendiente

        System.out.println("Hola " + nombre + ", tienes " + edad + " años.");

        // 2) Entrada desde archivo (si existe archivo 'entrada.txt')
        try (BufferedReader br = new BufferedReader(new FileReader("entrada.txt"))) {
            System.out.println("\nContenido de 'entrada.txt':");
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (FileNotFoundException e) {
            System.out.println("\nNo se encontró el archivo 'entrada.txt'.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3) Salida a archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("salida.txt"))) {
            bw.write("Nombre: " + nombre);
            bw.newLine();
            bw.write("Edad: " + edad);
            System.out.println("\nDatos guardados correctamente en 'salida.txt'.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
