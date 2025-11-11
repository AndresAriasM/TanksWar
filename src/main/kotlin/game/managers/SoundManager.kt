package game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music
import java.io.File

object SoundManager {
    
    private var currentMusic: Music? = null
    private var menuMusic: Music? = null
    private var gameMusic: Music? = null
    
    private var shootSound: Sound? = null
    private var explosionSound: Sound? = null
    private var powerupSound: Sound? = null
    private var levelCompleteSound: Sound? = null
    private var gameOverSound: Sound? = null
    private var damageSound: Sound? = null
    private var uiClickSound: Sound? = null
    
    private var soundEnabled = true
    private var musicEnabled = true
    private var soundVolume = 0.7f
    private var musicVolume = 0.5f
    
    enum class SoundType {
        SHOOT,
        EXPLOSION,
        POWERUP,
        COLLISION,
        SHOOT_ENEMY,
        LEVEL_COMPLETE,
        GAME_OVER,
        UI_CLICK,
        DAMAGE
    }
    
    fun initialize() {
        println("🔊 Inicializando SoundManager...")
        try {
            cargarMusica()
            cargarEfectos()
            println("✅ SoundManager inicializado correctamente")
        } catch (e: Exception) {
            println("⚠️ Error al inicializar SoundManager: ${e.message}")
        }
    }
    
    private fun cargarMusica() {
        try {
            println("   Buscando archivos de música...")
            
            // Cargar menu music
            menuMusic = try {
                val archivo = File("src/resources/assets/music/menu-music.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: menu-music.wav")
                    Gdx.audio.newMusic(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            
            // Cargar game music
            gameMusic = try {
                val archivo = File("src/resources/assets/music/game-music.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: game-music.wav")
                    Gdx.audio.newMusic(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    println("   ⚠️ game-music.wav no encontrado, usando menu-music como fallback")
                    menuMusic
                }
            } catch (e: Exception) {
                menuMusic
            }
            
            if (menuMusic != null) {
                menuMusic?.isLooping = true
                gameMusic?.isLooping = true
                println("✅ Música cargada y lista")
            } else {
                println("   Nota: Música deshabilitada para esta sesión")
            }
            
        } catch (e: Exception) {
            println("⚠️ Error cargando música: ${e.message}")
        }
    }
    
    private fun cargarEfectos() {
        try {
            println("   Buscando archivos de efectos...")
            
            // Cargar shoot sound
            shootSound = try {
                val archivo = File("src/resources/assets/sounds/shoot.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: shoot.wav")
                    Gdx.audio.newSound(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            
            // Cargar explosion sound
            explosionSound = try {
                val archivo = File("src/resources/assets/sounds/explosion.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: explosion.wav")
                    Gdx.audio.newSound(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            
            // Cargar powerup sound
            powerupSound = try {
                val archivo = File("src/resources/assets/sounds/powerup.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: powerup.wav")
                    Gdx.audio.newSound(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            
            // Cargar collision sound
            damageSound = try {
                val archivo = File("src/resources/assets/sounds/colision.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: colision.wav")
                    Gdx.audio.newSound(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            
            // Cargar enemy shoot sound
            uiClickSound = try {
                val archivo = File("src/resources/assets/sounds/shoot_enemy.wav")
                if (archivo.exists()) {
                    println("   ✓ Encontrado: shoot_enemy.wav")
                    Gdx.audio.newSound(Gdx.files.absolute(archivo.absolutePath))
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
            
            val loadedCount = listOf(shootSound, explosionSound, powerupSound, damageSound, uiClickSound).count { it != null }
            if (loadedCount > 0) {
                println("✅ Efectos de sonido: $loadedCount/5 cargados")
            }
            
        } catch (e: Exception) {
            println("⚠️ Error cargando efectos: ${e.message}")
        }
    }
    
    fun playSound(soundType: SoundType) {
        if (!soundEnabled) return
        try {
            when (soundType) {
                SoundType.SHOOT -> shootSound?.play(soundVolume)
                SoundType.EXPLOSION -> explosionSound?.play(soundVolume)
                SoundType.POWERUP -> powerupSound?.play(soundVolume * 0.8f)
                SoundType.COLLISION -> damageSound?.play(soundVolume * 0.7f)
                SoundType.SHOOT_ENEMY -> uiClickSound?.play(soundVolume)
                SoundType.LEVEL_COMPLETE -> levelCompleteSound?.play(soundVolume * 0.9f)
                SoundType.GAME_OVER -> gameOverSound?.play(soundVolume)
                SoundType.UI_CLICK -> uiClickSound?.play(soundVolume * 0.5f)
                SoundType.DAMAGE -> damageSound?.play(soundVolume * 0.7f)
            }
        } catch (e: Exception) {
            // Ignorar
        }
    }
    
    fun playMenuMusic() {
        if (!musicEnabled) return
        try {
            currentMusic?.stop()
            menuMusic?.let {
                it.volume = musicVolume
                it.play()
                currentMusic = it
                println("🎵 Reproduciendo: menu-music.wav")
            }
        } catch (e: Exception) {
            println("⚠️ Error reproduciendo música: ${e.message}")
        }
    }
    
    fun playGameMusic() {
        if (!musicEnabled) return
        try {
            currentMusic?.stop()
            gameMusic?.let {
                it.volume = musicVolume
                it.play()
                currentMusic = it
                println("🎵 Reproduciendo: game-music.wav")
            }
        } catch (e: Exception) {
            println("⚠️ Error reproduciendo música: ${e.message}")
        }
    }
    
    fun stopMusic() {
        try {
            currentMusic?.stop()
        } catch (e: Exception) {
            // Ignorar
        }
    }
    
    fun pauseMusic() {
        try {
            currentMusic?.pause()
        } catch (e: Exception) {
            // Ignorar
        }
    }
    
    fun resumeMusic() {
        try {
            currentMusic?.play()
        } catch (e: Exception) {
            // Ignorar
        }
    }
    
    fun setSoundVolume(volume: Float) {
        soundVolume = volume.coerceIn(0f, 1f)
    }
    
    fun setMusicVolume(volume: Float) {
        musicVolume = volume.coerceIn(0f, 1f)
        currentMusic?.volume = musicVolume
    }
    
    fun getSoundVolume(): Float = soundVolume
    fun getMusicVolume(): Float = musicVolume
    
    fun toggleSound() {
        soundEnabled = !soundEnabled
    }
    
    fun toggleMusic() {
        musicEnabled = !musicEnabled
        if (musicEnabled) {
            currentMusic?.play()
        } else {
            currentMusic?.pause()
        }
    }
    
    fun isSoundEnabled(): Boolean = soundEnabled
    fun isMusicEnabled(): Boolean = musicEnabled
    
    fun dispose() {
        try {
            menuMusic?.stop()
            gameMusic?.stop()
            currentMusic?.stop()
            
            menuMusic?.dispose()
            gameMusic?.dispose()
            
            shootSound?.dispose()
            explosionSound?.dispose()
            powerupSound?.dispose()
            levelCompleteSound?.dispose()
            gameOverSound?.dispose()
            damageSound?.dispose()
            uiClickSound?.dispose()
            
            println("✅ Recursos de audio liberados")
        } catch (e: Exception) {
            println("⚠️ Error liberando recursos: ${e.message}")
        }
    }
}