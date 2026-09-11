package bicicletas;

public class ClienteOcasional extends Cliente {

    private String tarjetaCredito;

    public ClienteOcasional(String dni, String nombre, String apellido, String tarjetaCredito) {
        super(dni, nombre, apellido);
        this.tarjetaCredito = tarjetaCredito;
    }

    @Override
    public boolean esSuscripto() {
        return false;
    }
}
