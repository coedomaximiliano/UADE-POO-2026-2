package tp3;

import java.time.LocalDate;

import tp3.Automovil.Color;
import tp3.Automovil.Combustible;
import tp3.Automovil.Tipo;

public class Main {

	public static void main(String[] args) {
	    Automovil auto = new Automovil("Ferrari", LocalDate.of(2022, 5, 12), 2.0f, 
	            Combustible.GASOLINA, Tipo.EJECUTIVO, 4, 5, 180.0f, Color.ROJO, 0f,false);
	    
	    
	    auto.setVelocidadActualKMH(100);
	    System.out.println("Velocidad actual: " + auto.getVelocidadActualKMH() + " km/h");
	    
	    auto.acelerarCantidad(20);
	    System.out.println("Velocidad tras acelerar 20 km/h: " + auto.getVelocidadActualKMH() + " km/h");
	    
	    auto.desacelerarCantidad(50);
	    System.out.println("Velocidad tras desacelerar 50 km/h: " + auto.getVelocidadActualKMH() + " km/h");
	    
	    auto.frenar();
	    System.out.println("El auto ha frenado. Velocidad actual: " + auto.getVelocidadActualKMH() + " km/h");
	    
	    auto.setVelocidadActualKMH(60); 
	    double tiempo = auto.calcularTiempoLlegada(150);
	    System.out.println("Tiempo estimado para recorrer 150 km a " + auto.getVelocidadActualKMH() + " km/h: " + tiempo + " horas");
	}
}
