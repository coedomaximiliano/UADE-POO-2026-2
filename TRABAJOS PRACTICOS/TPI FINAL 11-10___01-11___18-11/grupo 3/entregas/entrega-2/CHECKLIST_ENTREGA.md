# Checklist entrega 2 — consigna → código

**Proyecto:** [`boleteria/`](../../boleteria/) · **Demo consola:** `DemoFlujoCompra` · **App Swing:** `BoleteriaApp`

Ejecutar la demo (desde `boleteria/`):

```bash
mvn compile exec:exec@demo-flujo-compra
# alternativa:
mvn compile && java -cp target/classes ar.edu.uade.poo.grupo3.tpo.DemoFlujoCompra
```

Swing: `mvn compile exec:exec` · No usar `exec:java` (puede colgar con Java reciente).

---

## Devolución docente (entrega 1) — flujo de compra real

| Pedido | Dónde está | Cómo probarlo |
|---|---|---|
| Centrar en venta de entradas (sin estacionamiento, experiencia inmersiva, precio dinámico) | Solo documentación histórica; **no hay clases** `Estacionamiento`, `Adicional`, etc. Ver [`boleteria/README.md`](../../boleteria/README.md) y [§13 DEFENSA](../../DEFENSA.md) | `rg -i estacionamiento boleteria/src` → 0 resultados |
| Registro / cuenta cliente | `Cliente`, `ClienteRepositorioArchivo`, `Boleteria.registrarCuenta` / `iniciarSesion`, UI `PanelRegistroCliente`, `PanelLoginCliente` | Demo §1; menú **Clientes → Registro** / **Iniciar sesión** |
| Compra: función, sector, asientos, reserva 5 min | `Compra`, `Funcion`, `Entrada`, `Boleteria.iniciarCompra` / `reservarAsientos` / `confirmarCompra`, `VentaController`, `PanelVenta` | Demo §2–5; pantalla **Venta de entradas** |
| Regla no dejar asiento suelto | `NoDejarAsientoSuelto`, registrada en `DatosIniciales` | Demo §3 (`SeleccionInvalidaException`) |
| Preventa con código de acceso | `Preventa`, `RequiereCodigoPreventa`, campo en `PanelVenta` | Demo §6; función #2 con código `ORBITA26` |
| Promoción | `Promocion`, `Boleteria.aplicarCodigoPromocional`, UI código promo en `PanelVenta` | Demo §4 (`FAN10`) y §6 (`BANCO20`) |
| Paquete VIP | `PaqueteVIP`, `Compra.agregarPaquete`, checkbox en `PanelVenta` | Demo §4 (Experiencia Gold) |
| Entrada + factura | `Entrada`, `Factura`, `Factura.guardar`, `FacturaDialog` | Demo §4 → `datos/facturas/FAC-*.txt`; botón **Pagar** abre diálogo |
| Devolución (48 hs, sin reintegrar cargos) | `Devolucion`, `DevolucionNoPermitidaException`, `PanelDevolucion`, `VentaController.devolverEntrada` | Demo §7; menú **Ventas → Devoluciones** |
| Reserva vencida | `ReservaVencidaException`, `Compra.estaVencida`, `liberarReservasVencidas` | Demo §5; en UI botón **Demo: adelantar el reloj 5 minutos** |
| Fachada única del dominio | `Boleteria` (Singleton + GRASP Controlador) | Toda la app y la demo hablan solo con `Boleteria.getInstancia()` |

---

## Requisitos entrega parcial 2 (cátedra)

