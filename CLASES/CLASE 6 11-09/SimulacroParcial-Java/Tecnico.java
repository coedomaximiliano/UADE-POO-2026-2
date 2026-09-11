/**
 * Tecnico de un equipo de futbol. Extiende Persona.
 *
 * Nota: el diagrama UML entregado no incluye "anosExperiencia" entre los
 * parametros del constructor de Tecnico, pero el enunciado pide inicializar
 * ese dato (ej: "40 anios de experiencia"), asi que se agrego al
 * constructor para poder cargarlo. Se puede quitar facilmente si se
 * prefiere respetar el diagrama al pie de la letra y setearlo aparte.
 */
public class Tecnico extends Persona {

    protected int anosExperiencia;
    protected boolean esNacional;

    public Tecnico(String nombre, String apellido, int edad, int anosExperiencia, boolean esNacional) {
        super(nombre, apellido, edad);
        this.anosExperiencia = anosExperiencia;
        this.esNacional = esNacional;
    }

    @Override
    public String toString() {
        String nacionalidad = esNacional ? "nacional" : "extranjero";
        return String.format("Tecnico: %s %s (%d anios) - %d anios de experiencia - %s",
                nombre, apellido, edad, anosExperiencia, nacionalidad);
    }
}
