package game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import game.managers.DataManager
import game.managers.SoundManager
import game.utils.ScreenType

class HomeScreen(camera: OrthographicCamera, batch: SpriteBatch) : BaseScreen(camera, batch) {
    override val screenType = ScreenType.HOME
    
    private var selectedOption = 0
    private val options = listOf("Jugar", "Puntuaciones", "Créditos", "Salir")
    private var optionSelected: String? = null
    
    override fun show() {
        super.show()
        println("🏠 Home Screen mostrada")
        selectedOption = 0
        optionSelected = null
        SoundManager.playMenuMusic()  // ← AGREGAR ESTA LÍNEA
    }
    
    override fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption = (selectedOption - 1).coerceIn(0, options.size - 1)
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption = (selectedOption + 1).coerceIn(0, options.size - 1)
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            selectOption()
        }
    }
    
    private fun selectOption() {
        optionSelected = options[selectedOption]
        println("✅ Opción seleccionada: $optionSelected")
        SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
    }
    
    override fun update(deltaTime: Float) {
        handleInput()
    }
    
    override fun render() {
        drawBackground(0.05f, 0.05f, 0.1f)
        
        // Título
        drawCenteredText(
            "TANK WARS",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.85f,
            3f
        )
        
        // Información del jugador
        val playerName = DataManager.getPlayerName()
        val highScore = DataManager.getHighScore()
        drawCenteredText(
            "Bienvenido: $playerName | HighScore: $highScore",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.75f,
            1f
        )
        
        // Menú de opciones
        val optionStartY = camera.viewportHeight * 0.6f
        val optionSpacing = 60f
        
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedOption
            val y = optionStartY - (index * optionSpacing)
            
            // Dibuja fondo de opción seleccionada
            if (isSelected) {
                drawRect(
                    camera.viewportWidth / 2 - 100f,
                    y - 20f,
                    200f,
                    40f,
                    0.2f, 0.8f, 0.2f,
                    filled = true
                )
            }
            
            // Texto de opción
            val prefix = if (isSelected) "► " else "  "
            drawCenteredText(
                "$prefix$option",
                camera.viewportWidth / 2,
                y,
                1.5f
            )
        }
        
        // Controles
        drawCenteredText(
            "Use ARRIBA/ABAJO para navegar, ENTER para seleccionar",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.1f,
            0.8f
        )
        
        // Nivel desbloqueado
        val levelUnlocked = DataManager.getLevelUnlocked()
        val secretUnlocked = if (DataManager.isSecretLevelUnlocked()) " | NIVEL SECRETO DESBLOQUEADO!" else ""
        drawCenteredText(
            "Nivel máximo desbloqueado: $levelUnlocked$secretUnlocked",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.05f,
            0.8f
        )
    }
    
    fun getSelectedOption(): String? = optionSelected
    
    fun resetSelection() {
        optionSelected = null
    }
    
    override fun dispose() {
        super.dispose()
    }
}