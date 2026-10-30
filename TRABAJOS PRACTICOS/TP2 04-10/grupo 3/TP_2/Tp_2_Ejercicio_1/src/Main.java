import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();

        Producto p1 = new Producto("Teclado", "P001", "Teclado mecanico", 45000);
        Producto p2 = new Producto("Mouse", "P002", "Mouse inalambrico", 18000);
        Producto p3 = new Producto("Monitor", "P003", "Monitor 24 pulgadas", 210000);
        sistema.altaProducto(p1);
        sistema.altaProducto(p2);
        sistema.altaProducto(p3);

        Proveedor prov1 = new Proveedor("Juan Perez", "30111222");
        Proveedor prov2 = new Proveedor("Maria Gomez", "28999888");
        sistema.altaProveedor(prov1);
        sistema.altaProveedor(prov2);

        OrdenCompra o1 = new OrdenCompra("OC-001", prov1, LocalDate.of(2026, 9, 15));
        o1.agregarDetalle(new DetalleCompra(p1, 10));
        o1.agregarDetalle(new DetalleCompra(p2, 25));
        sistema.generarOrden(o1);

        OrdenCompra o2 = new OrdenCompra("OC-002", prov1, LocalDate.of(2026, 9, 28));
        o2.agregarDetalle(new DetalleCompra(p3, 4));
        sistema.generarOrden(o2);

        OrdenCompra o3 = new OrdenCompra("OC-003", prov2, LocalDate.now());
        o3.agregarDetalle(new DetalleCompra(p2, 50));
        o3.agregarDetalle(new DetalleCompra(p3, 2));
        sistema.generarOrden(o3);

        System.out.println("--- Consulta DNI 30111222 ---");
        sistema.consultarOrdenesPorProveedor("30111222");

        System.out.println("\n--- Consulta DNI 28999888 ---");
        sistema.consultarOrdenesPorProveedor("28999888");

        System.out.println("\n--- Consulta DNI 11111111 ---");
        sistema.consultarOrdenesPorProveedor("11111111");
    }
}