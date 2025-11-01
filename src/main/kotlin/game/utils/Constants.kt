package game.utils

object Constants {
    // Dimensiones del juego
    const val SCREEN_WIDTH = 800f
    const val SCREEN_HEIGHT = 600f
    
    // Dimensiones de las entidades
    const val TANK_SIZE = 32f
    const val BULLET_SIZE = 8f
    const val POWERUP_SIZE = 24f
    const val OBSTACLE_SIZE = 32f
    
    // Velocidades
    const val TANK_SPEED = 200f
    const val TANK_ROTATION_SPEED = 180f
    const val BULLET_SPEED = 400f
    const val ENEMY_SPEED = 150f
    
    // Física
    const val TANK_HEALTH = 80
    const val BULLET_DAMAGE = 40
    const val ENEMY_DAMAGE = 15
    
    // UI Colors (RGB normalized 0-1)
    const val COLOR_PRIMARY_R = 0.2f
    const val COLOR_PRIMARY_G = 0.8f
    const val COLOR_PRIMARY_B = 0.2f
    
    const val COLOR_SECONDARY_R = 0.8f
    const val COLOR_SECONDARY_G = 0.2f
    const val COLOR_SECONDARY_B = 0.2f
    
    const val COLOR_BACKGROUND_R = 0.1f
    const val COLOR_BACKGROUND_G = 0.1f
    const val COLOR_BACKGROUND_B = 0.1f
    
    // Niveles
    const val TOTAL_LEVELS = 5
    const val SECRET_LEVEL = 6
    
    // Game states
    const val MAX_LIVES = 3
    const val INITIAL_SCORE = 0
}