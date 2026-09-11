/**
 * Representa un equipo de futbol: nombre, pais, tecnico, arquero y las
 * lineas de defensores, mediocampistas y delanteros.
 *
 * Nota sobre cantidades: el enunciado en texto menciona "cuatro defensas,
 * cuatro mediocampos y dos delanteros", pero tanto el diagrama de clases
 * (multiplicidades 4-defensa, 3-mediocampo, 3-delantero) como los datos de
 * ejemplo (4 defensores, 3 mediocampistas, 3 delanteros) coinciden entre
 * si y no con el texto. Se implemento siguiendo el diagrama + los datos de
 * ejemplo, que es lo que realmente se puede verificar.
 */
public class EquipoFutbol {

    private String nombre;
    private String pais;
    private Tecnico tecnico;
    private Arquero arquero;
    private Defensor[] defensa;
    private Mediocampo[] mediocampo;
    private Delantero[] delantero;

    public EquipoFutbol(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    public EquipoFutbol(String nombre, String pais, Tecnico tecnico, Arquero arquero,
                         Defensor[] defensa, Mediocampo[] mediocampo, Delantero[] delantero) {
        this(nombre, pais);
        this.tecnico = tecnico;
        this.arquero = arquero;
        this.defensa = defensa;
        this.mediocampo = mediocampo;
        this.delantero = delantero;
    }

    /**
     * Devuelve un String formateado con la conformacion completa del
     * equipo: nombre, pais, tecnico y todos los jugadores.
     */
    public String imprimir() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("Equipo: ").append(nombre).append(" (").append(pais).append(")\n");
        sb.append("=================================================\n");

        if (tecnico != null) {
            sb.append(tecnico.toString()).append("\n");
        }

        sb.append("-- Arquero --\n");
        if (arquero != null) {
            sb.append(arquero.toString()).append("\n");
        }

        sb.append("-- Defensa --\n");
        if (defensa != null) {
            for (int i = 0; i < defensa.length; i++) {
                sb.append(defensa[i].toString()).append("\n");
            }
        }

        sb.append("-- Mediocampo --\n");
        if (mediocampo != null) {
            for (int i = 0; i < mediocampo.length; i++) {
                sb.append(mediocampo[i].toString()).append("\n");
            }
        }

        sb.append("-- Delanteros --\n");
        if (delantero != null) {
            for (int i = 0; i < delantero.length; i++) {
                sb.append(delantero[i].toString()).append("\n");
            }
        }

        return sb.toString();
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }
}
