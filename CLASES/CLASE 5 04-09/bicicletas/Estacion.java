package bicicletas;

import java.util.ArrayList;
import java.util.List;

/**
 * AGREGACION: Estacion sólo referencia Bicicletas que ya existen; una
 * bicicleta puede quitarse de una estación y agregarse a otra sin dejar
 * de existir (se reasigna).
 */
public class Estacion {

    private String codigo;
    private String ubicacion;
    private List<Bicicleta> bicicletas = new ArrayList<>();

    public Estacion(String codigo, String ubicacion) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
    }

    public void agregarBicicleta(Bicicleta b) {
        bicicletas.add(b);
    }

    public void quitarBicicleta(Bicicleta b) {
        bicicletas.remove(b);
    }

    public List<Bicicleta> getBicicletas() {
        return bicicletas;
    }

    public String getCodigo() {
        return codigo;
    }
}
