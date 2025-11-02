package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class AllyTank(x: Float = 100f, y: Float = 100f) : Tank(x, y) {
    var targetX = x
    var targetY = y
    private var moveTimer = 0f
    private var shootTimer = 0f
    
    override fun update(deltaTime: Float) {
        moveTimer += deltaTime
        shootTimer += deltaTime
        
        // Moverse hacia punto random cada 5 segundos
        if (moveTimer > 5f) {
            targetX = Random.nextFloat() * Constants.SCREEN_WIDTH
            targetY = Random.nextFloat() * Constants.SCREEN_HEIGHT
            moveTimer = 0f
        }
        
        // Moverse hacia el objetivo
        val dx = targetX - x
        val dy = targetY - y
        val distance = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        
        if (distance > 10f) {
            angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
            moveForward(deltaTime)
        }
        
        updateShootCooldown(deltaTime)
    }
    
    override fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        drawTank(shapeRenderer, 0f, 1f, 0.5f) // Azul-verde para aliado
        shapeRenderer.end()
    }
    
    fun shoot(): Bullet? {
        if (canShoot() && Random.nextFloat() < 0.15f) {
            resetShootCooldown()
            
            val radians = Math.toRadians(angle.toDouble())
            val cannonLength = size * 0.8f
            val bulletX = x + (cos(radians) * cannonLength).toFloat()
            val bulletY = y + (sin(radians) * cannonLength).toFloat()
            
            return Bullet(
                x = bulletX,
                y = bulletY,
                angle = angle,
                damage = Constants.BULLET_DAMAGE,
                isPlayerBullet = true
            )
        }
        return null
    }
}