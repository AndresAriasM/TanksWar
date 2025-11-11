package game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.managers.DataManager
import game.managers.LevelManager
import game.managers.SoundManager
import game.utils.ScreenType

class LevelSelectScreen(camera: OrthographicCamera, batch: SpriteBatch) : BaseScreen(camera, batch) {
    override val screenType = ScreenType.LEVEL_SELECT
    
    private var selectedLevel = 1
    private var levelSelected: Int? = null
    private var backPressed = false
    private val unlockedLevel = DataManager.getLevelUnlocked()
    private val secretLevelUnlocked = DataManager.isSecretLevelUnlocked()
    
    override fun show() {
        super.show()
        println("📋 Level Select Screen mostrada")
        selectedLevel = DataManager.getCurrentLevel()
        levelSelected = null
        backPressed = false
    }
    
    override fun handleInput() {
        // Navegación horizontal
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            selectedLevel = (selectedLevel - 1).coerceAtLeast(1)
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            val maxLevel = if (secretLevelUnlocked) 6 else minOf(unlockedLevel, 5)
            selectedLevel = (selectedLevel + 1).coerceAtMost(maxLevel)
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        
        // Navegación vertical
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedLevel = (selectedLevel - 1).coerceAtLeast(1)
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            val maxLevel = if (secretLevelUnlocked) 6 else minOf(unlockedLevel, 5)
            selectedLevel = (selectedLevel + 1).coerceAtMost(maxLevel)
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        
        // Seleccionar nivel
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (selectedLevel <= unlockedLevel || (selectedLevel == 6 && secretLevelUnlocked)) {
                levelSelected = selectedLevel
                DataManager.setCurrentLevel(selectedLevel)
                SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
            }
        }
        
        // Volver al menú
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            backPressed = true
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
    }
    
    override fun update(deltaTime: Float) {
        handleInput()
    }
    
    override fun render() {
        drawBackground(0.05f, 0.08f, 0.12f)
        
        // Título
        drawCenteredText(
            "SELECCIONA UN NIVEL",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.9f,
            2.5f
        )
        
        // Grid de niveles
        val gridStartX = camera.viewportWidth * 0.15f
        val gridStartY = camera.viewportHeight * 0.75f
        val levelBoxWidth = 120f
        val levelBoxHeight = 100f
        val spacingX = 150f
        val spacingY = 130f
        
        // Dibujar los 6 niveles (2 filas de 3)
        for (level in 1..6) {
            val row = (level - 1) / 3
            val col = (level - 1) % 3
            val x = gridStartX + (col * spacingX)
            val y = gridStartY - (row * spacingY)
            
            val isUnlocked = level <= unlockedLevel || (level == 6 && secretLevelUnlocked)
            val isSelected = level == selectedLevel
            
            drawLevelBox(x, y, levelBoxWidth, levelBoxHeight, level, isUnlocked, isSelected)
        }
        
        // Información del nivel seleccionado
        drawLevelInfo()
        
        // Controles
        batch.begin()
        font.data.setScale(0.9f)
        font.color.set(0.7f, 0.7f, 0.7f, 1f)
        font.draw(batch, "◄ Anterior    Siguiente ►    ENTER para jugar    ESC para volver", 
                  camera.viewportWidth * 0.05f, camera.viewportHeight * 0.05f)
        batch.end()
    }
    
    private fun drawLevelBox(x: Float, y: Float, width: Float, height: Float, level: Int, isUnlocked: Boolean, isSelected: Boolean) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        // Fondo del cuadro
        if (isSelected) {
            shapeRenderer.color.set(0.2f, 0.8f, 0.2f, 1f) // Verde para seleccionado
        } else if (isUnlocked) {
            shapeRenderer.color.set(0.15f, 0.15f, 0.25f, 1f) // Azul oscuro para desbloqueado
        } else {
            shapeRenderer.color.set(0.1f, 0.1f, 0.1f, 1f) // Gris oscuro para bloqueado
        }
        
        shapeRenderer.rect(x - width / 2, y - height / 2, width, height)
        
        // Borde
        if (isSelected) {
            shapeRenderer.color.set(0.4f, 1f, 0.4f, 1f)
        } else if (isUnlocked) {
            shapeRenderer.color.set(0.3f, 0.3f, 0.5f, 1f)
        } else {
            shapeRenderer.color.set(0.2f, 0.2f, 0.2f, 1f)
        }
        
        shapeRenderer.rect(x - width / 2 - 3, y - height / 2 - 3, width + 6, height + 6)
        
        shapeRenderer.end()
        
        // Texto del nivel
        batch.begin()
        font.data.setScale(1.8f)
        
        if (isUnlocked) {
            font.color.set(1f, 1f, 1f, 1f)
        } else {
            font.color.set(0.5f, 0.5f, 0.5f, 1f)
        }
        
        val levelText = if (level == 6) "SECRETO" else "NIVEL $level"
        val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, levelText)
        font.draw(batch, levelText, x - layout.width / 2, y + layout.height / 4)
        
        // Indicador de estado
        font.data.setScale(0.8f)
        if (!isUnlocked) {
            font.color.set(1f, 0f, 0f, 1f)
            val lockedText = "BLOQUEADO"
            val lockedLayout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, lockedText)
            font.draw(batch, lockedText, x - lockedLayout.width / 2, y - 15f)
        } else if (isSelected) {
            font.color.set(1f, 1f, 0f, 1f)
            val selectText = "SELECCIONADO"
            val selectLayout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, selectText)
            font.draw(batch, selectText, x - selectLayout.width / 2, y - 15f)
        }
        
        batch.end()
    }
    
    private fun drawLevelInfo() {
        val infoY = camera.viewportHeight * 0.25f
        
        batch.begin()
        font.data.setScale(1.2f)
        font.color.set(0.2f, 0.8f, 0.2f, 1f)
        
        val levelConfig = LevelManager.levelConfigs[selectedLevel]
        if (levelConfig != null) {
            font.draw(batch, "NIVEL ${levelConfig.levelNumber}: ${levelConfig.name}", 
                      camera.viewportWidth * 0.1f, infoY)
            
            font.data.setScale(1f)
            font.color.set(1f, 1f, 1f, 1f)
            
            var currentY = infoY - 40f
            font.draw(batch, "• Enemigos: ${levelConfig.enemyCount}", camera.viewportWidth * 0.1f, currentY)
            
            currentY -= 30f
            font.draw(batch, "• Dificultad: " + when (levelConfig.levelNumber) {
                1 -> "Fácil 🟢"
                2 -> "Normal 🟡"
                3 -> "Moderado 🟠"
                4 -> "Difícil 🔴"
                5 -> "Épico 💀"
                6 -> "Caos Extremo 💀💀"
                else -> "Desconocida"
            }, camera.viewportWidth * 0.1f, currentY)
            
            currentY -= 30f
            levelConfig.timeLimit?.let {
                font.draw(batch, "• Límite de tiempo: ${it.toInt()} segundos", 
                          camera.viewportWidth * 0.1f, currentY)
            }
        }
        
        batch.end()
    }
    
    fun getLevelSelected(): Int? = levelSelected
    fun isBackPressed(): Boolean = backPressed
    
    fun reset() {
        levelSelected = null
        backPressed = false
    }
    
    override fun dispose() {
        super.dispose()
    }
}