# boleteria/ — Proyecto Java

Sistema de venta de entradas para sedes de espectáculos (inspirado en la Esfera de Las Vegas):
el precio depende del piso y de la distancia al escenario, los asientos se reservan por 5 minutos,
hay promociones por código, factura y devoluciones.

## Cómo ejecutarlo

Requiere **Java 17 o superior**.

- **Doble click** en `Ejecutar Boleteria.command` (Mac): abre la aplicación Swing.
- Desde la terminal, en esta carpeta (`boleteria/`): `java -jar Boleteria-TPO.jar`
- Con Maven (Swing): `mvn compile exec:exec` — usa JVM separada (`exec:java` puede colgar con Java reciente).

Si `mvn` no está en el PATH, usá la ruta completa (por ejemplo `~/.local/opt/apache-maven-3.9.9/bin/mvn`) o compilá y ejecutá:

```bash
mkdir -p target/classes
find src/main/java -name '*.java' > /tmp/sources.txt
javac -d target/classes -sourcepath src/main/java @/tmp/sources.txt
java -cp target/classes ar.edu.uade.poo.grupo3.tpo.vista.BoleteriaApp
```

### Probar una compra (Swing)

1. **Ventas → Venta de entradas** (Ctrl+V).
2. Elegí función, sector, cliente (por ejemplo *FLR · Floor*) y marcá asientos en el mapa.
3. **Reservar**: los asientos quedan en verde y arranca la cuenta regresiva de 5 minutos.
4. Opcional: código promocional `FAN10` o `BANCO20` → **Aplicar**.
5. Elegí el medio de pago y **Pagar**: se muestra el comprobante y se guarda en `datos/facturas/`.
6. Para ver qué pasa si no pagás a tiempo: botón *Demo: adelantar el reloj 5 minutos*.
7. Para devolver una entrada: **Ventas → Devoluciones** (Ctrl+D), el código del comprobante (por ejemplo `E0001-01`) → **Verificar entrada** → **Efectuar devolución**.

Clientes de ejemplo: Ana García, Bruno Díaz y Carla Méndez (`datos/clientes.txt`).
Promociones de ejemplo: `BANCO20` (20 %) y `FAN10` (10 %).

## Qué incluye esta entrega

| Pedido de la consigna | Dónde está |
|---|---|
| Código de las clases del diagrama (con los ajustes de la Entrega 1) | `src/main/java/ar/edu/uade/poo/grupo3/tpo` |
| 4 pantallas Swing de ABM | Sedes: **Consulta**, **Alta**, **Modificación** y **Baja** (`vista/Panel*Sede*.java` + `controlador/SedeController.java`) |
| Adelanto: pantalla de **Venta de entradas** | `vista/PanelVenta.java` + `controlador/VentaController.java` + `vista/FacturaDialog.java` (mapa de asientos, reserva de 5 min, promo, pago y factura) |
| Persistencia en archivos de texto | carpeta `datos/` (ver abajo) |
| Excepción personalizada del negocio | `modelo/excepcion/`: `SeleccionInvalidaException`, `ReservaVencidaException`, `DevolucionNoPermitidaException` |
| Al menos 3 try/catch | ver tabla de try/catch |
| Al menos 3 patrones implementados | ver tabla de patrones |

## Ajustes respecto de la Entrega 1

- Se quitaron `Estacionamiento`, `ExperienciaInmersiva`, `PrecioDinamico`/`PoliticaPrecio`, `Adicional` y `TipoCargo`
  para centrar el proyecto en la venta de entradas.
- Se agregaron: reserva de **5 minutos** (`Compra.MINUTOS_RESERVA`), `MedioPago`, `Devolucion`
  (hasta 48 hs antes), `Factura` y `Promocion` por código.
- En la entrega 2 se quitaron `Recital`, `Artista`, `Cuenta`, `Pago`, `Cargo`, `SectorFactory`, `FacturaArchivo`
  y `AutenticacionException`: su lógica pasó a `Evento`, `Cliente`, `Compra`, `Sector.crear` y `Factura.guardar`
  (25 clases en el modelo en vez de 33, sin perder funcionalidad).
- En la entrega final, para simplificar, se quitaron `PaqueteVIP`, `Preventa`, las reglas de venta (`ReglaDeVenta`,
  `NoDejarAsientoSuelto`, `RequiereCodigoPreventa`), el inicio de sesión de clientes (`PanelLoginCliente`,
  `PanelRegistroCliente`), `EventoRepositorioArchivo` y la demo por consola. En la venta el cliente se elige de una lista.

