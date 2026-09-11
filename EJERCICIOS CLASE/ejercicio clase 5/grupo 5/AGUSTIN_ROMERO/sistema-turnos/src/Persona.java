abstract class Persona {
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String dni;

    public Persona(String nombre, String apellido, String fechaNacimiento, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
}
