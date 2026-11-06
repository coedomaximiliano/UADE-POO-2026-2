import java.time.LocalDate;
import java.util.ArrayList;

public class OrdenCompra {
    private String codigoCompra;
    private Proveedor proveedor;
    private LocalDate fecha;
    private ArrayList<DetalleCompra> detalleCompras;

    public OrdenCompra(String codigoCompra, Proveedor proveedor, LocalDate fecha) {
        this.codigoCompra = codigoCompra;
        this.proveedor = proveedor;
        this.fecha = fecha;
        this.detalleCompras = new ArrayList<>();
    }
    public String getCodigoCompra() {
        return codigoCompra;
    }
    public void setCodigoCompra(String codigoCompra) {
        this.codigoCompra = codigoCompra;
    }
    public Proveedor getProveedor() {
        return proveedor;
    }
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public void agregarDetalle (DetalleCompra detalle){
        this.detalleCompras.add(detalle);
    }

    public ArrayList<DetalleCompra> getDetalleCompras() {
        return detalleCompras;
    }

    public int calcularTotal() {
        int total = 0;
        for (DetalleCompra detalle : detalleCompras) {
            total += detalle.getCantidad() * detalle.getProducto().getPrecio();
        }
        return total;
    }
}

