package game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import game.managers.SoundManager
import game.utils.ScreenType

class SplashScreen(camera: OrthographicCamera, batch: SpriteBatch) : BaseScreen(camera, batch) {
    override val screenType = ScreenType.SPLASH
    
    private var elapsedTime = 0f
    private val totalDuration = 3f // 3 segundos de splash
    private var skipNextCheck = false
    
    override fun show() {
        super.show()
        println("🎬 Splash Screen mostrada")
    }
    
    override fun handleInput() {
        // Presionar cualquier tecla para saltar
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isTouched()) && !skipNextCheck) {
            elapsedTime = totalDuration
            skipNextCheck = true
        }
    }
    
    override fun update(deltaTime: Float) {
        handleInput()
        elapsedTime += deltaTime
    }
    
    override fun render() {
        drawBackground(0.1f, 0.1f, 0.15f)
        
        // Título principal
        drawCenteredText(
            "🎮 TANK WARS 🎮",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.7f,
            3f
        )
        
        // Subtítulo
        drawCenteredText(
            "Un Juego de Estrategia y Acción",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.5f,
            1.5f
        )
        
        // Indicador de progreso
        val progress = (elapsedTime / totalDuration * 100).toInt()
        drawCenteredText(
            "Cargando... $progress%",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.3f,
            1.5f
        )
        
        // Presiona para continuar
        if (elapsedTime > 1f) {
            drawCenteredText(
                "Presiona cualquier tecla para continuar",
                camera.viewportWidth / 2,
                camera.viewportHeight * 0.15f,
                1f
            )
        }
    }
    
    fun isFinished(): Boolean = elapsedTime >= totalDuration
    
    override fun dispose() {
        super.dispose()
    }
}