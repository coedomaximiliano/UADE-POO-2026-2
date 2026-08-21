# Cómo contribuir

Cada grupo sube su código mediante fork + pull request. Nadie tiene permiso de escritura directa sobre este repositorio.

## Pasos

1. **Fork**: un integrante del grupo hace fork de este repositorio a su cuenta de GitHub (botón "Fork" arriba a la derecha), y agrega al resto del grupo como colaboradores de su fork (`Settings > Collaborators`).

2. **Cloná el fork del grupo**:
   ```
   git clone https://github.com/TU-USUARIO/UADE-POO-2026-2.git
   cd UADE-POO-2026-2
   ```

3. **Ubicá la carpeta de tu grupo**, según qué estés entregando:
   - **Trabajo práctico**: ya existe una carpeta para tu grupo dentro de `TRABAJOS PRACTICOS/TPN/`, ej: `TRABAJOS PRACTICOS/TP1/grupo 3/`. No la crees de nuevo, usá la que ya está.
   - **Ejercicio de clase**: creá la carpeta de tu grupo dentro de `EJERCICIOS CLASE/ejercicio clase N/`, ej: `EJERCICIOS CLASE/ejercicio clase 1/grupo 3/`.

   Reemplazá `N` por el número de TP o de clase, y `grupo 3` por el número de tu grupo.

4. **Creá una rama** para la entrega del grupo:
   ```
   git checkout -b tpN-grupoN
   ```
   o, si es un ejercicio de clase:
   ```
   git checkout -b claseN-grupoN
   ```

5. **Commiteá y pusheá** a tu fork (las rutas con espacios van entre comillas):
   ```
   git add "TRABAJOS PRACTICOS/TPN/grupo N"
   git commit -m "TPN: GrupoN"
   git push origin tpN-grupoN
   ```
   o, para un ejercicio de clase:
   ```
   git add "EJERCICIOS CLASE/ejercicio clase N/grupo N"
   git commit -m "Clase N: GrupoN"
   git push origin claseN-grupoN
   ```

6. **Abrí un Pull Request** desde el fork del grupo hacia `coedomaximiliano/UADE-POO-2026-2` (rama `main`).

## Reglas

- Subí código **únicamente** dentro de la carpeta de tu propio grupo. No modifiques archivos de otros grupos ni de `CLASES/`.
- **Un solo PR por grupo y por entrega**: coordínense entre los integrantes para no pisarse. Correcciones posteriores van como nuevos commits en la misma rama, no un PR nuevo.
- El título del PR debe indicar la entrega y el grupo, ej: `TP1: Grupo3` o `Clase 1: Grupo3`.
