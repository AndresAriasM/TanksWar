package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class AirplaneType {
    BOMBER,        // Lanza balas
    OBSTACLE_DROP  // Lanza obstáculos
}

class Airplane(
    var x: Float = 0f,
    var y: Float = 50f,
    val type: AirplaneType = AirplaneType.BOMBER
) {
    var isActive = true
    private var timer = 0f
    private val speed = 150f
    private var shootTimer = 0f
    
    fun update(deltaTime: Float) {
        timer += deltaTime
        shootTimer += deltaTime
        
        // Movimiento izquierda a derecha
        x += speed * deltaTime
        
        if (x > Constants.SCREEN_WIDTH + 100f) {
            isActive = false
        }
    }
    
    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        if (type == AirplaneType.BOMBER) {
            shapeRenderer.color.set(1f, 0.5f, 0f, 1f)  // Naranja
        } else {
            shapeRenderer.color.set(0.5f, 0.5f, 1f, 1f)  // Azul
        }
        
        // Cuerpo del avión
        shapeRenderer.rect(x - 20f, y - 5f, 40f, 10f)
        
        // Alas
        shapeRenderer.rect(x - 30f, y, 60f, 3f)
        
        // Cola
        shapeRenderer.rect(x - 25f, y - 10f, 10f, 3f)
        
        shapeRenderer.end()
    }
    
    fun shouldShoot(): Boolean {
        return shootTimer > if (type == AirplaneType.BOMBER) 1f else 2f
    }
    
    fun resetShootTimer() {
        shootTimer = 0f
    }
    
    fun getDropPosition(): Pair<Float, Float> = Pair(x, y - 30f)
}