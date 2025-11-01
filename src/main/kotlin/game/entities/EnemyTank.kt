package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.utils.Constants
import game.utils.EnemyType
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class EnemyTank(
    x: Float,
    y: Float,
    val enemyType: EnemyType = EnemyType.NORMAL
) : Tank(x, y) {
    
    private var aiTimer: Float = 0f
    private var directionChangeInterval: Float = 2f
    private var targetX: Float = x
    private var targetY: Float = y
    private var lastAngle: Float = 90f
    private var scoreValue: Int = 100
    
    init {
        configureByType()
    }
    
    private fun configureByType() {
        when (enemyType) {
            EnemyType.NORMAL -> {
                speed = Constants.ENEMY_SPEED
                maxHealth = 50
                health = 50
                shootCooldown = 1.5f
                scoreValue = 100
            }
            EnemyType.FAST -> {
                speed = Constants.ENEMY_SPEED * 1.5f
                maxHealth = 30
                health = 30
                shootCooldown = 1f
                scoreValue = 150
            }
            EnemyType.TANK -> {
                speed = Constants.ENEMY_SPEED * 0.6f
                maxHealth = 100
                health = 100
                shootCooldown = 2f
                scoreValue = 200
            }
            EnemyType.SMART -> {
                speed = Constants.ENEMY_SPEED * 1.2f
                maxHealth = 60
                health = 60
                shootCooldown = 1.2f
                scoreValue = 250
            }
        }
    }
    
    override fun update(deltaTime: Float) {
        if (!isAlive) return
        
        aiTimer += deltaTime
        updateShootCooldown(deltaTime)
        
        // Cambiar dirección cada cierto tiempo
        if (aiTimer >= directionChangeInterval) {
            updateAI()
            aiTimer = 0f
        }
        
        // Moverse en la dirección actual
        moveForward(deltaTime)
    }
    
    override fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        // Color según tipo
        val (r, g, b) = when (enemyType) {
            EnemyType.NORMAL -> Triple(1f, 1f, 0f)      // Amarillo
            EnemyType.FAST -> Triple(1f, 0.5f, 0f)      // Naranja
            EnemyType.TANK -> Triple(0.5f, 0f, 1f)      // Púrpura
            EnemyType.SMART -> Triple(1f, 0f, 1f)       // Magenta
        }
        
        drawTank(shapeRenderer, r, g, b)
        shapeRenderer.end()
    }
    
    private fun updateAI() {
        // Evitar bordes - cambiar dirección si toca los límites
        if (x < 50f || x > Constants.SCREEN_WIDTH - 50f) {
            angle = if (x < 50f) 0f else 180f
        }
        if (y < 50f || y > Constants.SCREEN_HEIGHT - 50f) {
            angle = if (y < 50f) 270f else 90f
        }
        
        // Rotación según tipo
        when (enemyType) {
            EnemyType.NORMAL -> {
                rotate(3f)
            }
            EnemyType.FAST -> {
                rotate(8f)
            }
            EnemyType.TANK -> {
                rotate(2f)
            }
            EnemyType.SMART -> {
                rotate(4f)
            }
        }
        
        // Cambiar dirección aleatoriamente para no quedarse pegado
        if (Random.nextFloat() > 0.95f) {
            angle += Random.nextInt(-45, 45)
        }
    }
    
    companion object {
        var gamePlayerX = 0f
        var gamePlayerY = 0f
    }
    
    fun shoot(): Bullet? {
        if (canShoot() && Random.nextFloat() < 0.3f) { // 30% de probabilidad
            resetShootCooldown()
            
            val radians = Math.toRadians(angle.toDouble())
            val cannonLength = size * 0.8f
            val bulletX = x + (cos(radians) * cannonLength).toFloat()
            val bulletY = y + (sin(radians) * cannonLength).toFloat()
            
            return Bullet(
                x = bulletX,
                y = bulletY,
                angle = angle,
                damage = (Constants.BULLET_DAMAGE / 2).toInt(),
                isPlayerBullet = false
            )
        }
        return null
    }
    
    fun getScoreValue(): Int = scoreValue
}