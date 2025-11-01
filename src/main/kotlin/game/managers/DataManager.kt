package game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import java.io.File

data class GameData(
    var playerName: String = "Player",
    var highScore: Int = 0,
    var totalScore: Int = 0,
    var levelUnlocked: Int = 1,
    var currentLevel: Int = 1,
    var lives: Int = 3,
    var secretLevelUnlocked: Boolean = false,
    var playerStats: MutableMap<String, Int> = mutableMapOf()
)

object DataManager {
    private var gameData = GameData()
    private val fileName = "tankgame_data.txt"
    
    fun initialize() {
        loadData()
    }
    
    fun saveData() {
        try {
            val dataString = buildString {
                append("${gameData.playerName}|")
                append("${gameData.highScore}|")
                append("${gameData.totalScore}|")
                append("${gameData.levelUnlocked}|")
                append("${gameData.currentLevel}|")
                append("${gameData.lives}|")
                append("${gameData.secretLevelUnlocked}|")
                
                // Guardar stats adicionales
                val stats = gameData.playerStats.entries.joinToString(",") { 
                    "${it.key}:${it.value}" 
                }
                append(stats)
            }
            
            val file = File(Gdx.files.getLocalStoragePath() + fileName)
            file.writeText(dataString)
            
            println("✅ Datos guardados: $fileName")
        } catch (e: Exception) {
            println("❌ Error al guardar datos: ${e.message}")
        }
    }
    
    fun loadData() {
        try {
            val file = File(Gdx.files.getLocalStoragePath() + fileName)
            
            if (file.exists()) {
                val data = file.readText().split("|")
                
                if (data.size >= 7) {
                    gameData.playerName = data[0]
                    gameData.highScore = data[1].toIntOrNull() ?: 0
                    gameData.totalScore = data[2].toIntOrNull() ?: 0
                    gameData.levelUnlocked = data[3].toIntOrNull() ?: 1
                    gameData.currentLevel = data[4].toIntOrNull() ?: 1
                    gameData.lives = data[5].toIntOrNull() ?: 3
                    gameData.secretLevelUnlocked = data[6].toBoolean()
                    
                    // Cargar stats adicionales
                    if (data.size > 7 && data[7].isNotEmpty()) {
                        data[7].split(",").forEach { stat ->
                            val parts = stat.split(":")
                            if (parts.size == 2) {
                                gameData.playerStats[parts[0]] = parts[1].toIntOrNull() ?: 0
                            }
                        }
                    }
                }
                
                println("✅ Datos cargados exitosamente")
            } else {
                println("ℹ️ Primer inicio: archivo de datos no encontrado, creando nuevo")
                saveData()
            }
        } catch (e: Exception) {
            println("❌ Error al cargar datos: ${e.message}")
        }
    }
    
    // Getters y Setters
    fun getPlayerName(): String = gameData.playerName
    fun setPlayerName(name: String) {
        gameData.playerName = name
    }
    
    fun getHighScore(): Int = gameData.highScore
    fun setHighScore(score: Int) {
        if (score > gameData.highScore) {
            gameData.highScore = score
            saveData()
        }
    }
    
    fun getTotalScore(): Int = gameData.totalScore
    fun setTotalScore(score: Int) {
        gameData.totalScore = score
        saveData()
    }
    
    fun getLevelUnlocked(): Int = gameData.levelUnlocked
    fun setLevelUnlocked(level: Int) {
        if (level > gameData.levelUnlocked) {
            gameData.levelUnlocked = level
            saveData()
        }
    }
    
    fun getCurrentLevel(): Int = gameData.currentLevel
    fun setCurrentLevel(level: Int) {
        gameData.currentLevel = level
    }
    
    fun getLives(): Int = gameData.lives
    fun setLives(lives: Int) {
        gameData.lives = lives
    }
    
    fun isSecretLevelUnlocked(): Boolean = gameData.secretLevelUnlocked
    fun unlockSecretLevel() {
        gameData.secretLevelUnlocked = true
        saveData()
    }
    
    fun addPlayerStat(key: String, value: Int) {
        val current = gameData.playerStats[key] ?: 0
        gameData.playerStats[key] = current + value
        saveData()
    }
    
    fun getPlayerStat(key: String): Int = gameData.playerStats[key] ?: 0
    
    fun resetGame() {
        gameData = GameData(
            playerName = gameData.playerName,
            highScore = gameData.highScore,
            levelUnlocked = gameData.levelUnlocked,
            secretLevelUnlocked = gameData.secretLevelUnlocked
        )
    }
    
    fun getAllData(): GameData = gameData.copy()
}