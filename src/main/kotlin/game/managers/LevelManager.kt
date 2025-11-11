package game.managers

import game.utils.PowerUpType
import kotlin.random.Random

data class LevelConfig(
    val levelNumber: Int,
    val name: String,
    val enemyCount: Int,
    val mapLayout: Array<IntArray>,
    val powerUpLocations: List<Pair<Float, Float>>,
    val powerUpTypes: List<PowerUpType>,
    val timeLimit: Float? = null
)

object LevelManager {
    private var currentLevel = 1
    var levelConfigs = mutableMapOf<Int, LevelConfig>()
    
    init {
        initializeLevels()
    }
    
    private fun initializeLevels() {
        // Level 1: Introducción - 5 enemigos
        levelConfigs[1] = LevelConfig(
            levelNumber = 1,
            name = "Inicio Desafiante",
            enemyCount = 5,
            mapLayout = generateMapLevel1(),
            powerUpLocations = listOf(
                Pair(400f, 300f),
                Pair(200f, 150f)
            ),
            powerUpTypes = listOf(PowerUpType.HEALTH, PowerUpType.SHIELD),
            timeLimit = 120f
        )
        
        // Level 2: Intermedio - 10 enemigos (+5)
        levelConfigs[2] = LevelConfig(
            levelNumber = 2,
            name = "Desafío Progresivo",
            enemyCount = 10,
            mapLayout = generateMapLevel2(),
            powerUpLocations = listOf(
                Pair(400f, 450f),
                Pair(150f, 300f),
                Pair(650f, 150f)
            ),
            powerUpTypes = listOf(PowerUpType.SPEED, PowerUpType.RAPID_FIRE, PowerUpType.ARMOR),
            timeLimit = 150f
        )
        
        // Level 3: Desafío moderado - 12 enemigos (+5)
        levelConfigs[3] = LevelConfig(
            levelNumber = 3,
            name = "Moderadamente Épico",
            enemyCount = 12,
            mapLayout = generateMapLevel3(),
            powerUpLocations = listOf(
                Pair(400f, 500f),
                Pair(100f, 100f),
                Pair(700f, 500f),
                Pair(700f, 100f)
            ),
            powerUpTypes = listOf(
                PowerUpType.RAPID_FIRE,
                PowerUpType.SHIELD,
                PowerUpType.SPEED,
                PowerUpType.HEALTH
            ),
            timeLimit = 180f
        )
        
        // Level 4: Difícil - 15 enemigos (+5)
        levelConfigs[4] = LevelConfig(
            levelNumber = 4,
            name = "Muy Difícil - Caos",
            enemyCount = 15,
            mapLayout = generateMapLevel4(),
            powerUpLocations = listOf(
                Pair(400f, 300f),
                Pair(200f, 500f),
                Pair(600f, 100f)
            ),
            powerUpTypes = listOf(PowerUpType.ARMOR, PowerUpType.SHIELD, PowerUpType.RAPID_FIRE),
            timeLimit = 200f
        )
        
        // Level 5: Épico - 20 enemigos (+5)
        levelConfigs[5] = LevelConfig(
            levelNumber = 5,
            name = "Épico Total - Infierno",
            enemyCount = 20,
            mapLayout = generateMapLevel5(),
            powerUpLocations = listOf(
                Pair(400f, 300f),
                Pair(100f, 100f),
                Pair(700f, 500f),
                Pair(100f, 500f),
                Pair(700f, 100f)
            ),
            powerUpTypes = listOf(
                PowerUpType.RAPID_FIRE,
                PowerUpType.SHIELD,
                PowerUpType.ARMOR,
                PowerUpType.SPEED,
                PowerUpType.HEALTH
            ),
            timeLimit = 240f
        )
        
        // Level Secreto - 25 enemigos (+5)
        levelConfigs[6] = LevelConfig(
            levelNumber = 6,
            name = "Nivel Secreto: Apocalipsis",
            enemyCount = 25,
            mapLayout = generateSecretLevel(),
            powerUpLocations = listOf(
                Pair(400f, 300f),
                Pair(200f, 200f),
                Pair(600f, 400f),
                Pair(300f, 500f),
                Pair(500f, 100f)
            ),
            powerUpTypes = listOf(
                PowerUpType.RAPID_FIRE,
                PowerUpType.SHIELD,
                PowerUpType.ARMOR,
                PowerUpType.SPEED,
                PowerUpType.HEALTH
            ),
            timeLimit = 300f
        )
        
        println("📋 LevelManager inicializado con 6 niveles")
        println("🎮 Dificultad extrema activada:")
        println("  - Nivel 1: 5 enemigos")
        println("  - Nivel 2: 10 enemigos")
        println("  - Nivel 3: 12 enemigos")
        println("  - Nivel 4: 15 enemigos")
        println("  - Nivel 5: 20 enemigos")
        println("  - Secreto: 25 enemigos")
    }
    
