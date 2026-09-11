package bicicletas;

public class Bicicleta {

    private String codigo;
    private String estado; // "DISPONIBLE" | "EN_USO"

    public Bicicleta(String codigo) {
        this.codigo = codigo;
        this.estado = "DISPONIBLE";
    }

    public boolean estaDisponible() {
        return "DISPONIBLE".equals(estado);
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }
}
