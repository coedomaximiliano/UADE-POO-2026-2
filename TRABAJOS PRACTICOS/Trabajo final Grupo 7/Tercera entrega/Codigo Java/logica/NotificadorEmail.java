package logica;

import modelo.Turno;

public class NotificadorEmail extends Notificador {
    private String ultimoMensaje = "Todavia no se preparo ningun recordatorio.";

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    @Override
    public boolean enviar(Turno turno) {
        if (turno == null) {
            ultimoMensaje = "No se pudo preparar el recordatorio: falta el turno.";
            return false;
        }
        String email = turno.getPaciente().getEmail();
        int arroba = email.indexOf('@');
        if (arroba <= 0 || arroba == email.length() - 1 || email.contains(" ")) {
            ultimoMensaje = "Recordatorio pendiente. Revisar el correo del paciente.";
            System.out.println("No se pudo preparar el recordatorio: revisar el correo del paciente.");
            return false;
        }

        // Esto es una simulacion, todavia no hay conexion a un servidor de correo.
        ultimoMensaje = "SIMULACION - no se envia un correo real.\n\nPara: " + email
                + "\nAsunto: Recordatorio de turno\n\nHola " + turno.getPaciente().getNombre()
                + ".\nSu turno con " + turno.getMedico().getNombre()
                + " (" + turno.getMedico().getEspecialidad() + ")"
                + "\nes el " + turno.getFecha() + " a las " + turno.getHora() + ".";
        System.out.println("\n--- SIMULACION DE CORREO (no se envia realmente) ---");
        System.out.println("Para: " + email);
        System.out.println("Asunto: Recordatorio de turno");
        System.out.println("Hola " + turno.getPaciente().getNombre() + ".");
        System.out.println("Su turno con " + turno.getMedico().getNombre()
                + " (" + turno.getMedico().getEspecialidad() + ")");
        System.out.println("es el " + turno.getFecha() + " a las " + turno.getHora() + ".");
        System.out.println("-------------------------------------------------\n");
        return true;
    }
}
