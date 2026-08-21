// ===================== Clase abstracta Animal =====================
abstract class Animal {
    // Atributo original
    private String nombre;
 
    // 2 atributos mas (punto 3)
    private int edad;
    private double peso;
 
    public Animal(String nombre, int edad, double peso) {
        this.nombre = nombre;
        this.edad = edad;
        this.peso = peso;
    }
 
    // Método abstracto original (punto 2)
    public abstract void hacerSonido();
 
    // 2 métodos mas (punto 4)
    public void dormir() {
        System.out.println(nombre + " está durmiendo. Zzz...");
    }
 
    public void comer() {
        System.out.println(nombre + " está comiendo.");
    }
 
    // Getters y setters
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public int getEdad() {
        return edad;
    }
 
    public void setEdad(int edad) {
        this.edad = edad;
    }
 
    public double getPeso() {
        return peso;
    }
 
    public void setPeso(double peso) {
        this.peso = peso;
    }
}
 
// ===================== Clase Perro =====================
class Perro extends Animal {
    // Atributo especifico de Perro (punto 5)
    private String raza;
 
    public Perro(String nombre, int edad, double peso, String raza) {
        super(nombre, edad, peso);
        this.raza = raza;
    }
 
    @Override
    public void hacerSonido() {
        System.out.println(getNombre() + " dice: Guau!");
    }
 
    // Método especifico de Perro (punto 6)
    public void buscarPelota() {
        System.out.println(getNombre() + " corre a buscar la pelota.");
    }
 
    public String getRaza() {
        return raza;
    }
 
    public void setRaza(String raza) {
        this.raza = raza;
    }
}
 
// ===================== Clase Gato =====================
class Gato extends Animal {
    // Atributo especifico de Gato (punto 5)
    private int vidas;
 
    public Gato(String nombre, int edad, double peso, int vidas) {
        super(nombre, edad, peso);
        this.vidas = vidas;
    }
 
    @Override
    public void hacerSonido() {
        System.out.println(getNombre() + " dice: Miau!");
    }
 
    // Método especifico de Gato (punto 6)
    public void trepar() {
        System.out.println(getNombre() + " trepa a lo mas alto del mueble.");
    }
 
    public int getVidas() {
        return vidas;
    }
 
    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
}
 
// ===================== Clase Main =====================
public class Animales {
    public static void main(String[] args) {
        Perro perro = new Perro("Rex", 3, 12.5, "Labrador");
        Gato gato = new Gato("Michi", 2, 4.2, 9);
 
        // Polimorfismo: tratamos ambos como Animal
        Animal[] animales = { perro, gato };
 
        for (Animal a : animales) {
            a.hacerSonido();
            a.comer();
            a.dormir();
        }
 
        // Uso de métodos específicos de cada subclase
        System.out.println();
        System.out.println(perro.getNombre() + " es de raza " + perro.getRaza());
        perro.buscarPelota();
 
        System.out.println();
        System.out.println(gato.getNombre() + " tiene " + gato.getVidas() + " vidas");
        gato.trepar();
    }
}