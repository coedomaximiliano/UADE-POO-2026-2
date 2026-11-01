# Diagramas de clases · Entrega 2

## Archivos

| Archivo | Uso |
|---------|-----|
| `diagrama_clases_entrega2.puml` | **Entrega actual** — alineado al código en `boleteria/` y a la interfaz Swing + MVC. |
| `diagrama_clases_v4.puml` | Versión anterior centrada solo en el dominio (sin capa vista/controlador explícita). |
| `diagrama_clases_v3.puml` | Histórico previo a v4. |

Imagen exportada: `diagrama_clases_entrega2.png` (desde el `.puml`).

Regenerar (requiere Java; sin Graphviz usar layout Smetana):

```bash
java -jar plantuml.jar -tpng -Playout=smetana diagramas/clases/diagrama_clases_entrega2.puml
mv diagramas/clases/TPO_diagrama_clases_entrega2.png diagramas/clases/diagrama_clases_entrega2.png
```

## Cambios respecto al diagrama original grupo 3 (imagen TPO)

- **Eliminado:** subsistema de **estacionamiento** (`Estacionamiento`, reservas de cochera, `Vehículo`, etc.).
- **Eliminado / fusionado en el código:** `Recital`, `Artista`, `Cuenta`, clase `Pago`, `Cargo`, `SectorFactory`, `FacturaArchivo`, `AutenticacionException`, precios dinámicos y cargos adicionales del diseño inicial.
- **Agregado:** reserva de compra (**5 minutos**), estados (`EstadoCompra`, `EstadoEntrada`, `EstadoFuncion`), **Devolución** (48 h), **Factura** con persistencia en archivo, **Promoción** por código, **Paquete VIP**, **Preventa** con código, reglas `NoDejarAsientoSuelto` y `RequiereCodigoPreventa`, persistencia `Repositorio` + repos de sedes/clientes.
- **Agregado (Entrega 2):** capa **MVC** — `SedeController`, `VentaController`, vista **Swing** (`VentanaPrincipal`, paneles, `FacturaDialog`).
- **Modelo:** `Boleteria` como **Singleton / fachada** del dominio (no es el controlador MVC); los controladores delegan en ella.
- **Sectores:** jerarquía `Sector` → `SectorNumerado` / `SectorGeneral`, creación con `Sector.crear` y enum `TipoSector`.

## Clases incluidas en `diagrama_clases_entrega2.puml`

**Vista:** `BoleteriaApp`, `VentanaPrincipal`, `Pantalla`, paneles ABM/consulta/venta/devolución, `FormularioSector`, `FacturaDialog`.

**Controlador:** `SedeController`, `VentaController`.

**Modelo:** `Boleteria`; infraestructura (`Sede`, `Sector`, `SectorNumerado`, `SectorGeneral`, `Asiento`, `TipoSector`); eventos (`Evento`, `Funcion`, `Preventa`, `EstadoFuncion`); ventas (`Cliente`, `Compra`, `Entrada`, `Factura`, `Devolucion`, `PaqueteVIP`, enums de estado y pago); `Promocion`; reglas y excepciones; persistencia.

## Clases del código no dibujadas (a propósito)

- `DatosIniciales`, `DemoFlujoCompra` — bootstrap / demo por consola.
- `Estilo`, `Aviso` — utilidades visuales menores.

## Discrepancias código ↔ diagrama

1. **`Boleteria` en v4:** figuraba como `<<controller>>`; en el código y en **entrega2** es **modelo (Singleton/fachada)**; el MVC lo hacen `SedeController` y `VentaController`.
2. **`Promocion`:** en v4 estaba dentro del paquete lógico “Ventas”; en Java está en `modelo.promociones` — entrega2 usa paquete **Promociones**.
3. **Excepciones:** en v4 compartían paquete con reglas; en Java están en `modelo.excepcion` — entrega2 las separa.
4. **Métodos omitidos en el diagrama** (legibilidad): getters, `calcularSubtotalEntradas` / `calcularSubtotalPaquetes`, `getSegundosRestantes`, `cancelarCompra`, búsquedas auxiliares de `Boleteria`, etc. — el comportamiento está en el código fuente.
5. **`PanelInicio` → `Boleteria`:** registro e inicio de sesión se delegan a la fachada del modelo (sin pasar por `VentaController`).
6. **Persistencia:** **sedes/sectores**, **clientes** y **eventos/funciones** (`EventoRepositorioArchivo` → `eventos.txt`, `funciones.txt`); compras/entradas y promociones siguen en memoria en runtime — el diagrama no lista cada `.txt`.
