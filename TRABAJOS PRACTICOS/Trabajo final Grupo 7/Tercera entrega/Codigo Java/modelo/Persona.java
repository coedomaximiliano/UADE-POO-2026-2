package modelo;

public abstract class Persona {
    private String nombre;
    private String dni;
    private String email;

    // Estos datos los comparten el paciente y el medico.
    public Persona(String nombre, String dni, String email) {
        if (nombre == null || nombre.trim().isEmpty()
                || dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre y DNI son obligatorios.");
        }
        this.nombre = nombre.trim();
        this.dni = dni.trim();
        this.email = email == null ? "" : email.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public abstract String mostrarDatos();

    public void actualizarDatos(String nombre, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
        this.email = email == null ? "" : email.trim();
    }

    @Override
    public String toString() {
        // Asi el combo muestra el nombre en lugar de la referencia del objeto.
        return mostrarDatos();
    }
}
