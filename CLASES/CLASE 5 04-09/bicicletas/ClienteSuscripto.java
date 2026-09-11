package bicicletas;

public class ClienteSuscripto extends Cliente {

    private String nroSuscripcion;
    private double planMensual;

    public ClienteSuscripto(String dni, String nombre, String apellido, String nroSuscripcion, double planMensual) {
        super(dni, nombre, apellido);
        this.nroSuscripcion = nroSuscripcion;
        this.planMensual = planMensual;
    }

    @Override
    public boolean esSuscripto() {
        return true;
    }
}
