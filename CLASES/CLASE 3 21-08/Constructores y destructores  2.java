class Recurso {
    private String nombre;

    public Recurso(String nombre) {
        this.nombre = nombre;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            System.out.println("Liberando recurso: " + nombre);
        } finally {
            super.finalize();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Recurso r = new Recurso("Archivo.txt");
        r = null; // Quitamos la referencia
        System.gc(); // Sugerimos al GC que ejecute la recolección
        System.out.println("Fin del programa");
    }
}