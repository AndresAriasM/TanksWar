package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants

class Obstacle(
    var x: Float,
    var y: Float,
    var width: Float = Constants.OBSTACLE_SIZE,
    var height: Float = Constants.OBSTACLE_SIZE,
    val isDestructible: Boolean = false,
    var health: Int = 50
) {
    var isActive: Boolean = true
    
    fun update(deltaTime: Float) {
        // Los obstáculos pueden no necesitar actualización por ahora
    }
    
    fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        if (!isActive) return
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        if (isDestructible) {
            // Calcular intensidad de color basado en salud
            val healthPercent = (health.toFloat() / 50f).coerceIn(0f, 1f)
            shapeRenderer.color.set(1f, 0.5f * healthPercent, 0f, 1f)
            
            // Dibujar con efecto de daño
            shapeRenderer.rect(x, y, width, height)
            
            // Borde exterior
            shapeRenderer.color.set(0.8f, 0.3f, 0f, 1f)
            shapeRenderer.rect(x - 2, y - 2, width + 4, height + 4)
            
            // Patrón de grietas (pequeños cuadrados internos)
            shapeRenderer.color.set(0.3f, 0.15f, 0f, 0.7f)
            for (i in 0..2) {
                for (j in 0..2) {
                    shapeRenderer.rect(
                        x + (i * width / 3) + 3,
                        y + (j * height / 3) + 3,
                        width / 3 - 4,
                        height / 3 - 4
                    )
                }
            }
        } else {
            // Obstáculos permanentes (más oscuros)
            shapeRenderer.color.set(0.15f, 0.15f, 0.15f, 1f)
            shapeRenderer.rect(x, y, width, height)
            
            // Borde metálico
            shapeRenderer.color.set(0.3f, 0.3f, 0.3f, 1f)
            shapeRenderer.rect(x - 2, y - 2, width + 4, height + 4)
            
            // Líneas de refuerzo
            shapeRenderer.color.set(0.2f, 0.2f, 0.2f, 0.8f)
            shapeRenderer.line(x, y + height / 2, x + width, y + height / 2)
            shapeRenderer.line(x + width / 2, y, x + width / 2, y + height)
        }
        
        shapeRenderer.end()
    }
    
    fun takeDamage(damage: Int) {
        if (isDestructible) {
            health -= damage
            if (health <= 0) {
                isActive = false
            }
        }
    }
    
    fun getHealthPercentage(): Int = if (isDestructible) (health.toFloat() / 50f * 100).toInt() else 100
}