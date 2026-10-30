package aplicacion;

import logica.GestorTurnos;
import logica.Notificador;
import logica.NotificadorEmail;
import logica.RegistroMedicos;
import logica.RegistroPacientes;
import logica.RepositorioTurnos;
import logica.TurnoException;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainConsola {
    // Guardar todos los .java en la misma carpeta, sin paquetes.
    // Compilar: javac -encoding UTF-8 *.java
    // Ejecutar el menu: java MainConsola
    // Para ver una prueba ya cargada: java MainConsola demo
    public static void main(String[] args) {
        RegistroPacientes pacientes = new RegistroPacientes();
        RegistroMedicos medicos = new RegistroMedicos();
        RepositorioTurnos repositorio = new RepositorioTurnos();

        // Uso el tipo padre para probar el polimorfismo con NotificadorEmail.
        Notificador notificador = new NotificadorEmail();
        GestorTurnos gestor = new GestorTurnos(pacientes, medicos, repositorio, notificador);

        pacientes.agregar(new Paciente("Ana Perez", "30111222", "ana@example.com", "HC001"));
        pacientes.agregar(new Paciente("Juan Diaz", "32444555", "juan@example.com", "HC002"));
        medicos.agregar(new Medico("Laura Gomez", "25111222", "laura@example.com", "MP100", "Clinica medica"));
        medicos.agregar(new Medico("Pedro Ruiz", "26333444", "pedro@example.com", "MP200", "Cardiologia"));

        System.out.println("SISTEMA DE TURNOS - GRUPO 7");
        System.out.println("Prueba por consola. Los datos se pierden al cerrar.");
        System.out.println("El correo es simulado, no se mandan mensajes reales.\n");

        // Con java MainConsola demo se ejecuta un ejemplo sin tener que escribir datos.
        if (args.length > 0 && args[0].equalsIgnoreCase("demo")) {
            probarEjemplo(gestor);
            return;
        }

        Scanner teclado = new Scanner(System.in);
        String opcion;
        do {
            System.out.println("\n1. Registrar paciente   2. Registrar medico");
            System.out.println("3. Ver pacientes       4. Ver medicos");
            System.out.println("5. Cargar turno        6. Ver todos los turnos");
            System.out.println("7. Cancelar turno      8. Registrar atencion");
            System.out.println("9. Reintentar aviso   10. Buscar por medico");
            System.out.println("11. Buscar por paciente 12. Buscar por fecha");
            System.out.println("0. Salir");
            if (!teclado.hasNextLine()) {
                break;
            }
            opcion = leer(teclado, "Opcion: ");

            try {
                switch (opcion) {
                    case "1":
                        pacientes.agregar(new Paciente(leer(teclado, "Nombre: "),
                                leer(teclado, "DNI: "), leer(teclado, "Email: "),
                                leer(teclado, "Historia clinica: ")));
                        System.out.println("Paciente registrado.");
                        break;
                    case "2":
                        medicos.agregar(new Medico(leer(teclado, "Nombre: "),
                                leer(teclado, "DNI: "), leer(teclado, "Email: "),
                                leer(teclado, "Matricula: "), leer(teclado, "Especialidad: ")));
                        System.out.println("Medico registrado.");
                        break;
                    case "3":
                        for (Paciente paciente : pacientes.listar()) {
                            System.out.println(paciente.mostrarDatos());
                        }
                        break;
                    case "4":
                        for (Medico medico : medicos.listar()) {
                            System.out.println(medico.mostrarDatos());
                        }
                        break;
                    case "5":
                        String dni = leer(teclado, "DNI del paciente (ej. 30111222): ");
                        String matricula = leer(teclado, "Matricula del medico (ej. MP100): ");
                        LocalDate fecha = LocalDate.parse(leer(teclado, "Fecha (aaaa-mm-dd): "));
                        LocalTime hora = LocalTime.parse(leer(teclado, "Hora (hh:mm): "));
                        gestor.cargarTurno(dni, matricula, fecha, hora);
                        break;
                    case "6":
                        mostrarTurnos(gestor.consultarTurnos());
                        break;
                    case "7":
                        gestor.cancelarTurno(Integer.parseInt(leer(teclado, "Numero de turno: ")));
                        System.out.println("Turno cancelado.");
                        break;
                    case "8":
                        gestor.atenderTurno(Integer.parseInt(leer(teclado, "Numero de turno: ")));
                        System.out.println("Atencion registrada.");
                        break;
                    case "9":
                        boolean preparado = gestor.enviarRecordatorio(
                                Integer.parseInt(leer(teclado, "Numero de turno: ")));
                        System.out.println(preparado ? "Aviso simulado correctamente." : "El aviso sigue pendiente.");
                        break;
                    case "10":
                        mostrarTurnos(gestor.consultarPorMedico(leer(teclado, "Matricula: ")));
                        break;
                    case "11":
                        mostrarTurnos(gestor.consultarPorPaciente(leer(teclado, "DNI: ")));
                        break;
                    case "12":
                        mostrarTurnos(gestor.consultarPorFecha(
                                LocalDate.parse(leer(teclado, "Fecha (aaaa-mm-dd): "))));
                        break;
                    case "0":
                        System.out.println("Fin de la prueba.");
                        break;
                    default:
                        System.out.println("Esa opcion no existe.");
                }
            } catch (TurnoException e) {
                System.out.println("No se pudo realizar la operacion: " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Revisar el formato. Ejemplo de fecha: 2027-03-20 y hora: 10:30.");
            } catch (NumberFormatException e) {
                System.out.println("El numero de turno tiene que ser un entero.");
            } catch (IllegalArgumentException e) {
                System.out.println("Revisar los datos: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Entrada finalizada. Fin de la prueba.");
                opcion = "0";
            }
        } while (!opcion.equals("0"));
        teclado.close();
    }

    private static String leer(Scanner teclado, String mensaje) {
        System.out.print(mensaje);
        // Puede pasar si el compilador online no tiene mas datos de entrada.
        if (!teclado.hasNextLine()) {
            throw new IllegalStateException("Se termino la entrada. Volver a ejecutar para continuar.");
        }
        return teclado.nextLine().trim();
    }

    private static void mostrarTurnos(ArrayList<Turno> turnos) {
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para mostrar.");
        }
        for (Turno turno : turnos) {
            System.out.println(turno.mostrarDetalle());
        }
    }

    private static void probarEjemplo(GestorTurnos gestor) {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 0);
        try {
            Turno turno = gestor.cargarTurno("30111222", "MP100", fecha, hora);
            System.out.println(turno.mostrarDetalle());
            try {
                gestor.cargarTurno("32444555", "MP100", fecha, hora);
            } catch (TurnoException e) {
                System.out.println("Prueba de horario ocupado: " + e.getMessage());
            }
            gestor.cancelarTurno(turno.getId());
            System.out.println("Se cancelo el turno " + turno.getId() + ".");
            Turno nuevo = gestor.cargarTurno("32444555", "MP100", fecha, hora);
            System.out.println("Se pudo usar el horario liberado.");
            gestor.atenderTurno(nuevo.getId());
            mostrarTurnos(gestor.consultarTurnos());
        } catch (TurnoException e) {
            System.out.println("Problema en el ejemplo: " + e.getMessage());
        }
    }
}
