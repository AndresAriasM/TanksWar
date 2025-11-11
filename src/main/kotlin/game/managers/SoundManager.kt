package game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music

/**
 * Gestor de sonido optimizado para archivos WAV
 * 
 * ✅ CORREGIDO:
 * - Eliminado método resume() (no existe en LibGDX Music)
 * - Agregado loop automático (Music.isLooping = true)
 * - Música se repite indefinidamente cuando termina
 * 
 * Estructura de archivos esperada:
 * src/resources/assets/
 *   ├── music/
 *   │   ├── menu_music.wav
 *   │   └── game_music.wav
 *   └── sounds/
 *       ├── shoot.wav
 *       ├── explosion.wav
 *       ├── powerup.wav
 *       ├── damage.wav
 *       ├── level_complete.wav
 *       ├── game_over.wav
 *       └── ui_click.wav
 */
object SoundManager {
    
    // ========== MÚSICA ==========
    private var currentMusic: Music? = null
    private var menuMusic: Music? = null
    private var gameMusic: Music? = null
    
    // ========== EFECTOS DE SONIDO ==========
    private var shootSound: Sound? = null
    private var explosionSound: Sound? = null
    private var powerupSound: Sound? = null
    private var levelCompleteSound: Sound? = null
    private var gameOverSound: Sound? = null
    private var damageSound: Sound? = null
    private var uiClickSound: Sound? = null
    
    // ========== CONFIGURACIÓN ==========
    private var soundEnabled = true
    private var musicEnabled = true
    private var soundVolume = 0.7f
    private var musicVolume = 0.5f
    
    enum class SoundType {
        SHOOT,
        EXPLOSION,
        POWERUP,
        LEVEL_COMPLETE,
        GAME_OVER,
        UI_CLICK,
        DAMAGE
    }
    
    // ========== INICIALIZACIÓN ==========
    
    fun initialize() {
        println("🔊 Inicializando SoundManager para WAV...")
        
        try {
            // Cargar música (WAV)
            cargarMusica()
            
            // Cargar efectos de sonido (WAV)
            cargarEfectos()
            
            println("✅ SoundManager inicializado correctamente")
            println("   Música: 2 archivos (con loop automático)")
            println("   Efectos: 7 archivos")
        } catch (e: Exception) {
            println("⚠️ Error al inicializar SoundManager: ${e.message}")
            println("   Revisa que los archivos estén en:")
            println("   - src/resources/assets/music/ (archivos WAV)")
            println("   - src/resources/assets/sounds/ (archivos WAV)")
        }
    }
    
    private fun cargarMusica() {
        try {
            menuMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/menu_music.wav"))
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/game_music.wav"))
            
            // ✅ CONFIGURAR LOOP AUTOMÁTICO
            menuMusic?.isLooping = true
            gameMusic?.isLooping = true
            
            println("✅ Música cargada: menu_music.wav, game_music.wav")
            println("   ✓ Loop automático habilitado (se repite indefinidamente)")
        } catch (e: Exception) {
            println("⚠️ Error cargando música: ${e.message}")
        }
    }
    
    private fun cargarEfectos() {
        try {
            shootSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shoot.wav"))
            explosionSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/explosion.wav"))
            powerupSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/powerup.wav"))
            levelCompleteSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/level_complete.wav"))
            gameOverSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/game_over.wav"))
            damageSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/damage.wav"))
            uiClickSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/ui_click.wav"))
            println("✅ Efectos de sonido cargados: 7 archivos WAV")
        } catch (e: Exception) {
            println("⚠️ Error cargando efectos: ${e.message}")
        }
    }
    
    // ========== REPRODUCIR EFECTOS DE SONIDO ==========
    
    fun playSound(soundType: SoundType) {
        if (!soundEnabled) return
        
        try {
            when (soundType) {
                SoundType.SHOOT -> shootSound?.play(soundVolume)
                SoundType.EXPLOSION -> explosionSound?.play(soundVolume)
                SoundType.POWERUP -> powerupSound?.play(soundVolume * 0.8f)
                SoundType.LEVEL_COMPLETE -> levelCompleteSound?.play(soundVolume * 0.9f)
                SoundType.GAME_OVER -> gameOverSound?.play(soundVolume)
                SoundType.UI_CLICK -> uiClickSound?.play(soundVolume * 0.5f)
                SoundType.DAMAGE -> damageSound?.play(soundVolume * 0.7f)
            }
        } catch (e: Exception) {
            println("⚠️ Error reproduciendo sonido ${soundType.name}: ${e.message}")
        }
    }
    
