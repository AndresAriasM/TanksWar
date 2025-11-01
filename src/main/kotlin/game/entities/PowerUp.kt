package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import game.utils.PowerUpType

class PowerUp(
    var x: Float,
    var y: Float,
    val type: PowerUpType,
    val duration: Float = 10f
) {
    val size = Constants.POWERUP_SIZE
    var isActive: Boolean = true
    var elapsedTime: Float = 0f
    
    fun update(deltaTime: Float) {
        elapsedTime += deltaTime
        if (elapsedTime >= duration) {
            isActive = false
        }
    }
    
    fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        // Color según tipo
        val (r, g, b) = when (type) {
            PowerUpType.RAPID_FIRE -> Triple(1f, 1f, 0f)
            PowerUpType.SHIELD -> Triple(0f, 1f, 1f)
            PowerUpType.SPEED -> Triple(0f, 1f, 0.5f)
            PowerUpType.HEALTH -> Triple(1f, 0f, 0f)
            PowerUpType.ARMOR -> Triple(0.5f, 0.5f, 0.5f)
        }
        
        // Núcleo brillante
        shapeRenderer.color.set(r, g, b, 1f)
        shapeRenderer.circle(x, y, size / 2, 16)
        
        // Estrella interior (4 puntos)
        val starSize = size / 3
        shapeRenderer.color.set(1f, 1f, 1f, 0.8f)
        shapeRenderer.line(x - starSize, y, x + starSize, y)
        shapeRenderer.line(x, y - starSize, x, y + starSize)
        
        // Halo pulsante exterior
        shapeRenderer.color.set(r, g, b, 0.3f)
        shapeRenderer.circle(x, y, size * 0.8f, 16)
        
        shapeRenderer.end()
    }
    
    private fun drawPowerUpShape(shapeRenderer: ShapeRenderer) {
        // No se usa, se dibuja en render()
    }
    
    fun getDescription(): String = when (type) {
        PowerUpType.RAPID_FIRE -> "Disparo Rápido"
        PowerUpType.SHIELD -> "Escudo"
        PowerUpType.SPEED -> "Velocidad"
        PowerUpType.HEALTH -> "Salud"
        PowerUpType.ARMOR -> "Armadura"
    }
}