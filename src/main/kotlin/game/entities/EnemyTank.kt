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
    
    private fun updateAI() {
        val isNearWall = x < 60f || x > Constants.SCREEN_WIDTH - 60f || 
                         y < 60f || y > Constants.SCREEN_HEIGHT - 60f
        
        if (isNearWall) {
            angle += Random.nextInt(90, 180).toFloat()
        }
        
        when (enemyType) {
            EnemyType.NORMAL -> {
                rotate(3f)
                if (Random.nextFloat() > 0.97f) angle += Random.nextInt(-60, 60)
            }
            EnemyType.FAST -> {
                rotate(8f)
                if (Random.nextFloat() > 0.95f) angle += Random.nextInt(-45, 45)
            }
            EnemyType.TANK -> {
                rotate(2f)
                if (Random.nextFloat() > 0.98f) angle += Random.nextInt(-30, 30)
            }
            EnemyType.SMART -> {
                rotate(4f)
                if (Random.nextFloat() > 0.96f) angle += Random.nextInt(-50, 50)
            }
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
                damage = (Constants.BULLET_DAMAGE / 2).toInt(),
                isPlayerBullet = false
            )
        }
        return null
    }
    
    fun getScoreValue(): Int = scoreValue
}