    // ========== REPRODUCIR MÚSICA ==========
    
    fun playMenuMusic() {
        if (!musicEnabled) return
        
        try {
            // Detener música actual si hay
            currentMusic?.stop()
            
            menuMusic?.let {
                // ✅ LOOP AUTOMÁTICO YA CONFIGURADO EN initialize()
                it.volume = musicVolume
                it.play()
                currentMusic = it
                println("🎵 Reproduciendo: menu_music.wav (con loop automático)")
            }
        } catch (e: Exception) {
            println("⚠️ Error reproduciendo música de menú: ${e.message}")
        }
    }
    
    fun playGameMusic() {
        if (!musicEnabled) return
        
        try {
            // Detener música actual si hay
            currentMusic?.stop()
            
            gameMusic?.let {
                // ✅ LOOP AUTOMÁTICO YA CONFIGURADO EN initialize()
                it.volume = musicVolume
                it.play()
                currentMusic = it
                println("🎵 Reproduciendo: game_music.wav (con loop automático)")
            }
        } catch (e: Exception) {
            println("⚠️ Error reproduciendo música de juego: ${e.message}")
        }
    }
    
    fun stopMusic() {
        try {
            currentMusic?.stop()
            println("🔇 Música detenida")
        } catch (e: Exception) {
            println("⚠️ Error deteniendo música: ${e.message}")
        }
    }
    
    fun pauseMusic() {
        try {
            currentMusic?.pause()
            println("⏸️ Música pausada")
        } catch (e: Exception) {
            println("⚠️ Error pausando música: ${e.message}")
        }
    }
    
    // ✅ MÉTODO CORREGIDO - En lugar de resume(), usamos play()
    fun resumeMusic() {
        try {
            currentMusic?.play()
            println("▶️ Música reanudada")
        } catch (e: Exception) {
            println("⚠️ Error reanudando música: ${e.message}")
        }
    }
    
    // ========== CONTROL DE VOLUMEN ==========
    
    fun setSoundVolume(volume: Float) {
        soundVolume = volume.coerceIn(0f, 1f)
        println("🔊 Volumen efectos: ${(soundVolume * 100).toInt()}%")
    }
    
    fun setMusicVolume(volume: Float) {
        musicVolume = volume.coerceIn(0f, 1f)
        currentMusic?.volume = musicVolume
        println("🎵 Volumen música: ${(musicVolume * 100).toInt()}%")
    }
    
    fun getSoundVolume(): Float = soundVolume
    fun getMusicVolume(): Float = musicVolume
    
    // ========== TOGGLE ==========
    
    fun toggleSound() {
        soundEnabled = !soundEnabled
        println(if (soundEnabled) "🔊 Efectos ON" else "🔇 Efectos OFF")
    }
    
    fun toggleMusic() {
        musicEnabled = !musicEnabled
        if (musicEnabled) {
            currentMusic?.play()
            println("🎵 Música ON")
        } else {
            currentMusic?.pause()
            println("🎵 Música OFF")
        }
    }
    
    fun isSoundEnabled(): Boolean = soundEnabled
    fun isMusicEnabled(): Boolean = musicEnabled
    
    // ========== ESTADO ==========
    
    fun getStatus(): String {
        return buildString {
            append("🔊 ESTADO DE AUDIO\n")
            append("├─ Efectos: ${if (soundEnabled) "ON" else "OFF"} (${(soundVolume * 100).toInt()}%)\n")
            append("├─ Música: ${if (musicEnabled) "ON" else "OFF"} (${(musicVolume * 100).toInt()}%)\n")
            append("├─ Loop: ${currentMusic?.isLooping ?: false}\n")
            append("└─ Estado: ${if (currentMusic?.isPlaying == true) "REPRODUCIENDO" else "DETENIDA"}")
        }
    }
    
    // ========== LIMPIEZA ==========
    
    fun dispose() {
        try {
            println("🔊 Liberando recursos de audio WAV...")
            
            // Detener y liberar música
            menuMusic?.stop()
            gameMusic?.stop()
            currentMusic?.stop()
            
            menuMusic?.dispose()
            gameMusic?.dispose()
            
            // Liberar efectos
            shootSound?.dispose()
            explosionSound?.dispose()
            powerupSound?.dispose()
            levelCompleteSound?.dispose()
            gameOverSound?.dispose()
            damageSound?.dispose()
            uiClickSound?.dispose()
            
            println("✅ Recursos de audio liberados correctamente")
        } catch (e: Exception) {
            println("⚠️ Error liberando recursos: ${e.message}")
        }
    }
}