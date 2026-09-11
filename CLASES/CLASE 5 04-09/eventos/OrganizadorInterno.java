package eventos;

public class OrganizadorInterno extends Organizador {

    private String legajo;

    public OrganizadorInterno(String nombre, String telefono, String legajo) {
        super(nombre, telefono);
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }
}
