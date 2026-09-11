public class Turno {
    private String fecha;
    private String hora;
    private Estado estado;

    public Turno(String fecha, String hora, Estado estado) {
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public Estado getEstado() {
        return estado;
    }
}
