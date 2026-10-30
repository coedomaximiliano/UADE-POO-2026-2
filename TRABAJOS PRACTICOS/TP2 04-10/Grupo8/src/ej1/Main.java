package ej1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static void main(String[] args) {
		SistemaGestionProductos sistema = new SistemaGestionProductos(new HashMap<>(), new HashMap<>(),
				new HashMap<>());
		cargarDatosDeEjemplo(sistema);
		Scanner scanner = new Scanner(System.in);

		int opcion;
		do {
			System.out.println("\n--- Sistema de compras ---");
			System.out.println("1. Alta de producto");
			System.out.println("2. Generar orden de compra");
			System.out.println("3. Consultar ordenes de un proveedor");
			System.out.println("4. Listar productos");
			System.out.println("0. Salir");
			opcion = pedirEntero(scanner, "Opcion: ");

			switch (opcion) {
			case 1 -> altaDeProducto(scanner, sistema);
			case 2 -> generarOrden(scanner, sistema);
			case 3 -> consultarOrdenes(scanner, sistema);
			case 4 -> listarProductos(sistema);
			case 0 -> System.out.println("Hasta luego.");
			default -> System.out.println("Opcion invalida.");
			}
		} while (opcion != 0);
	}

	// a) Alta de productos
	private static void altaDeProducto(Scanner scanner, SistemaGestionProductos sistema) {
		int codigo = pedirEntero(scanner, "Codigo del producto: ");
		if (sistema.getProducts().containsKey(codigo)) {
			System.out.println("Ya existe un producto con ese codigo.");
			return;
		}
		System.out.print("Descripcion: ");
		String descripcion = scanner.nextLine().trim();
		double precio = pedirPrecio(scanner, "Precio unitario: ");

		sistema.agregarProducto(new Producto(codigo, precio, descripcion));
		System.out.println("Producto agregado.");
	}

	// b) Generacion de la orden de compra
	private static void generarOrden(Scanner scanner, SistemaGestionProductos sistema) {
		int dni = pedirEntero(scanner, "DNI del proveedor: ");
		Proveedor proveedor = sistema.getSuppliers().get(dni);
		if (proveedor == null) {
			System.out.println("No existe un proveedor con ese DNI.");
			return;
		}

		LocalDate fecha = pedirFecha(scanner, "Fecha de la orden (dd/MM/yyyy): ");

		int codigoProducto = pedirEntero(scanner, "Codigo del producto: ");
		Producto producto = sistema.getProducts().get(codigoProducto);
		if (producto == null) {
			System.out.println("No existe un producto con ese codigo.");
			return;
		}

		int cantidad = pedirEntero(scanner, "Cantidad a comprar: ");
		if (cantidad <= 0) {
			System.out.println("La cantidad debe ser mayor a 0.");
			return;
		}

		// Numero de orden: el siguiente al mas alto que exista
		int numero = 1;
		for (int existente : sistema.getPurchaseOrders().keySet())
			if (existente >= numero)
				numero = existente + 1;

		OrdenDeCompra orden = new OrdenDeCompra(numero, producto, cantidad, proveedor, fecha);
		sistema.getPurchaseOrders().put(numero, orden);
		System.out.println("Orden generada:");
		imprimirOrden(orden);
	}

	// c) Consulta de las ordenes de un proveedor
	private static void consultarOrdenes(Scanner scanner, SistemaGestionProductos sistema) {
		int dni = pedirEntero(scanner, "DNI del proveedor: ");
		Proveedor proveedor = sistema.getSuppliers().get(dni);
		if (proveedor == null) {
			System.out.println("No existe un proveedor con ese DNI.");
			return;
		}

		System.out.println("\nProveedor: " + proveedor.getFullName());
		double importeTotal = 0;
		for (OrdenDeCompra orden : sistema.getPurchaseOrders().values()) {
			if (orden.getSupplier().equals(proveedor)) {
				imprimirOrden(orden);
				importeTotal += orden.getAmount() * orden.getProduct().getUnitPrice();
			}
		}
		System.out.println("Cantidad de ordenes: " + sistema.BuscarProovedores(proveedor));
		System.out.printf("Importe total de las ordenes: $%.2f%n", importeTotal);
	}

	private static void listarProductos(SistemaGestionProductos sistema) {
		if (sistema.getProducts().isEmpty())
			System.out.println("No hay productos cargados.");
		for (Producto producto : sistema.getProducts().values())
			System.out.printf("Producto %d | %s | $%.2f%n", producto.getCode(), producto.getDescription(),
					producto.getUnitPrice());
	}

	private static void imprimirOrden(OrdenDeCompra orden) {
		System.out.printf("Orden Nro %d | Fecha: %s | Proveedor: %s | Producto: %d - %s | Cantidad: %d%n",
				orden.getCode(), orden.getDate(), orden.getSupplier().getFullName(), orden.getProduct().getCode(),
				orden.getProduct().getDescription(), orden.getAmount());
	}

	// Pide un entero hasta que el ingreso sea valido
	private static int pedirEntero(Scanner scanner, String mensaje) {
		while (true) {
			System.out.print(mensaje);
			try {
				return Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Ingrese un numero entero.");
			}
		}
	}

	// Acepta 1500.50 o 1500,50. Pide de nuevo si no es valido o no es positivo
	private static double pedirPrecio(Scanner scanner, String mensaje) {
		while (true) {
			System.out.print(mensaje);
			try {
				double precio = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
				if (precio > 0)
					return precio;
				System.out.println("El precio debe ser mayor a 0.");
			} catch (NumberFormatException e) {
				System.out.println("Ingrese un numero valido.");
			}
		}
	}

	private static LocalDate pedirFecha(Scanner scanner, String mensaje) {
		while (true) {
			System.out.print(mensaje);
			try {
				return LocalDate.parse(scanner.nextLine().trim(), FORMATO_FECHA);
			} catch (DateTimeParseException e) {
				System.out.println("Formato invalido, use dd/MM/yyyy.");
			}
		}
	}

	// Datos de ejemplo, inventados para poder probar el sistema
	private static void cargarDatosDeEjemplo(SistemaGestionProductos sistema) {
		Producto teclado = new Producto(1, 15000.50, "Teclado mecanico");
		Producto mouse = new Producto(2, 8000, "Mouse inalambrico");
		sistema.agregarProducto(teclado);
		sistema.agregarProducto(mouse);

		Set<Producto> deAna = new HashSet<>();
		deAna.add(teclado);
		deAna.add(mouse);
		Proveedor ana = new Proveedor(30111222, "Facundo Garcia", deAna);
		Proveedor luis = new Proveedor(28999888, "Luis Gomez", new HashSet<>());
		sistema.getSuppliers().put(ana.getDNI(), ana);
		sistema.getSuppliers().put(luis.getDNI(), luis);
	}
}
