# Contribuir a Tank Wars

Gracias por querer contribuir. Aquí tienes una guía rápida para poner el proyecto en marcha y contribuir de forma ordenada.

## Preparación del entorno

1. Instala JDK 11+ (recomendado Java 17).
2. Clona el repositorio y abre la raíz del proyecto.
3. Usa el Gradle wrapper para compilar y ejecutar:

```powershell
    gradle build
    gradle run
```

## Estilo y buenas prácticas

- Usa Kotlin idiomático y respeta las convenciones del proyecto.
- Mantén métodos pequeños; si una clase crece mucho, considera extraer responsabilidades.
- Agrega comentarios en funciones públicas si su propósito no es evidente.

## Añadir niveles

1. Crea un archivo en `src/main/kotlin/game/maps/` siguiendo el patrón de `Level1.kt`.
2. Define `enemyCount`, `timeLimit` y cualquier otra configuración pertinente en `LevelManager` si es necesario.
3. Asegúrate de que el nivel esté registrado o descubierto por `LevelManager`.

## Añadir assets (sprites, sonido)

1. Coloca nuevos assets en `resources/assets/` dentro de la subcarpeta correcta (`music/`, `sounds/`, `images/`, etc.).
2. Actualiza `SoundManager` o la gestión de texturas para cargar los nuevos recursos.

## Pruebas rápidas

- Prueba manual: compila y ejecuta; verifica que el nivel/asset se carga y no hay excepciones en la consola.

## Pull requests

- Forkea el repo y crea una rama con un nombre descriptivo (`feature/agregar-nivel-x`, `fix/bug-disparo`).
- Haz commits pequeños y atómicos. Incluye descripciones claras en los mensajes.
- Abre PR hacia `main` con una descripción del cambio y capturas si aplican.

## Contacto

Si necesitas ayuda con la arquitectura o priorización de tareas, abre un issue indicando lo que quieres implementar.
