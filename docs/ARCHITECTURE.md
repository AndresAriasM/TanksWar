## Arquitectura - Tank Wars

Este documento describe la organización del código y el flujo principal del juego.

Estructura principal (paquetes):

- `game` - paquete raíz.
  - `entities/` - Clases que representan objetos del juego:
    - `PlayerTank`, `AllyTank`, `EnemyTank`, `Bullet`, `Obstacle`, `PowerUp`, `Airplane`, etc.
    - Responsabilidad: lógica de actualización, render y colisiones de cada objeto.

  - `screens/` - Pantallas del juego (extienden `BaseScreen`):
    - `SplashScreen`, `HomeScreen`, `LevelSelectScreen`, `GameScreen`, `ScoreScreen`, `CreditsScreen`.
    - `GameScreen` contiene la mayor parte de la lógica de juego: ciclo de actualización, spawn de enemigos/powerups, detección de colisiones y HUD.

  - `managers/` - Gestores y servicios globales:
    - `DataManager`: persistencia de puntuaciones, niveles desbloqueados y estadísticas del jugador.
    - `SoundManager`: carga y reproducción de música y efectos.
    - `LevelManager`: configuración de niveles (cantidad de enemigos, tiempo límite, etc.).
    - `MapManager`: carga y generación de obstáculos en el mapa.
    - `EnemyManager`: creación y comportamiento de enemigos.

  - `maps/` - Definiciones de niveles (ej. `Level1.kt`, ...). Aquí se especifica la configuración por nivel.

  - `utils/` - Utilidades y constantes:
    - `Constants.kt`: parámetros de juego (tamaños, velocidades, colores, límites de niveles).
    - `CollisionSystem.kt`: funciones para detección de colisiones (AABB, círculo, etc.).
    - `Enums.kt`: enumeraciones utilizadas en el juego (direcciones, tipos de power-ups, estados).

Flujo de arranque

1. `Main.kt` crea la configuración y lanza `GameApplication`.
2. `GameApplication.create()` inicializa gestores, la cámara y las pantallas, y muestra la `SplashScreen`.
3. El loop principal (`render`) limpia la pantalla, actualiza la pantalla activa y delega la lógica de transición entre pantallas.

Flujo en partida (`GameScreen`)

- `handleInput()` procesa entradas (pausa, disparo, navegación del menú de pausa).
- `update()` gestiona spawn de aviones, power-ups, actualización de entidades y comprobaciones de colisiones (balas-tanque, balas-obstáculo, player-obstáculo, etc.).
- Colisiones y efectos: cuando una bala impacta se aplica daño; si un enemigo muere se incrementa la puntuación y se reproduce un efecto.

Principios y responsabilidades

- Cada `Entity` encapsula su lógica de movimiento, render y estado.
- Los `Managers` actúan como servicios: centralizan estado compartido (niveles, sonido, datos persistentes).
- `GameScreen` orquesta la lógica de alto nivel: tiempo de nivel, condiciones de victoria/derrota, spawn y HUD.

Extensiones recomendadas

- Separar más lógica en sistemas (ej. Sistema de disparo, Sistema de IA) si el juego crece.
- Añadir interfaces para facilitar tests unitarios (por ejemplo, `ISoundManager`).

Si quieres, puedo añadir un diagrama de llamadas (sequence diagram) o un diagrama de paquetes en formato Markdown/PlantUML.
