package ejercicio2;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static void main(String[] args) {
		SistemaDeReserva sistema = new SistemaDeReserva(cargarVuelos());
		BuscadorDeVuelos buscador = new BuscadorDeVuelos();
		Scanner scanner = new Scanner(System.in);

		System.out.print("Ciudad de origen: ");
		String origen = scanner.nextLine().trim();
		System.out.print("Ciudad de destino: ");
		String destino = scanner.nextLine().trim();
		LocalDate desde = pedirFecha(scanner, "Fecha mas temprana (dd/MM/yyyy): ");
		LocalDate hasta = pedirFecha(scanner, "Fecha mas tardia (dd/MM/yyyy): ");

		boolean primeraClase = pedirOpcion(scanner, "Clase (1 = Primera, 2 = Turista): ");
		boolean fumador = pedirOpcion(scanner, "Seccion (1 = Fumador, 2 = No fumador): ");

		List<Vuelo> encontrados = buscador.buscarVuelos(sistema.getFlights(), fumador, primeraClase,
				desde.atStartOfDay(), hasta.atTime(23, 59), destino, origen);

		if (encontrados.isEmpty()) {
			System.out.println("No hay vuelos acordes a sus intereses. Lo invitamos a volver en otro momento.");
			return;
		}

		System.out.println("\nVuelos disponibles:");
		for (Vuelo vuelo : encontrados)
			System.out.println(vuelo);

		Vuelo elegido = pedirVuelo(scanner, encontrados);
		if (elegido == null) {
			System.out.println("No se confirmo ningun vuelo. Lo invitamos a volver en otro momento.");
			return;
		}

		System.out.print("Nombre: ");
		String nombre = scanner.nextLine().trim();
		System.out.print("Apellido: ");
		String apellido = scanner.nextLine().trim();

		Pasaje pasaje = sistema.recordReserve(
				new Pasaje(sistema.nextTicketNumber(), elegido.getFlightCode(), nombre, apellido, primeraClase, fumador));

		if (pasaje == null)
			System.out.println("No se pudo realizar la reserva.");
		else
			System.out.println("\nReserva realizada. Pasaje emitido:\n" + pasaje);
	}

	private static LocalDate pedirFecha(Scanner scanner, String mensaje) {
		while (true) {
			System.out.print(mensaje);
			try {
				return LocalDate.parse(scanner.nextLine().trim(), FORMATO_FECHA);
			} 
            catch (DateTimeParseException e) {
				System.out.println("Formato invalido, use dd/MM/yyyy.");
			}
		}
	}
	private static boolean pedirOpcion(Scanner scanner, String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String respuesta = scanner.nextLine().trim();
			if (respuesta.equals("1"))
				return true;
			if (respuesta.equals("2"))
				return false;
			System.out.println("Ingrese 1 o 2.");
		}
	}

	private static Vuelo pedirVuelo(Scanner scanner, List<Vuelo> opciones) {
		while (true) {
			System.out.print("Numero de vuelo a confirmar (0 = ninguno): ");
			String respuesta = scanner.nextLine().trim();
			if (respuesta.equals("0"))
				return null;
			for (Vuelo vuelo : opciones)
				if (String.valueOf(vuelo.getFlightCode()).equals(respuesta))
					return vuelo;
			System.out.println("Ese numero no esta en la lista.");
		}
	}

	private static List<Vuelo> cargarVuelos() {
		List<Vuelo> vuelos = new ArrayList<>();

		vuelos.add(new Vuelo(LocalDateTime.of(2026, 11, 10, 8, 30),  "Madrid",   "Buenos Aires", 101, 2, 4, 10, 30));
		vuelos.add(new Vuelo(LocalDateTime.of(2026, 11, 12, 22, 0),  "Madrid",   "Buenos Aires", 102, 0, 3, 5, 20));
		vuelos.add(new Vuelo(LocalDateTime.of(2026, 11, 11, 9, 0),   "Madrid", "Buenos Aires", 201, 1, 2, 8, 25));
		return vuelos;
	}
}