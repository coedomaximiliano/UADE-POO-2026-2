package eventos;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Cliente cliente = new Cliente("27888999", "Sofia Lopez");
        Organizador organizador = new OrganizadorInterno("Martin Diaz", "011-5555-1234", "LEG-09");

        Salon salon = new Salon("Salon Jacaranda", 300);
        Sector principal = salon.agregarSector("Sala principal", 220);
        Sector patio = salon.agregarSector("Patio", 80);

        CentroDeEventos centro = new CentroDeEventos("Centro Norte", "Av. Libertador 4500");
        centro.agregarSalon(salon);

        Evento evento = new Evento("Casamiento Lopez-Fernandez", LocalDate.of(2026, 12, 5),
                850000.0, 200000.0, cliente, organizador, salon);

        System.out.println("Evento: " + evento.getNombre());
        System.out.println("Cliente: " + evento.getCliente().getNombre());
        System.out.println("Organizador: " + evento.getOrganizador().getNombre());
        System.out.println("Salon: " + evento.getSalon().getNombre() + " (" + salon.getSectores().size() + " sectores)");
        System.out.println("Centro: " + centro.getSalones().size() + " salon(es) asociados");
        System.out.println("Fecha limite de sena: " + evento.fechaLimiteSenia());
        System.out.println("Sectores: " + principal.getNombre() + " (" + principal.getCapacidad() + "), " + patio.getNombre() + " (" + patio.getCapacidad() + ")");
    }
}
