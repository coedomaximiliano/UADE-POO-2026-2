import java.util.ArrayList;

public class Sistema {
    private ArrayList<Producto> productos;
    private ArrayList<Proveedor> proveedores;
    private ArrayList<OrdenCompra> ordenes;

    public Sistema() {
        this.productos = new ArrayList<>();
        this.proveedores = new ArrayList<>();
        this.ordenes = new ArrayList<>();
    }

    public void altaProducto(Producto producto) {
        productos.add(producto);
    }

    public void altaProveedor(Proveedor proveedor) {
        if (buscarProveedor(proveedor.getDni()) == null) {
            proveedores.add(proveedor);
        } else {
            System.out.println("El proveedor ya existe.");
        }
    }

    public Proveedor buscarProveedor(String dni) {
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getDni().equals(dni)) {
                return proveedor;
            }
        }
        return null;
    }

    public Producto buscarProducto(String codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }

    public void generarOrden(OrdenCompra orden) {
        if (buscarProveedor(orden.getProveedor().getDni()) == null) {
            System.out.println("Proveedor inexistente.");
            return;
        }
        for (DetalleCompra detalle : orden.getDetalleCompras()) {
            if (buscarProducto(detalle.getProducto().getCodigo()) == null) {
                System.out.println("Producto inexistente.");
                return;
            }
        }
        ordenes.add(orden);
        System.out.println("Orden de compra generada correctamente.");
    }

    public void consultarOrdenesPorProveedor(String dni) {
        System.out.println("Ordenes del proveedor:");
        int valorTotal = 0;
        String nombreProveedor = null;
        for (OrdenCompra orden : ordenes) {
            if (orden.getProveedor().getDni().equals(dni)) {
                nombreProveedor = orden.getProveedor().getNombre();
                System.out.println("Proveedor: " + nombreProveedor + " - Orden: " + orden.getCodigoCompra());
                valorTotal += orden.calcularTotal();
            }
        }
        if (nombreProveedor != null) {
            System.out.println("Proveedor: " + nombreProveedor + " - Valor total: $" + valorTotal);
        } else {
            System.out.println("Valor total: $0");
        }
    }
}