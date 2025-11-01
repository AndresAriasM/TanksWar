package game.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.ScreenType

abstract class BaseScreen(
    val camera: OrthographicCamera,
    val batch: SpriteBatch
) {
    protected val shapeRenderer = ShapeRenderer()
    protected val font = BitmapFont()
    protected var isInitialized = false
    
    abstract val screenType: ScreenType
    abstract fun handleInput()
    abstract fun update(deltaTime: Float)
    abstract fun render()
    
    open fun show() {
        println("📺 Mostrando pantalla: ${screenType.name}")
        isInitialized = true
    }
    
    open fun hide() {
        println("📺 Ocultando pantalla: ${screenType.name}")
    }
    
    open fun dispose() {
        shapeRenderer.dispose()
        font.dispose()
    }
    
    protected fun drawBackground(r: Float, g: Float, b: Float) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color.set(r, g, b, 1f)
        shapeRenderer.rect(0f, 0f, camera.viewportWidth, camera.viewportHeight)
        shapeRenderer.end()
    }
    
    protected fun drawText(text: String, x: Float, y: Float, scale: Float = 1f) {
        font.data.setScale(scale)
        batch.begin()
        font.draw(batch, text, x, y)
        batch.end()
    }
    
    protected fun drawCenteredText(text: String, x: Float, y: Float, scale: Float = 1f) {
        font.data.setScale(scale)
        batch.begin()
        val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, text)
        font.draw(batch, text, x - layout.width / 2, y + layout.height / 2)
        batch.end()
    }
    
    protected fun drawRect(x: Float, y: Float, width: Float, height: Float, r: Float, g: Float, b: Float, filled: Boolean = true) {
        shapeRenderer.begin(
            if (filled) ShapeRenderer.ShapeType.Filled else ShapeRenderer.ShapeType.Line
        )
        shapeRenderer.color.set(r, g, b, 1f)
        shapeRenderer.rect(x, y, width, height)
        shapeRenderer.end()
    }
}