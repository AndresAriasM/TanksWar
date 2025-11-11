# Tank Wars

Tank Wars es un juego 2D de tanques desarrollado en Kotlin usando LibGDX. Este repositorio contiene el código fuente, niveles y recursos del juego.

## Resumen

Objetivo: Derrotar oleadas de enemigos, coleccionar power-ups y completar niveles.

Tecnologías principales:
- Kotlin
- LibGDX (v1.11.0)
- Gradle (wrapper incluido)

## Contenido de la documentación
- Este `README.md`: visión general, cómo ejecutar y comandos principales.
- `docs/ARCHITECTURE.md`: descripción de la arquitectura y paquetes.
- `CONTRIBUTING.md`: guía rápida para desarrolladores y cómo añadir contenido.

## Requisitos

- JDK 11 o superior (se recomienda Java 17)
- Gradle (se usa el wrapper incluido)

## Ejecutar en Windows (PowerShell)

Desde la raíz del proyecto, usa el Gradle wrapper incluido:

```powershell
.\gradlew.bat build
.\gradlew.bat run
```

Si prefieres ejecutar con Gradle instalado globalmente:

```powershell
# (opcional)
gradle build; gradle run
```

Nota: el entry point está en `src/main/kotlin/game/Main.kt` y la configuración de la ventana se hace en ese archivo.

## Controles

- Movimiento: Flechas (ARRIBA/ABAJO/IZQUIERDA/DERECHA) o WASD
- Disparar: Barra espaciadora (SPACE)
- Pausa: ESC o P
- Navegar menús: ARRIBA/ABAJO o W/S
- Seleccionar: ENTER o SPACE

## Estructura del proyecto (resumen)

- `src/main/kotlin/game/`
  - `entities/`: `PlayerTank`, `EnemyTank`, `Bullet`, `PowerUp`, etc.
  - `managers/`: `DataManager`, `EnemyManager`, `LevelManager`, `MapManager`, `SoundManager`.
  - `screens/`: `SplashScreen`, `HomeScreen`, `GameScreen`, `LevelSelectScreen`, `ScoreScreen`, `CreditsScreen`.
  - `maps/`: definiciones de niveles (ej. `Level1.kt` ... `Level5.kt`, `SecretLevel.kt`).
  - `utils/`: constantes, enums y sistemas utilitarios (`CollisionSystem`, `Constants`, `Enums`).

- `resources/assets/`: música, sonidos y demás recursos.

## Cómo contribuir (resumen)

Consulta `CONTRIBUTING.md` para pasos de configuración, estilo de código y cómo añadir enemigos, niveles o assets.

## Diagrama y diseño

- Diagrama manual: https://www.canva.com/design/DAG4aon0wj0/zYI2VANI9ohVAAfu-G9jAQ/edit
- Diagrama digital (Figma): https://www.figma.com/design/wb62apMlQeEXFpaoT6HXhw/Tank-Wars?node-id=0-1

## Estado

- Versión en código: 1.0.0 (impreso desde `GameApplication` durante la inicialización)

---
Si quieres que añada ejemplos de ejecución automatizada, capturas de pantalla o un manual de niveles, dime qué formato prefieres (README corto, página de GitHub Pages, PDF, etc.) y lo implemento.
