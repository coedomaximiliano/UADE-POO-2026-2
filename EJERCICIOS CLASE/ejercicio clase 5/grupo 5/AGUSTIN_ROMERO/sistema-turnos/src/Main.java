public class Main {
     public static void main(String[] args) {
         Medico medicoAgus = new Medico(
                 "Agus",
                 "Romero",
                 "15-09-2002", "44384207",
                 "traumatología",
                 "10495860"
         );

         Paciente pacienteOscar = new Paciente(
                 "Oscar",
                 "Ramirez",
                 "20-12-1990", "29384207",
                 146
         );

         Turno nuevoTurno = new Turno(
                 "15-09-2026",
                 "10:00",
                 Estado.PENDIENTE
         );

         medicoAgus.AgregarTurno(nuevoTurno);
         pacienteOscar.AgregarTurno(nuevoTurno);

         System.out.println("Fecha del turno: " + nuevoTurno.getFecha());
         System.out.println("Hora del turno: " + nuevoTurno.getHora());

         if(nuevoTurno.getEstado() == Estado.PENDIENTE) {
             System.out.println("Estado del turno: pendiente");
         } else {
             System.out.println("Estado del turno: cancelado o atendido");
         }

         System.out.println("Medico " + medicoAgus.getNombre());
         System.out.println("Paciente " + pacienteOscar.getNombre());
     }
}
