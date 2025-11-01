package game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import java.io.File

object SoundManager {
    private val sounds = mutableMapOf<String, Sound?>()
    private var soundEnabled = true
    
    // Tipos de sonidos
    enum class SoundType {
        SHOOT,
        EXPLOSION,
        POWERUP,
        LEVEL_COMPLETE,
        GAME_OVER,
        UI_CLICK,
        DAMAGE
    }
    
    fun initialize() {
        println("🔊 Inicializando gestor de sonidos...")
        // Los sonidos serían cargados desde archivos, por ahora simulamos
        soundEnabled = true
    }
    
    fun playSound(soundType: SoundType) {
        if (!soundEnabled) return
        
        when (soundType) {
            SoundType.SHOOT -> {
                println("🔊 Sonido: Disparo")
                // sound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"))
                // sound.play()
            }
            SoundType.EXPLOSION -> {
                println("💥 Sonido: Explosión")
            }
            SoundType.POWERUP -> {
                println("⚡ Sonido: Power-up")
            }
            SoundType.LEVEL_COMPLETE -> {
                println("🎉 Sonido: Nivel completado")
            }
            SoundType.GAME_OVER -> {
                println("☠️ Sonido: Game Over")
            }
            SoundType.UI_CLICK -> {
                println("🖱️ Sonido: Click UI")
            }
            SoundType.DAMAGE -> {
                println("⚠️ Sonido: Daño")
            }
        }
    }
    
    fun toggleSound() {
        soundEnabled = !soundEnabled
        println(if (soundEnabled) "🔊 Sonido ON" else "🔇 Sonido OFF")
    }
    
    fun isSoundEnabled(): Boolean = soundEnabled
    
    fun dispose() {
        sounds.values.forEach { it?.dispose() }
        sounds.clear()
        println("🔊 Sonidos liberados")
    }
}