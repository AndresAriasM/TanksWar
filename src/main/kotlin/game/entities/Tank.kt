package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import game.utils.Direction
import kotlin.math.cos
import kotlin.math.sin

abstract class Tank(
    var x: Float,
    var y: Float,
    var size: Float = Constants.TANK_SIZE,
    var maxHealth: Int = Constants.TANK_HEALTH
) {
    var health: Int = maxHealth
    var direction: Direction = Direction.UP
    var angle: Float = 90f
    var speed: Float = Constants.TANK_SPEED
    var isAlive: Boolean = true
    var shootCooldown: Float = 0.5f
    var currentShootCooldown: Float = 0f
    
    // Potenciadores
    var rapidFireMultiplier: Float = 1f
    var speedMultiplier: Float = 1f
    var damageMultiplier: Float = 1f
    var hasShield: Boolean = false
    
    abstract fun update(deltaTime: Float)
    abstract fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer)
    
    fun takeDamage(damage: Int) {
        if (hasShield) {
            hasShield = false
            println("🛡️ Escudo absorbió el daño")
            return
        }
        
        health -= damage
        if (health <= 0) {
            health = 0
            isAlive = false
            println("💥 Tanque destruido")
        }
    }
    
    fun heal(amount: Int) {
        health = (health + amount).coerceAtMost(maxHealth)
    }
    
    fun activateShield() {
        hasShield = true
    }
    
    fun moveForward(deltaTime: Float) {
        val actualSpeed = speed * speedMultiplier
        val radians = Math.toRadians(angle.toDouble())
        x += (cos(radians) * actualSpeed * deltaTime).toFloat()
        y += (sin(radians) * actualSpeed * deltaTime).toFloat()
        
        // Limitar dentro de pantalla
        clampToBounds()
    }
    
    fun rotate(degrees: Float) {
        angle += degrees
        angle = (angle % 360 + 360) % 360
    }
    
    protected fun clampToBounds() {
        val margin = size / 2
        x = x.coerceIn(margin, Constants.SCREEN_WIDTH - margin)
        y = y.coerceIn(margin, Constants.SCREEN_HEIGHT - margin)
    }
    
    protected fun drawTank(shapeRenderer: ShapeRenderer, r: Float, g: Float, b: Float) {
        shapeRenderer.color.set(r, g, b, 1f)
        
        val halfSize = size / 2
        
        // Cuerpo principal (rectángulo redondeado simulado)
        shapeRenderer.rect(x - halfSize * 0.9f, y - halfSize * 0.9f, size * 0.9f, size * 0.9f)
        
        // Círculo en el centro (torreta)
        shapeRenderer.circle(x, y, halfSize * 0.5f, 12)
        
        // Cañón (línea gruesa)
        val radians = Math.toRadians(angle.toDouble())
        val cannonLength = size * 0.7f
        val cannonEndX = x + (cos(radians) * cannonLength).toFloat()
        val cannonEndY = y + (sin(radians) * cannonLength).toFloat()
        
        // Dibujar cañón como línea gruesa
        shapeRenderer.line(x, y, cannonEndX, cannonEndY)
        shapeRenderer.line(x - 2, y, cannonEndX - 2, cannonEndY)
        shapeRenderer.line(x + 2, y, cannonEndX + 2, cannonEndY)
        
        // Ruedas (dos círculos pequeños abajo)
        shapeRenderer.circle(x - halfSize * 0.6f, y - halfSize, halfSize * 0.25f, 8)
        shapeRenderer.circle(x + halfSize * 0.6f, y - halfSize, halfSize * 0.25f, 8)
        
        // Escudo visual si está activo
        if (hasShield) {
            shapeRenderer.color.set(0f, 1f, 1f, 0.3f)
            shapeRenderer.circle(x, y, size * 0.8f, 16)
            shapeRenderer.color.set(0f, 1f, 1f, 0.5f)
        }
    }
    
    fun canShoot(): Boolean = currentShootCooldown <= 0f
    
    fun updateShootCooldown(deltaTime: Float) {
        currentShootCooldown -= deltaTime
        if (currentShootCooldown < 0) currentShootCooldown = 0f
    }
    
    fun resetShootCooldown() {
        currentShootCooldown = shootCooldown / rapidFireMultiplier
    }
    
    fun getHealthPercentage(): Float = (health.toFloat() / maxHealth) * 100f
    
    fun drawHealthBar(shapeRenderer: ShapeRenderer) {
        val barWidth = 40f
        val barHeight = 4f
        val barX = x - barWidth / 2
        val barY = y + size / 2 + 8f
        
        // NO hacer begin aquí - ya está en progreso
        
        // Fondo rojo
        shapeRenderer.color.set(1f, 0f, 0f, 1f)
        shapeRenderer.rect(barX, barY, barWidth, barHeight)
        
        // Barra verde según salud
        shapeRenderer.color.set(0f, 1f, 0f, 1f)
        val healthWidth = (barWidth * health.toFloat()) / maxHealth.toFloat()
        shapeRenderer.rect(barX, barY, healthWidth, barHeight)
        
        // Borde
        shapeRenderer.color.set(1f, 1f, 1f, 0.8f)
        shapeRenderer.rect(barX - 1, barY - 1, barWidth + 2, barHeight + 2)
    }
}