## Patrones y buenas prácticas implementados

| Patrón | Dónde |
|---|---|
| **Singleton** | `modelo.Boleteria.getInstancia()` — una sola boletería en todo el sistema |
| **Controlador (GRASP)** | `Boleteria` recibe todas las operaciones del sistema: es la fachada del modelo (no confundir con los controladores de MVC) |
| **Factory** | `modelo.infraestructura.Sector.crear(...)` (método de fábrica estático) crea los sectores (lo usan las pantallas y el repositorio) |
| **Repository** | `modelo.persistencia.Repositorio<T>` con `SedeRepositorioArchivo` y `ClienteRepositorioArchivo` |
| **MVC** | **Vista** `vista.Panel*` (arma la pantalla y muestra mensajes) → **Controlador** `controlador.SedeController` / `controlador.VentaController` (recibe lo que hace el usuario, lleva la compra en curso) → **Modelo** `modelo.Boleteria` y el dominio. |
| **Creador / Experto (GRASP)** | `Compra` crea sus `Entrada`; `Funcion` sabe qué asientos están libres; `Sector` calcula su multiplicador |
| **Herencia y polimorfismo** | `Sector` → `SectorNumerado` / `SectorGeneral` (`getCapacidad`, `calcularMultiplicador`) |

## Manejo de excepciones (try/catch)

1. `vista.PanelAltaSede.guardar()` — datos inválidos (`IllegalArgumentException`) y error al escribir el archivo (`IOException`).
2. `vista.PanelModificacionSede` — sede con funciones programadas (`IllegalStateException`) y `IOException` al guardar.
3. `vista.PanelBajaSede.darDeBaja()` — no se puede dar de baja una sede con funciones (`IllegalStateException`) y `IOException`.
4. `vista.BoleteriaApp` — error al leer los archivos de `datos/` al iniciar.
5. `modelo.Boleteria.confirmarCompra()` — si falla guardar la factura, la venta no se pierde.
6. `vista.PanelVenta` — `SeleccionInvalidaException` al reservar y `ReservaVencidaException` al pagar.
7. `vista.PanelDevolucion` — `DevolucionNoPermitidaException` al devolver una entrada.

## Archivos de datos (`datos/`)

| Archivo | Formato |
|---|---|
| `sedes.txt` | `nombre|direccion` |
| `sectores.txt` | `sede|NUM|codigo|nombre|piso|distancia|filasXasientos` o `sede|GEN|codigo|nombre|piso|distancia|capacidad` |
| `clientes.txt` | `dni|nombre|email` |
| `facturas/FAC-*.txt` | un comprobante por compra confirmada |

Si la carpeta `datos/` no existe, se crea con datos de ejemplo. Los eventos y funciones no se guardan en archivos:
se crean en memoria cada vez que arranca la app (`DatosIniciales`), con funciones a 1, 15 y 30 días de ese día.

## Estructura del código

Los paquetes siguen las tres capas de MVC:

```
ar.edu.uade.poo.grupo3.tpo
├── vista/               Pantallas Swing: BoleteriaApp, VentanaPrincipal, Panel* (ABM de sedes), PanelVenta, PanelDevolucion,
│                        FacturaDialog, FormularioSector, Estilo, Aviso. Solo arman la pantalla y muestran mensajes.
├── controlador/         SedeController (ABM de sedes) y VentaController (compra en curso y reloj de la reserva).
│                        Reciben lo que hace el usuario y se lo piden al modelo.
├── modelo/              Todo el dominio y su persistencia
│   ├── Boleteria        fachada del modelo (Singleton + Controlador GRASP)
│   ├── infraestructura/ Sede, Sector (con la fábrica Sector.crear), SectorNumerado, SectorGeneral, Asiento
│   ├── eventos/         Evento, Funcion
│   ├── ventas/          Cliente, Compra, Entrada, Devolucion, Factura
│   ├── promociones/     Promocion
│   ├── excepcion/       excepciones del negocio
│   ├── enums/           EstadoFuncion, EstadoCompra, EstadoEntrada, MedioPago, TipoSector
│   └── persistencia/    Repositorio<T>, SedeRepositorioArchivo, ClienteRepositorioArchivo
└── DatosIniciales       configuración y datos de ejemplo
```
