package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import kotlin.math.cos
import kotlin.math.sin

class Bullet(
    var x: Float,
    var y: Float,
    val angle: Float,
    val damage: Int = Constants.BULLET_DAMAGE,
    val isPlayerBullet: Boolean = true,
    var speed: Float = Constants.BULLET_SPEED
) {
    var isActive: Boolean = true
    private val size = Constants.BULLET_SIZE
    
    fun update(deltaTime: Float) {
        val radians = Math.toRadians(angle.toDouble())
        x += (cos(radians) * speed * deltaTime).toFloat()
        y += (sin(radians) * speed * deltaTime).toFloat()
        
        // Desactivar si sale de pantalla
        if (x < 0 || x > Constants.SCREEN_WIDTH ||
            y < 0 || y > Constants.SCREEN_HEIGHT) {
            isActive = false
        }
    }
    
    fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        if (isPlayerBullet) {
            shapeRenderer.color.set(0f, 1f, 0f, 1f) // Verde
        } else {
            shapeRenderer.color.set(1f, 0f, 0f, 1f) // Rojo
        }
        
        // Núcleo brillante
        shapeRenderer.circle(x, y, size / 2 + 1, 12)
        
        // Halo exterior
        if (isPlayerBullet) {
            shapeRenderer.color.set(0f, 1f, 0f, 0.5f)
        } else {
            shapeRenderer.color.set(1f, 0f, 0f, 0.5f)
        }
        shapeRenderer.circle(x, y, size, 12)
        
        shapeRenderer.end()
    }
    
    fun getBounds(): Pair<Float, Float> = Pair(x, y)
}