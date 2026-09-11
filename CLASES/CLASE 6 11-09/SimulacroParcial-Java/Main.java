/**
 * Clase de prueba: arma la Seleccion "de ensueno" Argentina con los datos
 * del enunciado y muestra por consola la conformacion del equipo.
 */
public class Main {

    public static void main(String[] args) {

        Tecnico tecnico = new Tecnico("Cesar Luis", "Menotti", 76, 40, true); // no es extranjero -> esNacional = true

        Arquero arquero = new Arquero("Amadeo", "Carrizo", 30, true, 10);

        Defensor[] defensas = new Defensor[4];
        defensas[0] = new Defensor("Roberto", "Perfumo", 24, true);
        defensas[1] = new Defensor("Daniel", "Passarella", 23, true);
        defensas[2] = new Defensor("Javier", "Zanetti", 29, true);
        defensas[3] = new Defensor("Oscar", "Ruggeri", 29, true);

        Mediocampo[] mediocampistas = new Mediocampo[3];
        mediocampistas[0] = new Mediocampo("Diego", "Maradona", 28, true, 1200);
        mediocampistas[1] = new Mediocampo("Fernando", "Redondo", 25, true, 12);
        mediocampistas[2] = new Mediocampo("Juan Roman", "Riquelme", 31, true, 1);

        Delantero[] delanteros = new Delantero[3];
        delanteros[0] = new Delantero("Lionel", "Messi", 36, true, 150);
        delanteros[1] = new Delantero("Gabriel", "Batistuta", 28, true, 300);
        delanteros[2] = new Delantero("Alfredo", "Di Stefano", 30, true, 400);

        EquipoFutbol seleccionArgentina = new EquipoFutbol(
                "Seleccion de ensueno",
                "Argentina",
                tecnico,
                arquero,
                defensas,
                mediocampistas,
                delanteros
        );

        System.out.println(seleccionArgentina.imprimir());
    }
}