    fun getCurrentLevelConfig(): LevelConfig? = levelConfigs[currentLevel]
    
    fun setCurrentLevel(level: Int) {
        currentLevel = level.coerceIn(1, 6)
        println("📍 Nivel actual: $currentLevel")
    }
    
    fun nextLevel() {
        if (currentLevel < 5) {
            currentLevel++
            println("⬆️ Siguiente nivel: $currentLevel")
        }
    }
    
    fun getTotalLevels(): Int = 5
    fun hasSecretLevel(): Boolean = true
    
    // Generadores de mapas
    private fun generateMapLevel1(): Array<IntArray> {
        return arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 0, 2, 0, 2, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
    }
    
    private fun generateMapLevel2(): Array<IntArray> {
        return arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 1, 0, 1, 1, 0, 1, 1, 0),
            intArrayOf(0, 1, 2, 0, 1, 2, 0, 1, 2, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 2, 0, 2, 0, 2, 0, 2, 0, 0),
            intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 1, 0, 1, 1, 0, 1, 1, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
    }
    
    private fun generateMapLevel3(): Array<IntArray> {
        return arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 0, 2, 0, 2, 0, 1, 0, 0),
            intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 2, 0, 2, 0, 0, 0, 0),
            intArrayOf(0, 2, 0, 1, 0, 1, 0, 2, 0, 0),
            intArrayOf(0, 1, 0, 2, 0, 2, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 1, 0, 1, 1, 0, 1, 1, 0),
            intArrayOf(0, 2, 2, 0, 2, 2, 0, 2, 2, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
    }
    
    private fun generateMapLevel4(): Array<IntArray> {
        return arrayOf(
            intArrayOf(1, 1, 0, 1, 0, 1, 0, 1, 1, 1),
            intArrayOf(1, 2, 0, 2, 0, 2, 0, 2, 2, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 0, 2, 0, 2, 0, 1, 1, 1),
            intArrayOf(1, 2, 0, 1, 0, 1, 0, 2, 2, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 0, 2, 0, 2, 0, 1, 1, 1),
            intArrayOf(1, 2, 0, 1, 0, 1, 0, 2, 2, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        )
    }
    
    private fun generateMapLevel5(): Array<IntArray> {
        return arrayOf(
            intArrayOf(1, 1, 1, 1, 0, 1, 1, 1, 1, 1),
            intArrayOf(1, 2, 2, 1, 0, 1, 2, 2, 2, 1),
            intArrayOf(1, 2, 2, 1, 0, 1, 2, 2, 2, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 2, 0, 1, 1, 1, 0, 2, 2, 1),
            intArrayOf(1, 2, 0, 1, 2, 1, 0, 2, 2, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 0, 1, 2, 1, 0, 1, 1, 1),
            intArrayOf(1, 2, 0, 2, 2, 2, 0, 2, 2, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        )
    }
    
    private fun generateSecretLevel(): Array<IntArray> {
        return arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
            intArrayOf(2, 0, 0, 2, 0, 0, 2, 0, 0, 2),
            intArrayOf(2, 0, 2, 0, 0, 2, 0, 2, 0, 2),
            intArrayOf(2, 2, 0, 0, 2, 0, 0, 0, 2, 2),
            intArrayOf(0, 0, 0, 2, 0, 2, 0, 0, 0, 0),
            intArrayOf(2, 2, 0, 0, 2, 0, 0, 0, 2, 2),
            intArrayOf(2, 0, 2, 0, 0, 2, 0, 2, 0, 2),
            intArrayOf(2, 0, 0, 2, 0, 0, 2, 0, 0, 2),
            intArrayOf(2, 2, 2, 0, 2, 2, 0, 2, 2, 2),
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        )
    }
}