# Entrega parcial 2 — Estado final (01/11/2026, verificación 18:16 ART)

## Veredicto

**Parcial 2 listo para entregar:** el código cumple la consigna de la cátedra (diagrama, patrones, excepciones, try/catch, ABM sedes, persistencia inicial, venta, devoluciones, menú Swing y demo consola). Quedan mejoras opcionales documentadas para la **entrega final del 18/11/2026** (persistencia de compras/entradas y pulido de métricas en inicio).

---

## Consigna → cumplimiento

| Requisito parcial 2 | Estado | Evidencia |
|---|---|---|
| Clases del diagrama (sin estacionamiento / adicionales) | OK | `boleteria/src/.../tpo/` · [`diagrama_clases_entrega2.png`](../../diagramas/clases/diagrama_clases_entrega2.png) |
| ≥ 3 patrones / buenas prácticas | OK | Singleton, Strategy, Factory, Repository, MVC, GRASP |
| Excepción de negocio | OK | `SeleccionInvalidaException`, `ReservaVencidaException`, `DevolucionNoPermitidaException` |
| ≥ 3 try/catch | OK | Pantallas ABM, venta, devolución, registro, login, app, facturas, demo |
| 4 pantallas ABM sedes | OK | Consulta, Alta, Modificación, Baja |
| Persistencia inicial | OK | `sedes`, `sectores`, `clientes`, `eventos`, `funciones`, `factura_seq.txt`, comprobantes en `facturas/` |
| Pantalla venta (adelanto final) | OK | `PanelVenta` + `VentaController` |
| Pantalla devoluciones | OK | `PanelDevolucion` |
| ≥ 3 diálogos | OK | Baja sede, error arranque, `FacturaDialog` |
| Menú y navegación | OK | `VentanaPrincipal` |
| Registro + login cliente (flujo docente) | OK | `PanelRegistroCliente`, `PanelLoginCliente`, sesión en `Boleteria` |

Detalle archivo por archivo: [`CHECKLIST_ENTREGA.md`](CHECKLIST_ENTREGA.md).

---

## Qué subir al campus

1. **Código:** carpeta [`boleteria/`](../../boleteria/) (fuentes Java + `pom.xml` + `datos/` de ejemplo: sedes, sectores, clientes, eventos, funciones; no hace falta subir `datos/facturas/` generadas en pruebas).
2. **Diagrama:** [`diagramas/clases/diagrama_clases_entrega2.png`](../../diagramas/clases/diagrama_clases_entrega2.png) (y `.puml` si lo piden editable).
3. **Documentación del parcial:** este archivo + [`README.md`](README.md) + [`CHECKLIST_ENTREGA.md`](CHECKLIST_ENTREGA.md).
4. **Documentación extendida (~10 pp.):** [`entregas/entrega-final/TPO POO - Grupo 3 - Documentacion.pdf`](../../entregas/entrega-final/TPO%20POO%20-%20Grupo%203%20-%20Documentacion.pdf) (ya alineada al código).

---

## Cómo ejecutar (desde `boleteria/`)

Maven (ruta local del grupo):

```bash
cd boleteria
~/.local/opt/apache-maven-3.9.9/bin/mvn compile exec:exec@demo-flujo-compra
# App Swing:
~/.local/opt/apache-maven-3.9.9/bin/mvn compile exec:exec
```

Demo consola alternativa:

```bash
cd boleteria
mvn compile && java -cp target/classes ar.edu.uade.poo.grupo3.tpo.DemoFlujoCompra
```

**Swing — flujo sugerido para corrección:** Clientes → Registro o Iniciar sesión → Ventas → Venta de entradas (reservar / pagar) → Devoluciones si aplica.

Contraseña de clientes de ejemplo (seed): ver `DatosIniciales.CONTRASENIA_DEMO` (`boleteria123`).

---

## Pendiente para entrega final (18/11/2026)

- Persistir **compras y entradas** (repositorio o archivos) para recuperar devoluciones y stock tras reiniciar.
- Revisar **ocupación de asientos** persistida entre sesiones (hoy vive en memoria mientras corre la JVM).
- Regenerar PNG del diagrama si se cambia el `.puml` (`plantuml` o export manual).
- Capturas actualizadas con pantalla de login (opcional).

---

## Verificación automática (01/11/2026 18:16 ART)

| Prueba | Resultado |
|---|---|
| `mvn -B clean compile` (desde `boleteria/`) | OK · 51 fuentes |
| `mvn -B exec:exec@demo-flujo-compra` | OK · exit 0 · termina en «Fin de la demo.» |
| Persistencia `datos/*.txt` tras demo | OK · `sedes`, `sectores`, `clientes`, `eventos`, `funciones`, `factura_seq.txt` legibles |
| Comprobante en disco | OK · p. ej. `datos/facturas/FAC-0001-00000010.txt` (demo §4) |
| Diagrama vs código | OK · `diagrama_clases_entrega2.puml` incluye login/registro UI; PNG regenerado |

---

## Cambios cerrados en esta revisión

- `PanelLoginCliente` y menú **Clientes → Iniciar sesión**.
- Sesión de cliente en `Boleteria`; `PanelVenta` usa el cliente logueado y bloquea reservar sin sesión.
- Secuencia de facturas en `datos/factura_seq.txt` (fallback desde archivos `FAC-*.txt`).
- Diagrama `.puml`: `EventoRepositorioArchivo` agregado; `PanelRegistroCliente` y `PanelLoginCliente` (PNG regenerado 01/11/2026 18:16).
