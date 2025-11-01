package game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import game.managers.SoundManager
import game.utils.ScreenType

class CreditsScreen(camera: OrthographicCamera, batch: SpriteBatch) : BaseScreen(camera, batch) {
    override val screenType = ScreenType.CREDITS
    
    private var backPressed = false
    private var scrollOffset = 0f
    private val scrollSpeed = 50f
    
    private val credits = listOf(
        Pair("TANK WARS", ""),
        Pair("Un Juego de Acción y Estrategia", ""),
        Pair("", ""),
        Pair("DESARROLLO", ""),
        Pair("Programador Principal", "Estudiante de Ingeniería"),
        Pair("Motor Gráfico", "LibGDX 1.11.0"),
        Pair("Lenguaje", "Kotlin"),
        Pair("", ""),
        Pair("ASSETS", ""),
        Pair("Música y Sonidos", "Generados dinámicamente"),
        Pair("Gráficos", "Renderización 2D en tiempo real"),
        Pair("", ""),
        Pair("TECNOLOGÍA", ""),
        Pair("Java Runtime", "JDK 17"),
        Pair("Build System", "Gradle 8.14.3"),
        Pair("IDE", "Visual Studio Code"),
        Pair("Control de Versiones", "Git"),
        Pair("", ""),
        Pair("AGRADECIMIENTOS", ""),
        Pair("A todos los jugadores", ""),
        Pair("Por su apoyo y diversión", ""),
        Pair("", ""),
        Pair("¡GRACIAS POR JUGAR!", ""),
        Pair("", ""),
        Pair("Presiona ESC para volver", "")
    )
    
    override fun show() {
        super.show()
        println("🎬 Credits Screen mostrada")
        scrollOffset = 0f
    }
    
    override fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            backPressed = true
            SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            scrollOffset -= scrollSpeed * Gdx.graphics.deltaTime
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            scrollOffset += scrollSpeed * Gdx.graphics.deltaTime
        }
    }
    
    override fun update(deltaTime: Float) {
        handleInput()
        scrollOffset += scrollSpeed * deltaTime * 0.5f
    }
    
    override fun render() {
        drawBackground(0.05f, 0.1f, 0.15f)
        
        drawCenteredText(
            "CRÉDITOS",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.95f,
            3f
        )
        
        batch.begin()
        var yPosition = camera.viewportHeight - 100f - scrollOffset
        
        credits.forEach { (title, subtitle) ->
            if (title.isEmpty()) {
                yPosition -= 20f
            } else {
                if (subtitle.isEmpty()) {
                    font.data.setScale(1.5f)
                    font.color.set(0.2f, 0.8f, 0.2f, 1f)
                } else {
                    font.data.setScale(1f)
                    font.color.set(1f, 1f, 1f, 1f)
                }
                
                val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, title)
                font.draw(
                    batch,
                    title,
                    camera.viewportWidth / 2 - layout.width / 2,
                    yPosition
                )
                
                yPosition -= 30f
            }
        }
        batch.end()
        
        drawCenteredText(
            "Usa ARRIBA/ABAJO para desplazar o ESC para volver",
            camera.viewportWidth / 2,
            camera.viewportHeight * 0.02f,
            0.8f
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