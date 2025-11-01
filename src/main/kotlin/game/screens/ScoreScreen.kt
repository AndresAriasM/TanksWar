package game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.managers.DataManager
import game.managers.SoundManager
import game.utils.ScreenType

class ScoreScreen(camera: OrthographicCamera, batch: SpriteBatch) : BaseScreen(camera, batch) {
    override val screenType = ScreenType.SCORE
    
    private var backPressed = false
    private var displayStats: List<Pair<String, String>> = emptyList()
    
    override fun show() {
        super.show()
        println("📊 Score Screen mostrada")
        updateStats()
    }
    
    private fun updateStats() {
        val data = DataManager.getAllData()
        displayStats = listOf(
            Pair("Nombre del Jugador", data.playerName),
            Pair("Puntuación Total", data.totalScore.toString()),
            Pair("Puntuación Máxima", data.highScore.toString()),
            Pair("Nivel Desbloqueado", data.levelUnlocked.toString()),
            Pair("Vidas Restantes", data.lives.toString()),
            Pair("Nivel Secreto Desbloqueado", if (data.secretLevelUnlocked) "SÍ" else "NO"),
            Pair("Balas Disparadas", data.playerStats["bullets_fired"].toString()),
            Pair("Enemigos Eliminados", data.playerStats["enemies_killed"].toString()),
            Pair("Power-ups Recogidos", data.playerStats["powerups_collected"].toString())
        )
    }
    
    override fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            backPressed = true
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
    }
    
    override fun update(deltaTime: Float) {
        handleInput()
    }
    
    override fun render() {
        drawBackground(0.05f, 0.05f, 0.15f)
        
        // Título
        drawCenteredText(
            "PUNTUACIONES Y ESTADÍSTICAS",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.9f,
            2.5f
        )
        
        // Mostrar estadísticas
        val startY = camera.viewportHeight * 0.75f
        val lineHeight = 35f
        
        displayStats.forEachIndexed { index, (label, value) ->
            val y = startY - (index * lineHeight)
            drawText(
                "$label: ",
                camera.viewportWidth * 0.1f,
                y,
                1.2f
            )
            
            // Valor con color destacado
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
            shapeRenderer.color.set(0.2f, 0.8f, 0.2f, 1f)
            shapeRenderer.rect(
                camera.viewportWidth * 0.4f,
                y - 20f,
                150f,
                25f
            )
            shapeRenderer.end()
            
            drawText(
                value,
                camera.viewportWidth * 0.42f,
                y,
                1.2f
            )
        }
        
        // Instrucciones
        drawCenteredText(
            "Presiona ENTER o ESC para volver",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.05f,
            1f
        )
    }
    
    fun isBackPressed(): Boolean = backPressed
    
    fun reset() {
        backPressed = false
    }
    
    override fun dispose() {
        super.dispose()
    }
}