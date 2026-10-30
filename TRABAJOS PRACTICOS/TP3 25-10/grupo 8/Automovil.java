package tp3;

import java.time.LocalDate;

public class Automovil {
	private String Fabricante;
	private LocalDate añoDeFabricacion;
	private float volumenCilindrajeLitros;
	private Combustible combustible;
	private Tipo tipo;
	private int numeroDePuertas;
	private int cantidadDeAsientos;
	private float velocidadMaximaKMH;
	private Color color;
	private float velocidadActualKMH;
	private boolean esAutomatico;
	private int cantidadMultas = 0;
	private final float COSTO_POR_MULTA = 5000;
	
	public enum Combustible {
		GASOLINA, BIOETANOL, DIESEl, BIODIESEL, GAS_NATURAL

	}

	public enum Tipo {
		CARRO_DE_CIUDAD, SUBCOMPACTO, COMPACTO, FAMILIAR, EJECUTIVO, SUV
	}

	public enum Color {
		BLANCO, NEGRO, ROJO, NARANJA, AMARILLO, VERDE, AZUL, VIOLETA
	}

	public Automovil(String fabricante, LocalDate añoDeFabricacion, float volumenCilindrajeLitros,
			Combustible combustible, Tipo tipo, int numeroDePuertas, int cantidadDeAsientos, float velocidadMaximaKMH,
			Color color, float velocidadActualKMH,boolean automatico) {
		Fabricante = fabricante;
		this.añoDeFabricacion = añoDeFabricacion;
		this.volumenCilindrajeLitros = volumenCilindrajeLitros;
		this.combustible = combustible;
		this.tipo = tipo;
		this.numeroDePuertas = numeroDePuertas;
		this.cantidadDeAsientos = cantidadDeAsientos;
		this.velocidadMaximaKMH = velocidadMaximaKMH;
		this.color = color;
		this.velocidadActualKMH = velocidadActualKMH;
		this.esAutomatico = automatico;
	}

	public String getFabricante() {
		return Fabricante;
	}

	public void setFabricante(String fabricante) {
		Fabricante = fabricante;
	}

	public LocalDate getAñoDeFabricacion() {
		return añoDeFabricacion;
	}

	public void setAñoDeFabricacion(LocalDate añoDeFabricacion) {
		this.añoDeFabricacion = añoDeFabricacion;
	}

	public float getVolumenCilindrajeLitros() {
		return volumenCilindrajeLitros;
	}

	public void setVolumenCilindrajeLitros(float volumenCilindrajeLitros) {
		this.volumenCilindrajeLitros = volumenCilindrajeLitros;
	}

	public Combustible getCombustible() {
		return combustible;
	}

	public void setCombustible(Combustible combustible) {
		this.combustible = combustible;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public int getNumeroDePuertas() {
		return numeroDePuertas;
	}

	public void setNumeroDePuertas(int numeroDePuertas) {
		this.numeroDePuertas = numeroDePuertas;
	}

	public int getCantidadDeAsientos() {
		return cantidadDeAsientos;
	}

	public void setCantidadDeAsientos(int cantidadDeAsientos) {
		this.cantidadDeAsientos = cantidadDeAsientos;
	}

	public float getVelocidadMaximaKMH() {
		return velocidadMaximaKMH;
	}

	public void setVelocidadMaximaKMH(float velocidadMaximaKMH) {
		this.velocidadMaximaKMH = velocidadMaximaKMH;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getVelocidadActualKMH() {
		return velocidadActualKMH;
	}

	public void setVelocidadActualKMH(float velocidadActualKMH) {
		this.velocidadActualKMH = velocidadActualKMH;
	}

	public boolean isEsAutomatico() {
		return esAutomatico;
	}

	public void setEsAutomatico(boolean esAutomatico) {
		this.esAutomatico = esAutomatico;
	}

	@Override
	public String toString() {
		return "Automovil [Fabricante=" + Fabricante + ", añoDeFabricacion=" + añoDeFabricacion
				+ ", volumenCilindrajeLitros=" + volumenCilindrajeLitros + ", combustible=" + combustible + ", tipo="
				+ tipo + ", numeroDePuertas=" + numeroDePuertas + ", cantidadDeAsientos=" + cantidadDeAsientos
				+ ", velocidadMaximaKMH=" + velocidadMaximaKMH + ", color=" + color + ", velocidadActualKMH="
				+ velocidadActualKMH + "]";
	}

	public void acelerarCantidad(float velocidadAcelerarKMH) throws IllegalArgumentException {
		if (velocidadAcelerarKMH < 0) {
			throw new IllegalArgumentException("La cantidad a acelerar no puede ser negativa");
		}
		if ((velocidadAcelerarKMH + velocidadActualKMH) > velocidadMaximaKMH) {
	        cantidadMultas++;
	        System.out.println("Se ha generado una multa por exceder la maxima velocidad,total multas: " + cantidadMultas + "| Importe a pagar: "+ (cantidadMultas*COSTO_POR_MULTA));
	        setVelocidadActualKMH(velocidadMaximaKMH);
		}
		else
		{
			setVelocidadActualKMH(velocidadActualKMH + velocidadAcelerarKMH);			
		}
	}

	public void acelerarObjetivo(float velocidadObjetivoKMH) throws IllegalArgumentException {
		if (velocidadObjetivoKMH > velocidadMaximaKMH)
			throw new IllegalArgumentException(
					"La velocidad objetivo a acelerar no puede ser mayor a la velocidad maxima del vehiculo");
		if (velocidadObjetivoKMH < 0)
			throw new IllegalArgumentException("La velocidad objetivo no puede ser menor a 0");
		if (velocidadActualKMH > velocidadObjetivoKMH)
			throw new IllegalArgumentException("La velocidad objetivo no puede ser menor a la velocidad actual");
		setVelocidadActualKMH(velocidadObjetivoKMH);
	}

	public void desacelerarCantidad(float velocidadDesacelerarKMH) throws IllegalArgumentException
	{
		if(velocidadDesacelerarKMH < 0)
		{
			throw new IllegalArgumentException("La cantidad a desacelerar no puede ser negativa");
		}
		if(velocidadActualKMH < velocidadDesacelerarKMH)
			throw new IllegalArgumentException("La cantidad a desacelerar supera la velocidad actual y dejaria el resultado en negativo");
		setVelocidadActualKMH(velocidadActualKMH - velocidadDesacelerarKMH);
	}

	public void desacelerarObjetivo(float velocidadObjetivoKMH) throws IllegalArgumentException {
		if (velocidadObjetivoKMH < 0)
			throw new IllegalArgumentException("La velocidad objetivo no puede ser menor a 0");
		if (velocidadActualKMH < velocidadObjetivoKMH)
			throw new IllegalArgumentException("La velocidad objetivo no puede ser mayor a la velocidad actual");
		setVelocidadActualKMH(velocidadObjetivoKMH);
	}
	public void frenar()
	{
		setVelocidadActualKMH(0);
	}
	
	public void mostrarAuto()
	{
		System.out.println(this);
	}
	
	public double calcularTiempoLlegada(float distanciaKM) {
	    if (velocidadActualKMH == 0) 
	    	throw new ArithmeticException("No se puede calcular el tiempo de llegada porque el automovil esta detenido (velocidad en 0 km/h)");
	    return distanciaKM / velocidadActualKMH;
	}
	
	public boolean tieneMultas() {
	    return cantidadMultas > 0;
	}

	public float getValorTotalMultas() {
	    return cantidadMultas*COSTO_POR_MULTA;
	}

}
