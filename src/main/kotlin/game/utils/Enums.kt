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
    NORMAL,
    FAST,
    TANK,
    SMART
}

enum class PowerUpType {
    RAPID_FIRE,
    SHIELD,
    SPEED,
    HEALTH,
    ARMOR,
    BLAST,
    ALLY_SPAWN
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
    LEVEL_SELECT,
    GAME,
    SCORE,
    CREDITS
}