| Pedido de la consigna | Dónde está |
|---|---|
| Clases del diagrama (ajustes entrega 1) | `boleteria/src/main/java/ar/edu/uade/poo/grupo3/tpo/` · diagrama [`diagrama_clases_entrega2.png`](../../diagramas/clases/diagrama_clases_entrega2.png) · [`diagrama_clases_entrega2.puml`](../../diagramas/clases/diagrama_clases_entrega2.puml) |
| ≥3 patrones / buenas prácticas | Singleton (`Boleteria`), Strategy (`ReglaDeVenta`), Factory (`Sector.crear`), Repository (`Repositorio`, `*RepositorioArchivo`), MVC (`vista` / `controlador` / `modelo`), GRASP |
| Excepción personalizada de negocio | `SeleccionInvalidaException`, `ReservaVencidaException`, `DevolucionNoPermitidaException` en `modelo/excepcion/` |
| ≥3 try/catch | `PanelVenta`, `PanelDevolucion`, `PanelRegistroCliente`, pantallas ABM sedes, `BoleteriaApp`, `Factura.guardar`, `DemoFlujoCompra` |
| 4 pantallas Swing ABM sedes | `PanelConsultaSedes`, `PanelAltaSede`, `PanelModificacionSede`, `PanelBajaSede` |
| Primer avance de persistencia | `boleteria/datos/sedes.txt`, `sectores.txt`, `clientes.txt`, `eventos.txt`, `funciones.txt`, `factura_seq.txt`, `facturas/` · `SedeRepositorioArchivo`, `ClienteRepositorioArchivo`, `EventoRepositorioArchivo` |
| Pantalla venta (adelanto final) | `PanelVenta` + `VentaController` |
| Pantalla devoluciones | `PanelDevolucion` |
| ≥3 diálogos | Confirmación baja sede, error de arranque (`BoleteriaApp`), comprobante (`FacturaDialog`) |
| Menú y navegación | `VentanaPrincipal` (`JMenuBar` + `CardLayout`) |

---

## Integración UI venta (`PanelVenta`)

| Acción usuario | Controlador | Modelo |
|---|---|---|
| Elegir función / sector / código preventa | — (solo vista) | — |
| Cliente logueado (sesión) | — | `Boleteria.getClienteSesion()` vía `VentaController` |
| Marcar asientos / cantidad general | — | disponibilidad vía `Funcion` |
| **Reservar** | `VentaController.reservar` | `iniciarCompra`, `reservarAsientos`, `agregarPaquete` opcional |
| **Aplicar** promo | `aplicarPromocion` | `aplicarCodigoPromocional` |
| **Pagar** | `pagar` | `confirmarCompra` → `Factura` |
| Reloj 5 min | `Timer` + `segundosRestantes` / `verificarVencimiento` | `Compra.MINUTOS_RESERVA` |
| **Cancelar** | `cancelar` | `cancelarCompra` |

La vista no llama a `Boleteria` directamente; solo a `VentaController`.

---

## Gaps conocidos (no bloquean la demo)

- [x] **Eventos y funciones** en `eventos.txt` / `funciones.txt` (`EventoRepositorioArchivo`; seed en `DatosIniciales` si vacío).
- [x] **Secuencia de facturas** persistida en `datos/factura_seq.txt` (si falta el archivo, se infiere desde `datos/facturas/`).
- [ ] **Compras / entradas en memoria:** las ventas confirmadas no se recargan al reiniciar la app (las facturas `.txt` sí quedan en disco). Previsto para la entrega final.
- [x] **Login en UI:** `PanelLoginCliente` (Clientes → Iniciar sesión); `PanelVenta` exige sesión activa para reservar.
- [ ] **`PanelInicio`** lee métricas directo de `Boleteria` (solo lectura; aceptable en parcial 2).
- [ ] Ejecutar siempre desde **`boleteria/`** (ruta relativa `datos/`).

Documentación extendida (~10 páginas): [`entregas/entrega-final/`](../../entregas/entrega-final/) · guía de defensa completa: [`DEFENSA.md`](../../DEFENSA.md).

---

## Escenarios cubiertos por `DemoFlujoCompra` (última corrida OK — 01/11/2026 18:16 ART, exit 0)

1. Registro, email duplicado, login fallido / exitoso  
2. Búsqueda de funciones y disponibilidad  
3. Asiento suelto rechazado  
4. Compra con 4 asientos + VIP + `FAN10` + factura en disco  
5. Reserva vencida a los 6 minutos  
6. Preventa sin código / con `ORBITA26` + `BANCO20`  
7. Devolución permitida y rechazada (&lt; 48 hs a la función)

**Salida esperada:** la consola debe cerrar con la línea `Fin de la demo.` (BUILD SUCCESS).
