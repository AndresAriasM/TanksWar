package game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import game.managers.DataManager
import game.managers.LevelManager
import game.managers.SoundManager
import game.screens.*
import game.utils.ScreenType
import game.utils.Constants

class GameApplication : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var camera: OrthographicCamera
    
    // Pantallas
    private lateinit var splashScreen: SplashScreen
    private lateinit var homeScreen: HomeScreen
    private lateinit var levelSelectScreen: LevelSelectScreen
    private lateinit var gameScreen: GameScreen
    private lateinit var scoreScreen: ScoreScreen
    private lateinit var creditsScreen: CreditsScreen
    
    private var currentScreen: BaseScreen? = null
    private var currentScreenType: ScreenType = ScreenType.SPLASH
    
    override fun create() {
        // Inicializar cámara y batch
        camera = OrthographicCamera()
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT)
        batch = SpriteBatch()
        
        // Inicializar gestores
        DataManager.initialize()
        SoundManager.initialize()
        LevelManager // Initialize companion object
        
        // Crear pantallas
        splashScreen = SplashScreen(camera, batch)
        homeScreen = HomeScreen(camera, batch)
        levelSelectScreen = LevelSelectScreen(camera, batch)
        gameScreen = GameScreen(camera, batch)
        scoreScreen = ScoreScreen(camera, batch)
        creditsScreen = CreditsScreen(camera, batch)
        
        // Mostrar splash screen
        switchScreen(ScreenType.SPLASH)
        
        println("==================================================")
        println("🎮 TANK WARS - Inicializado correctamente")
        println("==================================================")
        println("Versión: 1.0.0")
        println("LibGDX: 1.11.0")
        println("Kotlin: 1.9.20")
        println("Java: 17")
        println("==================================================")
    }
    
    override fun render() {
        // Limpiar pantalla
        Gdx.gl.glClearColor(
            Constants.COLOR_BACKGROUND_R,
            Constants.COLOR_BACKGROUND_G,
            Constants.COLOR_BACKGROUND_B,
            1f
        )
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        // Actualizar cámara
        camera.update()
        batch.projectionMatrix = camera.combined
        
        // Actualizar y renderizar pantalla actual
        currentScreen?.let {
            it.update(Gdx.graphics.deltaTime)
            it.render()
        }
        
        // Lógica de transición entre pantallas
        handleScreenTransitions()
    }
    
    private fun handleScreenTransitions() {
        when (currentScreenType) {
            ScreenType.SPLASH -> {
                if (splashScreen.isFinished()) {
                    switchScreen(ScreenType.HOME)
                }
            }
            
            ScreenType.HOME -> {
                val selected = homeScreen.getSelectedOption()
                when (selected) {
                    "Jugar" -> {
                        switchScreen(ScreenType.LEVEL_SELECT)
                        homeScreen.resetSelection()
                    }
                    "Puntuaciones" -> {
                        switchScreen(ScreenType.SCORE)
                        homeScreen.resetSelection()
                    }
                    "Créditos" -> {
                        switchScreen(ScreenType.CREDITS)
                        homeScreen.resetSelection()
                    }
                    "Salir" -> {
                        Gdx.app.exit()
                    }
                }
            }
            
            ScreenType.LEVEL_SELECT -> {
                if (levelSelectScreen.getLevelSelected() != null) {
                    switchScreen(ScreenType.GAME)
                    levelSelectScreen.reset()
                }
                if (levelSelectScreen.isBackPressed()) {
                    switchScreen(ScreenType.HOME)
                    levelSelectScreen.reset()
                }
            }
            
            ScreenType.GAME -> {
                if (gameScreen.isBackToMenu()) {
                    switchScreen(ScreenType.HOME)
                }
            }
            
            ScreenType.SCORE -> {
                if (scoreScreen.isBackPressed()) {
                    switchScreen(ScreenType.HOME)
                    scoreScreen.reset()
                }
            }
            
            ScreenType.CREDITS -> {
                if (creditsScreen.isBackPressed()) {
                    switchScreen(ScreenType.HOME)
                    creditsScreen.reset()
                }
            }
        }
    }
    
    private fun switchScreen(screenType: ScreenType) {
        // Ocultar pantalla anterior
        currentScreen?.hide()
        
        // Cambiar a nueva pantalla
        currentScreenType = screenType
        currentScreen = when (screenType) {
            ScreenType.SPLASH -> splashScreen
            ScreenType.HOME -> homeScreen
            ScreenType.LEVEL_SELECT -> levelSelectScreen
            ScreenType.GAME -> gameScreen
            ScreenType.SCORE -> scoreScreen
            ScreenType.CREDITS -> creditsScreen
        }
        
        currentScreen?.show()
    }
    
    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT)
    }
    
    override fun dispose() {
        println("🛑 Liberando recursos...")
        batch.dispose()
        splashScreen.dispose()
        homeScreen.dispose()
        levelSelectScreen.dispose()
        gameScreen.dispose()
        scoreScreen.dispose()
        creditsScreen.dispose()
        SoundManager.dispose()
        DataManager.saveData()
        println("✅ Juego finalizado")
    }
}