public class Automovil {

    private static final double VALOR_MULTA = 50000;

    private String marca;
    private int modelo;
    private double motor;
    private TipoCombustible tipoCombustible;
    private TipoAutomovil tipoAutomovil;
    private int numeroPuertas;
    private int cantidadAsientos;
    private int velocidadMaxima;
    private Color color;
    private int velocidadActual;
    private boolean automatico;
    private int cantidadMultas;
    private double valorTotalMultas;

    public Automovil(String marca, int modelo, double motor,
                     TipoCombustible tipoCombustible, TipoAutomovil tipoAutomovil,
                     int numeroPuertas, int cantidadAsientos, int velocidadMaxima,
                     Color color, int velocidadActual, boolean automatico) {
        this.marca = marca;
        this.modelo = modelo;
        this.motor = motor;
        this.tipoCombustible = tipoCombustible;
        this.tipoAutomovil = tipoAutomovil;
        this.numeroPuertas = numeroPuertas;
        this.cantidadAsientos = cantidadAsientos;
        this.velocidadMaxima = velocidadMaxima;
        this.color = color;
        this.velocidadActual = velocidadActual;
        this.automatico = automatico;
        this.cantidadMultas = 0;
        this.valorTotalMultas = 0;
    }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public int getModelo() { return modelo; }
    public void setModelo(int modelo) { this.modelo = modelo; }

    public double getMotor() { return motor; }
    public void setMotor(double motor) { this.motor = motor; }

    public TipoCombustible getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(TipoCombustible tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public TipoAutomovil getTipoAutomovil() { return tipoAutomovil; }
    public void setTipoAutomovil(TipoAutomovil tipoAutomovil) { this.tipoAutomovil = tipoAutomovil; }

    public int getNumeroPuertas() { return numeroPuertas; }
    public void setNumeroPuertas(int numeroPuertas) { this.numeroPuertas = numeroPuertas; }

    public int getCantidadAsientos() { return cantidadAsientos; }
    public void setCantidadAsientos(int cantidadAsientos) { this.cantidadAsientos = cantidadAsientos; }

    public int getVelocidadMaxima() { return velocidadMaxima; }
    public void setVelocidadMaxima(int velocidadMaxima) { this.velocidadMaxima = velocidadMaxima; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public int getVelocidadActual() { return velocidadActual; }
    public void setVelocidadActual(int velocidadActual) { this.velocidadActual = velocidadActual; }

    public boolean isAutomatico() { return automatico; }
    public void setAutomatico(boolean automatico) { this.automatico = automatico; }

    public int getCantidadMultas() { return cantidadMultas; }

    public void acelerar(int incremento) {
        if (velocidadActual + incremento > velocidadMaxima) {
            System.out.println("No se puede acelerar a más de " + velocidadMaxima + " km/h.");
            cantidadMultas++;
            valorTotalMultas += VALOR_MULTA;
            System.out.println("Multa generada. Total de multas: " + cantidadMultas);
        } else {
            velocidadActual += incremento;
        }
    }

    public void desacelerar(int decremento) {
        if (velocidadActual - decremento < 0) {
            System.out.println("No se puede desacelerar a una velocidad negativa.");
        } else {
            velocidadActual -= decremento;
        }
    }

    public void frenar() {
        velocidadActual = 0;
    }

    public double calcularTiempoLlegada(double distancia) {
        if (velocidadActual == 0) {
            System.out.println("El vehículo está detenido, no se puede calcular el tiempo de llegada.");
            return -1;
        }
        return distancia / velocidadActual;
    }

    public void imprimirAtributos() {
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Motor: " + motor + " L");
        System.out.println("Tipo de combustible: " + tipoCombustible);
        System.out.println("Tipo de automóvil: " + tipoAutomovil);
        System.out.println("Número de puertas: " + numeroPuertas);
        System.out.println("Cantidad de asientos: " + cantidadAsientos);
        System.out.println("Velocidad máxima: " + velocidadMaxima + " km/h");
        System.out.println("Color: " + color);
        System.out.println("Velocidad actual: " + velocidadActual + " km/h");
        if (automatico) {
            System.out.println("Automático: Sí");
        } else {
            System.out.println("Automático: No");
        }
        System.out.println("Cantidad de multas: " + cantidadMultas);
        System.out.println("Valor total de multas: $" + valorTotalMultas);
    }

    public boolean tieneMultas() {
        return cantidadMultas > 0;
    }

    public double getValorTotalMultas() {
        return valorTotalMultas;
    }

    public static void main(String[] args) {
        Automovil auto = new Automovil("Ford", 2018, 3.0,
                TipoCombustible.DIESEL, TipoAutomovil.EJECUTIVO,
                5, 6, 250, Color.NEGRO, 0, true);

        auto.imprimirAtributos();
        System.out.println("-----------------------------");

        auto.setVelocidadActual(100);
        System.out.println("Velocidad actual: " + auto.getVelocidadActual() + " km/h");

        auto.acelerar(20);
        System.out.println("Velocidad actual: " + auto.getVelocidadActual() + " km/h");

        auto.desacelerar(50);
        System.out.println("Velocidad actual: " + auto.getVelocidadActual() + " km/h");

        System.out.println("Tiempo estimado para 140 km: " + auto.calcularTiempoLlegada(140) + " horas");

        auto.frenar();
        System.out.println("Velocidad actual: " + auto.getVelocidadActual() + " km/h");

        System.out.println("-----------------------------");
        auto.acelerar(300);
        auto.acelerar(260);
        if (auto.tieneMultas()) {
            System.out.println("¿Tiene multas? Sí");
        } else {
            System.out.println("¿Tiene multas? No");
        }
        System.out.println("Valor total de multas: $" + auto.getValorTotalMultas());
    }
}