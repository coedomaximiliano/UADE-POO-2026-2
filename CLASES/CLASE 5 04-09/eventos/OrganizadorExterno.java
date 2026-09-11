package eventos;

public class OrganizadorExterno extends Organizador {

    private String empresa;
    private String cuit;

    public OrganizadorExterno(String nombre, String telefono, String empresa, String cuit) {
        super(nombre, telefono);
        this.empresa = empresa;
        this.cuit = cuit;
    }

    public String getEmpresa() {
        return empresa;
    }
}
