package game.utils

enum class Direction(val angle: Float) {
    UP(90f),
    DOWN(270f),
    LEFT(180f),
    RIGHT(0f),
    UP_LEFT(135f),
    UP_RIGHT(45f),
    DOWN_LEFT(225f),
    DOWN_RIGHT(315f),
    NONE(0f)
}

enum class EnemyType {
    NORMAL,      // Velocidad normal, IA básica
    FAST,        // Rápido pero frágil
    TANK,        // Lento pero fuerte
    SMART        // IA mejorada
}

enum class PowerUpType {
    RAPID_FIRE,  // Dispara más rápido
    SHIELD,      // Escudo temporal
    SPEED,       // Movimiento más rápido
    HEALTH,      // Recupera vidas
    ARMOR        // Resistencia aumentada
}

enum class GameState {
    PLAYING,
    PAUSED,
    LEVEL_COMPLETE,
    GAME_OVER,
    VICTORY
}

enum class ScreenType {
    SPLASH,
    HOME,
    GAME,
    SCORE,
    CREDITS
}