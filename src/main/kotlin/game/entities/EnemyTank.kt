package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import game.utils.EnemyType
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class EnemyTank(
    x: Float = 100f,
    y: Float = 100f,
    val enemyType: EnemyType = EnemyType.NORMAL
) : Tank(x, y) {
    private val scoreValue = when (enemyType) {
        EnemyType.NORMAL -> 100
        EnemyType.FAST -> 150
        EnemyType.TANK -> 200
        EnemyType.SMART -> 300
    }
    
    private var aiTimer = 0f
    
    override fun update(deltaTime: Float) {
        aiTimer += deltaTime
        updateAI()
        moveForward(deltaTime)
        updateShootCooldown(deltaTime)
    }
    
    override fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        drawTank(shapeRenderer, 1f, 0f, 0f)
        shapeRenderer.end()
    }
    
    private var targetX = 0f
    private var targetY = 0f
    private var moveTimer = 0f
    
    private fun updateAI() {
        moveTimer += 0.016f
        
        // Cambiar objetivo cada 3-7 segundos
        if (moveTimer > (3f + Random.nextFloat() * 4f)) {
            targetX = Random.nextFloat() * Constants.SCREEN_WIDTH
            targetY = Random.nextFloat() * Constants.SCREEN_HEIGHT
            moveTimer = 0f
        }
        
        // Moverse hacia objetivo
        val dx = targetX - x
        val dy = targetY - y
        val distance = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        
        if (distance > 20f) {
            angle = Math.toDegrees(kotlin.math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
        }
        
        // Cambio aleatorio ocasional
        if (Random.nextFloat() > 0.98f) {
            angle += Random.nextInt(-30, 30)
        }
    }
    
    fun shoot(): Bullet? {
        if (canShoot() && Random.nextFloat() < 0.3f) {
            resetShootCooldown()
            
            val radians = Math.toRadians(angle.toDouble())
            val cannonLength = size * 0.8f
            val bulletX = x + (cos(radians) * cannonLength).toFloat()
            val bulletY = y + (sin(radians) * cannonLength).toFloat()
            
            return Bullet(
                x = bulletX,
                y = bulletY,
                angle = angle,
                damage = Constants.ENEMY_DAMAGE,
                isPlayerBullet = false
            )
        }
        return null
    }
    
    fun getScoreValue(): Int = scoreValue
}